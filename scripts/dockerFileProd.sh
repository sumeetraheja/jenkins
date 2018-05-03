#!/usr/bin/env bash
echo "FROM openjdk:8-jre-alpine" > Dockerfile
echo "MAINTAINER 'Middleware Team'" >> Dockerfile
echo "VOLUME /tmp" >> Dockerfile
echo "EXPOSE 8082" >> Dockerfile
echo "ADD target/${PROJECT_NAME}.war app.war" >> Dockerfile
echo "ADD /properties/${PROJECT_NAME}/application-prod.yml application-prod.yml" >> Dockerfile
echo "CMD java $JAVA_OPTS -jar app.war --spring.config.location=application-prod.yml" >> Dockerfile

echo "Dockerfile is `cat Dockerfile`"

cp Dockerfile ./src/main/resources/Dockerfile