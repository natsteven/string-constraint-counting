/**
 * 
 */
package edu.boisestate.cs.graph;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import edu.boisestate.cs.automatonModel.A_Model_Inverse;
//import edu.boisestate.cs.solvers.Solver_Inverse;
import edu.boisestate.cs.solvers.Solver_Inverse;


/**
 * @author marli
 *
 */
public class InvConstraintInput_r3<T extends A_Model_Inverse<T>>  extends A_Inv_Constraint<T> {

	
	//private SolutionSet<T> solutionSet;
	
	private int solutionIndex = -1;
	
	
	public InvConstraintInput_r3 (int ID, Solver_Inverse<T> solver) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.outputSet = new HashMap<Integer,T>();
		this.solutionSet = new SolutionSetInternal<T>(ID);
		this.op  = Operation.INIT_SYM;
		this.argString = "[NONE]";

	}
	
	
	public InvConstraintInput_r3 (int ID, Solver_Inverse<T> solver, List<Integer> args, SolutionSet<T> solutionSet) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		this.op  = Operation.INPUT;
		this.argString = "[NONE]";
		//this.solutionSet = solutionSet;
	}
	
	public InvConstraintInput_r3 (int ID, Solver_Inverse<T> solver, List<Integer> args, SolutionSet<T> solutionSet, int base, int input) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		this.op  = Operation.INPUT;
		this.argString = "[NONE]";
		//this.solutionSet = solutionSet;
		this.nextID = base;
		this.prevID = input;
		solver.duplicateString(args.get(0), ID);
	}
	
	
	@Override
	public boolean evaluate(I_Inv_Constraint<T> inputConstraint, int sourceIndex)  {
		
		//T solution = solver.getSymbolicModel(this.prevConstraint.getID());
		System.out.format("EVALUATE INPUT %d ...\n",ID);
		T solution = inputConstraint.output(sourceIndex);
		
		if (true) {
			System.out.print("DEBUG " + op.toString() + " " + ID);
			System.out.print(" solutions .... ");
			
    		BigInteger oneHundred = new BigInteger("300");
    		
    		if (solution.modelCount().compareTo(oneHundred) > 0) {
    			System.out.print("Too many values to output,  " + solution.modelCount() + "  example: ");
    			System.out.println(solution.getShortestExampleString());
    		} else {
    			for (String s : solution.getFiniteStrings()) {
    				System.out.print(s + " ");
    			}
    		System.out.println();
    		}			
			
			
//			for (String s : solution.getFiniteStrings()) {
//				System.out.print(s + " ");
//			}
//			System.out.println();
		
		
		}
		
//		if (solutionIndex == -1) {
//			//solutionIndex = solutionSet.add(solution);
//		} else { 
//			//solutionSet.set(solution, solutionIndex);
//		}
		
		System.out.format("SETTING INPUT %d INCOMING EDGE %d\n", ID, inputConstraint.getID());
		solutionSet.setSolution(inputConstraint.getID(), solution);
		
		if (!solutionSet.isConsistent()) {
			System.out.println("      solution set " + this.ID + " not consistent, falling back ...");
			solutionSet.remSolution(inputConstraint.getID());
			return false;
		}
		
		//if (debug) {
			System.out.println("      solution set " + ID + " consistent ...");
		//}
		
		return true;
	}



}
