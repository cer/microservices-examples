#! /bin/bash

set -e

KEEP_RUNNING=
USE_EXISTING=

while [ ! -z "$*" ] ; do
  case $1 in
    "--keep-running" )
      KEEP_RUNNING=yes
      ;;
    "--use-existing" )
      USE_EXISTING=yes
      ;;
    "--help" )
      echo ./build-and-test-all.sh --keep-running --use-existing
      exit 0
      ;;
  esac
  shift
done

. ./set-env.sh

if [ -z "$USE_EXISTING" ] ; then
  docker-compose down --remove-orphans
fi

docker-compose up -d rabbitmq mongodb

cd eureka-server
./gradlew build

cd ../zipkin-server
./gradlew build

cd ..

cd spring-boot-restful-service

./gradlew build

cd ..

docker-compose up -d  --build restfulservice

echo -n waiting for restfulservice to start..


echo Launching RESTful service in Docker container
echo This takes about 30 seconds...

./wait-for-services.sh ${DOCKER_HOST_IP?} /health 8081
./wait-for-services.sh ${DOCKER_HOST_IP?} /eureka/apps/REGISTRATION-SERVICE 8761

cd spring-boot-webapp

export USER_REGISTRATION_URL=http://${DOCKER_HOST_IP?}:8081/user

./gradlew build

cd ..

docker-compose up -d --build web

./wait-for-running-system.sh

./register-user.sh

echo User registered

if [ -z "$KEEP_RUNNING" ] ; then
  docker-compose down --remove-orphans
fi

echo Success
