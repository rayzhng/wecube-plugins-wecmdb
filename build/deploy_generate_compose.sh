#!/bin/bash

if [ $# -ne 2 ]
  then
    echo "Usage: deploy_generate_compose.sh CONFIG IMAGE_VERSION"
    exit 1
fi

source $1

image_version=$2

build_path=`dirname $0`

sed "s~{{WECUBE_PLUGINS_WECMDB_IMAGE_NAME}}~$wecube_plugins_wecmdb_image_name~g" ${build_path}/docker-compose.tpl > docker-compose.yml
sed -i "s~{{WECUBE_PLUGINS_WECMDB_SERVER_PORT}}~$wecube_plugins_wecmdb_server_port~g" docker-compose.yml
sed -i "s~{{IMAGE_VERSION}}~$image_version~g" docker-compose.yml
 
