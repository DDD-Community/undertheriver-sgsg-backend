#!/bin/bash

health_check() {
  OK=204
  FAIL=404

  echo "> Health check 시작"
  echo "> curl -I http://localhost:$1/health "

  for RETRY_COUNT in {1..15}; do
    RESPONSE=$(curl -I http://localhost:$1/health)
    UP_COUNT=$(echo $RESPONSE | grep ${OK} | wc -l)

    if [ $UP_COUNT -ge 1 ]; then # $up_count >= 1 ("UP" 문자열이 있는지 검증)
      echo "> Health check 성공"
      return ${OK}
    else
      echo "> Health check의 응답을 알 수 없거나 혹은 status가 UP이 아닙니다."
      echo "> Health check: ${RESPONSE}"
    fi

    echo "> Health check 연결 실패. 재시도..."
    sleep 10
  done

  return ${FAIL}
}