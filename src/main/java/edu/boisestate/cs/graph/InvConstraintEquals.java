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

	//the result of euqals to evalute to: true or false;
	private boolean result;


	public InvConstraintEquals (int ID, Solver_Inverse<T> solver, boolean  result) {

		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		//this.argID = argID;
		this.outputSet = new HashMap<Integer,T>();
		//this.argString = "[" + argList.get(0) + "]";
		this.op = Operation.EQUALS;
		this.result = result;
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
			inputs = solver.getSymbolicModel(nextConstraint.getID()).clone();
		}

		T argResult = solver.getSymbolicModel(argConstraint.getID()).clone();
		//System.out.println("arg " + argResult.getFiniteStrings());
		//T predicateResult = solver.getSymbolicModel(nextConstraint.getID());
		//	System.out.println("solver " + solver);
		//System.out.println("nextConstr " + nextConstraint.getID());
		//System.out.println("predicate " + inputs.getFiniteStrings());

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
				//System.out.println("arg " + (this.argConstraint==null? null : this.argConstraint.getOp()));
				//System.out.println("oper " + this.nextConstraint.getOp());
				// place symbolic string from solver string table into output set, position 1
				//because the second argument is concrete inputs will always has one values
				//thus there is no need to intersect here.
				outputSet.put(indxSymb, inputs);
				outputSet.put(indxConcr, solver.getSymbolicModel(argID));//the argument for now is concrete, so whatever is coming from it
			} else {
				//none of them are concrete need to ensure consistency
				//should be operator dependent
				System.out.println("Two Symbolic " + this.getOp()); 
				if(result) {
					//expected result is true	
					T input = inputs.getShortestExampleModel();
					System.out.println("Solution " + input.getFiniteStrings());
					//remove it from the inputs
					inputs.minus(input);
					//because of the equality operator perform intersection
					//of both automata there is no need to do intersection,
					//such narrowing already ensures that input is in both models.
					//set it to both outputs
					outputSet.put(1, input);
					outputSet.put(2, input);
					if(!inputs.isEmpty()) {
						//adding to backtracking
						ret = new Tuple<Boolean,Boolean>(true, false);
					}
				} else {
					//expected result is false
					System.out.println("Not equals");

					//argument model
					T input2 = solver.getSymbolicModel(argConstraint.getID()).clone();
					//if both have no common strings: intersection is empty
					boolean common = !solver.intersect(inputs, argID).isEmpty();
					System.out.println("Target and args have common strings? " + common);

					//if no common string then propagate them both up
					if(!common) { //turn back to !common after done debugging
						outputSet.put(1, inputs);
						outputSet.put(2, input2);
						//return the default true, true
						//no backtracking here back
					} else {
						
						//TOD: an optimization where inputs, over which we iterate
						//single values has the smallest number of strings in
						//its solution set.

						T input1 = inputs.getShortestExampleModel();
						//remove it from the set
						inputs.minus(input1);
						//create a copy of the full model

						//leave the set of strings to which input1 is not equal to.
						input2.minus(input1);
						//no need to do intersection the incoming values are actually ones
						//from the nodes themselves, and not over-approximating computations.
						
						//try a different concrete string and find all string for arg
						//that are not equal to that string, and propagate up.
						while (input2.isEmpty()) {
							input1 = inputs.getShortestExampleModel();
							inputs.minus(input1);
							input2 = solver.getSymbolicModel(argConstraint.getID()).clone();
							input2.minus(input1);
						}
						
//						System.out.println("target: " + input1.getFiniteStrings() + " arg: " + 
//								input2.getFiniteStrings());
						//send them up
						outputSet.put(1, input1);
						outputSet.put(2, input2);
						if(!inputs.isEmpty()) {
							//adding to backtracking
							ret = new Tuple<Boolean,Boolean>(true, false);
						}
					}
				}
			}

		} else {
			System.out.println("ERROR: Euqals has no forward results, UNSAT");
			ret = new Tuple<Boolean,Boolean>(false, true);
		}

		//eas: depends on the type of the query and arguments it might needs to backtrack
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
