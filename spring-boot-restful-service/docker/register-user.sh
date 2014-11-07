curl -v -d '{"emailAddress": "foo@bar.com"}' -H "content-type: application/json" http://${DOCKER_HOST_IP}:8080/user

