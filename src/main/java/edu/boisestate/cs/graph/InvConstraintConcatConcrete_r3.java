/**
 * 
 */
package edu.boisestate.cs.graph;

import java.util.List;

import edu.boisestate.cs.automatonModel.A_Model_Inverse;
//import edu.boisestate.cs.solvers.Solver_Inverse;
import edu.boisestate.cs.solvers.Solver_Inverse_r3;

/**
 * @author marli
 *
 */
public class InvConstraintConcatConcrete_r3<T extends A_Model_Inverse<T>> extends A_Inv_Constraint_r3<T> {

	
	public InvConstraintConcatConcrete_r3 (int ID, Solver_Inverse_r3<T> solver, List<Integer> args) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		this.op = Operation.CONCAT_CON;
		this.argString = "[" + argList.get(0) + "] SUFFIX";
	}
	
	public InvConstraintConcatConcrete_r3 (int ID, Solver_Inverse_r3<T> solver, List<Integer> args, int base, int input) {
		
		// Store reference to solver
		this.solver = solver;
		this.ID = ID;
		this.argList = args;
		this.op = Operation.CONCAT_CON;
		this.argString = "[" + argList.get(0) + "] SUFFIX";
		this.nextID = base;
		this.prevID = input;
	}
	
	
	@Override
	public boolean evaluate(I_Inv_Constraint_r3<T> inputConstraint, int sourceIndex) {
		
		int arg = argList.get(0);
		solver.inv_append(ID, prevConstraint.getID(), arg);
		solver.intersectPrevious(ID, nextConstraint.getID());
		
		if (true) {
			System.out.println("\nDEBUG evaluate inv_Concat_Con " + op.toString() + " " + ID);
			System.out.print("      prefixes .... ");
			for (String s : solver.getSymbolicModel(ID).getFiniteStrings()) {
				System.out.print(s + " ");
			}
			System.out.println();
		}
		
		if (nextConstraint != null) {
			//solver.pushInvConstraint(nextConstraint);
			return true;
		} else {
			return false;
		}

	}

//	@Override
//	public boolean fallback() {
//		
//		if (debug) {
//			System.out.println("\nDEBUG fallback " + op.toString() + " " + ID);
//			System.out.println("      falling back to " + prevID);
//		}
//		
//		// This operation has no further results to give, so it 
//		// continues to fallback to the previous constraint.
//		if (prevConstraint != null) {
//			return prevConstraint.fallback();
//		} else {
//			return false;
//		}
//	}


}
