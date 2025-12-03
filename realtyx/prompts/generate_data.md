# Prompt: Generate Synthetic Data for RealtyX

Use this prompt to create scripts or data-generation plans to populate the `/data` folder with realistic test data (properties, users, agents, brokers, reviews).

Context to provide:
- Scale: (number of properties, e.g., 10000)
- Locale: (country/city preferences)
- Data fidelity: (minimal | realistic | realistic+images)
- Image source: (unsplash | placeholder | none)
- Output format: (JSON files, CSV)

Prompt Template:
```
You are a data engineer tasked with generating synthetic test data for the RealtyX application.

Context:
- Scale: {{scale}}
- Locale: {{locale}}
- Fidelity: {{fidelity}}
- Image source: {{image_source}}
- Output path: c:/Users/289544/Downloads/realtyx/data

Requirements:
1. Provide a schema for each JSON file to be generated (properties.json, agents.json, buyers.json, sellers.json, admin_users.json, brokers.json, reviews.json).
2. Produce sampling strategies for addresses, prices, property types, bedrooms, bathrooms, square footage, lat/long ranges, and timestamps.
3. Provide a script skeleton (Node.js or Python) that generates the requested number of records and writes to JSON files in the `data` folder.
4. Include optional steps to fetch images from Unsplash (rate-limit safe) or attach placeholder image URLs.
5. Provide quick verification checks (counts, sample records, simple schema validation).
6. Suggest seeding strategies for deterministic tests (use a seed value).

Deliverables:
- JSON schemas
- Script template (Node.js preferred for the project)
- Example command to run the generator
- Validation steps (quick Node.js script to assert schema conformance)

Example usages:
- Generate 10,000 properties across 5 major Indian cities
- Create 500 agents with linked properties
```

Developer tips:
- Prefer streaming JSON writes for very large datasets to avoid memory spikes.
- Use a deterministic PRNG with a seed for reproducible datasets.
- Respect Unsplash API terms and optionally cache downloaded thumbnails.