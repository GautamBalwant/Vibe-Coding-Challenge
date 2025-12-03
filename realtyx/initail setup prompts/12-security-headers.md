
# Prompt: Security Headers & Rate Limiting

**Goal**: Strengthen security for UI and API.

**Instructions**
- Node.js: use `helmet` to set CSP, HSTS, X-Frame-Options.
- Spring Boot: configure `SecurityFilterChain` to add headers; rate limit `/api/leads` and search endpoints.
- Validate inputs; sanitize output.

**Acceptance Criteria**
- Headers present in responses; rate limiting observed under stress.
