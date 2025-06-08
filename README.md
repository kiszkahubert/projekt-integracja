# Zestawianie danych na temat minionych i trwających konfliktów zbrojnych z cenami surowców

## Stack
- Java 17 / TypeScript
- Spring Boot 3.4.6
- PostgreSQL
- Angular
- NGINX
- Docker / Docker Compose

## Instalacja i uruchomienie

1. Sklonuj repozytorium:
```bash
git clone [URL_REPOZYTORIUM]
```

2. Przejdź do katalogu projektu:
```bash
cd projekt-integracja
```

3. Uruchom aplikację za pomocą Docker Compose:
```bash
docker compose up -d
```

Aplikacja będzie dostępna pod następującymi endpointami:
- Frontend: http://localhost:4000
- Backend: http://localhost:8080
- Postgres: localhost:5432

## Struktura projektu
```
projekt-integracja/
├── frontend/           # Aplikacja Angular
├── src/                # Backend Spring Boot
├── baza-dump/          # Backup bazy danych
├── Dockerfile          # Dla obrazu backendu
├── Dockerfile.frontend # Dla obrazu frontendu
├── docker-compose.yml  # Pulluje obrazy z zewnętrznego registry więc nic nie trzeba budować
└── nginx.conf          # Congig servera NGINX
```
