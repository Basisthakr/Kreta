# Kreta

Kreta is a subscription and payment processing backend built with Spring Boot and Stripe. It handles user registration with Stripe customer creation, subscription lifecycle management, API key generation and rate limiting, usage-based billing, webhook processing with idempotency, and grace period logic for failed payments.

Kreta is Hindi for buyer or payee.


## Tech Stack

- Spring Boot 4
- PostgreSQL
- Redis
- Stripe Java SDK
- JWT authentication


## Setup

### Prerequisites

- Java 21 or higher
- PostgreSQL running locally
- Redis running locally
- A Stripe account in test mode

### Environment Variables

```
STRIPE_SECRET_KEY=sk_test_...
STRIPE_WEBHOOK_SECRET=whsec_...
DB_URL=jdbc:postgresql://localhost:5432/kreta
DB_USERNAME=your_db_user
DB_PASSWORD=your_db_password
JWT_SECRET=your_jwt_secret
REDIS_HOST=localhost
REDIS_PORT=6379
```

### Running

1. Clone the repo
2. Set the environment variables above
3. Create a PostgreSQL database named kreta
4. Run the application. Hibernate creates the tables on startup. The DataInitializer seeds the plans table with FREE, BASIC, PRO, and MAX tiers.


## Why the Schema Looks the Way it Does

Stripe is a separate service with its own database. Kreta has no access to Stripe's tables. The only way the two systems can talk about the same person or the same subscription is through shared IDs.

When a user registers, Kreta makes an API call to Stripe asking it to create a customer for that email. Stripe creates a record in its own system and returns a customer ID that looks like cus_abc123. Kreta stores this on the User row. Every future Stripe call that involves this person passes that customer ID so Stripe knows who Kreta is referring to.

When a user subscribes to a plan, Kreta sends Stripe the customer ID and the price ID of the chosen plan. Stripe creates a subscription and returns a subscription ID that looks like sub_xyz456. Kreta stores this on the Subscription row. When Stripe sends webhook events about a billing cycle, those events contain the subscription ID. Kreta uses it to find the right row and update it.

### Plans table

Seeded once at startup by a class that implements CommandLineRunner. Only changes when the business changes its pricing. Stores Stripe Price IDs for monthly and yearly billing, alongside local limits like API call limit per month, rate limit per minute, and max API keys allowed. Kreta checks these limits locally using Redis without calling Stripe on every request.

### Subscription table

Keeps one row per subscription period per user, including past ones. This is the full billing history. If a user contacts support saying they paid for a subscription and did not receive access, the history is there to verify. The current active subscription is identified by status. Stripe sends the billing period start and end dates in webhook payloads. Grace period expiry and access suspension are Kreta's own fields and Stripe has no knowledge of them.

### Invoice table

Created from Stripe webhook events. When Stripe fires invoice.paid or invoice.payment_failed, Kreta creates or updates a row here. Stores the Stripe-hosted invoice URL so users can view and download their invoice PDF without Kreta calling Stripe again. References the Subscription table since an invoice is for a specific subscription billing cycle.

### API keys table

Stores SHA-256 hashes of API keys, not the plaintext. SHA-256 is used instead of BCrypt because API keys are long random strings with enough entropy that slow hashing is not needed for security, and BCrypt's intentional slowness would add unacceptable latency to every API request. The plaintext key is shown to the user exactly once on creation. The prefix column stores the first several characters in plaintext so users can identify which key is which in their dashboard.

### Usage records table

Tracks API call counts per user per billing period. Redis handles real-time counting and rate limit enforcement. This table receives periodic flushes from Redis every few minutes and is used to report usage to Stripe for usage-based billing calculations at the end of each period. The reportedToStripe flag tracks what has already been sent to Stripe.

### Webhook events table

This is the idempotency log. Stripe guarantees at least once delivery, meaning the same event can arrive more than once due to network issues or timeouts. Before processing any incoming event, Kreta checks if that event ID already exists in this table. If it does, the event is skipped and Kreta returns 200 to Stripe. If it does not exist, Kreta processes the event and inserts the ID. Rows are never updated or deleted. The presence of a row is the proof that the event was handled.


## Notes

- All monetary values are stored in paise to avoid floating point errors. 99900 means 999 rupees.
- Free plan users have a subscription row with stripeSubscriptionId set to null. PostgreSQL allows multiple null values in a unique column, so this works without constraint violations.
- API keys are prefixed with krt_live_ so users can identify them as Kreta keys.
- Stripe is the source of truth for billing. Kreta's database is the local mirror that the application queries at runtime to avoid calling Stripe on every request.


## API Endpoints

To be added as endpoints are built.