#!/usr/bin/env bash
env

MAJOR_VERSION=1
ENV_NAME="prod"
GIT_REVISION_SHORT=`git rev-parse --short HEAD`
echo "GIT_REVISION_SHORT=$GIT_REVISION_SHORT" > build.properties
echo "MAJOR_VERSION=$MAJOR_VERSION" >> build.properties
DOCKER_IMAGE_TAG=${MAJOR_VERSION}.${BUILD_TIMESTAMP}.${GIT_REVISION_SHORT}
echo "DOCKER_IMAGE_TAG=$DOCKER_IMAGE_TAG" >> build.properties
echo "ENV_NAME=$ENV_NAME" >> build.properties
echo "build.properties is `cat build.properties`"
echo "GIT_TAG=$DOCKER_IMAGE_TAG" >> build.properties
cp build.properties ./src/main/resources/build.properties