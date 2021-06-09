#!/bin/bash
ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/health_check.sh

if [ !$(sudo docker container ls | grep nginx) ]; then
  echo "> NGINX 컨테이너 실행"
  sudo service nginx stop
  sudo docker-compose up -d --build nginx
fi

CURRENT_PORT=$(cat /etc/nginx/service-url.inc | grep -Po '[0-9]+' | tail -1)
echo "> 현재 사용중인 포트: ${CURRENT_PORT}"

TARGET_PORT=0
if [ ${CURRENT_PORT} -eq 8080 ]; then
  TARGET_PORT=8081
elif [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8080
else
  TARGET_PORT=8080
fi

SERVICE_NAME=sgsg-application
CURRENT_CONTAINER=${SERVICE_NAME}-${CURRENT_PORT}
NEW_CONTAINER=${SERVICE_NAME}-${TARGET_PORT}
echo "> SPRINGBOOT 컨테이너 실행 | PORT: ${TARGET_PORT}"
sudo docker-compose run -d -p ${TARGET_PORT}:8080 --name ${NEW_CONTAINER} springboot

if [ !$(health_check ${TARGET_PORT}) ]; then
  echo "> 리버스 프록시 설정 변경"
  echo "set \$resolver_ip 127.0.0.11; set \$service_url http://${NEW_CONTAINER}:8080;" | sudo tee /etc/nginx/service-url.inc
  echo "> 엔진엑스 리로드"
  sudo docker exec -it nginx service nginx reload
  echo "> 이전 버전 컨테이너 종료"
  sudo docker rm -f CURRENT_CONTAINER
fi
