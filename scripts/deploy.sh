#!/bin/bash

LOG_PATH=/home/ubuntu/deploy.log

if [ !$(sudo docker container ls | grep nginx) ]; then
  echo "> NGINX 컨테이너 실행" >> $LOG_PATH
  sudo service nginx stop
  sudo docker-compose up nginx -d --build
fi

CURRENT_PORT = $(cat /etc/nginx/service_url.inc | grep -Po '[0-9]+' | tail -1)
echo "> 현재 사용중인 포트: ${CURRENT_PORT}"

TARGET_PORT=0
if [ ${CURRENT_PORT} -eq 8080 ]; then
  TARGET_PORT = 8081
elif [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT = 8080
else
  TARGET_PORT = 8080
fi

NEW_CONTAINER = sgsg-application-${TARGET_PORT}
echo "> SPRINGBOOT 컨테이너 실행 | PORT: ${TARGET_PORT}"
sudo docker-compose run -d -p ${TARGET_PORT}:8080 --name sgsg-application-${TARGET_PORT} springboot

echo "> 헬스 체크"
STARTED = 0
for RETRY_COUNT in {1..15}; do
  echo "curl -I http://localhost:${TARGET_PORT}/health"
  RESPONSE=$(curl -I http://localhost:${TARGET_PORT}/health)
  UP_COUNT=$(echo $RESPONSE | grep '204' | wc -l)

  if [ $UP_COUNT -ge 1 ]; then # $up_count >= 1 ("UP" 문자열이 있는지 검증)
    echo "> Health check 성공"
    STARTED = 1
    break
  else
    echo "> Health check의 응답을 알 수 없거나 혹은 status가 UP이 아닙니다."
    echo "> Health check: ${RESPONSE}"
  fi

  if [ $RETRY_COUNT -eq 10 ]; then
    echo "> Health check 실패. "
    STARTED = 1
  fi

  echo "> Health check 연결 실패. 재시도..."
  sleep 10
done

if [ STARTED ]; then
  echo "> 리버스 프록시 설정 변경"
  echo "set \$service_url http://127.0.0.1:${TARGET_PORT};" | sudo tee /ubuntu/nginx/service_url.inc
  echo "> 엔진엑스 리로드"
  sudo docker exec -it nginx service nginx reload
  echo "> 이전 버전 컨테이너 종료"
  sudo docker rm -f sgsg-application-${CURRENT_PORT}
fi