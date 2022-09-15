#!/bin/bash

mplayer -ao alsa:device=hw=0.0 /root/final/file/mp3/Jpg.mp3 -af volume=10

sleep 5

fswebcam -r 1280x720 --no-banner Image.jpg

if [ -e /root/final/code/Image.jpg ]; then

    mv Image.jpg /root/final/file/jpg

    aws --endpoint-url=http://kr.objectstorage.ncloud.com s3 mv /root/final/file/jpg/Image.jpg s3://artik/jpg/ --acl public-read

    node /root/final/code/artikcloud-jpg-data-on-upload.js

else

    node /root/final/code/artikcloud-jpg-data-error-upload.js

fi