#!/bin/sh

rootdir=$(dirname "$PWD")
export logdir="$rootdir/log"

LIBS=./libs
PUB_LIB=""
MAIN_CLASS=io.nuls.Bootstrap

for jar in `find $LIBS -name "*.jar"`
do
     PUB_LIB="$PUB_LIB:""$jar"
done

MAIN_LIB=$rootdir/dist_lib/vCloud.jar

CONF_PATH=$rootdir/conf

#project class path
export CLASSPATH=$CLASSPATH:$CONF_PATH:$MAIN_LIB:.:$PUB_LIB

1>${rootdir}/stdout.log 0>${rootdir}/stderr.log &
#nohup java -server -DLANG="zh_CN.GBK" -Dlog.dir=${logdir} $MAIN_CLASS 1>${rootdir}/stdout.log 0>${rootdir}/stderr.log 2>&1 &
nohup java -Xms1024m -Xmx4096m  -XX:NewSize=256m -XX:MaxNewSize=256m -XX:PermSize=256M -XX:MaxPermSize=256M  -classpath $CLASSPATH $MAIN_CLASS 1>${rootdir}/stdout.log 0>${rootdir}/stderr.log 2>&1 &

[ ! -d "${rootdir}/pid" ] && { mkdir "${rootdir}/pid";}
touch ${rootdir}/pid/pid
echo $! > ${rootdir}/pid/pid
