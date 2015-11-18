#!/bin/bash
docker stop cas
docker rm cas -f

docker run -p 8080:8080 -p 8443:8443 --name="cas" apereo/cas 