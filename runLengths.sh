#!/bin/bash

len=2

while true; do
	res=$(./run "$1" $len)
	if [[ "$res" == "sat"* ]]; then
		echo "$res"
		break
	fi
	echo "$res at $len"
	len=$((len+1))
	if [ $len -gt 15 ]; then
		break
	fi
done


