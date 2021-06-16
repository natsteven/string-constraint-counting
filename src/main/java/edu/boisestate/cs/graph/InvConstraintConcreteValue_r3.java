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
public class InvConstraintConcreteValue_r3<T extends A_Model_Inverse<T>>  extends A_Inv_Constraint_r3<T> {

	
	//private SolutionSet<T> solutionSet;
	
	private int solutionIndex = -1;
	
	
	public InvConstraintConcreteValue_r3 (int ID, Solver_Inverse_r3<T> solver) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.outputSet = new HashMap<Integer,T>();
		this.solutionSet = new SolutionSetInternal<T>(ID);
		this.op  = Operation.INIT_CON;
		this.argString = "[NONE]";

	}
	
	
	public InvConstraintConcreteValue_r3 (int ID, Solver_Inverse_r3<T> solver, List<Integer> args, SolutionSet<T> solutionSet) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		this.op  = Operation.INIT_CON;
		this.argString = "[NONE]";
		//this.solutionSet = solutionSet;
	}
	
	public InvConstraintConcreteValue_r3 (int ID, Solver_Inverse_r3<T> solver, List<Integer> args, SolutionSet<T> solutionSet, int base, int input) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		this.op  = Operation.INIT_CON;
		this.argString = "[NONE]";
		//this.solutionSet = solutionSet;
		this.nextID = base;
		this.prevID = input;
		solver.duplicateString(args.get(0), ID);
	}
	
	
	@Override
	public boolean evaluate(I_Inv_Constraint_r3<T> inputConstraint, int sourceIndex)  {
		
		System.out.format("EVALUATE CONCRETE VALUE %d ...\n",ID);
		
		T concrete = solver.getSymbolicModel(ID);
		String test = concrete.getShortestExampleString();
		T output = inputConstraint.output(sourceIndex);
		if (output.containsString(test)) {
			return true;
		}
		
		return false;
	}



}
