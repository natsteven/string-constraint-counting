package edu.boisestate.cs.solvers;

import edu.boisestate.cs.automatonModel.A_Model;

public interface I_Solver<T extends A_Model<T>> {

	void append(int id, int base, int arg, int start, int end);

	void append(int id, int base, int arg);

	void contains(boolean result, int base, int arg);

	void deleteCharAt(int id, int base, int loc);

	void delete(int id, int base, int start, int end);

	void endsWith(boolean result, int base, int arg);

	void equals(boolean result, int base, int arg);

	void equalsIgnoreCase(boolean result, int base, int arg);

	String getSatisfiableResult(int id);

	void insert(int id, int base, int arg, int offset);

	void insert(int id, int base, int arg, int offset, int start, int end);

	void isEmpty(boolean result, int base);

	boolean isSatisfiable(int id);

	boolean isSingleton(int id, String actualValue);

	boolean isSingleton(int id);

	boolean isSound(int id, String actualValue);

	void newConcreteString(int id, String string);

	void newSymbolicString(int id);

	void propagateSymbolicString(int id, int base);

	void replaceCharFindKnown(int id, int base, char find);

	void replaceCharKnown(int id, int base, char find, char replace);

	void replaceCharReplaceKnown(int id, int base, char replace);

	void replaceCharUnknown(int id, int base);

	String replaceEscapes(String value);

	void replaceStrings(int id, int base, int argOne, int argTwo);

	void reverse(int id, int base);

	void setCharAt(int id, int base, int arg, int offset);

	void setLength(int id, int base, int length);

	void shutDown();

	void startsWith(boolean result, int base, int arg);

	void substring(int id, int base, int start);

	void substring(int id, int base, int start, int end);

	void toLowerCase(int id, int base);

	void toUpperCase(int id, int base);

	void trim(int id, int base);
}
