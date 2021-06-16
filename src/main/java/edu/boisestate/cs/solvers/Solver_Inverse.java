package edu.boisestate.cs.solvers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

//import edu.boisestate.cs.Alphabet;
//import edu.boisestate.cs.BasicTimer;
//import edu.boisestate.cs.automatonModel.AutomatonModel;
//import edu.boisestate.cs.automatonModel.A_Model;
import edu.boisestate.cs.automatonModel.A_Model_Inverse;
//import edu.boisestate.cs.automatonModel.AutomatonModelManager;
import edu.boisestate.cs.automatonModel.A_Model_Manager;
import edu.boisestate.cs.graph.I_Inv_Constraint;
import edu.boisestate.cs.util.Tuple;
//import edu.boisestate.cs.automatonModel.BoundedAutomatonModel;
//import edu.boisestate.cs.util.DotToGraph;
//import edu.boisestate.cs.util.Tuple;

public class Solver_Inverse<T extends A_Model_Inverse<T>> extends Solver_Count<T> implements I_Solver_Inverse<T>{

   // protected final A_Model_Manager<T> modelManager;
	
    protected Map<Integer, T> invStringMap = new HashMap<>();
    private boolean reduceToShortest = true;
    private static int ARG_OFFSET = 1000;	// symbolic arguments are stored in the symbolic string table with this offset
    private Stack<I_Inv_Constraint> constraintStack;

