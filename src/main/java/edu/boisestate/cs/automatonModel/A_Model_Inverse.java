package edu.boisestate.cs.automatonModel;

import dk.brics.automaton.Automaton;
import edu.boisestate.cs.Alphabet;


public abstract class A_Model_Inverse <T extends A_Model_Inverse <T>> extends A_Model<T> implements I_Model_Inverse <T> {
	
	// MJR - Inverse operations may need to widen string to arbitrary size since original length may not be known.
	// MJR - inv_Substring
	// MJR - widened string cut back to size when intersected with previous state
	protected int maxStringPadding = 64;
	protected Automaton automaton;
    protected boolean debug = false;
	
    protected A_Model_Inverse(Alphabet alphabet, int initialBoundLength) {
    	super(alphabet,initialBoundLength);
    }
    
    public String getShortestExampleString() {
        return this.automaton.getShortestExample(true);
    }
    
    public abstract T getShortestExampleModel();
    
    public Automaton getAutomatonObject() {
 	   return this.automaton;
    }

    protected void printDebug(String message) {
        if (debug) System.out.println(message);
    }

}
