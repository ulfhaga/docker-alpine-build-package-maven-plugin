# TOOLS TO CREATE LINUX ALPINE PACKAGE

Creates an [Alpine](https://alpinelinux.org/) package with theses two Maven plugins se.docker.alpine:plugin-maven, [io.fabric8:docker-maven-plugin](https://dmp.fabric8.io/)
and a docker image called docker-alpine-build-package-maven-plugin/server-alpine-jvm.

In the plugin se.docker.alpine:plugin-maven, you can specify the package name, version, scripts etc.

## How it works

1. The plugin io.fabric8:docker-maven-plugin starts the container from the image server-alpine-jvm. (The image must have been build first). 
2. The plugin se.docker.alpine:plugin-maven has the package name, version, scripts etc. This information will be sent to the container.
3. An Alpine package can now be build in the container.
4. The plugin se.docker.alpine:plugin-maven gets the Alpine package from container.

## Project structure

The code for the plugin se.docker.alpine:plugin-maven will be found in the folder maven-plugin.
The subproject for the docker image will be found in the folder server.

## Build the plugin and the docker image

For the first time a docker image needs to be created. 
The name is docker-alpine-build-package-maven-plugin/server-alpine-jvm.
Build the docker image and the plugin Maven plugin se.docker.alpine:plugin-maven with:

    mvn -P docker install
    # verify it exists
    docker image inspect docker-alpine-build-package-maven-plugin/server-alpine-jvm 

If no docker image needs to be created because it exits. 
Build only the Maven plugin se.docker.alpine:plugin-maven with:

    mvn install 

## Usage

See [example](sample/README.md) in file sample/pom.xml.

You will find two plugins that are needed and they are.

The plugin `se.docker.alpine:plugin-maven` act as a client. 
The plugin `io.fabric8:docker-maven-plugin` that act as server for the client.

A micro-server is in the docker container that builds an Alpine package. 
That created package can be installed with the apk command. 


## Architecture

The maven plugin `se.docker.alpine:plugin-maven` use the client (module client) to communicate 
with server `io.fabric8:docker-maven-plugin` inside an Alpine docker container. The container creates the Alpine package. 
The plugin can now receive the package via its client (module client).

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

The created package file name is mypackage-1.0.apk in this example 
and can be tested a new alpine container.

    docker run --rm --name alpine -dit alpine sh  

    # docker exec -it alpine mkdir -p /etc/apt/keys 
    # docker cp sample/target/pubKey.tar alpine:/  
    # docker exec -it alpine tar -xvf pubKey.tar -C /etc/apt/keys
 
    docker cp sample/target/mypackage-1.0-r0.apk  alpine:/tmp
    docker exec -it alpine /bin/ash
    apk add --allow-untrusted /tmp/mypackage-1.0-r0.apk
 
    /usr/bin/hello.sh
    # 'Hello world!' will be printed.


## Developing

Read the file [Development](Development.md)
The micro-server in the docker  [Server](server/README.md)

## References

| Title      | Link |
| ----------- | ----------- |
| APKBUILD Reference               | https://wiki.alpinelinux.org/wiki/APKBUILD_Reference
| Creating an Alpine package       | https://wiki.alpinelinux.org/wiki/Creating_an_Alpine_package       |
 
 
 
