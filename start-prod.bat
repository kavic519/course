@echo off
echo 启动生产环境 (MySQL)...
set SPRING_PROFILES_ACTIVE=prod
mvn spring-boot:run -Dspring-boot.run.profiles=prod
pause