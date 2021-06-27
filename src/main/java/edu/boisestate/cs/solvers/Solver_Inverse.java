package edu.boisestate.cs.solvers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.Stack;

import edu.boisestate.cs.automatonModel.A_Model_Inverse;
import edu.boisestate.cs.automatonModel.A_Model_Manager;
//import edu.boisestate.cs.graph.I_Inv_Constraint;
//import edu.boisestate.cs.graph.I_Inv_Constraint;
import edu.boisestate.cs.util.Tuple;


public class Solver_Inverse<T extends A_Model_Inverse<T>> extends Solver_Count<T> implements I_Solver_Inverse<T>{

   // protected final A_Model_Manager<T> modelManager;
	
    protected Map<Integer, T> invStringMap = new HashMap<>();
    private boolean reduceToShortest = true;
    //private static int ARG_OFFSET = 1000;	// TODO: REMOVE symbolic arguments are stored in the symbolic string table with this offset
    //private Stack<I_Inv_Constraint> constraintStack; // REMOVE

    /**
     * Constructor with modelManager
     * @param modelManager
     */
    public Solver_Inverse(A_Model_Manager<T> modelManager) {
        super(modelManager);
        //constraintStack = new Stack<I_Inv_Constraint>(); // TODO: REMOVE
        // initialize factory from parameter
        //this.modelManager = modelManager;
    }

    /**
     * Constructor with modelManager and initialBound
     * @param modelManager
     * @param initialBound
     */
    public Solver_Inverse(A_Model_Manager<T> modelManager, int initialBound) {
        super(modelManager,initialBound);
        //constraintStack = new Stack<I_Inv_Constraint>(); // TODO: REMOVE
        // initialize factory from parameter
        //this.modelManager = modelManager;
    }

	@Override
	public void outputConcreteStrings() {

		for (Integer i : concreteStringMap.keySet()) {
			System.out.println("CS ID: " + i + "  " + concreteStringMap.get(i));
		}
		
	}

	@Override
	public void outputSymbolicStrings() {
		for (Integer i : invStringMap.keySet()) {
			System.out.println("SS ID: " + i + "  " + invStringMap.get(i));
		}
		
	}
	
	public T getSymbolicModel (int id) {
		return invStringMap.get(id);
	}

//	@Override // TODO: REMOVE
//	public void inv_append(int id, int base, int arg, int start, int end) {
//		
//		
//	}

	/**
	 * r3 version returns T
	 * 
	 * 03/18/2021 MJR
	 * 
	 * @param id - id of symbolic string location to store result
	 * @param input - symbolic string to use as input 
	 * @param arg - symbolic string to use as argument
	 */
	@Override
	public T inv_append(int id, int input, int arg) {
		
		T inputModel = invStringMap.get(input);
		T argModel = invStringMap.get(arg);
		T resModel = inputModel.inv_concatenate(argModel);
		//invStringMap.put(id, resModel);
		return resModel;
	}
	
	
	/**
	 * Performs base.inv_concatenate(arg), stores result as symbolic string id
	 * 
	 * 05/15/2020 MJR
	 * 
	 * @param id - id of symbolic string location to store result
	 * @param input - symbolic string to use as input 
	 * @param arg - symbolic string to use as argument
	 */
//	@Override // TODO: REMOVE
//	public void inv_append (int id, int input, int arg, int base) {
//		
//		T inputModel = invStringMap.get(input);
//		T argModel = invStringMap.get(arg);
//		T baseModel = invStringMap.get(base);
//		Tuple<T,T> results = inputModel.inv_concatenate_sym(baseModel, argModel);
//		//T resModel = inputModel.inv_concatenate(baseModel, argModel);
//		T resModel = results.get1();
//		invStringMap.put(id, resModel);
//		// we store the new arg value at a different location so we don't overwrite the original.
//		invStringMap.put(arg + ARG_OFFSET, results.get2());
//		
//	}

