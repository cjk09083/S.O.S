#!/bin/bash

wget -O toilet.txt "http://minsanggyu2.cafe24.com/motion_determine_error.php?device_id=9c8deecc68ba40bb89a1635f397c5f5e&toilet_id=24"
mv toilet.txt /root/final/file/txt/motion

node /root/final/code/motion/artikcloud-toilet-data-upload.js