#!/bin/bash
if [ $# -eq 0 ]; then
    echo >&2 "Usage: jmapSeries <pid> <run_user> [ <count> [ <delay> ] ]"
    echo >&2 "    Defaults: count = 10, delay = 60 (seconds)"
    exit 1
fi
pid=$1         # required
user=$2        # required
count=${3:-10} # defaults to 10 times
delay=${4:-60} # defaults to 60 seconds
while [ $count -gt 0 ]
do
    jmap -J-d64 -heap $pid >jmap.$pid.$(date +%H%M%S.%N)
    sleep $delay
    let count--
    echo -n "."
done
