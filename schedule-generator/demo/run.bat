@echo off
echo Starting Spring Boot application...
cd %~dp0
call mvn spring-boot:run
pause 