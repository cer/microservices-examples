A simple example of microservices that is described in this [blog post](http://plainoldobjects.com/2014/11/16/deploying-spring-boot-based-microservices-with-docker/).

The services are built using

* Spring Boot
* Spring Cloud
* Netflix OSS Eureka
* RabbitMQ

There is also a [microservices example that uses event sourcing](https://github.com/cer/event-sourcing-examples).

Building and running the microservices
===

You can find instructions for building the services in the READMEs.
Start by [building the RESTful backend service](spring-boot-restful-service).
Once you have build the services, you can launch them by running `docker-compose up -d`.

The quick version
==

The script `build-and-test-all.sh` might work for you depending on your machine.
