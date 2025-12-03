
# Prompt: Spring Boot – Bulk CSV Import → JSON

**Goal**: Implement `POST /api/admin/import` that accepts CSV, validates rows, and writes to `properties.json`.

**Instructions**
- Parse CSV using Apache Commons CSV.
- Column mapping: `title,description,propertyType,price,currency,bedrooms,bathrooms,areaSqft,address.line,address.city,address.state,address.pincode,amenities,status,brokerId`.
- Validate price/area numeric ranges; collect errors per row.
- Dry-run mode (`?dryRun=true`) returns preview without writing.
- On success: append new properties; update `import_jobs.json` with status and error report.

**Acceptance Criteria**
- Large files (5–10k rows) processed without blocking (use async executor).
- Validation report returned with counts.
