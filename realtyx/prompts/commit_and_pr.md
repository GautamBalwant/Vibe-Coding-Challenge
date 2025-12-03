# Prompt: Commit Message and PR Description Helper

This prompt helps developers generate good commit messages and a comprehensive PR description.

Context to provide:
- Short description of change
- Files modified (high level)
- Motivation and user-visible impact
- Any migration or environment changes
- Testing performed

Prompt Template:
```
You are an experienced developer helping craft a concise commit message and an informative PR description for RealtyX.

Inputs:
- Summary: {{one-line summary}}
- Why: {{motivation}}
- Scope: {{files_changed}}
- Breaking changes: {{yes/no}}
- Migration steps: {{migration}}
- Tests: {{tests_performed}}

Outputs:
1. Suggested Git commit message (short and long form)
2. Structured PR description with sections: Summary, Motivation, Changes, How to Test, Rollout/Migration, Backwards Compatibility, Notes.
3. List of reviewers to ping (roles: backend, frontend, infra).
4. Suggested labels and milestone.

Best practices included in output:
- Use present tense in the commit title
- Keep title ≤ 50 characters, body ≤ 72 chars lines
- Reference issue numbers if applicable
- Include testing commands and sample API calls
```

Example:
- Title: "Add property tagging API and UI"
- Body: "Adds a tags field to properties, updates UI with tag filter, and migrates sample data."
- PR description should include sample curl commands to test endpoints.