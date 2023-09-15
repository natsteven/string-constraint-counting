/**
 * 
 */
package edu.boisestate.cs.graph;

import java.util.HashMap;
import java.util.List;

import edu.boisestate.cs.automatonModel.A_Model_Inverse;
//import edu.boisestate.cs.solvers.Solver_Inverse;
import edu.boisestate.cs.solvers.Solver_Inverse;
import edu.boisestate.cs.util.Tuple;

/**
 * @author Marlin Roberts, 2020-2021
 *
 */
public class InvConstraintEquals<T extends A_Model_Inverse<T>> extends A_Inv_Constraint<T> {

	// This will hold a reference to the containing solver.
	// This allows the constraint access to the solver functions and string tables.
	//private Solver_Inverse<T> solver;
	
	//private int ID;
	
	//private I_Inv_Constraint prevConstraint;
	//private I_Inv_Constraint nextConstraint;
	
	//private Operation op;
	
	//private List<Integer> argList;
	
	//private String argString;
	
	//to keep the initial value
	private T inputs = null;
	
	
	public InvConstraintEquals (int ID, Solver_Inverse<T> solver) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		//this.argID = argID;
		this.outputSet = new HashMap<Integer,T>();
		//this.argString = "[" + argList.get(0) + "]";
		this.op = Operation.EQUALS;
	}
	
	
	public InvConstraintEquals (int ID, Solver_Inverse<T> solver, int argID) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argID = argID;
		this.outputSet = new HashMap<Integer,T>();
		//this.argString = "[" + argList.get(0) + "]";
		this.op = Operation.EQUALS;
		
	}
	
	public InvConstraintEquals (int ID, Solver_Inverse<T> solver, List<Integer> args) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		//this.argString = "[" + argList.get(0) + "]";
		this.op = Operation.EQUALS;
	}
	
	public InvConstraintEquals (int ID, Solver_Inverse<T> solver, List<Integer> args, int base, int input) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		//this.argString = "[" + argList.get(0) + "]";
		this.op = Operation.EQUALS;
		this.nextID = base;
	}
	
	
	@Override
	public boolean evaluate(I_Inv_Constraint<T> inputConstraint, int sourceIndex) {
		
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
	public Tuple<Boolean, Boolean> evaluate() {
	System.out.format("\nBFS EVALUATE PREDICATE %d ...\n",ID);
	Tuple<Boolean, Boolean>  ret = new Tuple<Boolean,Boolean>(true, true);
	//9-15-23 handling two symbolic values
	//requires ensuring consistency between values
	//propagated up to the children of the predicate
	//thus only output the matching pair and do similar tracking
	//as in symbolic concat
	
	
	//first time around inputs have not been initialize
	if(inputs == null) {
		System.out.println("original is null");
		inputs = solver.getSymbolicModel(nextConstraint.getID());
	}
	
	//T predicateResult = solver.getSymbolicModel(ID);
	//T predicateResult = solver.getSymbolicModel(nextConstraint.getID());
//	System.out.println("solver " + solver);
	//System.out.println("nextConstr " + nextConstraint.getID());
	System.out.println("predicate " + inputs.getFiniteStrings());
	
	if (!inputs.isEmpty()) {
		
		//different cases: 
		//if one of them is concrete the solver
		//already did the narrowing and symbolic part should match
		//so it's ok to pass an entire set up
		//better query for a singelton solutions set
		if(this.argConstraint.getOp() == Operation.INIT_CON || this.nextConstraint.getOp() == Operation.INIT_CON) {
			//predicate result would go with non-concrete constraint, which
			//could be target or a source
			int indxSymb = 1;
			int indxConcr = 2;
			if(this.nextConstraint.getOp() == Operation.INIT_CON){
				indxSymb = 2;
				indxConcr = 1;
			}
			System.out.println("arg " + (this.argConstraint==null? null : this.argConstraint.getOp()));
			System.out.println("oper " + this.nextConstraint.getOp());
			// place symbolic string from solver string table into output set, position 1
			outputSet.put(indxSymb, inputs);
			outputSet.put(indxConcr, solver.getSymbolicModel(argID));//the argument for now is concrete, so whatever is coming from it
		} else {
			//none of them are concrete need to ensure consistency
			//should be operator dependent
			System.out.println("Two Symbolic " + this.getOp()); 
			T input = inputs.getShortestExampleModel();
			System.out.println("Solution " + input.getFiniteStrings());
			//remove it from the inputs
			inputs.minus(input);
			//set it to both outputs
			outputSet.put(1, input);
			outputSet.put(2, input);
			if(!inputs.isEmpty()) {
				//adding to backtracking
				ret = new Tuple<Boolean,Boolean>(true, false);
			}
		}
	
		
	} else {
		System.out.println("ERROR: Euqals has no forward results, UNSAT");
		ret = new Tuple<Boolean,Boolean>(false, true);
	}
	
	//predicate always get its results nothing has left to try
		return ret;
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
