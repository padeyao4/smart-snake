#!/bin/env bash
if [ ! -d target ] ; then
    mvn clean package
fi

java -jar target/smart-snake-0.0.1-SNAPSHOT-jar-with-dependencies.jar
