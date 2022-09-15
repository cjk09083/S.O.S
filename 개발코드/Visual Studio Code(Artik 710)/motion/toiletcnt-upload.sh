#!/bin/bash

wget -O toiletcnt.txt "http://minsanggyu2.cafe24.com/motion_determine_error.php?device_id=9c8deecc68ba40bb89a1635f397c5f5e&toilet_id=24&toilet_times"
mv toiletcnt.txt /root/final/file/txt/motion

node /root/final/code/motion/artikcloud-toiletcnt-data-upload.js