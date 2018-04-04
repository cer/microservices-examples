#! /bin/bash -e

if [ -z "$DOCKER_HOST_IP" ] ; then
    if [ -z "$DOCKER_HOST" ] ; then
      export DOCKER_HOST_IP=`hostname`
    else
      echo using ${DOCKER_HOST?}
      XX=${DOCKER_HOST%\:*}
      export DOCKER_HOST_IP=${XX#tcp\:\/\/}
    fi
fi

export SPRING_RABBITMQ_HOST=${DOCKER_HOST_IP?}
export SPRING_DATA_MONGODB_URI=mongodb://${DOCKER_HOST_IP?}/userregistration