	@Override
	public void inv_contains(boolean result, int base, int arg) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void inv_deleteCharAt(int id, int base, int loc) {
		// TODO Auto-generated method stub
		
//	}

	/**
	 * R3 Version 
	 * Returns result T
	 * 
	 * 03/18/2021 MJR
	 */
	@Override
	public T inv_delete(T input, int start, int end) {
		
		//T baseModel = invStringMap.get(base);
		T resModel = input.inv_delete(start, end);
		//invStringMap.put(id, resultModel);
		return resModel;
		
	}

	/**
	 * R3 Version 
	 * Returns result T
	 * 
	 * 03/18/2021 MJR
	 */
	@Override
	public T inv_insert(T input, int start, int end){
		T resModel = input.delete(start, end);
		return resModel;
		
	}

	@Override 
	public void inv_insert(int id, int base, int arg, int offset, int start, int end) {
		// TODO remove
		
	}

	@Override
	public void inv_replaceCharFindKnown(int id, int base, char find) {
		// TODO REPLACE with r3 model
		
	}

	/**
	 * R3 Version 
	 * Returns result T
	 * 
	 * 03/18/2021 MJR
	 */
	@Override
	public T inv_replaceCharKnown(T input, char find, char replace) {
		//T baseModel = invStringMap.get(base);
		//T resModel = baseModel.inv_replace(find, replace);
		//invStringMap.put(id, resModel);
		
		//T baseModel = invStringMap.get(base);
		T resModel = input.inv_replace(find, replace);
		//invStringMap.put(id, resultModel);
		return resModel;
		
		
	}

	@Override
	public void inv_replaceCharReplaceKnown(int id, int base, char replace) {
		// TODO REPLACE with r3 model
		
	}

	@Override
	public void inv_replaceCharUnknown(int id, int base) {
		// TODO REPLACE with r3 model
		
	}

	@Override
	public String inv_replaceEscapes(String value) {
		// TODO REPLACE with r3 model
		return null;
	}

	@Override
	public void inv_replaceStrings(int id, int base, int argOne, int argTwo) {
		// TODO REPLACE with r3 model
		
	}

	
	/**
	 * R3 Version 
	 * Returns result T
	 * 
	 * 03/18/2021 MJR
	 */
	@Override 
	public T inv_reverse(T input) {
		
		T resultModel = input.reverse();
		return resultModel;
		
	}

	@Override
	public void inv_setCharAt(int id, int base, int arg, int offset) {
		// TODO REPLACE with r3 model
		
	}

	@Override
	public void inv_setLength(int id, int base, int length) {
		// TODO REPLACE with r3 model
		
	}

	@Override // TODO: REMOVE
	public void inv_substring(int id, int base, int start) {

		
	}

	/**
	 * R3 Version 
	 * Returns result T
	 * 
	 * 03/18/2021 MJR
	 */
	@Override
	public T inv_substring(T input, int start, int end) {
		
		//T baseModel = invStringMap.get(base);
		T resModel = input.inv_substring(start, end);
		//invStringMap.put(id, resultModel);
		return resModel;
	}

	/**
	 * R3 Version Performs base.inv_toLowercase
	 * Returns result T
	 * 
	 * 03/18/2021 MJR
	 */
	@Override
	public T inv_toLowerCase(T input) {
		
		//T baseModel = invStringMap.get(base);
		T resModel = input.inv_toLowercase();
		//invStringMap.put(id, resModel);
		return resModel;
	}

	/**
	 * R3 Version Performs base.inv_toUppercase
	 * Returns result T
	 * 
	 * 03/31/2021 MJR
	 */
	@Override
	public T inv_toUpperCase(T input) {
		
		//T baseModel = invStringMap.get(base);
		T resModel = input.inv_toUppercase();
		//invStringMap.put(id, resModel);
		return resModel;
		
	}

	/**
	 * R3 Version Performs base.inv_trim
	 * Returns result T
	 * 
	 * 06/21/2021 MJR
	 */
	@Override
	public T inv_trim(T input) {

		T resModel = input.inv_trim();
		return resModel;
		
	}
	
	
	public void showStrings(int id) {
		T baseModel = invStringMap.get(id);
		if (baseModel.getFiniteStrings() != null) {
			for (String s : baseModel.getFiniteStrings()) {
			System.out.println(s);
			}
		} else {
			System.out.println("No Strings...");
		}

		
	}
	
	public void initStringMap () {
		invStringMap.clear();
		
		for (Integer id : symbolicStringMap.keySet()) {
			invStringMap.put(id, symbolicStringMap.get(id).clone());
		}

	}
	
	public void initStringMapAccum () {
		//invStringMap.clear();
		
		for (Integer id : symbolicStringMap.keySet()) {
			if (!invStringMap.containsKey(id)) {
				invStringMap.put(id, symbolicStringMap.get(id).clone());
			}
		}
	}
	
	public void initStringMap (int prevID, int predID) {
		invStringMap.clear();
		
		for (Integer id : symbolicStringMap.keySet()) {
			invStringMap.put(id, symbolicStringMap.get(id).clone());
		}
		
		T prevString = invStringMap.get(prevID).clone();
		invStringMap.put(predID, prevString);

	}
	
	public void duplicateString(int source, int dest) {
		T destString = invStringMap.get(source).clone();
		invStringMap.put(dest, destString);
	}
	
	public void reduceStringToShortest(int id) {
		
		if (reduceToShortest) {
			T shortestString = invStringMap.get(id);
			shortestString = shortestString.getShortestExampleModel();
			invStringMap.put(id, shortestString);	
		}

	}
	
	public void setReduce (boolean reduce) {
		this.reduceToShortest = reduce;
	}
	
	// TODO: REMOVE
	public void intersectPrevious(int current, int previous) {
		T previousModel = invStringMap.get(previous);
		T currentModel = invStringMap.get(current);
		currentModel = currentModel.intersect(previousModel);
		invStringMap.put(current, currentModel);
	}
	
	public T intersect(T currentModel, int previous) {
		T previousModel = invStringMap.get(previous);
		T resModel = currentModel.intersect(previousModel);

		return resModel;
	}

//	@Override // TODO: REMOVE
//	public void pushInvConstraint(I_Inv_Constraint constraint) {
//
//		//constraintStack.push(constraint);
//		
//	}

//	@Override // TODO: REMOVE
//	public I_Inv_Constraint popInvConstraint() {
//		
//		//return constraintStack.pop();
//		return null;
//	}

	
	@Override // TODO: REMOVE 
	public List<Tuple<T, T>> inv_concat_sym_set(int id, int input, int arg, int base) {
		
		T inputModel = invStringMap.get(input);
		T argModel = invStringMap.get(arg);
		T baseModel = invStringMap.get(base);
		
		List<Tuple<T,T>> results = inputModel.inv_concatenate_sym_set(baseModel, argModel);

		return results;
	}
	
	@Override
	public void setSymString(int id, T stringModel) {
		invStringMap.put(id, stringModel);
		
	}
	
}
