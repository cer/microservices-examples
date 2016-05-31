#! /bin/bash -e

./wait-for-running-system.sh

echo The microservices are running
echo You can visit these URLS
echo http://${DOCKER_HOST_IP?}:8080/register.html - registration
echo http://${DOCKER_HOST_IP?}:8761 - Eureka console
echo http://${DOCKER_HOST_IP?}:8081/swagger-ui.html - the Swagger UI
