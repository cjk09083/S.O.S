#!/bin/bash
#
#TTS(text to speech) shell script file

#mijin : 한국어, 여성 음색/jinho : 한국어, 남성 음색
#clara : 영어, 여성 음색/matt : 영어, 남성 음색

curl -o "AppText.mp3" "https://naveropenapi.apigw.ntruss.com/voice/v1/tts" -d "speaker=mijin&speed=0&text=$1" -H "Content-Type: application/x-www-form-urlencoded; charset=UTF-8" -H "X-NCP-APIGW-API-KEY-ID: dfavmwupxm" -H "X-NCP-APIGW-API-KEY: xRhZp31XK7KAekxE3VFSFRu8aHY9catCLTHKyHYn" -v

mv AppText.mp3 /root/final/file/mp3

mplayer -ao alsa:device=hw=0.0 /root/final/file/mp3/AppText.mp3