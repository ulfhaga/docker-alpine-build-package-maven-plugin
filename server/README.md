# server project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `server-1.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/server-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/server-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.

# Start the docker container

mvn -P docker docker:start

# Stop the docker container

mvn -P docker docker:stop

# Test
tree /home/ulha/code/dockerAlpineBuildMavenPackage/server/target/docker/quarkus/server-alpine-jvm/build/maven

http://dmp.fabric8.io/#example
https://en.wikipedia.org/wiki/Representational_state_transfer
https://quarkus.io/guides/rest-json
https://github.com/fabric8io/docker-maven-plugin/tree/master/samples

docker exec -it  2b096e72c0bd   /bin/sh
docker exec -it server-alpine-jvm-1   /bin/sh


./mvnw verify -Pnative -Dquarkus.native.container-build=true 

docker run -i --rm -p 8080:8080 quarkus/server-alpine-jvm 
eller 
java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8881  target/server-1.0-SNAPSHOT-runner.jar


http://localhost:8080/v1/packages/1

curl -v -w "\n" -X POST  http://localhost:8080/v1/packages

curl -v -w "\n" -X PUT  -H "Content-Type: text/plain"  http://localhost:8080/v1/packages/1/name -d 'apa1' 

curl -v -w "\n" -X GET -H "Content-Type: application/json"  http://localhost:8080/v1/packages/1 
curl -v -w "\n" -X GET -H "Content-Type: application/json"  http://localhost:8080/v1/packages

