/**
 * 
 */
package edu.boisestate.cs.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.boisestate.cs.automatonModel.A_Model_Inverse;
import edu.boisestate.cs.solvers.Solver_Inverse_r3;
import edu.boisestate.cs.util.Tuple;

/**
 * @author marli
 *
 */
public class InvConstraintConcatSym_r3<T extends A_Model_Inverse<T>> extends A_Inv_Constraint_r3<T> {
	
	private I_Inv_Constraint suffixConstraint;
	private int suffixID;
	private boolean initialized = false;
	private List<Tuple<T,T>> outputs;
	private T inputModel;
	// private int input, arg, base;
	
	
	public InvConstraintConcatSym_r3 (int ID, Solver_Inverse_r3<T> solver) {
		
		// assignments to class variables in the abstract class A_Inv_Constraint
		this.solver = solver;
		this.ID = ID;
		//this.argList = args;
		this.op = Operation.CONCAT_SYM;
		//this.argString = "[" + argList.get(0) + "] SUFFIX";
		this.outputSet = new HashMap<Integer,T>();
		// assignments to class variables in InvConstraintConcatSymPref
		//this.suffixConstraint = suffixConstraint;
		//this.suffixID = suffixConstraint.getID();
		this.outputs = new ArrayList<Tuple<T,T>>();
	}
	
	
	
	@Override
	public boolean evaluate(I_Inv_Constraint_r3<T> inputConstraint, int sourceIndex) {
		
		evaluateCount++;
		
		System.out.format("EVALUATE CONCAT %d ...\n",ID);
		T inputs = inputConstraint.output(sourceIndex);
		//List<Tuple<T,T>> outputs;
		
		while (!inputs.isEmpty()) {
			
			T input = inputs.getShortestExampleModel();
			inputs.minus(input);
			
			newConcatChoiceCount++;
			
			T nextModel = solver.getSymbolicModel(nextConstraint.getID());
			T argModel = solver.getSymbolicModel(argConstraint.getID());
			
			outputs = input.inv_concatenate_sym_set(nextModel, argModel);
			
			for (Tuple<T,T> t : outputs) {
				System.out.format("RCVD:  P %4s  S %4s\n", t.get1().getShortestExampleString(),t.get2().getShortestExampleString());
			}
			
			while (!outputs.isEmpty()) {
				
				Tuple<T,T> split = outputs.remove(0);
				
				T prefix = split.get1();
				T suffix = split.get2();
				
				outputSet.put(1, prefix);
				outputSet.put(2, suffix);
				
				System.out.format("CHOSE: P %4s  S %4s\n", prefix.getShortestExampleString(), suffix.getShortestExampleString());
				
				newSplitOutputCount++;
				
				if (argConstraint.evaluate(this, 2)) {
					
					if (nextConstraint.evaluate(this, 1)) {
						
						return true;
					}
					
				}
				
			} // continue until suffix / prefix combinations are exhausted
		
		} // continue until all inputs are exhausted
		
		// catastrophic fail 
		return false;
	
	}

}
