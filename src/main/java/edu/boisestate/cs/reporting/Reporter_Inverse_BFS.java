package edu.boisestate.cs.reporting;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import edu.boisestate.cs.util.Tuple;

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
		
		//backtrack map - records the current current queue for the backtracked id
		Map<Integer, List<Integer>> backtrackMap = new HashMap<Integer, List<Integer>>();
		
		//processed elements -- it an acyclic graph, so we just need those for efficiency
		List<Integer> processedID = new ArrayList<Integer>();
		
		//now q contains depended predicates
		while(!qID.isEmpty()) {
			int currID = qID.remove(0);
			processedID.add(currID);
			I_Inv_Constraint<T> curr = allInverseConstraints.get(currID);
			System.out.println("node: " + curr);
			System.out.println("parents are: " + curr.getPrevID());
			//1st true - continue, false - backtrack
			//2nd true - don't add to the backtrack map, false do
			Tuple<Boolean, Boolean> result = curr.evaluate();
			if(result.get1() && !result.get2()) {
				//continue but add to the backtrack queue
				List<Integer> backtrackQ = new ArrayList<Integer>();
				backtrackQ.add(curr.getID());
				backtrackQ.addAll(qID);
			} else if (!result.get1()) {
				//find the "closest" node to backtrack to curr
				//in our case one that has smallest ID
				//eas: need to use lambda-expression here
				Integer backtrackID = Integer.MAX_VALUE;
				for(Integer ids : backtrackMap.keySet()) {
					if(backtrackID > ids) {
						backtrackID = ids;
					}
				}
				
				//get the queue
				qID = backtrackMap.get(backtrackID);
				//remove backtrackID from the map
				backtrackMap.remove(backtrackID);
				//it should not contain backtrackID
				Set<Integer> clearSet = eGraph.getAncestors(allConstraints.get(backtrackID));
				clearSet.retainAll(processedID);//only keep those that have been computed
				processedID.removeAll(clearSet);//now remove them from processed -- they will be added again
				//iterate for the clearSet and call clear on each inverse constraint 
				for(int nodeID : clearSet) {
					allInverseConstraints.get(nodeID).clear();
				}
				
			}
			
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
