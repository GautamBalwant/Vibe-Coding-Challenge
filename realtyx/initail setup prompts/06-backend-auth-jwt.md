
# Prompt: Spring Boot – JWT Auth with HttpOnly Cookie

**Goal**: Provide admin authentication using Spring Security + JWT, issuing HttpOnly cookie on login.

**Instructions**
- Endpoint `POST /api/auth/login` validates credentials from `admin_users.json`.
- Generate JWT with role claim; set cookie `ADMIN_TOKEN` (HttpOnly, Secure, SameSite=Lax).
- Add filter to read JWT from cookie; protect `/api/admin/**`.
- Logout `POST /api/auth/logout` clears cookie.
- Passwords hashed (bcrypt); provide seeding tool.

**Acceptance Criteria**
- Unauthenticated requests to admin endpoints return 401.
- Cookie-based session persists; token expiry enforced.
