#!/bin/bash

#export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.91-7.b14.fc24.arm/jre

#sh /root/final/CLI/ncfscmd.sh configure E1tuKPmRoOKtPonRaKIo jsUE57BbaNI4e7FRQ2mhTjdGSS9M68in1lIYOmYd

sleep 10

amixer -c 0 set 'Headphone' 100% on

cd /root/final/code

export GOOGLE_APPLICATION_CREDENTIALS="/root/final/code/artik-ebd81dd467f5.json"

node /root/final/code/artikcloud-error-data-download.js &