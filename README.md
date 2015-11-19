# Central Authentication Service (CAS) [![License](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/Jasig/cas/blob/master/LICENSE)

## Introduction
This branch hosts the [Docker](https://www.docker.com/) build configuration necessary to build a CAS image. See the `Dockerfile` for more info. 

## Versions
A docker image for CAS server `v4.1.1`. Images are tagged to match CAS server releases.

## Requirements
Latest version of [Docker](https://www.docker.com/) is required on the build machine.

## Configuration

### Image
* The running image will be available on the host via ports 80 and 443 and may be accessed via the host browser at the container-provided IP address.
* The built/tagged images are reflected in the `build|run.sh` files and need to be updated per CAS release.
* The CAS server will be deployed inside an embedded Jetty container that is built into the [CAS overlay project](http://bit.ly/1PPY47q).

## CAS Overlay
* The build will auto-copy the contents of the `src\main\webapp` to the docker image. You can set up your own custom overlay project based that directory structure that mimics that of the CAS web application itself.
 
### SSL
* Update the `thekeystore` file with the server certificate if you need access the CAS server via HTTPS. The password for the keystore is `changeit`.
* The build will auto-copy the keystore to the image and Jetty will be configured to use that keystore for HTTPS requests.

## Build

```bash
./build.sh
```

The image will be built as `apereo/cas:v$CasVersion`.

## Build

```bash
./run.sh
```
