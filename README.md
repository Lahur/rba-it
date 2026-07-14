# RBA IT

Spring Boot backend (Java, Gradle, H2, Kafka, Flyway) and React frontend for the card request service.

## Prerequisites

**Backend**
- **JDK 25** (Eclipse Temurin recommended) — required by the Gradle toolchain in `build.gradle`
- **Docker & Docker Compose** (optional) — convenient way to run Kafka broker used by the `dev`/`docker` profiles, or to run the whole stack in containers via `docker-compose.yml`. Not required if you point the app at an existing/remote Kafka installation instead.
- **Git**

No local Gradle installation is required — the project uses the Gradle Wrapper (`./gradlew`), which downloads the correct Gradle version on first run.

> Running the unit/integration test suite (`./gradlew test`) does **not** require Docker — tests run against an in-memory H2 database and Kafka autoconfiguration is disabled.

**Frontend (`rba-it-react`)**
- **Node.js 22.x** and npm — matches the version used in the Docker build image (`node:22-alpine`)
- Backend API running — the frontend calls the API at the URL configured by `VITE_API_BASE_URL` (defaults to `http://localhost:8080/api` in `rba-it-react/.env`)

## Running the backend locally (dev profile)

Start the supporting services (Kafka + Zookeeper):

```bash
docker compose --profile kafka up -d
```

> Docker is not mandatory here — any local or remote Kafka installation works. Just point `spring.kafka.bootstrap-servers` (in `application-dev.properties`) at it instead.

Then run the app:

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

The API is served under `http://localhost:8080/api`.

## Running backend tests

```bash
./gradlew test
```

## Running the frontend locally

```bash
cd rba-it-react
npm install
npm run dev
```

The dev server runs at `http://localhost:5173`.

Other useful commands (run from `rba-it-react/`):

```bash
npm run build    # production build
npm run lint     # lint the codebase
```

## Running the full stack in Docker

```bash
docker compose --profile app up --build
```

This builds and starts the backend, H2, Kafka, and the React frontend together. Once it's up, the following are available:

| Service | URL |
| --- | --- |
| Frontend | [http://localhost:5173](http://localhost:5173) |
| Backend API | [http://localhost:8080/api](http://localhost:8080/api) |
| H2 web console | [http://localhost:81](http://localhost:81) |
| Kafka UI | [http://localhost:8100](http://localhost:8100) |
| Kafka broker | `localhost:9092` |
