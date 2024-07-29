package edu.boisestate.cs.graph;

import java.util.HashMap;
import java.util.List;

import edu.boisestate.cs.automatonModel.A_Model_Inverse;
import edu.boisestate.cs.solvers.*;

/**
 * @author Marlin Roberts, 2020-2021
 *
 */
public class InvConstraintSetLength<T extends A_Model_Inverse<T>> extends A_Inv_Constraint<T> {
	
	// This will hold a reference to the containing solver.
	// This allows the constraint access to the solver functions and string tables.
//	private Solver_Inverse<T> solver;
//	
//	private int ID;
//	
//	private I_Inv_Constraint prevConstraint;
//	private I_Inv_Constraint nextConstraint;
//	
//	private Operation op;
//	
//	private List<Integer> argList;
//	
//	private String argString;
	private int length;
	
	public InvConstraintSetLength (int ID, Solver_Inverse<T> solver, List<Integer> args) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		this.op = Operation.SET_LENGTH;
		this.outputSet = new HashMap<Integer,T>();
		this.solutionSet = new SolutionSetInternal<T>(ID);
		this.argString = "0:LENGTH";
		this.length = argList.get(0);
		
	}
	
	public InvConstraintSetLength (int ID, Solver_Inverse<T> solver, List<Integer> args, int base, int input) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		this.op = Operation.SET_LENGTH;
		this.argString = "0:LENGTH";
		this.length = argList.get(0);
		this.nextID = base;
		this.prevIDs.add(input);
	}
	
	
	@Override
	public boolean evaluate(I_Inv_Constraint<T> inputConstraint, int sourceIndex) {
		
//		System.out.format("EVALUATE SET_LENGTH %d ...\n",ID);
		
		T inputModel = inputConstraint.output(sourceIndex);

		// perform inverse function on output from the input constraint at given index
		T resModel = solver.inv_setLength(inputModel, length);

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
//				System.out.println("SET_LENGTH SOLUTION SET INCONSISTENT...");
				solutionSet.remSolution(inputConstraint.getID());
				return false;
			}
			
		} else {
//			System.out.println("SET_LENGTH RESULT MODEL EMPTY...");
			// halt solving, fallback
			return false;
		}
		
	}


//	@Override
//	public void setNext(I_Inv_Constraint constraint) {
//		
//		this.nextConstraint = constraint;
//	}
//
//	@Override
//	public void setPrev(I_Inv_Constraint constraint) {
//		
//		this.prevConstraint = constraint;
//	}
//
//	@Override
//	public void setOp(Operation op) {
//		
//		this.op = op;
//	}
//
//	@Override
//	public Operation getOp() {
//		
//		return op;
//	}
//
//	@Override
//	public void setID(int ID) {
//		
//		this.ID = ID;
//	}
//
//	@Override
//	public int getID() {
//
//		return this.ID;
//	}

}
