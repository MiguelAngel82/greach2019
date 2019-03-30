#!/bin/bash

COUNTER=0
while [  $COUNTER -lt 20 ]; do
	echo The counter is $COUNTER
	let COUNTER=COUNTER+1
	time curl localhost:8090/fibonacci?series=40
	sleep 2
	time curl localhost:8091/fibonacci?series=40
	sleep 2
	time curl localhost:8092/fibonacci?series=40
	sleep 2
    time curl localhost:8093/fibonacci?series=40
	sleep 2
done
