# string-constraint-counting

To test solution, run the concrete singleton solver:
java SolveMain ./graphs/inverse/inverse_case_1.json -l 2 -s concrete -v 2

Where -s concrete is the concrete solver
-v 2 is for second version of it, which is singleton - it only tracks one values for a symbolic variable, and that values is read from the actual value of the symbolic variable in the graph.

Correct input values would always have true* in the reporter:
ID	SING	TSAT	FSAT	DISJ	PREV OPS
32	true	true*	false	yes	<S:5> = <init>{647} -> [10]<S:5>.toLowerCase(){28} -> [17]<S:10>.concat("AB"){20} -> [32]<S:17>.contains("aA"){695}

Incorrect input value would produce some false* in the reporter (changed the actual value of <S:5> to "bb" instead of "AA"
ID	SING	TSAT	FSAT	DISJ	PREV OPS
32	true	false*	true	yes	<S:5> = <init>{642} -> [10]<S:5>.toLowerCase(){26} -> [17]<S:10>.concat("AB"){17} -> [32]<S:17>.contains("aA"){685}

