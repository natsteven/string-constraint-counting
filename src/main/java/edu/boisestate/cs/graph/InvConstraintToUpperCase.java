/**
 * 
 */
package edu.boisestate.cs.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import edu.boisestate.cs.automatonModel.A_Model_Inverse;
import edu.boisestate.cs.solvers.*;
import edu.boisestate.cs.util.Tuple;

/**
 * @author Marlin Roberts, 2020-2021
 *
 */
public class InvConstraintToUpperCase<T extends A_Model_Inverse<T>> extends A_Inv_Constraint<T> {
	
	
	public InvConstraintToUpperCase (int ID, Solver_Inverse<T> solver) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.op  = Operation.TOUPPERCASE;
		this.outputSet = new HashMap<Integer,T>();
		this.solutionSet = new SolutionSetInternal<T>(ID);
		this.argString = "[NONE]";
	}
	

	
	public InvConstraintToUpperCase (int ID, Solver_Inverse<T> solver, List<Integer> args) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		this.op = Operation.TOUPPERCASE;
		this.argString = "[NONE]";
	}
	
	public InvConstraintToUpperCase (int ID, Solver_Inverse<T> solver, List<Integer> args, int base, int input) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		this.op = Operation.TOUPPERCASE;
		this.argString = "[NONE]";
		this.nextID = base;
		this.prevIDs = new HashSet<Integer>(); this.prevIDs.add(input);
	}
	
	
	@Override
	public boolean evaluate(I_Inv_Constraint<T> inputConstraint, int sourceIndex) {

		System.out.format("EVALUATE TOUPPER %d ...\n",ID);
		
		T inputModel = inputConstraint.output(sourceIndex);

		// perform inverse function on output from the input constraint at given index
		T resModel = solver.inv_toUpperCase(inputModel);

		// intersect result with forward analysis results from previous constraint
		resModel = solver.intersect(resModel, nextConstraint.getID());


		if (!resModel.isEmpty()) {
			solutionSet.setSolution(inputConstraint.getID(), resModel);

			if (solutionSet.isConsistent()) {
	
				// store result in this constraints output set at index 1
				outputSet.put(1, resModel);	
		
				// we have values, so continue solving ...
				return nextConstraint.evaluate(this, 1);
			} else {
				System.out.println("TOUPPER SOLUTION SET INCONSISTENT...");
				solutionSet.remSolution(inputConstraint.getID());
				return false;
			}
			
		} else {
			System.out.println("TOUPPER RESULT MODEL EMPTY...");
			// halt solving, fallback
			return false;
		}

	}
	
	@Override
	public Tuple<Boolean, Boolean> evaluate(){
		Tuple<Boolean,Boolean> ret = new Tuple<Boolean,Boolean>(true, true);
		printDebug("EVALUATE TOUPPER " + ID + " ...");
//		System.out.format("EVALUATE TOLOWER %d ...\n",ID);
		T inputs = incoming();
		if(inputs.isEmpty()) {
			printDebug("TOUPPER INCOMING SET INCONSISTENT");
//			System.out.println("TOUPPER INCOMING SET INCONSISTENT...");
			ret = new Tuple<Boolean,Boolean>(false, true);
		} else {
			//done performing intersection 
			// perform inverse function on output from the input constraint at given index
			T resModel = solver.inv_toUpperCase(inputs);

			// intersect result with forward analysis results from previous constraint
			resModel = solver.intersect(resModel, nextConstraint.getID());


			if (resModel.isEmpty()) {
				printDebug("TOUPPER RESULTS MODEL EMPTY...");
//				System.out.println("TOUPPER RESULT MODEL EMPTY...");
				// halt solving, fallback
				ret = new Tuple<Boolean,Boolean>(false, true);
			} else {
				outputSet.put(1, resModel);	
			}
		}

		return ret;
	}

}
