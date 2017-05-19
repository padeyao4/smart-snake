@echo off
@title java8-woaiwaixr

cd %~dp0

if not exist target ( mvn clean package )

java -jar target/smart-snake-0.0.1-SNAPSHOT-jar-with-dependencies.jar

