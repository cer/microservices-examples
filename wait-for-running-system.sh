#! /bin/bash -e

DOCKER_HOST_IP='localhost'
./wait-for-services.sh ${DOCKER_HOST_IP?} /health 8080 8081
./wait-for-services.sh ${DOCKER_HOST_IP?} /eureka/apps/REGISTRATION-SERVICE 8761
