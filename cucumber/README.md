# cucumber

Gherkin feature (`src/test/resources/lab/orders.feature`), Java step definitions (`OrderSteps.java`) and a JUnit Platform runner (`RunCucumberTest.java`).

## Goal

Show Orders scenarios written in business wording ("When I place an order for 2 of "ABC"") and bound to code, so a non-developer can read what is covered.

## Run it

```
mvn -B test
```

Expected: `Running lab.RunCucumberTest`, then `Tests run: 4, Failures: 0, Errors: 0, Skipped: 0` and `BUILD SUCCESS`.

## What it proves

- `orders.feature` has four scenarios with a short user-story header; the steps use `{int}` and `{string}` parameters, so wording can change without new code.
- `OrderSteps` has a `@Before` hook that starts `OrdersStub` on a free port and an `@After` hook that stops it. Cucumber creates a new step instance per scenario, so state never leaks between scenarios.
- Test data strategy: the lookup scenario uses a `Given` step to place its own order first, then reuses the returned id.

## Trade-offs

- Readable by non-programmers, but every phrase needs a step definition and the steps must be kept consistent.
- Starting a stub per scenario is cheap here; against a real service you would start it once.
- Response checks use string matching on the body, which is simple but less precise than JSON path assertions.

## When not to use it

- When only developers read the tests; the extra feature-file layer is overhead, and `../restassured` is shorter.
- When nobody outside the team will ever read or review the Gherkin.
