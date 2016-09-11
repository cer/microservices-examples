#! /bin/bash

set -e

docker-compose stop
docker-compose rm -v --force

docker-compose up -d

./wait-for-running-system.sh

#echo -n Sleeping for service discovery ...
#sleep 30
#echo ... running

./register-user.sh

set +e
(cd e2e-test ; ./gradlew cleanTest test)
set -e
(cd e2e-test ; ./gradlew cleanTest test)

docker-compose stop
docker-compose rm -v --force
