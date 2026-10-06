# test-e2e-bdd-api

The same small Orders API scenarios (create, fetch, reject a zero quantity, unknown id is 404) written four ways, so you can compare REST Assured, Karate, Cucumber and Playwright side by side.

## What is inside

| Folder | What it shows | Run |
| --- | --- | --- |
| [`restassured`](./restassured) | Fluent given/when/then API tests in plain JUnit 5 against an in-process stub | `mvn -B test` |
| [`karate`](./karate) | The same four scenarios as a Karate feature file, no step code | `mvn -B test` |
| [`cucumber`](./cucumber) | Gherkin business wording with Java step definitions | `mvn -B test` |
| [`playwright-compose`](./playwright-compose) | Playwright Test against an Orders API in Docker Compose, with flakiness controls | `./run.sh` |

## Prerequisites

- Java 21 and Maven 3.9 or newer (`restassured`, `karate`, `cucumber`)
- Docker with Compose v2, Node 22 and npm (`playwright-compose`)

## How to read it

Start with `restassured`, then open `karate` and `cucumber` to see how the same scenarios read in each style. Finish with `playwright-compose`, the only folder that talks to a real container over the network.
