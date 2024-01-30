# Shareit

## Стек: Java, Spring Boot, Docker, PostgreSQL, Hibernate, Maven, JUnit5, Mockito, RESTful API, MapStruct, Lombok

Сервис решает проблему связанную с необходимостью приобретения вещей для временного использования. Вместо того чтобы покупать новую вещь, пользователи могут найти ее на сервисе и взять в аренду на определенное время. Это позволяет экономить деньги и ресурсы, а также уменьшает нагрузку на окружающую среду.

Сервис для шеринга вещей предоставляет возможность пользователям, во-первых, возможность рассказывать, какими вещами они готовы поделиться, а во-вторых, находить нужную вещь и брать её в аренду на какое-то время. 
Сервис не только позволяет бронировать вещь на определённые даты, но и закрывает к ней доступ на время бронирования от других желающих. На случай, если нужной вещи на сервисе нет, у пользователей есть возможность оставлять запросы. 

## Микросервисная архитектура
Приложение состоит из 2 сервисов:

* Gateway. Принимает запросы от пользователей. Распределяет нагрузку, выполняет первичную проверку и направляет запросы дальше в основной сервис
* Server. Серверная часть приложения. Получает запросы, выполняет операции, отправляет данные клиенту

## Установка и запуск проекта
1. Необходимо настроенная система виртуализации, установленный Docker Desktop(скачать и установить можно с официального сайта https://www.docker.com/products/docker-desktop/)

2. Клонируйте репозиторий проекта на свою локальную машину:  
git clone https://github.com/Julia3202/java-shareit

3. Запустите коммандную строку и перейдите в коррень директории с проектом.

4. Соберите проект  
mvn clean package

5. Введите следующую команду, которая подготовит и запустит приложение на вашей локальной машине  
$  docker-compose up

Приложение будет запущено на порту 8080. Вы можете открыть свой веб-браузер и перейти по адресу [http://localhost:8080/] ,
чтобы получить доступ к приложению Share It.

![diagram](https://github.com/Julia3202/java-shareit/blob/main/BD%20shareit.jpg).
