@echo off
@title java8-woaiwaixr
if exist target (
   java -jar target/smart-snake-0.0.1-SNAPSHOT-jar-with-dependencies.jar
) else (
   mvn clean package
)
