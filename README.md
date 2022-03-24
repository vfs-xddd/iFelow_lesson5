#Экзамен. Задание 2

###Описание:
Разработать проект автотестов с использованием Cucumber, junit, maven, restassured allure.    
За основу тестов взять Лекцию с api тестами задача 1, 2.    
Тестируемые сайты:    
https://rickandmortyapi.com/    
https://reqres.in/.

###Сборка:
Maven, Cucumber, Junit5, Allure.

###Входные данные:
Входные константы берутся из resources/test.properties. Реализовано через System properties.

###Запуск:
`mvn clean test -Dcucumber.filter.tags="@All"`

###Построение отчета:
`mvn allure:serve`

