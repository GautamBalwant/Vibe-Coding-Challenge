
# Prompt: Analytics – GA4 Events

**Goal**: Track key events from SSR UI.

**Instructions**
- Include GA4 script in base layout when `process.env.GA_ID` exists.
- Fire events: `search_performed`, `filter_applied`, `listing_viewed`, `lead_submitted`, `favourite_added`.
- Ensure consent banner and opt-out.

**Acceptance Criteria**
- Events visible in GA4 realtime; respect privacy settings.
