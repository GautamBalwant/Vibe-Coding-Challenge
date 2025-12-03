
# Prompt: Create Home Page (Node.js + Express + EJS)

**Context**: Project uses Node.js (Express), EJS templates, Tailwind CSS for UI; Spring Boot provides REST APIs; data storage is filesystem JSON.

**Goal**: Generate a SSR Home page (`views/index.ejs`) showing a search bar, quick filters, and featured properties fetched from `GET /api/properties?featured=true`.

**Instructions**
- Create route in `app.js` or `routes/home.js`: `GET /`.
- Server-side fetch featured properties from Spring Boot API using `node-fetch` or `axios`.
- Render `views/index.ejs` with props: `{ title, featuredProperties }`.
- Add a Tailwind-based hero section: search input (keyword/city), filter chips (Apartment/Villa/Plot/Commercial), and a responsive card grid.
- Each card links to `/property/:id-:slug`.
- Include meta tags (EJS variables) for title and description.

**File Outputs**
- `views/index.ejs`
- `routes/home.js` (optional)
- Update `app.js` to use the route.

**Acceptance Criteria**
- SSR page loads without client-side framework.
- Featured properties visible in grid; links navigate to property detail.
- Lighthouse SEO score ≥ 90 for the home page.

**Placeholders**
- API base URL: `process.env.API_BASE_URL`
- Title: `RealtyX – Find Properties`
