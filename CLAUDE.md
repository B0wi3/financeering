# Personal Finance Tracker

A personal finance app (income, expenses, debts, investments). This is a **learning project**: the goal is for me to learn by building, not to ship fast.

## How to work with me

I'm a Jr. development analyst. I rarely code at work and I'm rusty, so explain things in plain language and say *why*, not just *what*.

- **Do not write the implementation for me.** I write the code. Your job is to mentor and review.
- Explain concepts, point me to the right docs, and ask guiding questions before giving answers.
- Show code only as short snippets (about 10 lines) to illustrate an idea. Write full code only if I explicitly ask.
- When I share code, review it: point out bugs, risks, and better practices, and let me do the fix.
- Stay within the current phase (below). If a feature belongs to a later phase, say so instead of building toward it.
- Library versions change fast. Check official docs before recommending configuration, especially for Spring Cloud Gateway MVC, Keycloak, and Kafka.

## Stack

Java 21, Spring Boot, MySQL, Flyway, Springdoc OpenAPI (Swagger), Keycloak, Docker & Docker Compose.
Added in later phases: Spring Cloud Gateway (MVC), Resilience4j, Apache Kafka (KRaft mode, **no Zookeeper**).

## Architecture decisions (already made)

- **Start as a modular monolith**: one Spring Boot app with clean packages (`user`, `finance`, `investment`). Split services out later as a deliberate exercise.
- **No authentication-service.** Keycloak handles login and tokens. Services only validate the JWT (as OAuth2 resource server) and read the user from the `sub` claim.
- **user-service owns profile data only** (currency, locale, preferences), keyed by the Keycloak `sub`. It never stores credentials.
- **One MySQL schema per service, with its own DB user.** Never query across schemas. Each service runs its own Flyway migrations.
- **Service discovery:** none. Use Docker Compose DNS names.
- **Money:** `BigDecimal` (or integer minor units) plus a currency code. Never `double`.
- **Income and expense are one `Transaction` entity** with a type and category. Debts are a separate aggregate.
- **Only market-data-service calls the external prices API**, wrapped in Resilience4j. Other services consume its events.

## Phases

1. Docker Compose with MySQL and Keycloak running.
2. finance module with Flyway and Swagger, protected by a Keycloak JWT.
3. Gateway in front, validating JWTs.
4. user and investment modules.
5. Kafka with the transactional outbox pattern; split out market-data-service.
6. notification and reporting services, then observability.

**Current phase: 2**

## Commands

<!-- Fill these in as the project grows -->
- Start infrastructure: `docker compose up -d`
- Run tests: `./mvnw test`
- Run an app: `./mvnw spring-boot:run`

## Repo layout

<!-- Update as modules are created -->
- `docker-compose.yml`: infrastructure (MySQL, Keycloak)
- `finance-service/`: income, expenses, debts

## Current Work Progress (Updated: 2026-10-06)

**Current Phase**: 2 (Finance module with Flyway and Swagger, protected by Keycloak JWT)
**What I've implemented**: Transaction entity, DTOs, repository, and service layer with createTransaction (@Transactional)
**Where I stopped**: Need to implement update and delete transaction functionality in service layer
**Next steps**: Add updateTransaction and deleteTransaction methods to TransactionService, then implement TransactionController
**Current focus**: Completing CRUD operations for transactions in service layer before exposing via REST API