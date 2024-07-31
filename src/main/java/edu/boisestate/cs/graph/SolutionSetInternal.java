/**
 * 
 */
package edu.boisestate.cs.graph;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import edu.boisestate.cs.automatonModel.A_Model_Inverse;

/**
 * @author Marlin Roberts
 *
 */
public class SolutionSetInternal<T extends A_Model_Inverse<T>>  {

	Map<Integer,T> solutions;
	int ID;
	
	/**
	 * Constructs solution set for use inside an inverse constraint.
	 * Maps incoming edge to solution.
	 * 
	 * @param ID = Containing Inverse constraint ID.
	 */
	public SolutionSetInternal (int ID) {
		
		this.ID = ID;
		solutions = new HashMap<Integer,T>();
	}
	
	/**
	 * Sets a solution for an incoming edge ID
	 * @param incomingEdge - ID of incoming edge
	 * @param solution - Automata solution
	 */
	public void setSolution (Integer incomingEdge, T solution) {
		
		solutions.put(incomingEdge, solution);
	}
	
	/**
	 * Removes a solution from solutions set
	 * @param incomingEdge - ID of incoming edge
	 */
	public boolean remSolution (Integer incomingEdge) {
		
		if (solutions.containsKey(incomingEdge)) {
			solutions.remove(incomingEdge);
			return true;
		} 
		return false;
	}
	
	/**
	 * Gets a single solution for incoming edge ID
	 * @param incomingEdge ID
	 * @return Automata solution for single incoming edge
	 */
	public T getSolution (Integer incomingEdge) {
		
		if (solutions.containsKey(incomingEdge)) {
			return solutions.get(incomingEdge);
		}
		return null;
	}
	
	/**
	 * Gets intersection of solutions for all incoming edges
	 * @return Automata solution for all incoming edges
	 */
	public T getSolution () {
		
		T firstSolution = null;
		
		for (Integer ID: solutions.keySet()) {
			T thisSolution = solutions.get(ID);
			if (thisSolution != null) {
				if (firstSolution == null) {
					firstSolution = thisSolution.clone();
				} else {
					firstSolution = firstSolution.intersect(thisSolution);	
				}
			}
		}
		
		return firstSolution;
	}
	
	/**
	 * Returns true if intersection of all incoming edge solutions is not empty
	 * @return - boolean
	 */
	public boolean isConsistent() {
		
		System.out.println("CHECKING CONSISTENCY - SOLUTIONS PRESENT: " + solutions.size());
		
		if (solutions.size() == 0) {
			return false;
		}
		
		T firstSolution = null;
		
		for (Integer ID: solutions.keySet()) {
			T thisSolution = solutions.get(ID);
			if (thisSolution != null) {
				if (firstSolution == null) {
					firstSolution = thisSolution;
				} else {
					firstSolution = firstSolution.intersect(thisSolution);	
				}
			}
		}
		
		if (firstSolution != null) {
			if (firstSolution.isEmpty()) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}

	}
	
	@Override
	public String toString() {
		
		StringBuilder output = new StringBuilder();
		Formatter fm = new Formatter (output);
		
		T solution = this.getSolution();
		
		fm.format("\nINPUT:      [%d]\n", ID);
		fm.format("EDGES:      [%d]\n", solutions.size());
		fm.format("CONSISTENT: [%s]\n", this.isConsistent() ? "YES" : "NO");
//		fm.format("COUNT:      [%d]\n", solution.modelCount());
		fm.close();

		return output.toString();
	}
	
	
}
