package edu.boisestate.cs.reporting;

import edu.boisestate.cs.BasicTimer;
import edu.boisestate.cs.Parser_2;
import edu.boisestate.cs.automatonModel.A_Model_Inverse;
import edu.boisestate.cs.graph.I_Inv_Constraint_r3;
import edu.boisestate.cs.graph.InvConstraintConcatSym_r3;
import edu.boisestate.cs.graph.InvConstraintConcreteValue_r3;
import edu.boisestate.cs.graph.InvConstraintDeleteCharAt_r3;
import edu.boisestate.cs.graph.InvConstraintDeleteStartEnd_r3;
import edu.boisestate.cs.graph.InvConstraintInput_r3;
import edu.boisestate.cs.graph.InvConstraintPredicate_r3;
import edu.boisestate.cs.graph.InvConstraintReplaceCharChar_r3;
import edu.boisestate.cs.graph.InvConstraintSubStringStartEnd_r3;
import edu.boisestate.cs.graph.InvConstraintToLowerCase_r3;
import edu.boisestate.cs.graph.InvConstraintToUpperCase_r3;
import edu.boisestate.cs.graph.Operation;
import edu.boisestate.cs.graph.PrintConstraint;
import edu.boisestate.cs.graph.SPFInput;
import edu.boisestate.cs.graph.SPFInputSet;
import edu.boisestate.cs.graph.SolutionSet;
import edu.boisestate.cs.graph.SymbolicEdge;
import edu.boisestate.cs.solvers.Solver_Inverse_r3;

import org.jgrapht.DirectedGraph;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Reporter that attempts to determine input solution for each predicate encountered.
 * 
 * @author Marlin Roberts
 * @param <T> - Automata model that implements inverse operations.
 */
@SuppressWarnings("unused")
public class Reporter_Inverse_r3<T extends A_Model_Inverse<T>> extends A_Reporter_2<T> {

    private final Solver_Inverse_r3<T> invSolver;
	protected Map<Integer,PrintConstraint> allConstraints = new HashMap<>();
	protected Map<Integer,I_Inv_Constraint_r3<T>> allInverseConstraints = new HashMap<>();
	protected Map<Integer,SolutionSet<T>> inputSolutions = new HashMap<>();
	protected Map<Integer,Integer> inputIndexes = new HashMap<>();
	protected List<Integer> predicateIDs = new ArrayList<>();
	private boolean saveResults = false;
	private String graphName;
	private String saveFile = "src\\test\\automata\\";
	
	// temporary implementation of solution output to file
	public String solutionFile = "";
	
	// prefix for output when running inside SPF
	private static String cid = "[IGEN] ";

    /**
     * Constructor for inverse reporter. Keeps a reference to the given solver as an inverse solver.
     * Stores constraints in a new map for reference later.
     * 
     * @param graph
     * @param parser
     * @param invSolver
     * @param debug
     */
    public Reporter_Inverse_r3 (DirectedGraph <PrintConstraint, SymbolicEdge> 	graph,
                      		   Parser_2<T> 										parser,
                      		   Solver_Inverse_r3<T> 							invSolver,
                      		   boolean 											debug) {
    	
        super (graph, parser, invSolver, debug);  	// solver instance variable in A_Reporter available 
        this.invSolver = invSolver; 				// same solver as inverse solver
        
        // this saves the set of constraints as references so that we can use access them later 
        // while building the inverse constraints. There are some graphs that have inputs stored twice, 
        // once with outgoing edges and once with no incoming/outgoing edges. The conditional avoids
        // adding references to the latter.
    	for (PrintConstraint p : graph.vertexSet()) {
    		if (!((graph.inDegreeOf(p) == 0) & (graph.outDegreeOf(p) == 0))) {
    			allConstraints.put(p.getId(), p);
    		}
    		
    	}
     }
    
    /**
     * sets save option for file output, used for getting solution back to SPF
     * 
     * @param save - boolean true = save to file
     * @param name - filename
     */
    public void setSaveOption (boolean save, String name) {
    	this.saveResults = save;
    	this.graphName = name;
    	this.saveFile = this.saveFile + graphName;
    }

