#! /bin/bash

export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
export DOCKER_HOST_IP=$(boot2docker ip 2>/dev/null)

export SPRING_DATA_MONGODB_URI=mongodb://${DOCKER_HOST_IP}/userregistration
export SPRING_RABBITMQ_HOST=${DOCKER_HOST_IP}
export USER_REGISTRATION_URL=http://${DOCKER_HOST_IP}:8081/user

