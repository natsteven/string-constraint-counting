/**
 * 
 */
package edu.boisestate.cs.graph;

import java.util.Formatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.boisestate.cs.automatonModel.A_Model_Inverse;
//import edu.boisestate.cs.solvers.Solver_Inverse;
import edu.boisestate.cs.solvers.Solver_Inverse;
import edu.boisestate.cs.util.Tuple;

/**
 * @author marli
 *
 */
public abstract class A_Inv_Constraint<T extends A_Model_Inverse<T>> implements I_Inv_Constraint<T> {

	// This will hold a reference to the containing solver.
	// This allows the constraint access to the solver functions and string tables.
	protected Solver_Inverse<T> solver;
	
	protected int ID;
	
	protected int nextID = -1;
	protected Set<Integer> prevIDs = new HashSet<Integer>();
	protected int argID = -1;
	
	public int fallbackCount = 0;
	public int evaluateCount = 0;
	public int newConcatChoiceCount = 0;
	public int newSplitOutputCount = 0;
	
	protected Set<I_Inv_Constraint<T>> prevConstraint;
	protected I_Inv_Constraint<T> nextConstraint;
	protected I_Inv_Constraint<T> argConstraint;
	
	protected SolutionSetInternal<T> solutionSet;
	
	protected Map<Integer,T> outputSet;
	
	protected Operation op; 
	
	protected List<Integer> argList;
	
	protected String argString;
	
	protected boolean debug = false;
	
	@Override
	public void clear() {
		outputSet.clear();
	}
	
	@Override
	public boolean evaluate(I_Inv_Constraint<T> inputConstraint, int sourceIndex) {
		
		return false;
	}
	
	@Override
	public Tuple<Boolean,Boolean> evaluate() {
		Tuple<Boolean,Boolean> ret = new Tuple<Boolean,Boolean>(true, true);
		T inputs = incoming();

		if(inputs.isEmpty()) {
			System.out.println("INCOMING SET INCONSISTENT...");
			ret = new Tuple<Boolean,Boolean>(false, true);
		}
		return ret;
	}
	
	protected T incoming() {
		Iterator<I_Inv_Constraint<T>> iter = prevConstraint.iterator();
		I_Inv_Constraint<T> prev = iter.next();
		//System.out.println("incoming for " + this);
		//System.out.println("prev " + prev);
		T inputs = prev.output(this).clone(); //need to close otherwise work on the actual object?
		//System.out.println("Inputs " + inputs.getFiniteStrings());

		while(iter.hasNext()) {
			T nextInput = iter.next().output(this);
			//System.out.println("Next " + nextInput.getFiniteStrings());
			inputs = inputs.intersect(nextInput);
			//System.out.println("Inputs " + inputs.getFiniteStrings());
		}
		//System.out.println(inputs.getFiniteStrings());
		return inputs;
	}

	@Override
	public T output(Integer index) {
		return outputSet.get(index);
	}
	
	
	@Override
	public T output(I_Inv_Constraint<T> childConstraint) {
		int index = -1;
		if(childConstraint.equals(nextConstraint)) {
			index = 1;
		} else if (childConstraint.equals(argConstraint)) {
			index = 2;
		}
		if (index == -1) {
			System.out.println("ERROR: child constraint not found in output");
			System.out.println("This: " + this);
			System.out.println("Child: " + childConstraint);
			System.out.println("Next: " + nextConstraint);
			// replace char is 'dependent' on an arg but arg is concrete. shouldnt evaluate output for replace on arg constraints?
			System.out.println("OutputSet: " + outputSet);
			return outputSet.get(1); // default to next assuming outputSet size is 1?
		}
		return outputSet.get(index);
	}
	
//	@Override
//	public boolean fallback() {
//		
//		return false;
//	}
	
	@Override
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override
	public void setNext(I_Inv_Constraint<T> constraint) {
		
		this.nextConstraint = constraint;
		this.nextID = constraint.getID();
	}
	
	@Override
	public void setArg(I_Inv_Constraint<T> constraint) {
		
		this.argConstraint = constraint;
		this.argID = constraint.getID();
	}

	@Override
	public void setPrev(Set<I_Inv_Constraint<T>> constraints) {
		
		this.prevConstraint = constraints;
		for(I_Inv_Constraint<T> c : constraints) {
			prevIDs.add(c.getID());
		}
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
	
	@Override
	public int getNextID() {

		return this.nextID;
	}
	
	@Override
	public int getArgID() {

		return this.argID;
	}
	
	@Override
	public Set<Integer> getPrevID() {

		return this.prevIDs;
	}
	
	
	public T getSolution() {
		
		
		return solutionSet.getSolution();
		
	};
	
	/**
	 * synch prevIDs
	 */
	@Override
	public void update() {
		Set<I_Inv_Constraint<T>> remove = new HashSet<I_Inv_Constraint<T>>();
		for(I_Inv_Constraint<T> c  : prevConstraint) {
			if(!prevIDs.contains(c.getID())) {
				remove.add(c);
			}
		}
		//remove all those that are not in the set
		prevConstraint.removeAll(remove);
	}
	
	
//	@Override
//	public void setNextID(int nextID) {
//
//		this.nextID = nextID;
//	}
	
//	@Override
//	public void setPrevID(int prevID) {
//
//		this.prevID = prevID;
//	}
	
	
	public String toString() {
		
		StringBuilder output = new StringBuilder();
		Formatter fm = new Formatter (output);
//		int arg0 = 0;
//		if (argList.size() >= 1) arg0 = argList.get(0);
		
		//fm.format("[%d] \t%s\tBASE(N): [%d] \tINP(P): [%d] \tARGS: [%d] ARG(0): [%d]",ID,op,nextID,prevID,argList.size(),arg0);
		fm.format("[%d] \t%-15s\tBASE(N): [%d] \t\tARG: [%d]",ID,op,nextID,argID);
		
//		fm.format("ID: [%d] | ", ID);
//		fm.format("OP: [%s] | ", op.toString());
//		fm.format("ARGS: %s | ", this.argString);
//		fm.format("NEXT: [%d] | ", this.nextConstraint != null ? this.nextConstraint.getID() : -1);
//		fm.format("PREV: [%d]\n", this.prevConstraint != null ? this.prevConstraint.getID() : -1);
		fm.close();
		
		
		
		
		return output.toString();
	}

}
