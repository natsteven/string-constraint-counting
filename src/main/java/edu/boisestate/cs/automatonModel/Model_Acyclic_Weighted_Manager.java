package edu.boisestate.cs.automatonModel;

import edu.boisestate.cs.Alphabet;
import edu.boisestate.cs.automaton.acyclic.AcyclicWeightedAutomaton;
import edu.boisestate.cs.automaton.acyclic.BasicAcyclicWeightedAutomaton;

public class Model_Acyclic_Weighted_Manager extends A_Model_Manager <Model_Acyclic_Weighted> {
	private final int boundLenght;
	
	public Model_Acyclic_Weighted_Manager(Alphabet alphabet, int initialBoundLenght){
		super(alphabet,initialBoundLenght);
		this.alphabet = alphabet;
		this.boundLenght = initialBoundLenght;
	}
	
//	static void setInstance(Alphabet alphabet, int initialBoundLength){
//		instance = new Model_Acyclic_Weighted_Manager(alphabet, initialBoundLength);
//	}
	
	@Override
	public Model_Acyclic_Weighted createString(String string) {
		AcyclicWeightedAutomaton a = BasicAcyclicWeightedAutomaton.makeString(string);
		
		return  new Model_Acyclic_Weighted(a, alphabet, boundLenght);
	}

	@Override
	public Model_Acyclic_Weighted createAnyString(int initialBound) {
		// TODO Auto-generated method stub
		return createAnyString(0, initialBound);
	}

	@Override
	public Model_Acyclic_Weighted createAnyString() {

		return createAnyString(0, boundLenght);
	}

	@Override
	public Model_Acyclic_Weighted createAnyString(int min, int max) {
		//create a symbolic string from min to max
		String charSet = alphabet.getCharSet();
		AcyclicWeightedAutomaton a  = BasicAcyclicWeightedAutomaton.makeCharSet(charSet);
		//repeat a from min to max
		a = a.repeat(min,max);
		return new Model_Acyclic_Weighted(a, alphabet, max);
	}

}
