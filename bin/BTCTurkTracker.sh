#!/bin/sh

if [ -z "$JAVA_HOME" ]; then
    echo "JAVA_HOME environment variable is missing."
    exit 1
fi

if [ $# -ne 2 -a $# -ne 3 ]; then
    echo "Usage: $0 <BTCTurkTracker.jar> <outputDirectory> [<updatePeriod>]"
    exit 1
fi
jarFile="$1"
outDir="$2"
updatePeriod="$3"

javaOpts="-server -d64 \
-Xmx128m -Xms128m -Xmn32m -XX:+UseParallelGC -XX:+AggressiveOpts \
-XX:+UseFastAccessorMethods -XX:MaxInlineSize=8192 -XX:-StackTraceInThrowable \
-XX:FreqInlineSize=8192 -XX:CompileThreshold=1500 \
-Djava.net.preferIPv4Stack=true"

if [ -z "$updatePeriod" ]
    then "$JAVA_HOME/bin/java" $javaOpts -jar "$jarFile" -o "$outDir"
    else "$JAVA_HOME/bin/java" $javaOpts -jar "$jarFile" -o "$outDir" -p "$updatePeriod"
fi
