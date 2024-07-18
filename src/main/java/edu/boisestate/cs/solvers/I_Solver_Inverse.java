package edu.boisestate.cs.solvers;

import java.util.List;

import edu.boisestate.cs.automatonModel.A_Model_Inverse;
//import edu.boisestate.cs.graph.I_Inv_Constraint;
import edu.boisestate.cs.util.Tuple;

public interface I_Solver_Inverse<T extends A_Model_Inverse<T>> {
	
	
//	public void pushInvConstraint(I_Inv_Constraint constraint);
//	
//	public I_Inv_Constraint popInvConstraint ();
	
	public void outputConcreteStrings ();
	
	public void outputSymbolicStrings ();
	
//	void inv_append(int id, int base, int arg, int start, int end);

	T inv_append(int id, int base, int arg);

	void inv_contains(boolean result, int base, int arg);

//	void inv_deleteCharAt(int id, int base, int loc);

	T inv_delete(T input, int start, int end);

	T inv_insert(T input, int start, int end);

	void inv_insert(int id, int base, int arg, int offset, int start, int end);

	void inv_replaceCharFindKnown(int id, int base, char find);

	T inv_replaceCharKnown(T input, char find, char replace);

	void inv_replaceCharReplaceKnown(int id, int base, char replace);

	void inv_replaceCharUnknown(int id, int base);

	String inv_replaceEscapes(String value);

	T inv_replaceStrings(T input, char[] find, char[] replace);

	T inv_reverse(T input);

	void inv_setCharAt(int id, int base, int arg, int offset);

	void inv_setLength(int id, int base, int length);

	void inv_substring(int id, int base, int start);

	T inv_substring(T input, int start, int end);

	T inv_substring(T input, int start);
	
	T inv_setLength(T input, int length);
	
	
	T inv_toLowerCase(T input);

	T inv_toUpperCase(T input);

	T inv_trim(T input);

	/**
	 * Performs base.inv_concatenate(arg), stores result as symbolic string id
	 * 
	 * 05/15/2020 MJR
	 * 
	 * @param id - id of symbolic string location to store result
	 * @param input - symbolic string to use as input 
	 * @param arg - symbolic string to use as argument
	 */
//	void inv_append(int id, int input, int arg, int base);

	public List<Tuple<T, T>> inv_concat_sym_set(int id, int input, int arg, int base); 
	public void setSymString(int id, T stringModel);

}
