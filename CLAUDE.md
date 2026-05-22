# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spec-driven customer support help desk built on **Vaadin 25 + Spring Boot 4 (Java 25)**. Specifications in `spec/` are the source of truth — code is generated from use cases, then visually verified and tested. The repo currently contains only `Application.java`; feature code is written when use cases are implemented.

## Commands

```bash
./mvnw                              # Run dev server on :8080 (default goal: spring-boot:run)
./mvnw clean package                # Production build
./mvnw test                         # Run all tests
./mvnw test -Dtest=ClassName        # Run a single test class
./mvnw test -Dtest=ClassName#method # Run a single test method
```

Port is overridable via `PORT` env var. Browser auto-launches in dev (`vaadin.launch-browser=true`).

## Spec-Driven Workflow

Specs in `spec/` are the source of truth — do **not** treat existing code as authoritative when it diverges from a spec; update code to match the spec, or update the spec (with the user) first.

- `spec/project-context.md` — vision, users, scope
- `spec/architecture.md` — stack and structure rules (**do not modify without asking**, same for `pom.xml`)
- `spec/datamodel/datamodel.md` — entities
- `spec/design-system.md` — Aura theme rules (see below)
- `spec/use-cases/use-case-NNN-*.md` — one feature per file; implemented one at a time via the `/implement-use-case` skill

User-facing skills (`/new-use-case`, `/implement-use-case`, `/visual-verification`, `/use-case-tests`) drive the workflow. `/implement-use-case` orchestrates the others.

## Architecture Rules

- **Package layout:** `com.example.<feature>/` containing `<Feature>View.java`, `<Feature>Service.java` (`@Service`), `<Feature>Repository.java` (Spring Data). `Application.java` is the entry point.
- **Routing:** Vaadin Flow (Java) views use `@Route`. Hilla React views use **file-based routing** under `src/main/frontend/views/` — not a central `routes.tsx`.
- **UI state:** Vaadin **Signals** are primary. Use non-shared signals for per-user state; shared signals require server push (`@Push` on `Application`).
- **Security:** Spring Security via `VaadinSecurityConfigurer`. Public routes/endpoints use `@AnonymousAllowed`; admin Flow routes use `@RolesAllowed("ADMIN")`. Login via Vaadin `LoginForm` at `/login`.
- **Allowed packages** for Vaadin scanning: `com.vaadin, org.vaadin, com.flowingcode, com.example` (see `application.properties`).

## Design System — Aura, NOT Lumo

This project uses the **Vaadin Aura** theme. Aura and Lumo are incompatible.

- **Never use `--lumo-*` variables.**
- Use `--aura-*` for Aura-specific tokens (typography, shadows, font sizes, accent colors).
- Use `--vaadin-*` for base tokens shared across themes (spacing `--vaadin-gap-*` / `--vaadin-padding-*`, radius, text/background colors).
- No hard-coded `px`/`rem`/`em` when a token exists (e.g. use `--aura-font-size-s`, `--vaadin-gap-m`).
- Custom CSS lives in `src/main/resources/META-INF/resources/styles.css`, loaded via `@StyleSheet("styles.css")` on `Application`.
- Color palette is derived: override base tokens like `--aura-accent-color-light`, `--aura-red`, etc. — never the derived `--aura-accent-*-text/border/surface` tokens.

## Testing

- JUnit 5 + **Vaadin Browserless Tests** (`browserless-test-junit6`) for view/component tests — no real browser needed.
- React views use Vitest.
- Visual verification of UI changes is done via the Playwright MCP (`/visual-verification` skill) — invoke after implementing UI changes.

## Tooling Notes

- The repo ships with `.mcp.json` configuring **Vaadin docs MCP** (use `mcp__Vaadin__*` tools to look up component APIs / theme tokens / version info) and **Playwright MCP** (for visual verification).
- Maven wrapper (`./mvnw`) is checked in — use it instead of a system `mvn`.
