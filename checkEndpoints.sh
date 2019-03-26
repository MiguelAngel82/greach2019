#!/bin/bash

COUNTER=0
while [  $COUNTER -lt 10 ]; do
	echo The counter is $COUNTER
	let COUNTER=COUNTER+1
    time curl localhost:8090/fibonacci?series=30
	sleep 1
done
