FROM openjdk:8u171-jre-alpine
RUN apk --no-cache add curl
MAINTAINER chris@chrisrichardson.net
EXPOSE 8080
HEALTHCHECK --start-period=30s --interval=5s CMD curl -f http://localhost:8080/health || exit 1
CMD java -jar spring-boot-restful-service.jar
COPY build/libs/spring-boot-restful-service.jar .
