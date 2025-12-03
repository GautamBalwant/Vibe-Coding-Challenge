
# Prompt: Create Admin Dashboard (Login + CRUD)

**Context**: Admin UI served by Node.js; backend Spring Boot handles auth (JWT) and data modifications.

**Goal**: Implement `/admin/login`, `/admin` (dashboard), listing table, create/edit/delete listing forms.

**Instructions**
- Login form posts to `/api/auth/login`; store JWT in HttpOnly cookie via Node proxy (Express sets cookie).
- Dashboard route validates cookie; if missing, redirect to login.
- CRUD routes call Spring Boot endpoints: `POST/PUT/DELETE /api/admin/properties`.
- Bulk upload page: upload CSV; Node forwards file to backend `POST /api/admin/import`.
- Views: `views/admin/login.ejs`, `views/admin/dashboard.ejs`, `views/admin/edit.ejs`, `views/admin/import.ejs`.

**Acceptance Criteria**
- Authenticated admin flows work end-to-end.
- Validation + success/error notifications displayed.
- File upload supports CSV and shows import status.
