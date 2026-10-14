# Personal Finance Tracker

A personal finance app for income, expenses, debts, and investments. This is a **learning project**: the goal is to learn by building, not to ship fast.

## How to work with me

I'm a Jr. development analyst. I rarely code at work and I'm rusty, so explain things in plain language and say why, not just what.

- **Do not write the implementation for me.** I write the code. Your job is to mentor and review.
- Explain concepts, point me to the right docs, and ask guiding questions before giving answers.
- Show code only as short snippets (about 10 lines) to illustrate an idea. Write full code only if I explicitly ask.
- When I share code, review it: point out bugs, risks, and better practices, and let me do the fix.
- Stay within the current phase. If a feature belongs to a later phase, say so instead of building toward it.
- Library versions change fast. Check official docs before recommending configuration, especially for Spring Cloud Gateway MVC, Keycloak, and Kafka.

## Stack

Java 21, Spring Boot, MySQL, Flyway, Springdoc OpenAPI (Swagger), Keycloak, Docker, and Docker Compose.

Planned later: Spring Cloud Gateway MVC, Resilience4j, and Apache Kafka in KRaft mode without Zookeeper.

## Architecture decisions

- Start as a modular monolith: one Spring Boot app with clean `user`, `transaction`, and future `investment` packages. Split services out later as a deliberate exercise.
- There is no authentication service. Keycloak handles login and tokens. The application validates JWTs as an OAuth2 resource server and reads the user from the `sub` claim.
- The user module owns profile data only (currency, locale, and preferences), keyed by the Keycloak `sub`. It never stores credentials.
- Money uses `BigDecimal` (or integer minor units) plus a currency code. Never use `double`.
- Income and expense are one `Transaction` entity with a type and category. Debts are a separate aggregate.
- Service discovery is not used. Docker Compose DNS names are used when services are split later.
- Only the future market-data service calls the external prices API, wrapped in Resilience4j. Other services consume its events.

## Phases

1. Docker Compose with MySQL and Keycloak running.
2. Finance and user modules with Flyway, Swagger, and Keycloak JWT protection.
3. Gateway in front, validating JWTs.
4. Investment module and further user-module work.
5. Kafka with the transactional outbox pattern; split out market-data-service.
6. Notification and reporting services, then observability.

**Current phase: 2**

## Commands

- Start infrastructure: `docker compose up -d`
- Run tests: `./mvnw test`
- Run the application: `./mvnw spring-boot:run`

## Repository layout

- `docker-compose.yml`: local MySQL and Keycloak infrastructure
- `src/main/java/com/bowie/financeering/`: the Spring Boot modular monolith
- `src/main/java/com/bowie/financeering/transaction/`: transaction domain
- `src/main/java/com/bowie/financeering/user/`: user profile domain
- `src/main/resources/db/migration/`: Flyway migrations
- `src/test/`: automated tests

## Current status (Updated: 2026-10-13)

The project is one Spring Boot application, not separate services. The finance and user areas are modules inside the same application.

Implemented:

- Transaction CRUD endpoints under `/api/finance/**`.
- User profile create, current-user read, update, and delete endpoints under `/api/user`.
- DTOs, services, repositories, and transaction ownership checks based on the authenticated Keycloak `sub`.
- Keycloak OAuth2 resource-server JWT validation.
- Authentication required for `/api/**`.
- Flyway baseline migrations for `transactions` and `users`.
- `BigDecimal` transaction amounts and explicit currency codes.

The current database schema uses:

- `transactions` as the transaction table.
- `transaction_type` with `EXPENSE` and `INCOME` values.
- `users` keyed by `user_sub`.

Remaining work before considering the current foundation stable:

1. Make local application startup reproducible with explicit database environment variables.
2. Make `./mvnw test` independent of a developer's `.env` file and development database, preferably with a MySQL Testcontainer.
3. Test the protected API with at least two Keycloak users and verify that one user cannot access another user's transactions.
4. Expand automated tests beyond the context-load smoke test to cover ownership and validation behavior.

Later work, intentionally out of the current scope:

- Global exception-to-HTTP response handling and broader validation cleanup.
- Keycloak role mapping and browser login/profile bootstrap flow.
- API Gateway and the remaining Phase 3 work.
- Investment, Kafka, outbox, notification, reporting, and observability modules.

Do not start later-phase infrastructure until the current API's database setup, authentication boundaries, and ownership behavior are reproducible and tested.
