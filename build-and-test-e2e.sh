#! /bin/bash

set -e

# Build and test the services

./build-test-and-run-all.sh

echo ================== Test the services launched with Docker Compose

./run-e2e-test.sh

echo ================== Test the services packaged as Docker images

./run-e2e-test-images.sh
