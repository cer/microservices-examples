docker run -d -p 8080:8080 \
-e USER_REGISTRATION_URL  \
--name sb_web \
sb_web

echo Open this webpage: http://${DOCKER_HOST_IP?}:8080/register.html