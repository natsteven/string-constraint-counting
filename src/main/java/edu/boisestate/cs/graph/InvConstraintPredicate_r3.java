/**
 * 
 */
package edu.boisestate.cs.graph;

import java.util.HashMap;
import java.util.List;

import edu.boisestate.cs.automatonModel.A_Model_Inverse;
//import edu.boisestate.cs.solvers.Solver_Inverse;
import edu.boisestate.cs.solvers.Solver_Inverse_r3;

/**
 * @author marli
 *
 */
public class InvConstraintPredicate_r3<T extends A_Model_Inverse<T>> extends A_Inv_Constraint_r3<T> {

	// This will hold a reference to the containing solver.
	// This allows the constraint access to the solver functions and string tables.
	//private Solver_Inverse<T> solver;
	
	//private int ID;
	
	//private I_Inv_Constraint prevConstraint;
	//private I_Inv_Constraint nextConstraint;
	
	//private Operation op;
	
	//private List<Integer> argList;
	
	//private String argString;
	
	public InvConstraintPredicate_r3 (int ID, Solver_Inverse_r3<T> solver) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		//this.argID = argID;
		this.outputSet = new HashMap<Integer,T>();
		//this.argString = "[" + argList.get(0) + "]";
		this.op = Operation.PREDICATE;
	}
	
	
	public InvConstraintPredicate_r3 (int ID, Solver_Inverse_r3<T> solver, int argID) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argID = argID;
		this.outputSet = new HashMap<Integer,T>();
		//this.argString = "[" + argList.get(0) + "]";
		this.op = Operation.PREDICATE;
	}
	
	public InvConstraintPredicate_r3 (int ID, Solver_Inverse_r3<T> solver, List<Integer> args) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		//this.argString = "[" + argList.get(0) + "]";
		this.op = Operation.PREDICATE;
	}
	
	public InvConstraintPredicate_r3 (int ID, Solver_Inverse_r3<T> solver, List<Integer> args, int base, int input) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		//this.argString = "[" + argList.get(0) + "]";
		this.op = Operation.PREDICATE;
		this.nextID = base;
	}
	
	
	@Override
	public boolean evaluate(I_Inv_Constraint_r3<T> inputConstraint, int sourceIndex) {
		
		System.out.format("\nEVALUATE PREDICATE %d ...\n",ID);
		//T predicateResult = solver.getSymbolicModel(ID);
		T predicateResult = solver.getSymbolicModel(nextConstraint.getID());
		
		if (!predicateResult.isEmpty()) {
					
			// place symbolic string from solver string table into output set, position 1
			outputSet.put(1, predicateResult);
			
			// call evaluate on the next constraint, source is this inverse constraint
			// returning false from here means we have an error, as the predicate must 
			// be satisfiable.
			return nextConstraint.evaluate(this, 1);
		}
		
		System.out.println("ERROR: Predicate has no forward results, UNSAT");
		return false;

	}


	@Override
	public void setOp(Operation op) {
		
		this.op = op;
	}

	@Override
	public Operation getOp() {
		
		return op;
	}

	@Override
	public void setID(int ID) {
		
		this.ID = ID;
	}

	@Override
	public int getID() {

		return this.ID;
	}

}
