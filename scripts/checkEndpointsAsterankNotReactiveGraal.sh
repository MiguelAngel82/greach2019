#!/bin/bash

COUNTER=0
while [  $COUNTER -lt 100 ]; do
	echo The counter is $COUNTER
	let COUNTER=COUNTER+1
	time curl localhost:8090/asterank-not-reactive
	sleep 2
	time curl localhost:8091/asterank-not-reactive
    sleep 2
	time curl localhost:8092/asterank-not-reactive
    sleep 2
    time curl localhost:8093/asterank-not-reactive
    sleep 2
done
