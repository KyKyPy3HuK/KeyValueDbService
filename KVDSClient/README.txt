# KVDSClient

Пример простого приложения использующего библиотеку-класс KVDSConnecrion
нео
Сборка:
mvn clean install

Запуск Microsoft PowerShell:
java -jar .\target\KVDSClient-1.0-SNAPSHOT.jar

Запуск bash:
java -jar target/KVDSClient-1.0-SNAPSHOT.jar

По умолчанию приложение отправляет запросы к сервису по адресу "http://localhost:8080", для изменения адреса сервиса необходимо изменить строку адреса в конструктре экземпляра KVDSConnecrion в методе nain нахрдящимся в фале src\main\java\com\punk_pozer\Main.java
