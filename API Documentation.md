# UfanetTestProject API Documentation
Этот проект представляет собой коллекцию Postman для тестирования API системы управления бассейном. API поддерживает CRUD-операции для клиентов, бассейнов, бронирований и праздничных графиков работы.

Базовый URL
Все запросы отправляются на:

http://localhost:8090/api/v0/

## Эндпоинты
### 1. Управление бассейнами (/pool/)

```json
Создать бассейн
POST /pool/create
Тело запроса:
{
    "name": "Ufa arena pool",
    "maxCapacity": 300,
    "maxVisitsPerDay": 5
}
```

```json Обновить бассейн
POST /pool/{id}/update
Тело запроса:
{
    "name": "Ufa arena pool",
    "maxCapacity": 400,
    "maxVisitsPerDay": 5
}
```

```json
Получить доступные слоты
GET /pool/{id}/timetable/available?date=2025-05-14
```

```json
Получить занятые слоты
GET /pool/{id}/timetable/all?date=2025-05-14
```


### 2. Управление клиентами (/pool/client/)

```json
Добавить клиента
POST /pool/client/add
Тело запроса:

{
    "name": "Alexey",
    "surname": "Pavlov",
    "patronymic": "Dmitrievich",
    "number": "12345678",
    "email": "test@mail.ru"
}
```

```json
Обновить клиента
POST /pool/client/{id}/update
Тело запроса:

{
    "name": "test",
    "surname": "Pavlov",
    "patronymic": "Dmitrievich",
    "number": "12345678",
    "email": "test43@mail.ru"
}
```

```json
Получить клиента
GET /pool/client/{id}/get
```

```json
Получить всех клиентов
GET /pool/client/all
```

### 3. Бронирование (/pool/timetable/)

```json
Забронировать слот
POST /pool/timetable/{poolId}/reserve
Тело запроса:

{
    "clientId": 1,
    "dateTime": "2025-05-14T14:00"
}
```

```json
Забронировать интервал
POST /pool/timetable/{poolId}/reserve-interval
Тело запроса:

{
    "clientId": 1,
    "date": "2025-05-14",
    "start": "15:00",
    "end": "18:00"
}
```

```json
Отменить бронь
GET /pool/timetable/{poolId}/cancel
Тело запроса:

{
    "clientId": 1,
    "orderId": "112025-05-1416:00"
}
```

```json
Подтвердить бронь
POST /pool/timetable/{poolId}/confirm
Тело запроса:

{
    "clientId": 1,
    "orderId": "112025-05-1414:00"
}
```

```json
Найти бронь по ФИО
POST /pool/timetable/{poolId}/getByFIO
Тело запроса:

{
    "name": "Alexey",
    "surname": "Pavlov",
    "patronymic": "Dmitrievich"
}
```

```json
Найти бронь по дате
POST /pool/timetable/{poolId}/getByDate
Тело запроса:

{
    "clientId": 1,
    "reservationDate": "2025-05-14"
}
```

### 4. Праздничные графики (/pool/timetable/holiday/)

```json
Добавить праздник
POST /pool/timetable/holiday/add
Тело запроса:

{
    "date": "2025-05-20",
    "description": "20 мая",
    "openTime": "13:00",
    "closeTime": "19:00"
}
```

```json
Обновить праздник
PUT /pool/timetable/holiday/{id}/update
Тело запроса:

{
    "date": "2025-05-20",
    "description": "20 мая",
    "openTime": "10:00",
    "closeTime": "20:00"
}
```

```json
Получить все праздники
GET /pool/timetable/holiday/all
```
