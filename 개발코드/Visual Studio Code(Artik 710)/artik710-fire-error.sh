#!/bin/bash

mplayer -ao alsa:device=hw=0.0 /root/final/file/mp3/FireError.mp3 -af volume=10
mplayer -ao alsa:device=hw=0.0 /root/final/file/mp3/firesiren.mp3 -af volume=10

node /root/final/code/artikcloud-streaming-data.js &

sleep 5

ffmpeg -f v4l2 -c:v rawvideo -s 640x360 -r 30 -i /dev/video0 -s 640x360 -f flv -c:v libx264 -profile:v baseline -maxrate 1500000 -bufsize 1500000 -pix_fmt yuv420p -r 30 "rtmp://upload-nc.nstream.video/live/tsbwvuxvwpxgqpmiq8k4xiu4lxvr12pi"