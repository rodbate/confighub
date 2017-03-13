#!/bin/sh


[ ! -e "$CLASSPATH" ] && CLASSPATH=${JAVA_HOME}/lib


BASE_DIR=`pwd`
CLASSPATH=.:${BASE_DIR}/lib:${BASE_DIR}:${BASE_DIR}/rodbate-test.jar:$CLASSPATH
MAIN_CLASS=com.rodbate.test.Main

JAVA_OPT="-server -Xms2g -Xmx2g -Xmn1g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m"
JAVA_OPT="${JAVA_OPT} -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=70 -XX:+CMSParallelRemarkEnabled -XX:SoftRefLRUPolicyMSPerMB=0 -XX:+CMSClassUnloadingEnabled -XX:SurvivorRatio=8 -XX:+DisableExplicitGC"
JAVA_OPT="${JAVA_OPT} -verbose:gc -Xloggc:${BASE_DIR}/gc.log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps"
JAVA_OPT="${JAVA_OPT} -cp $CLASSPATH"

start_server ()
{
    ${JAVA_HOME}/bin/java ${JAVA_OPT} ${MAIN_CLASS}	
}

shutdown_server ()
{
    pid=`ps ax | grep -i "${MAIN_CLASS}" |grep java | grep -v grep | awk '{print $1}'`
    if [ -z "${pid}" ] ; then
            echo "No sync server running."
            exit -1;
    fi

    echo "The sync server (${pid}) is running..."

    kill ${pid} > /dev/null 2>&1

    echo "shutdown server(${pid}) now" 
   
    pid=`ps ax | grep -i "${MAIN_CLASS}" |grep java | grep -v grep | awk '{print $1}'`
    while [ ! -z "$pid" ]
    do
       echo -e ". \c"
       sleep 1
       pid=`ps ax | grep -i "${MAIN_CLASS}" |grep java | grep -v grep | awk '{print $1}'`
    done

    echo "shutdown server successfully"
}


case $1 in
	start)
	start_server && echo "start server successfully ...."
	;;
	restart)
	shutdown_server
	start_server
	;;
	stop)
	shutdown_server && echo "stop server successfully ...."
	;;
	*)
	echo "Usage: $0 start | restart | stop"
esac
