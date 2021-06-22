package edu.boisestate.cs.automatonModel;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;
import dk.brics.automaton.RegExp;
import edu.boisestate.cs.Alphabet;

public class Model_Bounded_Manager extends A_Model_Manager<Model_Bounded> {

	// default maximum length of an accepted string
	private final int boundLength;

	public Model_Bounded_Manager(Alphabet alphabet, int boundLength) {
		super(alphabet, boundLength);
		this.alphabet = alphabet;
		this.boundLength = boundLength;

		// set automaton minimization as huffman
		Automaton.setMinimization(0);
	}

	// create an automaton which accepts any string with a max length of initalBound
	@Override
	public Model_Bounded createAnyString(int initialBound) {
		return this.createAnyString();
	}

	// create an automaton which accepts any string with a length between min and max
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

	// create an automaton which accepts any string with a max length of the default boundLength
	@Override
	public Model_Bounded createAnyString() {

		// create any string automaton from alphabet
		String charSet = this.alphabet.getCharSet();
		Automaton anyString = BasicAutomata.makeCharSet(charSet).repeat();

		// return model from automaton
		return new Model_Bounded(anyString, this.alphabet, this.boundLength);
	}

	// creates string that accepts only the given string
	@Override
	public Model_Bounded createString(String string) {
		// create string automaton
		Automaton stringAutomaton = BasicAutomata.makeString(string);
		// get string length as bound length
		int length = string.length();
		// return model from automaton
		return new Model_Bounded(stringAutomaton, this.alphabet, length);
	}
	
	/**
	 * Creates a new automaton model based on a given regular expression
	 * @param regex - Regular expression to be used
	 * @return Model_Bounded containing the resulting automaton
	 */
	public Model_Bounded createFromRegex(String regexString) {
		// save the regular expression as a RexExp object
		RegExp regex = new RegExp(regexString);
		// convert the RegExp to an automaton
		Automaton regexAutomaton = regex.toAutomaton();
		// return model from automaton
		return new Model_Bounded(regexAutomaton, this.alphabet, this.boundLength);
	}
}