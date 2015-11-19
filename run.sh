#!/bin/bash
docker stop cas
docker rm cas -f

docker run -p 80:8080 -p 443:8443 --name="cas" apereo/cas:v4.1.1