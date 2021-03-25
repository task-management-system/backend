# Overview

## What tools/frameworks do we use?

- **[Kotlin](https://kotlinlang.org/)** - our language of choice
- **[Gradle](https://gradle.org/)** - our build system of choice (using groovy)
- **[Ktor](https://github.com/ktorio/ktor)** - for creating web application
- **[ExposedSQL](https://github.com/JetBrains/Exposed)** - to access database
- **[HikariCP](https://github.com/brettwooldridge/HikariCP)** - for high-performance JDBC connection pool
- **[Koin](https://insert-koin.io/)** - for dependency injection
- **[PostgreSQL](https://www.postgresql.org/)** - for database
- **[HOCON](https://github.com/lightbend/config/)** - for application configuration
- **[Gson](https://github.com/google/gson)** - for JSON serialization/deserialization

## Goal

### Закрыть дипломку (◕‿◕)

## Instruction

### Run server with docker

#### You can get the .env file from pm [Muslim](https://github.com/SkyfaceD) or directly from [draft](https://github.com/task-management-system/draft/blob/main/.env) repository. (Don't ask me why I did this and not this. I don't know myself) 

#### Default listen ports:

- backend: 7000
- database: 7100

#### Requirements for Windows users

1. [docker](https://docs.docker.com/get-docker/) (I am not joking, it's really necessary tool)
2. [cmder](https://cmder.net/) or [conemu](https://conemu.github.io/en/Downloads.html) (I hope conemu has gzip tool
   installed, if not you can install it yourself or just download cmder)

#### TL;DR

Just type one simple command for up whole server (Currently unstable, if do nothing or exit with error see 'full
instruction' section)

```
muslim do-magic
```

#### Full instruction

```
gradlew clean build 
gzip -d -k assets/*.gz
docker compose create --build
docker compose start database
cat assets/*sql | docker exec -i tms-database psql -U dev -d tms
docker compose start backend
```

Explanation of each row

1. Build fat.jar
2. Unzip database dump
3. Create services from zero
4. Run database
5. Copy all data from sql file and put it on psql as dev to insert dump to tms database
6. Run backend

#### Cheat sheet

```
create extension "uuid-ossp" //Extension for generate uuid's, build in postgresql

gradlew clean build -x test //Exclude test task when build

docker exec -i database pg_dump -U dev -d tms -p 7100 > dump.sql //Create db dump from container. Passed options (-U user; -d database; -p listen port)
docker exec -i database pg_dump -U dev -d tms -p 7100 | gzip -9 > dump.sql.gz //With compress. Passed option (-9 or --best best compress value)
docker exec -i database pg_dump -U dev -d tms -p 7100 > dump_%date%_%time:~0,8%.sql //With current datetime.

cat dump.sql | docker exec -i tms-database psql -U dev -d tms //Restore db dump into container
```