
# Prompt: Create Explore Page (List/Grid + Filters)

**Context**: Node.js + EJS SSR UI; filters against Spring Boot API; data in JSON files on backend.

**Goal**: Build `/explore` page with filter form (city, type, minPrice, maxPrice, bedrooms) and list/grid toggle. Paginate via `page` and `size` params.

**Instructions**
- Route `GET /explore` parses query params; server-side fetch `GET /api/properties` with those params.
- Render `views/explore.ejs` with `{results, page, size, totalPages, query}`.
- Add filter bar and submit via GET (SSR). Preserve query params in pagination links.
- Implement grid/list toggle using a query param `view=grid|list`.
- Use Tailwind for layout; skeleton placeholders when data is loading (optional SSR streaming).

**Acceptance Criteria**
- Filters work server-side; no SPA needed.
- Pagination visible with prev/next.
- Accessible labels for all filter inputs.
