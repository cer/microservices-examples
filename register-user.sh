curl -v -d "$(echo '{"emailAddress": "fooSUFFIX@bar.com", "password" : "secret1234"}' | sed -e "s/SUFFIX/$(date +%s)/")" -H "content-type: application/json" http://${DOCKER_HOST_IP}:8081/user
