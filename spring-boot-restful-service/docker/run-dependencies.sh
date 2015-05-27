docker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq dockerfile/rabbitmq

docker run -d -p 27017:27017 --name mongodb dockerfile/mongodb mongod --smallfiles



