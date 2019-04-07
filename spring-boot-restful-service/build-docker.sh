#! /bin/bash -e

./gradlew assemble
docker build -t spring-boot-restful-service .
