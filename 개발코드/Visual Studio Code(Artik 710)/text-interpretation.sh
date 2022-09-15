#!/bin/bash

aws --endpoint-url=http://kr.objectstorage.ncloud.com s3 mv s3://artik/txt/AppText.txt /root/final/file/txt/AppText.txt

AppText=`cat /root/final/file/txt/AppText.txt`

curl -o "AppText.mp3" "https://naveropenapi.apigw.ntruss.com/voice/v1/tts" -d "speaker=mijin&speed=0&text=$AppText" -H "Content-Type: application/x-www-form-urlencoded; charset=UTF-8" -H "X-NCP-APIGW-API-KEY-ID: 27oeodmeng" -H "X-NCP-APIGW-API-KEY: y3syQtHDZPwxSfi5vdVeQiu94TrT42Gi6u1cBvhU" -v

mv AppText.mp3 /root/final/file/mp3

mplayer -ao alsa:device=hw=0.0 /root/final/file/mp3/AppText.mp3 -af volume=10

rm -rf /root/final/file/txt/AppText.txt