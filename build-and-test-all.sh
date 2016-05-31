#! /bin/bash

set -e

if [ -z "$DOCKER_HOST_IP" ] ; then
  if which docker-machine >/dev/null; then
    export DOCKER_HOST_IP=$(docker-machine ip default)
  else
    export DOCKER_HOST_IP=localhost
 fi
 echo set DOCKER_HOST_IP $DOCKER_HOST_IP
fi

docker-compose stop
docker-compose rm -v --force

docker-compose up -d rabbitmq mongodb

cd eureka-server
./gradlew build

cd ..

cd spring-boot-restful-service

export SPRING_RABBITMQ_HOST=${DOCKER_HOST_IP?}
export SPRING_DATA_MONGODB_URI=mongodb://${DOCKER_HOST_IP?}/userregistration

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
