#! /bin/bash

set -e

export DOCKER_HOST_IP=$(boot2docker ip)

docker-compose up -d --no-recreate rabbitmq mongodb

cd spring-boot-restful-service

export SPRING_RABBITMQ_HOST=${DOCKER_HOST_IP?}
export SPRING_DATA_MONGODB_URI=mongodb://${DOCKER_HOST_IP?}/userregistration

./gradlew build

cd ..

docker-compose up -d --no-deps restfulservice

echo -n waiting for restfulservice to start..

set +e

echo Launching RESTful service in Docker container
echo This takes about 30 seconds...

while [[ true ]]; do
        nc -z -w 4 ${DOCKER_HOST_IP?} 8081
        if [[ "$?" -eq "0" ]]; then
                echo connected
                break
        fi
        echo -n .
        sleep 1
done

set -e

cd spring-boot-webapp

export USER_REGISTRATION_URL=http://${DOCKER_HOST_IP?}:8081/user

./gradlew build

cd ..

set +e

echo Launching web app in Docker container
echo This takes about 30 seconds...

docker-compose up -d --no-deps web

while [[ true ]]; do
        nc -z -w 4 ${DOCKER_HOST_IP?} 8080
        if [[ "$?" -eq "0" ]]; then
                echo connected
                break
        fi
        echo -n .
        sleep 1
done

set -e

echo The microservices are running
echo You can visit these URLS
echo http://${DOCKER_HOST_IP?}:8080/register.html - registration
echo http://${DOCKER_HOST_IP?}:8761 - Eureka console
echo http://${DOCKER_HOST_IP?}:8081/swagger-ui.html - the Swagger UI
