# restassured

Four REST Assured tests (`OrdersApiTest.java`) against an in-process Orders API stub (`OrdersStub.java`).

## Goal

Cover the Orders API (create, fetch, reject invalid quantity, unknown id) with REST Assured and JUnit 5. The same four scenarios are repeated in `../karate` and `../cucumber` so the styles can be compared.

## Run it

```
mvn -B test
```

Expected: Maven finishes with `BUILD SUCCESS`; the surefire report for `lab.OrdersApiTest` shows `Tests run: 4, Failures: 0, Errors: 0, Skipped: 0`. Nothing else has to be running.

## What it proves

- Fluent `given().when().then()` assertions on status codes and JSON fields (`id`, `status`, `qty`) in `OrdersApiTest.java`.
- Test data strategy: `fetchesCreatedOrder` creates its own order through `POST /orders` before reading it, so tests do not depend on each other or on pre-loaded rows.
- Flakiness control: `OrdersStub` is a JDK `HttpServer` bound to port 0 (a free port), keeps state in memory and uses no sleeps, so runs cannot collide.

## Trade-offs

- Plain Java gives full IDE support, refactoring and debugging, but non-developers cannot read the tests.
- Request bodies are hand-built JSON strings; a larger suite would use typed request objects.
- The stub parses the body with a regex, which is enough for this lab but not a model of a real service.

## When not to use it

- When non-programmers must read or write the scenarios; use `../cucumber` or `../karate`.
- When the subject is browser behaviour rather than HTTP responses.
