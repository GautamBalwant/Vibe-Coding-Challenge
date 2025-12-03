
# Prompt: CI/CD – Build & Deploy

**Goal**: Set up GitHub Actions for Node UI and Spring Boot backend.

**Instructions**
- Node: install, lint, test, build; artifact `dist/`.
- Spring: `mvn -B verify`; build jar; run unit tests.
- Docker: two images (`ui`, `backend`), compose for dev.
- Deploy to staging after successful builds; smoke test endpoints.

**Acceptance Criteria**
- Pipeline green; artifacts produced; staging deployment accessible.
