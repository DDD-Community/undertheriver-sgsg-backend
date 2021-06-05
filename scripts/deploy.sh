#!/bin/bash

LOG_PATH=/home/ubuntu/deploy.log

if [ !$(sudo docker container ls | grep nginx) ]; then
  echo "> NGINX 컨테이너 실행" >> $LOG_PATH
  docker-compose up nginx -d --build
fi

echo "> SPRINGBOOT 컨테이너 실행" >> $LOG_PATH
docker-compose restart -f springboot -d --build