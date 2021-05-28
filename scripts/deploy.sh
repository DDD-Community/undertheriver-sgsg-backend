#!/bin/bash

REPOSITORY=/home/ubuntu/server
cd $REPOSITORY

APP_NAME=sgsg
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z "$CURRENT_PID" ]; then
  echo "> 종료할 것 없음." >>/home/ubuntu/deploy.log
else
  echo "> sudo kill -15 $CURRENT_PID" >>/home/ubuntu/deploy.log
  kill -15 "$CURRENT_PID" >>/home/ubuntu/deploy.log 2>&1
  sleep 5
fi

echo "> $JAR_PATH 배포"
if [ "$DEPLOYMENT_GROUP_NAME" == "backend-develop" ]; then
  nohup java -jar -Dspring.profiles.active=develop -Duser.timezone=KST "$JAR_PATH" >>/home/ubuntu/deploy.log 2>&1 &
elif [ "$DEPLOYMENT_GROUP_NAME" == "backend-production" ]; then
  nohup java -jar -Dspring.profiles.active=production -Duser.timezone=KST "$JAR_PATH" >>/home/ubuntu/deploy.log 2>&1 &
fi

echo "[$(date)] server deploy" >>/home/ubuntu/deploy.log
