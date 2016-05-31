#! /bin/bash

set -e

./build-and-test-all.sh

echo Launching services in Docker containers
echo This can take a while

docker-compose up -d

./wait-for-services.sh ${DOCKER_HOST_IP?} /health 8080 8081

./show-urls.sh
