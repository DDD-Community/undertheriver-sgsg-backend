#!/bin/bash

LOG_PATH=/home/ubuntu/deploy.log
echo "> 도커 컨테이너 종료" >>$LOG_PATH
docker-compose down >>$LOG_PATH
echo "> 도커 컨테이너 빌드/실행" >>$LOG_PATH
docker-compose up -d --build >>$LOG_PATH