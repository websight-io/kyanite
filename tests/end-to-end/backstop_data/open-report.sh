#!/bin/sh

BASEDIR=$(dirname "$0")
TIMESTAMP=$(date +%s)

npx -y http-server -d false -o "html_report/index.html?$TIMESTAMP" -c-1 -p 8087 "$BASEDIR"
