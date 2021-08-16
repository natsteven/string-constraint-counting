package edu.boisestate.cs.graph;

import java.util.Set;

import edu.boisestate.cs.automatonModel.A_Model_Inverse;
//import edu.boisestate.cs.solvers.*;

public interface I_Inv_Constraint<T extends A_Model_Inverse<T>> {
	
	public boolean evaluate(I_Inv_Constraint<T> inputConstraint, int sourceIndex);
	/**
	 * A second version of evaluate to be used in BFS algorithm
	 * Returns false if the node needs to backtracked because:
	 * either not all inputs were processed, or not all output solutions were examined
	 * @param inputConstraint
	 * @return true - don't backtrack, false - backtrack
	 */
	public boolean evaluate();
	
//	public boolean fallback();
	
	
	public T output(Integer index);
	public T output(I_Inv_Constraint<T> constraint);
	
	public void setDebug(boolean debug);
	
	public void setArg(I_Inv_Constraint<T> constraint);
	
	
	public int getArgID();
	
	public void setNext(I_Inv_Constraint<T> constraint);
	
	public int getNextID();
	
	public void setPrev(Set<I_Inv_Constraint<T>> constraint);
	
	public Set<Integer> getPrevID();
	
	public T getSolution();
//	public int getPrevID();
	
//	public void setNextID(int nextID);
	
//	public void setPrevID(int prevID);
	
	
	public void setOp(Operation op);

	public Operation getOp();
	
	public void setID(int ID);
	
	public int getID();
	
}
