package edu.boisestate.cs.reporting;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jgrapht.DirectedGraph;

import edu.boisestate.cs.Parser_2;
import edu.boisestate.cs.automatonModel.A_Model_Inverse;
import edu.boisestate.cs.graph.I_Inv_Constraint;
import edu.boisestate.cs.graph.InvDefaultDirectedGraph;
import edu.boisestate.cs.graph.Operation;
import edu.boisestate.cs.graph.PrintConstraint;
import edu.boisestate.cs.graph.SymbolicEdge;
import edu.boisestate.cs.solvers.Solver_Inverse;

public class Reporter_Inverse_BFS<T extends A_Model_Inverse<T>> extends Reporter_Inverse<T> {
	//this class should also remember all previous constraints, it might be in
	//allConstraints

	public Reporter_Inverse_BFS(DirectedGraph<PrintConstraint, SymbolicEdge> graph, Parser_2<T> parser, 
							Solver_Inverse<T> invSolver, boolean debug) {
		super(graph, parser, invSolver, debug);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void solveInputs() {
		//from Marlin's code
    	// output finalized inverse constraints for debug
        if (true) {
        	System.out.println(cid);
        	System.out.println(cid + "Inverse Constraint Set:");
        	for (I_Inv_Constraint<T> c : allInverseConstraints.values()) {
        		System.out.println(cid + c.toString() + "\t" + allConstraints.get(c.getID()).toString());
        	}
        	System.out.println(cid);
        }
        
        long startTime = System.nanoTime();
        
        // initialize our copy of the symbolic string map as it is right now
        invSolver.initStringMap();
        //end from Marlin's code
		
		
		System.out.println("Solving using BFS");
		//create a queue of all dependent predicates
		System.out.println(predicateIDs);
		InvDefaultDirectedGraph eGraph = (InvDefaultDirectedGraph)graph;
		List <Integer> qID = new ArrayList<Integer>();
		//predicateIDs have the last predicate is the current constraint predicate
		//and it contains all predicates solved so far
		qID.addAll(predicateIDs);
		qID.retainAll(eGraph.getDependedPredicates(predicateIDs.get(predicateIDs.size()-1)));
		System.out.println("Q " + qID);
		System.out.println(predicateIDs);
		List<I_Inv_Constraint<T>> q = new ArrayList<I_Inv_Constraint<T>>();
		for(Integer val : qID) {
			q.add(allInverseConstraints.get(val));
		}
		
		//now q contains depended predicates
		while(!qID.isEmpty()) {
			I_Inv_Constraint<T> curr = allInverseConstraints.get(qID.remove(0));
			System.out.println("node: " + curr);
			System.out.println("parents are: " + curr.getPrevID());
			boolean result = curr.evaluate();
			System.out.println("don't backtrack? " + result);
			
			//extend the queue
			if(curr.getNextID() != -1 && !qID.contains(curr.getNextID())) {
				qID.add(curr.getNextID());
			}
			if(curr.getArgID() != -1 && !qID.contains(curr.getArgID())) {
				qID.add(curr.getArgID());
			}
			
		}
		
		//ending -- Marlin's code
long endTime = System.nanoTime();
        
        long durationInNano = (endTime - startTime);
        
        long durationInMillis = TimeUnit.NANOSECONDS.toMillis(durationInNano);
        
        for (I_Inv_Constraint<T> i : allInverseConstraints.values())  {
        	if (i.getOp() == Operation.INIT_SYM) {
        		if (i.output(0) == null || i.output(0).isEmpty()) {
        			System.out.println("\nFAILURE: Failed to get solution to one or more inputs...");
        			System.out.println("\nSOLUTION TIME ms: 0");
        			return;
        		}
        	}
        }
        
        System.out.println("\nSOLUTION TIME ms: " + durationInMillis);
        
        for (I_Inv_Constraint<T> i : allInverseConstraints.values()) {
        	if (i.getOp() == Operation.INIT_SYM) {
        		T solution = i.output(0);//symbolic nodes hold their solution in the output values
        		
        		// populate map for output to file/SPF
        		inputSolution.put(i.getID(), solution);
        		
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

}
