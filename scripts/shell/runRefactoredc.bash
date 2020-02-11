#! /bin/bash
#file containing the names of the benchmarks
filename="$1"

while read -r line
do 
   name=( $line )
   echo
   echo ${name[0]}
   bench=${name[0]}

   pathfile=./graphs/benchmarks/${bench}
   #echo ${pathfile}
   
   if [ -f $pathfile ]; then
      #need to parse bench in order to figure out l
      len=3;
      
      if [[ $bench == *"_l2_"* ]]; then
	  len=2;
      elif [[ $bench == *"_l3_"* ]]; then
	  len=3;
      fi
   
      #echo $len
      #imports $CLASSPATH variable
      #run concrete
      fileName=(${bench//./ })
    
      outfileOrig=./data/refactor/type/${fileName[0]}.txt
      #echo $outfile
      

      outfile=${outfileOrig//type/concrete}
      typeDirectory=concrete
      #echo $outfile
      echo
      echo "	Running.....concrete"
      java -Xmx6g -cp ./target/classes/:$CLASSPATH edu.boisestate.cs.SolveMain -l ${len} ${pathfile} -r model-count -s concrete > ${outfile}
      
      #compare them
      echo "	Comparing...concrete"
      java -Xmx4g -cp ./target/classes/:$CLASSPATH edu.boisestate.cs.reporting.CheckRefactor ${fileName[0]}.txt ${typeDirectory}
      

      outfile=${outfileOrig//type/acyclicWeighted}
      typeDirectory=acyclicWeighted
      #echo $outfile
      #echo
      #echo "	Running.....acyclicWeighted"
      #java -Xmx4g -cp ./target/classes/:$CLASSPATH edu.boisestate.cs.SolveMain -l ${len} ${pathfile} -r model-count -s jsa  -v 3 > ${outfile}
      
      #compare them
      #echo "	Comparing...acyclicWeighted"
      #java -Xmx4g -cp ./target/classes/:$CLASSPATH edu.boisestate.cs.reporting.CheckRefactor ${fileName[0]}.txt ${typeDirectory}
      
      
      outfile=${outfileOrig//type/bounded}
      typeDirectory=bounded
      #echo $outfile
      #echo
      #echo "	Running.....bounded"
      #java -Xmx4g -cp ./target/classes/:$CLASSPATH edu.boisestate.cs.SolveMain -l ${len} ${pathfile} -r model-count -s jsa  -v 1 > ${outfile}
      
      #compare them
      #echo "	Comparing...bounded"
      #java -Xmx4g -cp ./target/classes/:$CLASSPATH edu.boisestate.cs.reporting.CheckRefactor ${fileName[0]}.txt ${typeDirectory}
      
      
      outfile=${outfileOrig//type/acyclic}
      typeDirectory=acyclic
      #echo $outfile
      #echo
      #echo "	Running.....acyclic"
      #java -Xmx4g -cp ./target/classes/:$CLASSPATH edu.boisestate.cs.SolveMain -l ${len} ${pathfile} -r model-count -s jsa  -v 2 > ${outfile}
      
      #compare them
      #echo "	Comparing...acyclic"
      #java -Xmx4g -cp ./target/classes/:$CLASSPATH edu.boisestate.cs.reporting.CheckRefactor ${fileName[0]}.txt ${typeDirectory}
    
    fi
   
done < $filename
