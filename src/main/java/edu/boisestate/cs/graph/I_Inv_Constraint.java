package edu.boisestate.cs.graph;

import edu.boisestate.cs.solvers.*;

/**
 * Deprecated - marked for deletion 
 * replaced by _r3 revisions 
 *
 * @author marli
 *
 */


public interface I_Inv_Constraint {
	
	public boolean evaluate();
	
	public boolean fallback();
	
	public void setDebug(boolean debug);
	
	public void setNext(I_Inv_Constraint constraint);
	
	public void setPrev(I_Inv_Constraint constraint);
	
	public int getNextID();
	
	public int getPrevID();
	
	public void setNextID(int nextID);
	
	public void setPrevID(int prevID);
	
	
	public void setOp(Operation op);

	public Operation getOp();
	
	public void setID(int ID);
	
	public int getID();
	
}
