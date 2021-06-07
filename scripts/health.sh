#!/bin/bash
ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/health_check.sh

$(health_check 8080)