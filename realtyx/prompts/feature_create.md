# Prompt: Create a New Feature for RealtyX

Use this prompt to generate a complete implementation plan, code scaffolding, and test ideas when starting a new feature in the RealtyX platform.

---

Goal: Add a new feature to the RealtyX microservices platform. Provide a high-level design, required file changes, API contracts, database/data-file changes, tests, and a short implementation checklist.

Context to provide the model (fill before running):
- Service: (backend | data-service | api-gateway | ui | service-registry)
- Language/Framework: (Java/Spring Boot | Node.js/Express | Other)
- Existing endpoints or UX affected:
- Data shape (example JSON or DB schema):
- Auth/Permissions: (roles allowed)
- Non-functional requirements: (performance, availability, scalability)

Prompt Template:
```
You are an expert full-stack developer helping implement a new feature in the RealtyX application.

Context:
- Service: {{service}}
- Framework: {{framework}}
- Repo path root: c:/Users/289544/Downloads/realtyx
- Current microservices: service-registry(8761), data-service(9090), backend(8080), api-gateway(8765), ui(3001)
- Data storage: JSON files in /data
- Auth: Session-based, roles: Buyer, Seller, Agent, Admin

User Story:
"{{short_user_story}}"

Requirements:
1. Provide an architectural diagram (text/ascii) showing where the feature lives.
2. List files to add or modify with brief purpose for each.
3. Provide API contract(s) (HTTP method, path, request body, response schema, status codes).
4. Include sample JSON data modifications or new JSON files.
5. Provide a minimal implementation scaffold (controller, service, model) in the chosen language.
6. Add unit and integration test plan and sample tests (happy path + 2 edge cases).
7. List migration steps and rollout plan (include feature flag suggestion if risky).
8. Provide a code checklist for PR reviewers (security, docs, tests, lint).

Constraints:
- Prefer minimal changes to unrelated files.
- Avoid adding new heavyweight dependencies unless justified.
- Follow existing project conventions (JSON-based data store if not otherwise specified).

Deliverables:
- Implementation plan and file patches (diff-style or file templates).
- Test cases and sample data files.
- Rollout steps and verification steps.

Be concise but thorough. Use bullet lists and code fences for examples.
```

Examples of use:
- Create an Agent ratings feature
- Add property tagging and categories
- Implement property import endpoint for CSV

---

Tips for Developers:
- Fill the "Context to provide" fields before invoking the prompt.
- Include the current file contents for the controller being extended for more targeted output.
- Use the generated scaffold as a starting point — adapt to coding standards and project style.