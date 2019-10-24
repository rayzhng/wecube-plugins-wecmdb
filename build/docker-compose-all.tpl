version: '2'
services:
  wecube-plugins-wecmdb-app:
    image: {{WECUBE_PLUGINS_WECMDB_IMAGE_NAME}}
    restart: always
    volumes:
      - /data/wecube-plugins-wecmdb/log:/log/
      - /etc/localtime:/etc/localtime
    network_mode: host
    ports:
      - {{WECUBE_PLUGINS_WECMDB_SERVER_PORT}}:{{WECUBE_PLUGINS_WECMDB_SERVER_PORT}}
    environment:
      - TZ=Asia/Shanghai
      - WECUBE_PLUGINS_WECMDB_SERVER_PORT={{WECUBE_PLUGINS_WECMDB_SERVER_PORT}}
      - CUSTOM_PARAM={{{CUSTOM_PARAM}}}
