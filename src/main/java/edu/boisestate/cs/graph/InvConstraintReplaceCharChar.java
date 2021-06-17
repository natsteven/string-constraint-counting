/**
 * 
 */
package edu.boisestate.cs.graph;

import java.util.HashMap;
import java.util.List;

import edu.boisestate.cs.automatonModel.A_Model_Inverse;
import edu.boisestate.cs.solvers.*;

/**
 * @author marli
 *
 */
public class InvConstraintReplaceCharChar<T extends A_Model_Inverse<T>> extends A_Inv_Constraint<T> {
	
	private int find,replace;
	
	public InvConstraintReplaceCharChar (int ID, Solver_Inverse<T> solver, List<Integer> args) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		this.op = Operation.REPLACE_CHAR_CHAR;
		this.outputSet = new HashMap<Integer,T>();
		this.solutionSet = new SolutionSetInternal<T>(ID);
		this.argString = "0:FIND 1:REPLACE";
		this.find = argList.get(0);
		this.replace = argList.get(1);
	}
	
	public InvConstraintReplaceCharChar (int ID, Solver_Inverse<T> solver, List<Integer> args, int base, int input) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		this.op = Operation.REPLACE_CHAR_CHAR;
		this.argString = "0:FIND 1:REPLACE";
		this.find = argList.get(0);
		this.replace = argList.get(1);
		this.nextID = base;
		this.prevID = input;
	}
	
	
	@Override
	public boolean evaluate(I_Inv_Constraint<T> inputConstraint, int sourceIndex) {
		
		// solver.inv_replaceCharKnown(ID, prevConstraint.getID(), (char) find, (char) replace);
		System.out.format("EVALUATE REPLACE CHAR %d ...\n",ID);
		
		T inputModel = inputConstraint.output(sourceIndex);

		// perform inverse function on output from the input constraint at given index
		T resModel = solver.inv_replaceCharKnown(inputModel, (char) find, (char) replace);

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
				System.out.println("REPLACE CHAR SOLUTION SET INCONSISTENT...");
				solutionSet.remSolution(inputConstraint.getID());
				return false;
			}
			
		} else {
			System.out.println("REPLACE CHAR RESULT MODEL EMPTY...");
			// halt solving, fallback
			return false;
		}
		
	}



}
