#! /bin/bash -e

for dir in spring-boot-* zipkin-server eureka-server ; do
	(cd $dir ; ./gradlew -b build.gradle $*)
done
