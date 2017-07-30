#! /bin/bash

set -e

cd spring-boot-restful-service

./gradlew clean

cd ../spring-boot-webapp

./gradlew clean