    /*
     * called when forward analysis reaches predicate, computes stats and inputs
     */
 	@Override
    protected void calculateStats(PrintConstraint constraint) {
    	   	   	
        // get constraint info as variables
        Map<String, Integer> sourceMap = constraint.getSourceMap();
        StringBuilder stats = new StringBuilder();
        String actualVal = constraint.getActualVal();
        int base = sourceMap.get("t");
        long tTime, fTime, inMCTime, tMCTime, fMCTime = 0;

        // get id of second symbolic string if it exists
        int arg = -1;
        if (sourceMap.get("s1") != null) {
            arg = sourceMap.get("s1");
        }

        // initialize boolean flags
        boolean isSingleton = false;
        boolean trueSat = false;
        boolean falseSat = false;

        // determine if symbolic strings are singletons
        boolean argIsSingleton = false;
        if(arg != -1) {
        	argIsSingleton = solver.isSingleton(sourceMap.get("s1"));
        }
        if (solver.isSingleton(base, actualVal) &&
            (sourceMap.get("s1") == null || argIsSingleton)) {
            isSingleton = true;
        }

        long initialCount = this.invSolver.getModelCount(base);
        inMCTime = BasicTimer.getRunTime();

        // store symbolic string values
        solver.setLast(base, arg);

        // test if true branch is SAT
        parser.assertBooleanConstraint(true, constraint);
        tTime = BasicTimer.getRunTime();
        if (solver.isSatisfiable(base)) {
            trueSat = true;
        }

        long trueModelCount = this.invSolver.getModelCount(base);
        tMCTime = BasicTimer.getRunTime();

        // revert symbolic string values
        solver.revertLastPredicate();

        // store symbolic string values
        solver.setLast(base, arg);

        // test if false branch is SAT
        parser.assertBooleanConstraint(false, constraint);
        fTime = BasicTimer.getRunTime();
        if (solver.isSatisfiable(base)) {
            falseSat = true;
        }

        long falseModelCount = this.invSolver.getModelCount(base);
        fMCTime = BasicTimer.getRunTime();

        // revert symbolic string values
        solver.revertLastPredicate();

        // if actual execution did not produce either true or false
        if (!actualVal.equals("true") && !actualVal.equals("false")) {
            System.err.println("warning constraint detected without true/false value");
            return;
        }

        // determine result of actual execution
        boolean result = true;
        if (actualVal.equals("false")) {
            result = false;
        }

        // branches disjoint?
        parser.assertBooleanConstraint(result, constraint);

        // update accumulated timer for base
        long prevTime = 0;
        if (timerMap.containsKey(base)) {
            prevTime = timerMap.get(base);
        }
        long lastTime = BasicTimer.getRunTime();
        timerMap.put(base, lastTime + prevTime);

        // update accumulated timer for arg
        prevTime = 0;
        if (timerMap.containsKey(arg)) {
            prevTime = timerMap.get(arg);
        }
        timerMap.put(arg, lastTime + prevTime);

        // store symbolic string values
        solver.setLast(base, arg);

        parser.assertBooleanConstraint(!result, constraint);

        // set yes or no for disjoint branches
        String disjoint = "yes";
        if (solver.isSatisfiable(base)) {
            disjoint = "no";
        }

        // set yes or no for disjoint branches
        long overlap = this.invSolver.getModelCount(base);

        // revert symbolic string values
        solver.revertLastPredicate();

        // get accumulated time
        long accTime = 0;
        if (timerMap.containsKey(base)) {
            accTime = timerMap.get(base);
        }

        // get constraint function name
        String constName = constraint.getSplitValue().split("!!")[0];
       
        // add boolean operation to operation list
        addBooleanOperation(base, arg, constName, constraint.getId(), argIsSingleton);

        // get operations
        String[] opsArray = this.operationsMap.get(base);
        String ops = joinStrings(Arrays.asList(opsArray), "\t -> \t");

        // gather column data in list
        List<String> columns = new ArrayList<>();
        // id
        columns.add(String.valueOf(constraint.getId()));
        // actual value
        columns.add(String.format("%s", constraint.getActualVal()));
        // is singleton?
        columns.add(String.valueOf(isSingleton));
        // true sat?
        columns.add(String.valueOf(trueSat));
        // false sat?
        columns.add(String.valueOf(falseSat));
        // disjoint?
        columns.add(String.format(disjoint));
        // id of initial model
        columns.add(String.valueOf(base));
        // initial model count
        columns.add(String.valueOf(initialCount));
        // true model count
        columns.add(String.valueOf(trueModelCount));
        // false model count
        columns.add(String.valueOf(falseModelCount));
        // overlap count
        columns.add(String.valueOf(overlap));
        // previous operations
        columns.add(ops);

        // generate row string
        String row = joinStrings(columns, "\t");

        // output row
        System.out.println(cid + row);

        
        // --------------------------------------------------------------------------------------------
        // The process for solving the inputs needed to reach the current predicate location starts here.
        // --------------------------------------------------------------------------------------------       

        // initialize our copy of the symbolic string map as it is right now
        //invSolver.initStringMap();
        //invSolver.initStringMapAccum();
        // we will rebuild all constraints, since this is a new path
        allInverseConstraints.clear();

        // clear previous input solutions
        //inputSolutions.clear();

        // save this predicate ID so we can grab the new inverse constraint
        // from the allInverseConstraints container later
        //int predicateID = constraint.getId();
        
        predicateIDs.add(constraint.getId());

        // build the transposed graph of inverse constraints
        buildICG_r3();

        // output finalized inverse constraints for debug
//        if (true) {
//        	System.out.println(cid);
//        	System.out.println(cid + "Inverse Constraint Set:");
//        	for (I_Inv_Constraint_r3<T> c : allInverseConstraints.values()) {
//        		System.out.println(cid + c.toString() + "\t" + allConstraints.get(c.getID()).toString());
//        	}
//        	System.out.println(cid);
//        }

        // get a reference to the predicate inverse constraint
       // I_Inv_Constraint_r3<T> predicate = allInverseConstraints.get(predicateID);

        // ********************************
        // The call that starts it all ....
        //predicate.evaluate(null, 0);
        // ********************************       

        // check for SAT here ...
        
        // TODO: consolidate solutions from inside sink nodes into a solution set
        // then either output solutions or write them to a file
        // THIS CODE DOES NOT CURRENTLY DO ANYTHING ....

        // indicate if output going to file ..
        if (solutionFile != "") {
        	System.out.println(cid + "Outputting to solution file: " + solutionFile);

        	// Code to output json solution file here ...
        	// A set of SPF inputs
        	SPFInputSet SPFInputs = new SPFInputSet();

        	// set this to reporter SAT ...
        	SPFInputs.SAT = true;

        	// for every solutionset, get possible strings, select one, add it to SPFInputSet
        	for (SolutionSet<T> ss : inputSolutions.values()) {
        		SPFInput SPFInput = new SPFInput();
        		SPFInput.ID = ss.getID();
        		SPFInput.input = ss.getSolution().getShortestExampleString();
        		SPFInputs.inputSet.add(SPFInput);
        	}

        	ObjectMapper mapper = new ObjectMapper(); 
        	mapper.enable(SerializationFeature.INDENT_OUTPUT);

        	try {
        		mapper.writeValue(new File(solutionFile), SPFInputs);
        	} catch (JsonGenerationException e1) {
        		System.err.println(cid + "Error Generating JSON ...");
        	} catch (JsonMappingException e1) {
        		System.err.println(cid + "Error Mapping JSON ...");
        	} catch (IOException e1) {
        		System.err.println(cid + "Error Writing JSON File ...");
        		// return false;
        	}

        }

        // output all input solutions

        // ------------------------------------------------------------------------------------    	
        // The input solution process stops here.
        // ------------------------------------------------------------------------------------
         
    }
    
