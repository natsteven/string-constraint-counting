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
public class InvConstraintSubStringStart<T extends A_Model_Inverse<T>> extends A_Inv_Constraint<T> {


	private int start,end;

	public InvConstraintSubStringStart (int ID, Solver_Inverse<T> solver, List<Integer> args) {

		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		this.op = Operation.SUBSTRING_START;
		this.outputSet = new HashMap<Integer,T>();
		this.solutionSet = new SolutionSetInternal<T>(ID);
		this.argString = "0:START";
		this.start = argList.get(0);
		//this.end = argList.get(1);
	}

	public InvConstraintSubStringStart (int ID, Solver_Inverse<T> solver, List<Integer> args, int base, int input) {

		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		this.op = Operation.SUBSTRING_START;
		this.argString = "0:START";
		this.start = argList.get(0);
		//this.end = argList.get(1);
		this.nextID = base;
		this.prevIDs = new HashSet<Integer>(); this.prevIDs.add(input);
	}


	@Override
	public boolean evaluate(I_Inv_Constraint<T> inputConstraint, int sourceIndex) {

		System.out.format("EVALUATE SUBSTRING %d ...\n",ID);

		T inputModel = inputConstraint.output(sourceIndex);

		// perform inverse function on output from the input constraint at given index
		T resModel = solver.inv_substring(inputModel, start);

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
				System.out.println("SUBSTRING SOLUTION SET INCONSISTENT...");
				solutionSet.remSolution(inputConstraint.getID());
				return false;
			}

		} else {
			System.out.println("SUBSTRING RESULT MODEL EMPTY...");
			// halt solving, fallback
			return false;
		}

	}

	@Override
	public Tuple<Boolean, Boolean> evaluate(){
		System.out.format("EVALUATE SUBSTRING %d ...\n",ID);
		Tuple<Boolean, Boolean>  ret = new Tuple<Boolean,Boolean>(true, true);

		T inputs = incoming();
		if(inputs.isEmpty()) {
			System.out.println("SUBSTRING SOLUTION INCOMING SET INCONSISTENT...");
			ret = new Tuple<Boolean,Boolean>(false, true);
		} else {
			// perform inverse function on output from the input constraint at given index
			T resModel = solver.inv_substring(inputs, start, end);

			// intersect result with forward analysis results from previous constraint
			resModel = solver.intersect(resModel, nextConstraint.getID());

			if(!resModel.isEmpty()) {
				// store result in this constraints output set at index 1
				outputSet.put(1, resModel);	
			} else {
				System.out.println("SUBSTRING OUTPUT SET IS EMTPY...");
				ret = new Tuple<Boolean,Boolean>(false, true);
			}
		}

		return ret;
	}

}
