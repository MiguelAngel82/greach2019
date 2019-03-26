#!/usr/bin/env bash
: ${1:?missing jar filename}
: ${2:?error string not given}
: ${FILE:=./out}
[ -x ${WATCH:="/bin/watch"} ] || { echo "$(basename $0): 'watch' command not found" 2>&1; exit 2; }
jar="$1"; shift
traza="$*"

trap '{ echo "$(basename $0): [C-c] pressed, exiting" ; }' SIGINT SIGQUIT

java -jar "${1}" | grep "${2}" >> ${FILE} &
${WATCH} -gtn1 tail ${FILE} >/dev/null 2>&1 && kill $!

trap '' SIGINT SIGQUIT
