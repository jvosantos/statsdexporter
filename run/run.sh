#!/bin/sh

[ -z $XMX ] && echo "XMX not set. " && exit 1;
[ -z $XMS ] && echo "XMS not set. " && exit 1;

exec java \
$XMX $XMS \
-jar \
-DconfigurationPath=$CONFIG_PATH \
-Denvironment=$ENVIRONMENT \
/usr/opt/service/service.jar
