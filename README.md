A simple example of microservices that is described in this [series of blog posts](http://plainoldobjects.com/2014/11/16/deploying-spring-boot-based-microservices-with-docker/).

There are two services:
* RESTful service - exposes a REST API for registering a user.
It saves the user registration in MongoDB and posts a message to RabbitMQ.
It is the example code for the article [Building microservices with Spring Boot - part 1](http://plainoldobjects.com/2014/04/01/building-microservices-with-spring-boot-part1/).

* Web application - implements the user registration UI and invokes the RESTful service.
It is the example code for the article [Building microservices with Spring Boot - part 2](https://plainoldobjects.com/2014/05/05/building-microservices-with-spring-boot-part-2/).

The services are written in Scala and use the following technologies.

* Spring Boot
* Spring Cloud
* Netflix OSS Eureka
* RabbitMQ

Note: There are [other example microservice applications](http://eventuate.io/exampleapps.html).

# Building and running the microservices

This project uses with [Docker Compose](https://docs.docker.com/compose/) to run the services as well as RabbitMQ and MongoDB.

The `spring-boot-webapp` project uses Selenium to test the web UI using the Chrome browser.
You will need to install [ChromeDriver](https://sites.google.com/a/chromium.org/chromedriver/getting-started).
On Mac OSX you can run `brew install chromedriver`.


## The quick way

The quickest way to build and run the services on Linux/Mac OSX is with the following commands:

```
. ./set-env.sh
./gradle-all.sh assemble
docker-compose up -d
./show-urls.sh
```

Otherwise, follow these instructions.

## Running MongoDB and RabbitMQ

The RESTful service uses RabbitMQ and MongoDB.
The easier way to run them is to using Docker:

```
 docker-compose up -d mongodb rabbitmq
```

You also need to set some environment variables so that the services can connect to them:

```
export DOCKER_HOST_IP=$(docker-machine ip default 2>/dev/null)

export SPRING_DATA_MONGODB_URI=mongodb://${DOCKER_HOST_IP}/userregistration
export SPRING_RABBITMQ_HOST=${DOCKER_HOST_IP}
```

## Build the Eureka server

This application uses Netflix OSS Eureka for service discovery.
Build the Spring Cloud based Eureka server using the following commands:

```
cd eureka-server
./gradlew build
```

## Build the Zipkin server

This application uses Zipkin for distributed tracing.
Build the Zipkin server using the following commands:

```
cd zipkin-server
./gradlew build
```

## Building the RESTful service

Use the following commands to build the RESTful service:

```
 cd spring-boot-restful-service
 ./gradlew build    
 ```

 ## Running the RESTful service

 You can run the service by using the following command in the top-level directory:

 ```
 docker-compose up -d restfulservice
 ```

## Using the RESTful service

Once the service has started, you can send a registration request using:

```
./register-user.sh
```

You can examine the MongoDB database using the following commands

```
$ ./mongodb-cli.sh
> show dbs;
local             0.031GB
mydb              0.031GB
userregistration  0.031GB
> use userregistration;
switched to db userregistration
>
>
> show collections;
registeredUser
system.indexes
>
>
> db.registeredUser.find()
{ "_id" : ObjectId("55a99b0993860551c6020e9d"), "_class" : "net.chrisrichardson.microservices.restfulspringboot.backend.RegisteredUser", "emailAddress" : "1437178632863-b-foo@bar.com", "password" : "secret" }
> exit
$
```

## Building the web application

Since the web application invokes the RESTful service you must set the following environment variable:

```
export USER_REGISTRATION_URL=http://${DOCKER_HOST_IP}:8081/user
```

Next, use the following commands to build the web application:

 ```
cd spring-boot-webapp
./gradlew build
```

## Running the web application

Run the web application using the following command in the top-level directory:

```
docker-compose up -d web
```

## Using the web application

You can access the web application by visiting the following URL: `http://${DOCKER_HOST_IP?}:8080/register.html`

There are also other URLs that you can visit.
The following command will wait until the services are available and displays the URLs:

```
./show-urls.sh
```

# Building and running Docker images

The previous instructions deployed the services as Docker containers without actually packaging the services as Docker images.
The `docker-compose.yml` file ran the image `java:openjdk-8u91-jdk` and used volume mapping to make the Spring Boot jar files accessible.
Follow these instructions to build and run the Docker images.

## Building the images

You can build the images by running the following command:

```
./build-docker-images.sh
```

This script is a simple wrapper around `docker build`.

## Running the images

You can now run the Docker images using the `docker-compose` command with `docker-compose-images.yml`:

```
docker-compose -f docker-compose-images.yml up -d
```

The following command will wait until the services are available and displays the URLs:

```
./show-urls.sh
```
