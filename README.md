# Exchange Platform

Monorepo with microservices for a currency transfer platform.

## Services
- `gateway` – API gateway with routing, JWT check
- `currency-service` – Transfers, balance, Kafka, MongoDB
- `user-service` – User registration, email, phone, PostgreSQL

## Run

```bash
docker-compose up --build
