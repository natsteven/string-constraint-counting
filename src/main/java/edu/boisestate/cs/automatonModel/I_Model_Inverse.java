package edu.boisestate.cs.automatonModel;

import java.util.List;
import edu.boisestate.cs.util.Tuple;

public interface I_Model_Inverse<T extends I_Model_Inverse<T>> {

	// concrete argument
	T inv_concatenate(T arg);
	
	// symbolic argument
	T inv_concatenate(T base, T arg);

	T inv_delete(int start, int end);

	T inv_insert(int offset, T argModel);

	T inv_replace(char find, char replace);

	T inv_replace(String find, String replace);

	T inv_replaceChar();

	T inv_replaceFindKnown(char find);

	T inv_replaceReplaceKnown(char replace);

	T inv_reverse();

	T inv_substring(int start, int end);
	
	T inv_substring(int start);

	T inv_setCharAt(int offset, T argModel);

	T inv_setLength(int length);

	T inv_suffix(int start);

	T inv_toLowercase();

	T inv_toUppercase();

	T inv_trim();
	
	/**
	 * 
	 * Returns new model with strings from input model removed.
	 * 
	 * OVER-ESTIMATION - .
	 * UNDER-ESTIMATION - .
	 * 
	 * @author Marlin Roberts
	 * 03/28/2021
	 */
	void minus(T model);

	/**
	 * 
	 * will return a valid prefix/suffix in a tuple of concated base.arg from this model.
	 * 
	 * OVER-ESTIMATION - .
	 * UNDER-ESTIMATION - .
	 * 
	 * @author Marlin Roberts
	 * 05/24/2020
	 */
	Tuple<T, T> inv_concatenate_sym(T base,	T arg);


	/**
	 * 
	 * will return all valid prefixes/suffixes in a list of tuples of concated base.arg from this model.
	 * it only works when this is a single string
	 * OVER-ESTIMATION - .
	 * UNDER-ESTIMATION - .
	 * 
	 * @author Marlin Roberts
	 * 05/24/2020
	 */
	List<Tuple<T, T>> inv_concatenate_sym_set (T base,	T arg);
	
	/**
	 * 
	 * will return all valid prefies/suffixes automata splits in a list of tuples of concated base.arg from this model.
	 * this works for any 
	 * OVER-ESTIMATION - .
	 * UNDER-ESTIMATION - .
	 * 
	 * @author Elena Sherman
	 * 03/24/2024
	 */
	List<Tuple<T, T>> inv_concatenate_sym_all (T base,	T arg);
	
	
	
}