#!/bin/bash

COUNTER=0
while [  $COUNTER -lt 100 ]; do
	echo The counter is $COUNTER
	let COUNTER=COUNTER+1
	time curl localhost:8090/wavToMp3
	sleep 2
	time curl localhost:8091/wavToMp3
	sleep 2
	time curl localhost:8092/wavToMp3
	sleep 2
    time curl localhost:8093/wavToMp3
	sleep 2
done
