#!/bin/sh

echo Building...
mvn clean compile assembly:single
echo Launching...
java -jar target/RPGMaker-1.0-jar-with-dependencies.jar