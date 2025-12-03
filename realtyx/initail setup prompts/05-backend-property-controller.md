
# Prompt: Spring Boot – Properties Controller (Filesystem JSON)

**Context**: Backend stores data in JSON files (e.g., `/data/properties.json`).

**Goal**: Implement REST endpoints for properties with filtering, sorting, and pagination, reading/writing the JSON file safely.

**Instructions**
- Create service `PropertyService` to read/write JSON using Jackson (`ObjectMapper`).
- Ensure thread-safety: use a `ReentrantReadWriteLock` around file operations.
- Endpoints:
  - `GET /api/properties` with params: `page,size,sort,q,city,type,minPrice,maxPrice,bedrooms`.
  - `GET /api/properties/{id}`.
  - `POST /api/admin/properties` (validate, assign new ID, append, write).
  - `PUT /api/admin/properties/{id}` (find & update, write).
  - `DELETE /api/admin/properties/{id}` (remove & write).
- Implement basic indexing in memory (cache on startup) to speed reads; write-through to file.
- Add ETag/Last-Modified headers.

**Acceptance Criteria**
- File corruption prevented; concurrent writes safe.
- Filters return correct subsets; pagination stable.
- Unit tests cover service and controller.
