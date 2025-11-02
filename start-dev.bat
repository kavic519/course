@echo off
echo 启动开发环境 (H2)...
set SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run -Dspring-boot.run.profiles=dev
pause