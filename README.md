# Komikku Source Backend (Spring Boot + Firebase)

Starter Spring Boot application that can power a custom Komikku source.
It includes:

- Firebase ID token authentication (Firebase Admin SDK)
- Firebase Storage upload/delete service hooks
- Source REST endpoints for search, manga details, chapters, and pages
- User sync endpoints for bookmarks/history
- OpenAPI docs and Actuator health endpoints

## Tech Stack

- Java 17
- Spring Boot 3.3
- Spring Security (stateless bearer auth)
- Spring Validation + Actuator + Cache
- Firebase Admin SDK
- OpenAPI (springdoc)

## Run Locally

```bash
mvn spring-boot:run
```

By default, Firebase is disabled and a dev token is used:

- `Authorization: Bearer dev-token`

## Enable Firebase

Set values in `src/main/resources/application.yml` or environment variables:

- `firebase.enabled=true`
- `firebase.project-id=<your-project-id>`
- `firebase.storage-bucket=<your-bucket-name>`
- `firebase.credentials-path=/absolute/path/to/service-account.json`

When enabled, bearer tokens are verified via Firebase Auth.

## API Overview

Base path: `/api/v1`

### Auth

- `GET /auth/me`

### Komikku source endpoints

- `GET /source/search?query=<q>&page=0&size=20`
- `GET /source/manga/{mangaId}`
- `GET /source/manga/{mangaId}/chapters?page=0&size=20`
- `GET /source/chapters/{chapterId}/pages`

### Storage endpoints

- `POST /storage/uploads` (multipart: `path`, `file`)
- `DELETE /storage/objects?path=<objectPath>`

### User sync endpoints

- `GET /users/me/bookmarks`
- `POST /users/me/bookmarks`
- `GET /users/me/history`
- `POST /users/me/history`

## OpenAPI + Health

- Swagger UI: `/swagger-ui.html`
- OpenAPI JSON: `/v3/api-docs`
- Health: `/actuator/health`