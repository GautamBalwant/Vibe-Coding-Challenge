# Prompts/Usage: How to use developer prompt templates

This folder contains a set of ready-to-use prompt templates to help developers build, update, test, and deploy features in the RealtyX microservices project. Use them with your local LLM assistant, automation tools, or copy into GitHub Copilot-style prompts.

Files:
- `feature_create.md` - Start a new feature: architecture, code templates, tests, and rollout plan.
- `feature_update.md` - Plan a refactor or update: diffs, migrations, tests, PR text.
- `generate_data.md` - Generate synthetic data: schemas, scripts, verification.
- `central_caching.md` - Add Redis caching: architecture, code, Docker compose changes.
- `code_review_and_tests.md` - Generate a review checklist and test plan for PRs.
- `commit_and_pr.md` - Craft commit messages and PR descriptions.
- `qa_checklist.md` - QA test cases and manual acceptance tests.

Quick examples

1) Generate a feature plan for "Property tagging":
- Open `feature_create.md` and fill these fields: Service=backend, Framework=Java/Spring Boot, short_user_story="As an admin I want to tag properties so buyers can filter by tags". Then run the prompt through your LLM to receive file scaffolds and API contracts.

2) Create deterministic test data (10,000 properties):
- Use `generate_data.md` with Scale=10000, Locale=India, Fidelity=realistic+images, Image source=unsplash to generate a Node.js script that will write `data/properties.json` with seeded randomness.

3) Add Redis caching for property lists:
- Use `central_caching.md` with Services=backend,data-service, Cache tech=Redis, Data types=properties_list, TTL=short to get code snippets and a `docker-compose` snippet to add Redis.

How to run the generated artifacts

- For scaffolded Node.js generators, run `node data/generator.js` (the generated script will include required commands).
- For Java code scaffolding, paste the suggested code into the appropriate packages and run `mvn -DskipTests=false test`.

Best practices

- Always run unit tests and integration tests locally before opening a PR.
- For high-impact changes, use the PR checklist from `code_review_and_tests.md` and consider a feature flag.
- Use deterministic seed values when generating test data to allow reliable test runs.

Contact

For questions about these prompts or to request new templates, open an issue in the project or contact the platform maintainer.