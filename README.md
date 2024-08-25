# Cloudify

![Overview](image-1.png)

## Overview
Multi-tenant file cloud. Users of the service can use it to upload and store files. The source of inspiration for the project is Google Drive. The idea is taken from [here](https://zhukovsd.github.io/java-backend-learning-course/projects/cloud-file-storage/). Frontend Repository of this project is [here](https://github.com/RodionMas/cloudify)

## Technologies / tools used:

### Backend

![java](https://github.com/VladislavLevchikIsAProger/tennis_scoreboard/assets/153897612/bc1ab298-7a78-42ec-8813-05b38668310e)
![spring-boot](<Spring boot (2).png>)
![spring-data-jpa](<Spring Data JPA (2).png>)
![spring-security](<Spring Security (2).png>)
![spring-sessions](<Spring Sessions (2).png>)
![gradle](Gradle.png)
![liquibase](<Liquibase (2).png>)
![hibernate](https://github.com/VladislavLevchikIsAProger/tennis_scoreboard/assets/153897612/071df0a5-79ef-4435-9c98-5a9b2383d420)

### Data

![minio](<Minio (2).png>)
![postgresql](https://github.com/VladislavLevchikIsAProger/weather_tracker/assets/153897612/8922bdba-ad57-4d69-b68c-ec505fff82e0)
![redis](<Redis (2).png>)

### Testing

![junit-logo](https://github.com/VladislavLevchikIsAProger/tennis_scoreboard/assets/153897612/a1a05826-fecb-4b7a-827c-946ffc72da32)
![tcontainers](<Docker TestContainers (2).png>)

### Deploy

![dockerfile](https://github.com/VladislavLevchikIsAProger/weather_tracker/assets/153897612/e22a80da-ca5a-438b-a5f5-605393f3208d)
![docker-compose](https://github.com/VladislavLevchikIsAProger/weather_tracker/assets/153897612/82390fb8-e6d4-4b15-b175-78eead5bc360)

### Docs API

![swagger](<Swagger (2).png>)

## Database diagram

![diagram](image.png)

## Swagger

You can test the application by going to http://localhost:8080/swagger-ui/index.html after launching the project (installation below). Or insert openapi.yaml into Postman or Insomnia

## Requirements Back
  + Java 17+
  + Intellij IDEA
  + Docker

## Requirements Front
  + Visual Studio
  + Node.js

## Project launch(Back)

+ Clone the repository:

   ```
   git clone https://github.com/VladislavLevchikIsAProger/cloud_file_storage.git
   ```
+ Open your cloned repository folder in Intellij IDEA
  
+ Open the console(Alt + F12) and type `docker-compose up -d`
  
  ![Screen-cmd](https://github.com/VladislavLevchikIsAProger/weather_tracker/assets/153897612/c2db9f1a-7b9e-4762-8fba-ee70cd3f49a7)

+ Start project

## Project launch(Front)

  + Clone the repository:

    ```
    https://github.com/RodionMas/cloudify.git
    ```

  + Open Visual Studio. Select File -> Open Folder and chose cloned folder
  
  + Open the console and type
    ```
    npm i
    npm start
    ```

  + And in the browser at localhost:3000, the visaul part will be available
## Communication
My Telegram - https://t.me/IamNotARapperr
