# Todo API - Spring Boot REST API

REST API для управления задачами с использованием PostgreSQL.

## Технологии

- Java 17
- Spring Boot 4.0
- Spring Data JPA
- PostgreSQL 18
- Maven

## Требования

- JDK 17+
- PostgreSQL 18+
- Maven

## Настройка базы данных

1. Установите PostgreSQL
2. Создайте базу данных:
```sql
CREATE DATABASE todoapi_db;
```

3. Настройте `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todoapi_db
spring.datasource.username=postgres
spring.datasource.password=ваш_пароль
```

## API Endpoints

### Создать задачу
```
POST /tasks
Content-Type: application/json

{
  "title": "Learn Spring Boot",
  "description": "Create CRUD API"
}
```

### Получить все задачи
```
GET /tasks
```

### Получить задачу по ID
```
GET /tasks/{id}
```

### Удалить задачу
```
DELETE /tasks/{id}
```

## Запуск
```bash
./mvnw spring-boot:run
```

Приложение запустится на `http://localhost:8080`
