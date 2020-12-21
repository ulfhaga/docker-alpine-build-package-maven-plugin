# TOOLS TO CREATE LINUX ALPINE PACKAGE

To create an Alpine package the maven plugin se.docker.alpine:plugin-maven is all that needed.
In that plugin the folder to all the files need to be specified and installation script, 
package name, version etc.

## Build

For the first time a docker image needs to created

    mvn -P docker install

If no docker image needs to be created run:

    mvn install

## Usage

See example in file sample/pom.xml.

There are two plugins that are needed

The plugin `se.docker.alpine:plugin-maven` act as a client. 
The plugin `io.fabric8:docker-maven-plugin` that act as server for the client.

The server is a docker container that builds an Alpine package. That package can be installed with the apk command. 

## Architecture

The maven plugin `se.docker.alpine:plugin-maven` use the client (module client) to communicate 
with server `io.fabric8:docker-maven-plugin` inside an Alpine docker container. The container creates the Alpine package. 
The plugin can now receive the package via the client (module client).


## Plugin configuration information

This Maven plugin `se.docker.alpine:plugin-maven` has following elements:

### name
The package name.
### source
A folder that contains files.
### version
Version of the package.
### arch
The package architecture(s) to build for. This can be one of: x86, x86_64, all, or noarch.
### license
The license tag must reflect the license of the source code
### url
Website address for the program.
### description
A brief, one line, description of what the package does
### target
A folder there create alpine package will be stored.
### packageFunction
A collection of linux commands. 
To access the files in source, use the varible $pkgdir".

## Test to install an alpine package

The created package file name is mypackage-1.0.apk in this example.

    docker run --rm --name alpine -dit alpine sh  
    docker cp sample/target/mypackage-1.0-r0.apk alpine:/  
    docker exec -it alpine mkdir -p /etc/apt/keys
    docker cp sample/target/key.rsa.pub alpine:/etc/apt/keys

    docker exec -it alpine /bin/ash
    apk add --allow-untrusted mypackage-1.0-r0.apk
    /usr/bin/hello.sh

## Developing

Read the file [Development](Development.md)

## References

| Title      | Link |
| ----------- | ----------- |
| APKBUILD Reference               | https://wiki.alpinelinux.org/wiki/APKBUILD_Reference
| Creating an Alpine package       | https://wiki.alpinelinux.org/wiki/Creating_an_Alpine_package       |
 
 
 