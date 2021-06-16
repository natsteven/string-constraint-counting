package edu.boisestate.cs.automatonModel;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;
//import dk.brics.string.stringoperations.*;
import edu.boisestate.cs.Alphabet;
//import edu.boisestate.cs.automatonModel.operations.*;

//import java.math.BigInteger;
//import java.util.Set;

public class Model_Acyclic_Inverse_Manager extends A_Model_Manager <Model_Acyclic_Inverse> {

    private final int boundLength;
    
    /**
     * 
     * @param alphabet
     * @param boundLength
     */
   public Model_Acyclic_Inverse_Manager(Alphabet alphabet, int boundLength) {
           	
       	super(alphabet,boundLength);
        this.alphabet = alphabet;
        this.boundLength = boundLength;

        // set automaton minimization as huffman
        Automaton.setMinimization(0);
    }

//    static void setInstance(Alphabet alphabet, int initialBoundLength) {
//        instance =
//                new Model_Acyclic_Manager(alphabet, initialBoundLength);
//    }

    @Override
    public Model_Acyclic_Inverse createAnyString(int initialBound) {
        return this.createAnyString(0, initialBound);
    }

    @Override
    public Model_Acyclic_Inverse createAnyString(int min, int max) {

        // create any string automaton from alphabet
        String charSet = this.alphabet.getCharSet();
        Automaton anyChar = BasicAutomata.makeCharSet(charSet);

        // create bounded automaton
        Automaton acyclicAutomaton = anyChar.repeat(min, max);

        // return model from bounded automaton
        return new Model_Acyclic_Inverse(acyclicAutomaton, this.alphabet, this.boundLength);
    }

    @Override
    public Model_Acyclic_Inverse createAnyString() {

        // create any string automaton from alphabet
        String charSet = this.alphabet.getCharSet();
        Automaton anyString = BasicAutomata.makeCharSet(charSet).repeat();

        // return model from automaton
        return new Model_Acyclic_Inverse(anyString, this.alphabet);
    }

    @Override
    public Model_Acyclic_Inverse createString(String string) {
    	
    	// check for null string, set to empty. 
    	// in case intersection with previous is empty. not the best fix. 
    	// better to make sure that inverse operations only return valid strings!
    	if (string == null) {
    		string = "";
    	}
    	
        // create string automaton
        Automaton stringAutomaton = BasicAutomata.makeString(string);
        // get string length as bound length
        int length = string.length();
        // return model from automaton
        return new Model_Acyclic_Inverse(stringAutomaton, this.alphabet, length);
    }
}
