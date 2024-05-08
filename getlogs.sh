#!/bin/bash

if ( [ $# -lt 3 ] ); then
  echo "Usage: ./getlogs.sh <directory> <length> <timeout>"
  exit 1
fi

directory=$1
length=$2
timeout=$3

directoryname=$(basename "$directory")

mkdir -p logs
mkdir -p logs/$directoryname

for file in "$directory"/*; do

    filename=$(basename "$file")
    outfile="logs/$directoryname/$filename-l$length.log"
    echo "Processing $filename"

    if [ -d "$file" ]; then
        echo "Skipping directory: $filename"
        continue
    fi 
    
    # Capture start time before timeout execution
    start_time=$(date +%s.%N)   
    
    # Execute with timeout and capture output/error
    output=$(timeout "$timeout"s java -cp target/string-constraint-solvers-1.0-SNAPSHOT-jar-with-dependencies.jar edu.boisestate.cs.SolveMain "$file" -l "$length" -s Inverse -v 2 2>&1)    
    
    tout=$?

    # Capture end time after execution (even on timeout)
    end_time=$(date +%s.%N) 

    # Calculate elapsed time (consider using bc for sub-second precision)
    elapsed_time=$(echo "$end_time - $start_time" | bc -l)  

    # Write output and calculated time to log file
    echo "** Time elapsed: $elapsed_time seconds **" > $outfile

    #Check if timed out ( $? returns non-zero for timeout )
    if [ $tout -eq 124 ]; then
      echo "TIMEOUT ($timeout seconds)" >> $outfile
      echo "TIMEOUT OCCURED"
    fi
    echo "$output" >> $outfile
done