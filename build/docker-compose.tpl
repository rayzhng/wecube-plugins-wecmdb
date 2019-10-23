version: '2'
services:
  wecube-plugins-wecmdb-app:
    image: {{WECUBE_PLUGINS_WECMDB_IMAGE_NAME}}:{{IMAGE_VERSION}}
    restart: always
    volumes:
      - /data/wecube-plugins-wecmdb/log:/log/
      - /etc/localtime:/etc/localtime
    network_mode: host
    ports:
      - {{WECUBE_PLUGINS_WECMDB_IMAGE_NAME}}:{{WECUBE_PLUGINS_WECMDB_IMAGE_NAME}}
    environment:
      - TZ=Asia/Shanghai
      - WECUBE_PLUGINS_WECMDB_IMAGE_NAME={{WECUBE_PLUGINS_WECMDB_IMAGE_NAME}}
      - CUSTOM_PARAM={{{CUSTOM_PARAM}}}
