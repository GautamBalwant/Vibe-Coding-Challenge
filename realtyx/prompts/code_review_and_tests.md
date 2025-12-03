# Prompt: Code Review Guidance & Test Plan Generator

Use this prompt to automatically generate a focused code review checklist and a test plan for new features or PRs.

Context to provide:
- Feature or PR title
- Diff summary or list of files changed
- Risk level: (low | medium | high)
- Affected services: (backend | data-service | api-gateway | ui)
- Security concerns: (input validation, auth, data exposure)

Prompt Template:
```
You are a senior engineer creating a code review checklist and test plan for a PR in the RealtyX repo.

Context:
- PR title: {{pr_title}}
- Files changed: {{file_list}}
- Risk level: {{risk}}
- Services: {{services}}

Deliverables:
1. A concise code review checklist (functional, security, performance, readability, tests).
2. Specific items tailored to changed files (e.g., controller authentication, DTO validation, SQL/IO safety).
3. A test plan: unit tests, integration tests, end-to-end steps (manual), and edge cases.
4. Test data suggestions and commands for running tests locally.
5. A sample GitHub PR description that includes testing instructions and verification steps.

Example:
- If the PR touches `/backend/src/main/java/com/realtyx/controller/AuthController.java`, add checks for password hashing, null inputs, and session fixation.

Notes:
- Encourage small PRs and a single responsibility per change.
- For high-risk changes, recommend feature flags and staged rollouts.