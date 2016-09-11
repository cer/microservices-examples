#! /bin/bash

set -e

. ./set-env.sh
docker-compose stop
docker-compose rm -v --force

docker-compose up -d rabbitmq mongodb

cd eureka-server
./gradlew build

cd ../zipkin-server
./gradlew build

cd ..

cd spring-boot-restful-service

./gradlew build

cd ..

docker-compose up -d restfulservice

echo -n waiting for restfulservice to start..


echo Launching RESTful service in Docker container
echo This takes about 30 seconds...

./wait-for-services.sh ${DOCKER_HOST_IP?} /health 8081
./wait-for-services.sh ${DOCKER_HOST_IP?} /eureka/apps/REGISTRATION-SERVICE 8761

cd spring-boot-webapp

export USER_REGISTRATION_URL=http://${DOCKER_HOST_IP?}:8081/user

./gradlew build

docker-compose stop
docker-compose rm -v --force
