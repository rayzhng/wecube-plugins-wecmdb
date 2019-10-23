#!/bin/sh
mkdir -p /log
java -jar /application/wecmdb-plugins-wecmdb.jar  --server.address=0.0.0.0 --server.port=${WECMDB-PLUGINS-WECMDB_SERVER_PORT} \
${CUSTOM_PARAM} >>/log/wecube-plugins-wecmdb.log 
