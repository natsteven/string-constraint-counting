package edu.boisestate.cs.automatonModel;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;
import edu.boisestate.cs.Alphabet;

public class Model_Bounded_Manager extends A_Model_Manager<Model_Bounded> {

	private final int boundLength;

	Model_Bounded_Manager(Alphabet alphabet, int boundLength) {
		super(alphabet, boundLength);
		this.alphabet = alphabet;
		this.boundLength = boundLength;

		// set automaton minimization as huffman
		Automaton.setMinimization(0);
	}

	@Override
	public Model_Bounded createAnyString(int initialBound) {
		return this.createAnyString(0, initialBound);
	}

	@Override
	public Model_Bounded createAnyString(int min, int max) {

		// create any string automaton from alphabet
		String charSet = this.alphabet.getCharSet();
		Automaton anyChar = BasicAutomata.makeCharSet(charSet);

		// create bounded automaton
		Automaton boundedAutomaton = anyChar.repeat(min, max);

		// return model from bounded automaton
		return new Model_Bounded(boundedAutomaton, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded createAnyString() {

		// create any string automaton from alphabet
		String charSet = this.alphabet.getCharSet();
		Automaton anyString = BasicAutomata.makeCharSet(charSet).repeat();

		// return model from automaton
		return new Model_Bounded(anyString, this.alphabet);
	}

	@Override
	public Model_Bounded createString(String string) {
		// create string automaton
		Automaton stringAutomaton = BasicAutomata.makeString(string);

		// return model from automaton
		return new Model_Bounded(stringAutomaton, this.alphabet, this.boundLength);
	}
}
