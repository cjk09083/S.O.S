#!/bin/bash

NOW=$(date -d '2 hour' +"%Y-%m-%d-%H-%M")
Time=$(date -d '2 hour' +"%H-%M")

aws --endpoint-url=http://kr.objectstorage.ncloud.com s3 mv s3://artik/reservation/$NOW.txt /root/final/file/txt/reservation/$NOW.txt

PlayTime=`cat /root/final/file/txt/reservation/$NOW.txt | cut -c 12-16`

echo $PlayTime

PlayText=`cat /root/final/file/txt/reservation/$NOW.txt | cut -c 18-`

echo $PlayText

if [ $Time == $PlayTime ]; then

    curl -o "$Time.mp3" "https://naveropenapi.apigw.ntruss.com/voice/v1/tts" -d "speaker=mijin&speed=0&text=2시간 후에 $PlayText" -H "Content-Type: application/x-www-form-urlencoded; charset=UTF-8" -H "X-NCP-APIGW-API-KEY-ID: dfavmwupxm" -H "X-NCP-APIGW-API-KEY: xRhZp31XK7KAekxE3VFSFRu8aHY9catCLTHKyHYn" -v

    mv $Time.mp3 /root/final/file/mp3/reservation

    mplayer -ao alsa:device=hw=0.0 /root/final/file/mp3/reservation/$Time.mp3 -af volume=10

    rm -rf /root/final/file/mp3/reservation/$Time.mp3

    rm -rf /root/final/file/txt/reservation/$NOW.txt
else
    echo NO events scheduled
fi