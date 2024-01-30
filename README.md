# Shareit

## Стек: Java, Spring Boot, Docker, PostgreSQL, Hibernate, Maven, JUnit5, Mockito, RESTful API, MapStruct, Lombok

Сервис решает проблему связанную с необходимостью приобретения вещей для временного использования. Вместо того чтобы покупать новую вещь, пользователи могут найти ее на сервисе и взять в аренду на определенное время. Это позволяет экономить деньги и ресурсы, а также уменьшает нагрузку на окружающую среду.

Сервис для шеринга вещей предоставляет возможность пользователям, во-первых, возможность рассказывать, какими вещами они готовы поделиться, а во-вторых, находить нужную вещь и брать её в аренду на какое-то время. 
Сервис не только позволяет бронировать вещь на определённые даты, но и закрывает к ней доступ на время бронирования от других желающих. На случай, если нужной вещи на сервисе нет, у пользователей есть возможность оставлять запросы. 

# Микросервисная архитектура
Приложение состоит из 2 сервисов:

* Gateway. Принимает запросы от пользователей. Распределяет нагрузку, выполняет первичную проверку и направляет запросы дальше в основной сервис
* Server. Серверная часть приложения. Получает запросы, выполняет операции, отправляет данные клиенту

![diagram](https://github.com/Julia3202/java-shareit/blob/main/BD%20shareit.jpg).
