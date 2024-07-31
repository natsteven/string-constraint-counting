package edu.boisestate.cs.reporting;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
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
	
	//private BufferedWriter out;

	public Reporter_Inverse_BFS(DirectedGraph<PrintConstraint, SymbolicEdge> graph, Parser_2<T> parser, 
			Solver_Inverse<T> invSolver, boolean debug) {
		super(graph, parser, invSolver, debug);
//		// TODO Auto-generated constructor stub
//		try {
//			out = new BufferedWriter(new FileWriter("./temp/solutions.txt"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@Override
	protected void solveInputs() {
		//from Marlin's code
		// output finalized inverse constraints for debug
		if (false) {
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
		TreeSet <Integer> qID = new TreeSet<Integer>();
		//predicateIDs have the last predicate is the current constraint predicate
		//and it contains all predicates solved so far
		qID.addAll(predicateIDs);
		qID.retainAll(eGraph.getDependedPredicates(predicateIDs.get(predicateIDs.size()-1)));
		//sort it so the predicate with the largest ids processed first
		//Collections.sort(qID, Collections.reverseOrder());
		System.out.println("Q " + qID);
		System.out.println(predicateIDs);
		List<I_Inv_Constraint<T>> q = new ArrayList<I_Inv_Constraint<T>>();
		Set<Integer> actual = new HashSet<Integer>();
		for(Integer val : qID) {
			q.add(allInverseConstraints.get(val));
			//remove nodes that have not been processed as predicates
			actual.addAll(eGraph.getAncestors(allConstraints.get(val)));
			actual.add(val);
		}
		System.out.println("actuall " + actual);
		//iterate over all actual nodes and remove them from the parents
		//those nodes that are not there
		for (I_Inv_Constraint<T> c : allInverseConstraints.values()) {
			System.out.println(c + "1 " + c.getPrevID());
			c.getPrevID().retainAll(actual);
			c.update();
			System.out.println(c + "2 " + c.getPrevID());
		}
		

		//backtrack map - records the current current queue for the backtracked id
		Map<Integer, TreeSet<Integer>> backtrackMap = new HashMap<Integer, TreeSet<Integer>>();

		//processed elements -- it an acyclic graph, so we just need those for efficiency
		List<Integer> processedID = new ArrayList<Integer>();

		//now q contains depended predicates
		while(!qID.isEmpty()) {
			int currID = qID.last();
			qID.remove(currID);
			processedID.add(currID);
			I_Inv_Constraint<T> curr = allInverseConstraints.get(currID);
			System.out.println("node: " + curr);
			System.out.println("parents are: " + curr.getPrevID());
			//1st true - continue, false - backtrack
			//2nd true - don't add to the backtrack map, false do
			Tuple<Boolean, Boolean> result = curr.evaluate();
			System.out.println("reslut " + result);
			if(result.get1()) {
				//continue by extending the queue
				if(curr.getNextID() != -1 && !qID.contains(curr.getNextID())) {
					qID.add(curr.getNextID());
				}
				if(curr.getArgID() != -1 && !qID.contains(curr.getArgID())) {
					qID.add(curr.getArgID());
				}
				if(!result.get2()) {
					//add to the backtrack queue
					TreeSet<Integer> backtrackQ = new TreeSet<Integer>();
					//just for debuging -- check to make sure there is no
					//curr.getID in the backtracking map
					if(backtrackMap.containsKey(curr.getID())) {
						System.out.println("ERRROR: adding the same id to the backtrack map " + curr.getID());
					} else {
						backtrackQ.add(curr.getID());
						backtrackQ.addAll(qID);
						backtrackMap.put(curr.getID(), backtrackQ);
					}
				}
			} else {
				//find the "closest" node to backtrack to curr
				//in our case one that has smallest ID since
				//the numbering go up as we go closer to the
				//predicates
				//eas: need to use lambda-expression here
				System.out.println("BACKTRAKING " + backtrackMap);
				Integer backtrackID = Integer.MAX_VALUE; //all our nodes have positive ids
				for(Integer ids : backtrackMap.keySet()) {
					if(backtrackID > ids) {
						backtrackID = ids;
					}
				}
				System.out.println("backtrackID " + backtrackID);
				//case when nothing to backtrack to
				if(backtrackID != Integer.MAX_VALUE) {
					//get the queue
					qID = backtrackMap.get(backtrackID);
					//remove backtrackID from the map
					backtrackMap.remove(backtrackID);
					//it should not contain backtrackID
					Set<Integer> clearSet = new HashSet<Integer>();
					//include all descendants of the elements in qID
					for(int clearEl : qID) {
							clearSet.addAll(eGraph.getAncestors(allConstraints.get(clearEl)));
					}
					clearSet.remove(backtrackID);//remove the node itself to make more choices
					clearSet.retainAll(processedID);//only keep those that have been computed
					processedID.removeAll(clearSet);//now remove them from processed -- they will be added again
					System.out.println("clearing  " + clearSet);
					//iterate for the clearSet and call clear on each inverse constraint 
					for(int nodeID : clearSet) {
						allInverseConstraints.get(nodeID).clear();
					}

				} else {
					//nothing to backtrack to stop iterations
					//need to try for the next length
					break;
				}
			}


		}

		//ending -- Marlin's code
		long endTime = System.nanoTime();

		long durationInNano = (endTime - startTime);

		long durationInMillis = TimeUnit.NANOSECONDS.toMillis(durationInNano);

		//for (I_Inv_Constraint<T> i : allInverseConstraints.values())  {
		//those inputs that have been processed
		for(int id : processedID) {
			I_Inv_Constraint<T> i = allInverseConstraints.get(id);
			if (i.getOp() == Operation.INIT_SYM) {
				if (i.output(0) == null || i.output(0).isEmpty()) {
					System.out.println("\nFAILURE: Failed to get solution to one or more inputs...");
					System.out.println("\nSOLUTION TIME ms: 0");
					return;
				}
			}
		}

		System.out.println("\nSOLUTION TIME ms: " + durationInMillis);

		//for (I_Inv_Constraint<T> i : allInverseConstraints.values()) {
		for(int id : processedID) {
			I_Inv_Constraint<T> i = allInverseConstraints.get(id);
			if (i.getOp() == Operation.INIT_SYM) {
				T solution = i.output(0);//symbolic nodes hold their solution in the output values

				// populate map for output to file/SPF
				inputSolution.put(i.getID(), solution);

				System.out.println(i.getID() + ": " + solution.getShortestExampleString());

			}
		}

	}

}
