# Personal Finance Tracker

A personal finance app (income, expenses, debts, investments). This is a **learning project**: the goal is for me to learn by building, not to ship fast.

## How to work with me

I'm a Jr. development analyst. I rarely code at work and I'm rusty, so explain things in plain language and say *why*, not just *what_.

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

- Start infrastructure: `docker compose up -d`
- Run tests: `./mvnw test`
- Run an app: `./mvnw spring-boot:run`

## Repo layout

- `docker-compose.yml`: infrastructure (MySQL, Keycloak)
- `finance-service/`: income, expenses, debts

## Current Work Progress (Updated: 2026-10-12)

**Current Phase**: 2 (Finance module with Flyway and Swagger, protected by Keycloak JWT) - with ongoing work on Phase 4 (User module) and authentication flow implementation
**What I've implemented**: 
- **Transaction module (finance-service)**: Complete - entity, DTOs, repository, service layer, and controller with full CRUD operations (create, read, update, delete) including @Transactional annotations, proper error handling (orElseThrow), and userSub authorization checks
- **User module (user-service)**: Substantial progress - entity, repository, DTOs (Create, Update, Response), service layer refined to use DTOs, and UserController implemented with CREATE, READ ALL, READ ONE, and UPDATE endpoints
**Authentication Integration**: 
- Keycloak configured as OAuth2 Resource Server for JWT validation
- Understanding of proper auth flow: Keycloak handles user authentication, application manages profile data (currency, locale)
- Identified need to configure Spring Security as both OIDC Client (for login flow) and Resource Server (for token validation)
- Resolved port conflict between Keycloak (8080) and application (planning to run on 8081)
**Persistent Issue**: RESOLVED - Transaction enum values now correctly store and retrieve as uppercase (EXPENSE/INCOME) in database
**Fix Applied**: Corrected column mapping in Transaction entity from `@Column(name = "transactionType")` to `@Column(name = "transaction_type")` to match Flyway migrations
**Verification**: Confirmed proper enum persistence through manual testing and integration tests
**Controller Status**: 
- TransactionController.java fully implemented with all CRUD endpoints at /api/finance/** (GET, POST, PUT, DELETE) using JwtAuthenticationToken for proper JWT principal extraction
- UserController.java created with POST, GET (all), GET (by id), and PUT endpoints (DELETE endpoint pending; security refinements needed on GET endpoints - specifically, GET endpoints incorrectly use @PathVariable for userSub instead of extracting from JWT)
**Service Layer Status**: 
- TransactionService.java corrected AccessDeniedException import and implements all CRUD operations with @Transactional
- UserService.java refined to accept/return DTOs instead of entities for better API contract; authorization checks implemented in updateUser; createUser and getUserById/getAllUsers require security enhancements
**Security Configuration**: SecurityConfig.java configured for OAuth2 Resource Server with Keycloak JWT validation (enabled)
**Next Steps**: 
1. Implement DELETE endpoint in UserController.java
2. Add authorization checks to UserService.getUserById() and refine getAllUsers() to prevent unauthorized data access
3. Fix UserController GET endpoints to extract userSub from JWT instead of @PathVariable
4. Verify all user module functionality through manual testing and integration tests
5. Begin Phase 3: Implement API Gateway (Spring Cloud Gateway MVC) for JWT validation and routing
6. **Authentication Enhancement Plan**:
   - Enable Keycloak self-registration for account creation
   - Configure Spring Boot app to run on port 8081 (to avoid conflict with Keycloak on 8080)
   - Implement post-login flow to auto-create user profiles with browser-detected locale/currency
   - Extract locale from Accept-Language header and map to currency (en_US→USD, pt_BR→BRL, etc.)
   - Allow users to override auto-detected values via profile update endpoint