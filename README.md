# Todo API - Spring Boot REST API

REST API для управления задачами (CRUD операции).

## Технологии

- Java 17
- Spring Boot 4.0
- Maven
- In-memory storage (ArrayList)

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