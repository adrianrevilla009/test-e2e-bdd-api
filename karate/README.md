# karate

Karate feature file (`src/test/resources/lab/orders.feature`) run by a JUnit 5 class (`OrdersKarateTest.java`) against an in-process stub.

## Goal

Express the same four Orders scenarios as `../restassured` in Karate's DSL, so the request, status and body checks live in a readable feature file with no step code.

## Run it

```
mvn -B test
```

Expected: `BUILD SUCCESS`; the surefire report for `lab.OrdersKarateTest` shows `Tests run: 1, Failures: 0, Errors: 0`. Karate logs a lot of debug output, so check the report rather than the console.

## What it proves

- `orders.feature` holds four scenarios; `match response == { id: '#number', status: 'NEW', qty: 2 }` checks the whole JSON shape in one line.
- `OrdersKarateTest` starts `OrdersStub`, passes its port in the `orders.port` system property and asserts both zero failures and exactly four scenarios passed, so a silently skipped feature fails the build.
- Test data strategy: the fetch scenario creates its own order and reads `response.id`, so scenarios are order-independent.

## Trade-offs

- Less code than Java and readable by testers, but the DSL is its own language with its own debugging story.
- The runner is JUnit-wrapped here to share one stub; parallel scenarios would need a stub that tolerates them.
- Verbose logging makes console output hard to scan unless you lower the log level.

## When not to use it

- When tests need heavy Java logic or reuse of production classes; plain REST Assured fits better.
- When business stakeholders want Gherkin wording tied to step definitions; use `../cucumber`.
