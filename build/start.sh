#!/bin/sh
mkdir -p /log
java -jar /application/wecube-plugins-wecmdb.jar  \
--server.address=0.0.0.0 \
--server.port=8081 \
--plugins.wecmdb-server-url=$1 >>/log/wecube-plugins-wecmdb.log 
