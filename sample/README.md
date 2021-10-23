# A sample 
mvn clean;
mvn package;

The package will be founded here:
target/mypackage-1.0-r0.apk


# What

The file src/test/resources/testData/source/hello.sh 
will be installed in an Alpine package called mypackage-1.0-r0.apk.
See file pom.xml plugin se.docker.alpine:plugin-maven.

With the command in Alpine Linux:
apk add mypackage-1.0-r0.apk;
the file will be installed in folder /usr/bin/

