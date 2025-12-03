
# Prompt: Create Property Detail Page

**Context**: SSR page `/property/:id-:slug` fetching property details from `GET /api/properties/{id}`.

**Goal**: Render media carousel, specs, amenities, broker info, contact form (POST `/api/leads`). Include share and favourite buttons (localStorage).

**Instructions**
- Route `GET /property/:id-:slug` → fetch property JSON; render `views/property.ejs`.
- Carousel: simple EJS loop with first image as hero; thumbnails below.
- Contact form posts to backend lead endpoint via server-side `POST` using Express `router.post('/leads')` proxy or direct POST to Spring Boot.
- Add JSON-LD structured data (`RealEstateListing`) in `<script type="application/ld+json">` using EJS.
- Favourite button: write client-side JS to store property ID in `localStorage['favourites']`.

**Acceptance Criteria**
- Page includes Open Graph tags and JSON-LD.
- Form validation (server + client) with error messages.
- AA accessibility: Alt text on all images; keyboard reachable controls.
