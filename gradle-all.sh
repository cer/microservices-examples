#! /bin/bash -e

for dir in spring-boot-* zipkin-server eureka-server hystrix-dashboard; do
	(cd $dir ; ./gradlew -b build.gradle $*)
done
