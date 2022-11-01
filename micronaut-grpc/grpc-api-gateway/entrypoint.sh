#!/usr/bin/env sh
set -eu
# inspired by https://serverfault.com/a/919212/475920
envsubst '${STOCK_DATA_HUB_ADDRESS}' < /etc/nginx/nginx.conf.template > /etc/nginx/nginx.conf

/docker-entrypoint.sh  "$@"
