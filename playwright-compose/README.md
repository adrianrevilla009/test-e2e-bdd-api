# playwright-compose

Playwright Test specs (`tests/orders.spec.js`) against a dependency-free Node Orders API (`server.js`) started by Docker Compose (`compose.yaml`), driven by `run.sh`.

## Goal

Run Playwright against a real HTTP service in a container, with the test-data and anti-flakiness habits a shared stack needs.

## Run it

```
./run.sh
```

Expected: the script runs `docker compose up -d --wait`, `npm ci`, then `npx playwright test`, which should report 4 passed tests. It always runs `docker compose down -v` on exit. The API is published on `127.0.0.1:3000`.

Not run end to end: Docker was not available when this README was written, so the stack and the specs have not been executed. The Node server and the specs were read against each other (the same four scenarios as the other folders) but not run.

## What it proves

- `compose.yaml` defines a `wget` healthcheck on `/health`; `docker compose up --wait` blocks until it passes, so tests do not start before the service is ready.
- Test data strategy in `orders.spec.js`: each test builds a unique sku from `test.info().testId` and creates the order it reads, so parallel tests share the stack safely.
- Flakiness controls in `playwright.config.js`: 10 s timeout, retries only when `CI` is set, trace kept on the first retry, no fixed sleeps.

## Trade-offs

- It exercises the real network path and a real container, but is slower and heavier than the in-process stubs in the other folders.
- Shared stack state means tests must be written to be independent; at scale you would need a reset endpoint or a tenant per test.
- The specs use Playwright's `request` fixture, so no browser is downloaded; browser specs would need `npx playwright install` and the `page` fixture.

## When not to use it

- When the service can start in-process; the stub approach in `../restassured` is faster.
- When you only need API assertions and cannot run Docker; the container adds cost without benefit.
