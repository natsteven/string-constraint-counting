package edu.boisestate.cs.automatonModel;

import edu.boisestate.cs.Alphabet;

public interface I_Model_Manager <T extends A_Model <T>> {

	Alphabet getAlphabet();

	/**
	 * Create a new automaton model from a concrete string
	 * @param string
	 * @return
	 */
	T createString(String string);

	/**
	 * Create a new symbolic string from 0 to up to a certain length
	 * @param initialBound the upper bound of the lenght (inlcusive)
	 * @return
	 */
	T createAnyString(int initialBound);

	/**
	 * A string with no upper bound - for unbounded models
	 * @return
	 */
	T createAnyString();

	/**
	 * Creates a symbolic string with length from min to max (both inclusive)
	 * @param min
	 * @param max
	 * @return
	 */
	T createAnyString(int min, int max);

}