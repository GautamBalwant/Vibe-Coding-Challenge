
# Prompt: SEO – Dynamic Meta & Sitemap

**Goal**: Implement meta tags using EJS variables and generate `sitemap.xml` from backend data.

**Instructions**
- EJS: `<title><%= pageTitle %></title>`, meta description using property attributes.
- Backend endpoint `GET /api/sitemap.xml` reads JSON and outputs URLs: `/`, `/explore`, `/property/:id-:slug`.
- Include Open Graph tags and Twitter Cards on property pages.
- Add canonical link and 404 handling.

**Acceptance Criteria**
- Sitemap validates against search engine tools.
- Shared listings have rich previews (OG/Twitter).
