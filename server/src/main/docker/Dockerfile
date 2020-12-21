####
# This Dockerfile is used in order to build a container that runs the Quarkus application in JVM mode.
# Used of maven plugin fabric8io docker-maven-plugin (https://dmp.fabric8.io/)
#
# Before building the docker image run:
#
# mvn clean package
#
# Then, build the image with:
#
# mvn -P docker install
#
# Then run the container using:
#
# docker run -i --rm  --name server-alpine-jvm --mount type=bind,src=homeDev,dst=/home/dev,bind-propagation=shared -p 64014:8080 docker-alpine-build-package-maven-plugin/server-alpine-jvm
# or
# mvn -P docker docker:start
# ( mvn -P docker docker:stop )
#
# If you want to include the debug port into your docker image
# you will have to expose the debug port (default 5005) like this :  EXPOSE 64014 5050
#
# Then run the container using :
#
# docker run -i --rm -p 64014:8080 -p 5005:5005 -e JAVA_ENABLE_DEBUG="true" --name server-alpine-jvm docker-alpine-build-package-maven-plugin/server-alpine-jvm
#
###
FROM alpine:3.12.0
LABEL Description="This image is used to start a Restful service to create an Alpine package" Vendor="UH" Version="0.1"

ARG JAVA_PACKAGE=openjdk11-jre-headless

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en'

# Install java and other packages
RUN apk add --update ${JAVA_PACKAGE}  \
    vim tree busybox leafpad \
    leafpad-lang \
    alpine-sdk \
    ttf-ubuntu-font-family \
    ca-certificates \
    sudo

# Create user dev
RUN adduser -h /home/dev --disabled-password dev

#  Add the user dev to group abuild
RUN addgroup dev abuild

# Add to sudoers
RUN echo 'dev ALL=(ALL:ALL) ALL' >> /etc/sudoers;

# Runner
COPY maven/src/main/docker/fabric8/run-java.sh /usr/local/lib/alpine/
RUN chown dev /usr/local/lib/alpine\
    && chmod "g+rwx" /usr/local/lib/alpine \
    && chown dev:root /usr/local/lib/alpine \
    && chown dev /usr/local/lib/alpine/run-java.sh \
    && chmod 540 /usr/local/lib/alpine/run-java.sh

## Configure the JAVA_OPTIONS, you can add -XshowSettings:vm to also display the heap size.
ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

## Copy the application
COPY maven/target/lib/* /usr/local/lib/alpine/lib/
COPY maven/target/*-runner.jar /usr/local/lib/alpine/app.jar
EXPOSE 8080


# Add Alpine package scripts

RUN sed -i -e 's/#PACKAGER="Your Name <your@email.address>"/PACKAGER="dev <dev@email.address>"/g'  /etc/abuild.conf

RUN mkdir -p /var/cache/distfiles
RUN chgrp abuild /var/cache/distfiles
RUN chmod g+w /var/cache/distfiles

RUN mkdir -p /usr/local/lib/alpine/scripts
COPY maven/src/main/docker/scripts/create_new_package.sh /usr/local/lib/alpine/scripts/

COPY maven/src/main/docker/aports /home/dev/aports
RUN chgrp -R -v abuild /home/dev/aports

# Healtcheck
HEALTHCHECK --start-period=1s --interval=30s --timeout=4s CMD curl -f http://localhost:8080/health/ready || exit 1

# Build keys
RUN sudo -u dev abuild-keygen -a -n -i
# RUN tar -cvf /home/dev/keys.tar /etc/apk/keys
RUN cp -u  /home/dev/.abuild/dev*.rsa.pub /etc/apk/keys
RUN cp -u  /home/dev/.abuild/dev*.rsa.pub /home/dev/key.rsa.pub
RUN cp -u  /home/dev/.abuild/dev*.rsa /home/dev/key.rsa

# Set user                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     WORKDIR /home/dev
USER dev
# USER root
WORKDIR /home/dev

ENTRYPOINT [ "/usr/local/lib/alpine/run-java.sh" ]