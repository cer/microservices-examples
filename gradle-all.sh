#! /bin/bash -e

for dir in spring-boot-*; do
	(cd $dir ; ./gradlew -b build.gradle $*)
done
