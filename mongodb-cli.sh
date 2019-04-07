#! /bin/bash

docker run --rm --network microservices-examples_default -i -t mongo:3.0.4 /usr/bin/mongo --host mongodb
