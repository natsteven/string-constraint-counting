/**
 * 
 */
package edu.boisestate.cs.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.boisestate.cs.automatonModel.A_Model_Inverse;
import edu.boisestate.cs.solvers.Solver_Inverse;
import edu.boisestate.cs.util.Tuple;

/**
 * @author Marlin Roberts, 2020-2021
 *
 */
public class InvConstraintConcatSym<T extends A_Model_Inverse<T>> extends A_Inv_Constraint<T> {

	//	private I_Inv_Constraint suffixConstraint;
	//	private int suffixID;
	//	private boolean initialized = false;
	private List<Tuple<T,T>> outputs;
	//	private T inputModel;
	// private int input, arg, base;
	private T inputs = null;
	//for BFS we need a map: input to outputs and does not use outputs
	private Map<T, List<Tuple<T,T>>> mapInOut = new HashMap<T, List<Tuple<T,T>>>();

	public InvConstraintConcatSym (int ID, Solver_Inverse<T> solver) {

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
	public boolean evaluate(I_Inv_Constraint<T> inputConstraint, int sourceIndex) {

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
	
	@Override
	public void clear() {
		super.clear();
		inputs = null;
		mapInOut.clear();
		
	}

	@Override
	public Tuple<Boolean, Boolean> evaluate() {
		Tuple<Boolean, Boolean> ret = new Tuple<Boolean, Boolean>(true, true); //continue and don't add to backtrack
		//compute the intersection of all incoming values

		if(inputs == null) {
			//the first time the node is evaluated
			//do the intersection
			inputs = incoming();
		}
		
		//remove one solution from the inputs
		T input = inputs.getShortestExampleModel();
		List<Tuple<T,T>> currOutput = new ArrayList<Tuple<T,T>>();
		//System.out.println("mapInOut " + mapInOut);
		if(mapInOut.containsKey(input)) {
			//already computed outputs before just get the next value
			currOutput = mapInOut.get(input);
		} else {
			//compute it fresh and add to the map
			//compute outgoing solutions
			T nextModel = solver.getSymbolicModel(nextConstraint.getID());
			T argModel = solver.getSymbolicModel(argConstraint.getID());
//			System.out.println("input " + input.getFiniteStrings());
//			System.out.println("nextM " + nextModel.getFiniteStrings());
//			System.out.println("argM " + argModel.getFiniteStrings());
			//let's try this input
			currOutput = input.inv_concatenate_sym_set(nextModel, argModel);
			//if not a single split is produced then try another if some inputs are left
			//System.out.println("currOutput " + currOutput);
			while(currOutput.isEmpty()) {
					//more inputs left?
					inputs.minus(input);
					System.out.println("inputs empty " + inputs.isEmpty());
					if(inputs.isEmpty()) {
						//backtrack to previous nodes, don't add to backtrack, no more inputs are left here
						return new Tuple<Boolean, Boolean>(false, true);
					}
					//try them that
					input = inputs.getShortestExampleModel();
					currOutput = input.inv_concatenate_sym_set(nextModel, argModel);
			}
			mapInOut.put(input, currOutput);
			
		}
		for (Tuple<T,T> t : currOutput) {
			System.out.format("RCVD:  P %4s  S %4s\n", t.get1().getShortestExampleString(),t.get2().getShortestExampleString());
		}
		
		//recomputing new outgoing values and updating the 
		//set of choices
		Tuple<T,T> split = currOutput.remove(0);

		T prefix = split.get1();
		T suffix = split.get2();

		outputSet.put(1, prefix);
		outputSet.put(2, suffix);
		
		//if there are more choices left in currOutput then 
		//backtrack to it
		if(!currOutput.isEmpty()) {
			ret = new Tuple<Boolean, Boolean>(true, false);//continue and add to backtrack
		} else {
			//this input has been processed
			//remove from inputs and from the map
			inputs.minus(input);
			mapInOut.remove(input);
			//check if more input left
			if(!inputs.isEmpty()) {
				ret = new Tuple<Boolean, Boolean>(true, false);//continue and add to backtrack since there are more inputs
			}
		}
		
		System.out.format("CHOSE: P %4s  S %4s\n", prefix.getShortestExampleString(), suffix.getShortestExampleString());


		return ret;
	}

}
