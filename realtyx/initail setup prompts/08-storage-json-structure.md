
# Prompt: Define JSON Storage Structure & Utilities

**Goal**: Standardize schema for JSON files and implement shared utility functions.

**Instructions**
- Files: `properties.json`, `brokers.json`, `admin_users.json`, `import_jobs.json`, `favourites.json`.
- Property schema fields: `id,title,description,propertyType,price,currency,bedrooms,bathrooms,areaSqft,address{line,city,state,pincode,country},amenities[],brokerId,media[],status,createdAt,updatedAt`.
- Create Java utility `JsonFileStore<T>` parametric on type with methods: `readAll()`, `writeAll(List<T>)`, `append(T)`, `findById(id)`.
- Implement lock & backup strategy: before write, create `.bak` copy; on failure, restore.

**Acceptance Criteria**
- Utilities reused across services; backup files present.
- Schema documented and enforced via validation.