    /**
     * Constructor with modelManager
     * @param modelManager
     */
    public Solver_Inverse(A_Model_Manager<T> modelManager) {
        super(modelManager);
        constraintStack = new Stack<I_Inv_Constraint>();

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
        constraintStack = new Stack<I_Inv_Constraint>();

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

	@Override
	public void inv_append(int id, int base, int arg, int start, int end) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Performs input.inv_concatenate(arg), used when argument is concrete.
	 * 
	 * 05/15/2020 MJR
	 * 
	 * @param id - id of symbolic string location to store result
	 * @param input - symbolic string to use as input 
	 * @param arg - symbolic string to use as argument
	 */
	@Override
	public void inv_append(int id, int input, int arg) {
		
		T inputModel = invStringMap.get(input);
		T argModel = invStringMap.get(arg);
		T resModel = inputModel.inv_concatenate(argModel);
		invStringMap.put(id, resModel);
		
	}
	
	
	/**
	 * Performs input.inv_concatenate_sym(base, arg), used when argument is symbolic
	 * 
	 * 05/15/2020 MJR
	 * 
	 * @param id - id of symbolic string location to store result
	 * @param input - symbolic string to use as input 
	 * @param arg - symbolic string to use as argument
	 */
	@Override
	public void inv_append (int id, int input, int arg, int base) {
		
		T inputModel = invStringMap.get(input);
		T argModel = invStringMap.get(arg);
		T baseModel = invStringMap.get(base);
		Tuple<T,T> results = inputModel.inv_concatenate_sym(baseModel, argModel);
		//T resModel = inputModel.inv_concatenate(baseModel, argModel);
		T resModel = results.get1();
		invStringMap.put(id, resModel);
		// we store the new arg value at a different location so we don't overwrite the original.
		invStringMap.put(arg + ARG_OFFSET, results.get2());
		
	}

	@Override
	public void inv_contains(boolean result, int base, int arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inv_deleteCharAt(int id, int base, int loc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inv_delete(int id, int base, int start, int end) {
		
		T baseModel = invStringMap.get(base);
		T resModel = baseModel.inv_delete(start, end);
		invStringMap.put(id, resModel);
		
	}

	@Override
	public void inv_insert(int id, int base, int arg, int offset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inv_insert(int id, int base, int arg, int offset, int start, int end) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inv_replaceCharFindKnown(int id, int base, char find) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inv_replaceCharKnown(int id, int base, char find, char replace) {
		T baseModel = invStringMap.get(base);
		T resModel = baseModel.inv_replace(find, replace);
		invStringMap.put(id, resModel);
		
	}

	@Override
	public void inv_replaceCharReplaceKnown(int id, int base, char replace) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inv_replaceCharUnknown(int id, int base) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String inv_replaceEscapes(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void inv_replaceStrings(int id, int base, int argOne, int argTwo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inv_reverse(int id, int base) {
		
		T baseModel = invStringMap.get(base);
		T resultModel = baseModel.inv_reverse();
		invStringMap.put(id, resultModel);
		
	}

	@Override
	public void inv_setCharAt(int id, int base, int arg, int offset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inv_setLength(int id, int base, int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inv_substring(int id, int base, int start) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inv_substring(int id, int base, int start, int end) {
		
		T baseModel = invStringMap.get(base);
		T resultModel = baseModel.inv_substring(start, end);
		invStringMap.put(id, resultModel);
		
	}

	/**
	 * Performs base.inv_toLowercase, stores result id
	 * 
	 * 05/15/2020 MJR
	 */
	@Override
	public void inv_toLowerCase(int id, int base) {
		
		T baseModel = invStringMap.get(base);
		T resModel = baseModel.inv_toLowercase();
		invStringMap.put(id, resModel);
		
	}

	@Override
	public void inv_toUpperCase(int id, int base) {
		
		T baseModel = invStringMap.get(base);
		T resModel = baseModel.inv_toUppercase();
		invStringMap.put(id, resModel);
		
	}

	@Override
	public void inv_trim(int id, int base) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void showStrings(int id) {
		T baseModel = invStringMap.get(id);
		//T baseModel = symbolicStringMap.get(id);
		if (baseModel.getFiniteStrings() != null) {
			for (String s : baseModel.getFiniteStrings()) {
			System.out.println(s);
			}
		} else {
			System.out.println("No Strings...");
		}

		
	}
	
	
	public void showStrings() {
		
		for (int i : symbolicStringMap.keySet()) {
			System.out.println("fwd constraint ID: " + i);
			System.out.print("values: ");
			T baseModel = symbolicStringMap.get(i);
					if (baseModel.getFiniteStrings() != null) {
			for (String s : baseModel.getFiniteStrings()) {
			System.out.print(s + " ");
			}
			System.out.println();
		} else {
			System.out.print("No Strings...");
		}
		}
		
		
		//T baseModel = symbolicStringMap.get(id);


		
	}
	
	
	public void initStringMap () {
		invStringMap.clear();
		
		for (Integer id : symbolicStringMap.keySet()) {
			invStringMap.put(id, symbolicStringMap.get(id).clone());
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
	
	public void intersectPrevious(int current, int previous) {
		T previousModel = invStringMap.get(previous);
		T currentModel = invStringMap.get(current);
		currentModel = currentModel.intersect(previousModel);
		invStringMap.put(current, currentModel);
	}

	
	@Override
	public void pushInvConstraint(I_Inv_Constraint constraint) {

		constraintStack.push(constraint);
		
	}

	@Override
	public I_Inv_Constraint popInvConstraint() {
		
		return constraintStack.pop();
	}

	@Override
	public boolean isStackEmpty() {
		
		return constraintStack.isEmpty();
	}

	@Override
	public void setSymString(int id, T stringModel) {
		invStringMap.put(id, stringModel);
		
	}

	@Override
	public List<Tuple<T, T>> inv_concat_sym_set(int id, int input, int arg, int base) {
		
		T inputModel = invStringMap.get(input);
		T argModel = invStringMap.get(arg);
		T baseModel = invStringMap.get(base);
		
		List<Tuple<T,T>> results = inputModel.inv_concatenate_sym_set(baseModel, argModel);
		//T resModel = inputModel.inv_concatenate(baseModel, argModel);
		//T resModel = results.get1();
		//invStringMap.put(id, resModel);
		// we store the new arg value at a different location so we don't overwrite the original.
		//invStringMap.put(arg + ARG_OFFSET, results.get2());
		return results;
	}
	
}
