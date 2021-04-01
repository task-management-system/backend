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
2. [cmder](https://cmder.net/)

#### TL;DR

Just type one simple command for up whole server (Currently unstable, if do nothing or exit with error
see [full instruction](https://github.com/task-management-system/backend/tree/feat/global-refactor#full-instruction)
section)

```
muslim do-magic
```

#### Full instruction

```
gradlew clean build 
gzip -d -k assets/*.gz OR manually unzip
docker compose create --build
docker compose start tms-database
cat assets/*sql | docker exec -i tms-database psql -U seasky-developer -d tms
docker compose start tms-backend
```

Explanation of each row

1. Build fat.jar
2. Unzip database dump
3. Create services from zero
4. Run database
5. Copy all data from sql file and put it on psql as dev to insert dump to tms database
6. Run backend

## Cheat sheet

```
create extension "uuid-ossp" //Extension for generate uuid's, build in postgresql

gradlew clean build -x test //Exclude test task when build

docker exec -i $container pg_dump -U $user -d $database -p $port > dump.sql //Create db dump from container. Passed options (-U user; -d database; -p listen port)
docker exec -i $container pg_dump -U $user -d $database -p $port | gzip -9 > dump.sql.gz //With compress. Passed option (-9 or --best best compress value)
docker exec -i $container pg_dump -U $user -d $database -p $port > dump_%date%_%time:~0,8%.sql //With current datetime.

cat *.sql | docker exec -i $container psql -U $user -d $database //Restore db dump into container
```

# Wiki

## Database rules

1. Name of the tables in the singular.
2. For multiple words use `snake_case` for both table and column names.
3. Override default constraint names, ex. in table below.

   Name|Value|Example
      ---|---|---
   Primary Key|pk_$tableName_$columnName|constraint pk_user<br>primary key(id)
   Foreign Key|fk_$tableName_$columnName_$referenceColumnName|constraint fk_user_role_id_id<br>foreign key(role_id)<br>references role(id)
   Unique|uq_$tableName_$columnName|constraint uq_user_username<br>unique(username)
   Check|ch_$tableName_$columnName|constraint ch_user_username<br>check(length(username) >= 4)
4. For UUID tables use extension `uuid-ossp`, for default value use a fourth version of uuid generator.

   Ex.

   ```
   create extension if not exists "uuid-ossp";
   create table user(
      id uuid default uuid_generate_v4(),
      ...
   )
   ```
5. For a password storing use `pgcrypto` extension.

   Ex.
   ```
   create extension if not exists "pgcrypto";
   create table "user"(
      ...
      "password" text not null,
      ...
   )
   
   insert into "user"(username, password)
   values('username', crypt('pass', gen_salt('bf')))
   
   select username, (password = crypt('pass', password)) as password_match from "user"
   ```