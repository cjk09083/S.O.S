#!/bin/bash

wget -O motiontime.txt "http://minsanggyu2.cafe24.com/motion_determine_error.php?device_id=9c8deecc68ba40bb89a1635f397c5f5e&time_data"
mv motiontime.txt /root/final/file/txt/motion

wget -O motionplace.txt "http://minsanggyu2.cafe24.com/motion_determine_error.php?device_id=9c8deecc68ba40bb89a1635f397c5f5e&one_place"
mv motionplace.txt /root/final/file/txt/motion

node /root/final/code/motion/artikcloud-motion-data-upload.js