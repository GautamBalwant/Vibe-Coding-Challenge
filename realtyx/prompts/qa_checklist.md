# Prompt: QA Checklist and Manual Test Cases

Use this prompt to generate a QA checklist and manual test cases for a feature or release.

Context to provide:
- Feature name or PR title
- Affected user flows (e.g., login, property search, property create)
- Roles to test (Buyer, Seller, Agent, Admin)
- Environments (local, docker-compose, staging)

Prompt Template:
```
You are a QA engineer preparing a checklist and manual test cases for the RealtyX application.

Context:
- Feature: {{feature_name}}
- Services: {{services}}
- Roles: {{roles}}

Deliverables:
1. High-level smoke tests to run after deployment.
2. Role-based acceptance tests with step-by-step instructions and expected results.
3. Edge-case tests (invalid input, auth failures, rate-limit behaviour).
4. Performance sanity checks (paginate, load list of 1000 items).
5. Regression checklist (common flows to verify: login, search, view property, CRUD operations for admin).

Output format:
- Numbered test cases, each with Preconditions, Steps, Expected Results, Postconditions.

Example:
- Test Case: Admin creates a new property
  Preconditions: admin user is logged in
  Steps: ...
  Expected: ...

Notes:
- Encourage using deterministic test data (seeded datasets) for repeatability.
- For microservices, include a step to verify service registry and gateway routes.