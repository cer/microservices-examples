#! /bin/bash

set -e

./build-docker-images.sh

docker-compose -f docker-compose-images.yml up -d

./wait-for-running-system.sh

#echo -n Sleeping for service discovery ...
#sleep 30
#echo ... running

set +e
(cd e2e-test ; ./gradlew cleanTest test)
set -e
(cd e2e-test ; ./gradlew cleanTest test)

docker-compose stop
docker-compose rm -v --force
