####
# This Dockerfile is used in order to build a container that runs the Quarkus application in JVM mode
#
# Before building the docker image run:
#
# mvn package
#
# Then, build the image with:
#
# docker build -f Dockerfile --tag docker-alpine-build-package-maven-plugin/server-alpine-jvm  .
#
# Then run the container using:
#
# docker run -i --rm -p 64014:8080 --name server-alpine-jvm docker-alpine-build-package-maven-plugin/server-alpine-jvm
#
# If you want to include the debug port into your docker image
# you will have to expose the debug port (default 5005) like this :  EXPOSE 64014 5050
#
# Then run the container using :
#
# docker run -i --rm -p 64014:8080 -p 5005:5005 -e JAVA_ENABLE_DEBUG="true" --name server-alpine-jvm  docker-alpine-build-package-maven-plugin/server-alpine-jvm
#
###
FROM alpine:3.12.0

ARG JAVA_PACKAGE=openjdk11-jre-headless
ARG RUN_JAVA_VERSION=1.3.8
ARG INSTALLATION_FOLDER=/usr/local/lib/alpine
ENV INSTALLATION_FOLDER_ENV=${INSTALLATION_FOLDER}

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en'



# Install java and the run-java script
# Also set up permissions for user `1001`
RUN apk add openjdk11-jre-headless \
    && apk update \
    && apk add curl \
    && apk add vim \
    && apk add tree \
    && apk add busybox \
    && apk add leafpad \
    && apk add leafpad-lang \
    && apk add alpine-sdk \
    && apk --update add ttf-ubuntu-font-family \
    && apk update \
    && apk add curl ca-certificates ${JAVA_PACKAGE} \
    && apk update \
    && mkdir -p ${INSTALLATION_FOLDER} \
    && chown 1001 ${INSTALLATION_FOLDER} \
    && chmod "g+rwX" ${INSTALLATION_FOLDER} \
    && chown 1001:root ${INSTALLATION_FOLDER} \
    && curl https://repo1.maven.org/maven2/io/fabric8/run-java-sh/${RUN_JAVA_VERSION}/run-java-sh-${RUN_JAVA_VERSION}-sh.sh -o ${INSTALLATION_FOLDER}/run-java.sh \
    && chown 1001 ${INSTALLATION_FOLDER}/run-java.sh \
    && chmod 540 ${INSTALLATION_FOLDER}/run-java.sh


# Configure the JAVA_OPTIONS, you can add -XshowSettings:vm to also display the heap size.
ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

COPY target/lib/* ${INSTALLATION_FOLDER}/lib/
COPY target/*-runner.jar ${INSTALLATION_FOLDER}/app.jar

EXPOSE 64014

#
# Alpine settings
#

# Update file /etc/abuild.conf
RUN sed -i -e 's/#PACKAGER="Your Name <your@email.address>"/PACKAGER="dev <dev@email.address>"/g'  /etc/abuild.conf

RUN mkdir -p /home/dev \
    && adduser -D dev \
    && chown -R dev:dev /home/dev

RUN addgroup dev abuild

RUN mkdir -p /var/cache/distfiles
#RUN chmod a+w /var/cache/distfiles
RUN chgrp abuild /var/cache/distfiles
RUN chmod g+w /var/cache/distfiles
# RUN mkdir /home/dev/aport
# RUN ln -s /home/dev/aports /home/dev/aport

RUN   mkdir -p ${INSTALLATION_FOLDER}/scripts
COPY  src/main/docker/scripts/create_new_package.sh ${INSTALLATION_FOLDER}/scripts/
WORKDIR /home/dev/
# USER dev

RUN git config --global user.name "dev"
RUN git config --global user.email "dev@email.address"
RUN git clone git://git.alpinelinux.org/aports
# RUN abuild-keygen -n
RUN abuild-keygen -a -i

USER 1001

HEALTHCHECK --interval=5m --timeout=3s CMD curl -f http://localhost/ || exit 1

ENTRYPOINT [ "/usr/local/lib/alpine/run-java.sh" ]