    /*
     * builds the transposed graph of inverse constraints.
     * first, creates an inverse constraint for every print constraint.
     * second, sets the internal next and arg references to the correct inverse constraint.
     */
    protected void buildICG_r3 () {

    	boolean localDebug = false;
    	
    	List<Integer> args;
    	
    	// create inverse constraint for every print constraint
    	// op was set during the forward graph construction
    	for (PrintConstraint pc : allConstraints.values()) {

    		int ID = pc.getId();
    		Operation op = pc.getOp();
    		//System.out.println("ID " + ID + " op " + op);
    		I_Inv_Constraint_r3<T> newConstraint;

    		switch (op) {

    		case INIT: 

    			if (localDebug) {
    				System.out.println("processed " + op.toString() + "  " + pc.getId());
    			} 
    			
   			
    			break;

    		case INIT_CON: 

    			newConstraint = new InvConstraintConcreteValue_r3<T>(ID,invSolver);
    			allInverseConstraints.put(ID, newConstraint);
    			
    			if (localDebug) {
    				System.out.println("processed " + op.toString() + "  " + pc.getId());
    			}

    			break;

    		case INIT_SYM:

    			newConstraint = new InvConstraintInput_r3<T>(ID,invSolver);
    			allInverseConstraints.put(ID, newConstraint);
    			
    			if (localDebug) {
    				System.out.println("processed " + op.toString() + "  " + pc.getId());
    			}

    			break;

    		case PREDICATE:

    			newConstraint = new InvConstraintPredicate_r3<T>(ID,invSolver);
    			allInverseConstraints.put(ID, newConstraint);
    			
    			if (localDebug) {
    				System.out.println("processed " + op.toString() + "  " + pc.getId());
    			}

    			break;

    		case CONCAT_SYM:

    			newConstraint = new InvConstraintConcatSym_r3<T>(ID,invSolver);
    			allInverseConstraints.put(ID, newConstraint);
    			
    			if (localDebug) {
    				System.out.println("processed " + op.toString() + "  " + pc.getId());
    			}

    			break;

    		case CONCAT_CON:

    			// FIX 
    			// **** Using symbolic code for now, needs concrete ported to r3
    			newConstraint = new InvConstraintConcatSym_r3<T>(ID,invSolver);
    			allInverseConstraints.put(ID, newConstraint);
    			
    			if (localDebug) {
    				System.out.println("processed " + op.toString() + "  " + pc.getId());
    			}

    			break;	

    		case TOUPPERCASE:
    			
       			newConstraint = new InvConstraintToUpperCase_r3<T>(ID,invSolver);
    			allInverseConstraints.put(ID, newConstraint);
    			
    			if (localDebug) {
    				System.out.println("processed " + op.toString() + "  " + pc.getId());
    			}

    			break;    	

    		case TOLOWERCASE:

    			newConstraint = new InvConstraintToLowerCase_r3<T>(ID,invSolver);
    			allInverseConstraints.put(ID, newConstraint); 
    			
    			if (localDebug) {
    				System.out.println("processed " + op.toString() + "  " + pc.getId());
    			}

    			break; 

    		case SUBSTR_STRT_END:
    			
    			args = pc.getArgList();
    			newConstraint = new InvConstraintSubStringStartEnd_r3<T>(ID,invSolver,args);
    			allInverseConstraints.put(ID, newConstraint);
    			
    			if (localDebug) {
    				System.out.println("processed " + op.toString() + "  " + pc.getId());
    			}
    			
    			break; 

    		case DELETE_START_END:
    			
    			args = pc.getArgList();
    			newConstraint = new InvConstraintDeleteStartEnd_r3<T>(ID,invSolver,args);
    			allInverseConstraints.put(ID, newConstraint);
    			
    			if (localDebug) {
    				System.out.println("processed " + op.toString() + "  " + pc.getId());
    			}

    			break; 

    		case DELETE_CHAR_AT:

    			args = pc.getArgList();
    			newConstraint = new InvConstraintDeleteCharAt_r3<T>(ID,invSolver,args);
    			allInverseConstraints.put(ID, newConstraint);
    			
    			if (localDebug) {
    				System.out.println("processed " + op.toString() + "  " + pc.getId());
    			}
    			
    			break; 

    		case REPLACE_CHAR_CHAR:

    			args = pc.getArgList();
    			newConstraint = new InvConstraintReplaceCharChar_r3<T>(ID,invSolver,args);
    			allInverseConstraints.put(ID, newConstraint);
    			
    			if (localDebug) {
    				System.out.println("processed " + op.toString() + "  " + pc.getId());
    			}
    			
    			break; 

    		default:
    			
    			if (localDebug) {
    				System.out.println("WARNING: Unhandled constraint type... " + op.toString() + "  " + pc.getId() + "  " + pc.getValue());
    			}
    			
    			
    		} // end switch


    	} // end for each printconstraint


    	// all inverse constraints have been created, now we set the next and arg constraint references
    	for (PrintConstraint pc : allConstraints.values()) {

    		if (pc.getOp() != Operation.UNDEFINED) {
    			    		// each print constraint has a corresponding inverse constraint, get a reference to it
    		I_Inv_Constraint_r3<T> invConstraint = allInverseConstraints.get(pc.getId());
    	   	
    		// the next inverse constraint to evaluate is the base of printconstraint
    		I_Inv_Constraint_r3<T> nextConstraint = allInverseConstraints.get(pc.getBase());
    		if (nextConstraint != null) {
    			invConstraint.setNext(nextConstraint);
    		}
    		
    		
    		// get the arglist and check if not empty
    		List<Integer> argList = pc.getArgList();
    		
    		if (!argList.isEmpty() && pc.getOp() != Operation.SUBSTR_STRT_END) {
    				// argument present, set invConstraint argument reference.
    	 			int arg = argList.get(0);
    	 			if (arg != -1) {
    	 				
    	    			if (localDebug) {
    	    				System.out.println("pcID: " + pc.getId() + " getting arg: " + arg);
    	    			}
    	 				
    	 				
    	 				invConstraint.setArg(allInverseConstraints.get(arg));
    	 			} // end if

    	   		} // end if
    		
    		} // end if
    		
    	}  // end for each printconstraint
    	
    } // end buildICG_r3
    
    
    
