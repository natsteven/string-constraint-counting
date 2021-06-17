package edu.boisestate.cs.graph;

import edu.boisestate.cs.automatonModel.A_Model_Inverse;
//import edu.boisestate.cs.solvers.*;

public interface I_Inv_Constraint<T extends A_Model_Inverse<T>> {
	
	public boolean evaluate(I_Inv_Constraint<T> inputConstraint, int sourceIndex);
	
//	public boolean fallback();
	
	
	public T output(Integer index);
	
	public void setDebug(boolean debug);
	
	public void setArg(I_Inv_Constraint<T> constraint);
	
	
	public int getArgID();
	
	public void setNext(I_Inv_Constraint<T> constraint);
	
	public int getNextID();
	
	public void setPrev(I_Inv_Constraint<T> constraint);
	
	public int getPrevID();
	
	public T getSolution();
//	public int getPrevID();
	
//	public void setNextID(int nextID);
	
//	public void setPrevID(int prevID);
	
	
	public void setOp(Operation op);

	public Operation getOp();
	
	public void setID(int ID);
	
	public int getID();
	
}
