# #!/usr/bin/env bash

#!/bin/sh
# By default debug mode is disable.
docker-compose stop;
docker-compose rm -f;
docker-compose up --build --force-recreate -d;
