#!/bin/bash

services=(common user owner admin config files alerts)

for service in "${services[@]}"
do
  docker-compose ps | grep $service > /dev/null
  exist=$?
  git diff --name-only HEAD^ HEAD | grep $service > /dev/null
  changed=$?
  if [ $exist -ne 0 ]; then
    echo Container $service not found, build started...
    docker-compose build $service
  elif [ $changed -eq 0 ]; then
    echo Service $service has changed, build started...
    docker-compose build $service
  else
    echo Service $service not changed
  fi
done

docker-compose up