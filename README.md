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


Output from solverMain -h

USAGE:

  java edu.boisestate.cs.SolveMain <Graph File> [-d] [-h] [-l <length>]
         [-r <reporter>] [-s <solver>] [-v <version>]

Run string constraint solver on specified control flow graph. The default
string constraint solver is jsa. The default reporter is sat.

OPTIONS:

 -d,--debug                     Runs the solver framework in debug mode.
                                Default value is false.
 -h,--help                      Display this message.
 -l,--length <length>           Initial bounding length of the underlying
                                symbolic string, used with JSA, inverse
                                and Concrete solvers. Default value is 10.
 -r,--reporter <reporter>       The reporter used to gather information
                                for each string constraint:
                                sat - Reports on the satisfiability of
                                each string constraint in the specified
                                graph
                                model-count - Reports on the number and
                                percent of string instances for each
                                branch leaving the string constraint,
                                includes satisfiability.
                                The default reporter is sat
 -s,--solver <solver>           The solver that will be used to solve
                                string constraints:
                                blank - The blank solver used for testing.
                                concrete - The concrete solver which
                                provides an oracle for other solvers.
                                jsa - The Java String Analyzer solver
                                which comes from the dk.brics automaton
                                and string libraries.
                                inverse - Input generation solver.
                                Reporter and automata types are ignored
                                with this solver.
                                The default solver is jsa
 -v,--model-version <version>   The version of the automaton model used by
                                the JSA string constraint solver:
                                1 - Bounded Automaton Model
                                2 - Acyclic Automaton Model
                                3 - Acyclic Weighted Automaton


USAGE EXAMPLES:

    java edu.boisestate.cs.SolveMain <PROJECT_ROOT>/graphs/iText02.json
        -s jsa -r sat -v 1 -l 10

Run sat reporter for the iText02.json constraint graph file using the JSA
solver with bounded automata and an initial bounding length of 10.

    java edu.boisestate.cs.SolveMain <PROJECT_ROOT>/graphs/iText02.json
        -s concrete -r model-count -l 10

Run model count reporter for the iText02.json constraint graph file using
the Concrete solver with an initial bounding length of 10.

ADDITIONAL INFORMATION:

See the code repository at
https://github.com/BoiseState/string-constraint-counting for more details.

