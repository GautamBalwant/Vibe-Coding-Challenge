# Prompt: Update an Existing Feature in RealtyX

Use this prompt to plan and generate code changes when updating or refactoring an existing feature.

Context to provide before running:
- Service: (backend | data-service | api-gateway | ui)
- Affected files or modules:
- Reason for change: (bugfix | performance | new requirement | security)
- Backwards compatibility required: (yes/no)
- Tests coverage required: (unit/integration/e2e)

Prompt Template:
```
You are an experienced engineer assigned to update an existing feature in the RealtyX repo.

Context:
- Service: {{service}}
- Affected module(s): {{module_list}}
- Current behavior: {{current_behavior}}
- Desired behavior: {{desired_behavior}}
- Backwards compatibility: {{compatibility}}

Tasks:
1. Provide a concise impact analysis (which classes/files/functions will change and why).
2. Show the minimal code changes needed (diffs or new file templates).
3. Provide migration or data transformation scripts if needed.
4. Provide a test plan: existing tests to change and new tests to add.
5. Suggest a rollback plan and monitoring steps after deployment.
6. Provide a sample PR description (title, summary, changes list, migration steps, testing instructions).

Deliverables:
- File diffs or code templates.
- Test examples and sample test commands.
- PR checklist for reviewers.
```

Notes:
- If the update affects API contracts, include versioning suggestions (v2 endpoints or feature flags).
- If data schema changes, include safe-migration steps to avoid downtime.