    /*
     * solves inputs after all forward analysis is complete
     */
    protected void solveInputs () {
        
    	// output finalized inverse constraints for debug
        if (true) {
        	System.out.println(cid);
        	System.out.println(cid + "Inverse Constraint Set:");
        	for (I_Inv_Constraint_r3<T> c : allInverseConstraints.values()) {
        		System.out.println(cid + c.toString() + "\t" + allConstraints.get(c.getID()).toString());
        	}
        	System.out.println(cid);
        }
        
        long startTime = System.nanoTime();
        
        // initialize our copy of the symbolic string map as it is right now
        invSolver.initStringMap();
        
        // this treats all predicates as unrelated.
        // need to replace with queue and backtracking across predicates.
        for (Integer predicateID : predicateIDs) {
            // get a reference to the predicate inverse constraint
            I_Inv_Constraint_r3<T> predicate = allInverseConstraints.get(predicateID);
            
            // ********************************
            // The call that starts it all ....
            predicate.evaluate(null, 0);
            // ******************************** 
            
        }
    	
        long endTime = System.nanoTime();
        
        long durationInNano = (endTime - startTime);
        
        long durationInMillis = TimeUnit.NANOSECONDS.toMillis(durationInNano);
        
        for (I_Inv_Constraint_r3<T> i : allInverseConstraints.values())  {
        	if (i.getOp() == Operation.INIT_SYM) {
        		if (i.getSolution() == null) {
        			System.out.println("\nFAILURE: Failed to get solution to one or more inputs...");
        			System.out.println("\nSOLUTION TIME ms: 0");
        			return;
        		}
        	}
        }
        
        System.out.println("\nSOLUTION TIME ms: " + durationInMillis);
        
        for (I_Inv_Constraint_r3<T> i : allInverseConstraints.values()) {
        	if (i.getOp() == Operation.INIT_SYM) {
        		T solution = i.getSolution();
        		
        		System.out.print("INPUT ID: " + i.getID() + "  COUNT: " + solution.modelCount() + "  VALUE(S): ");
        		BigInteger limit = new BigInteger("300");
        		
        		if (solution.modelCount().compareTo(limit) > 0) {
        			System.out.print("Too many values to output,  " + solution.modelCount() + "  example: ");
        			System.out.print(solution.getShortestExampleString());
        		} else {
        			for (String s : solution.getFiniteStrings()) {
        				System.out.print(s + " ");
        			}
        		
        		}			
        		System.out.println();
        	}
        }
        
        
    	
    }
    
    
    
    
    
    
    
    
    
    
    @Override
    protected void outputHeader() {

        // gather headers in list
        List<String> headers = new ArrayList<>();
        headers.add("ID");
        headers.add("ACT");
        headers.add("SING");
        headers.add("TSAT");
        headers.add("FSAT");
        headers.add("DSJ");
        headers.add("IN ID");
        headers.add("IN CT");
        headers.add("T CT");
        headers.add("F CT");
        headers.add("OLP");
        headers.add("PRE");

        // generate headers string
        String header = joinStrings(headers, "\t");

        // output header
        System.out.println(cid + header);
    }
}
