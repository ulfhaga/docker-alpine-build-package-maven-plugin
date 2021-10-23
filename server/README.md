# server project

The server will be deployed inside the docker container,
The server is a micr


## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `server-1.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/server-1.0-SNAPSHOT-runner.jar`. 
The application will not work properly because a call to the Restful method `getPackage` needs to be run in this project Docker container.

# Create and start containers

mvn -P docker docker:start

# Stop and destroy containers

mvn -P docker docker:stop

# Save image to a file

mvn -P docker docker:save

# Push images to a registry

mvn -P docker docker:push

# Show container logs

mvn -P docker docker:logs

# Test

## Start server and set some data

    mvn -P docker docker:start

    curl -v -w "\n" -X POST  http://localhost:64014/v1/packages
    curl -v -w "\n" -X PUT  -H "Content-Type: text/plain"  http://localhost:64014/v1/packages/1/name -d 'item1'
    curl -v -w "\n" -X GET -H "Content-Type: application/json"  http://localhost:64014/v1/packages/1 
    curl -v -w "\n" -X GET -H "Content-Type: application/json"  http://localhost:64014/v1/packages

    mvn -P docker docker:stop
