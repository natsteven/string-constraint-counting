/**
 * 
 */
package edu.boisestate.cs.graph;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import edu.boisestate.cs.automatonModel.A_Model_Inverse;

/**
 * @author marli
 *
 */
public class SolutionSet<T extends A_Model_Inverse<T>> {

	List<T> solutions;
	int ID;
	private static String cid = "[IGEN] ";
	
	
	public SolutionSet(int ID) {
		
		solutions = new ArrayList<T>();
		this.ID = ID;
		
	}
	
	public int add(T solution) {
		
		solutions.add(solution);
		
		return solutions.size() - 1;
		
	}
	
	public void set(T solution, int index) {
		
		solutions.set(index, solution);
		
	}
	
	
	public void setNull(int index) {
		
		solutions.set(index, null);
		
	}
	
	
	public boolean isConsistent() {
		
		if (solutions.size() == 0) {
			return false;
		}
		
		T firstSolution = null;
		
		for (T solution : solutions) {
			if (solution != null) {
				if (firstSolution == null) {
					firstSolution = solution;
				} else {
					firstSolution = firstSolution.intersect(solution);	
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
	
	public T getSolution () {
		
		T firstSolution = null;
		
		for (T solution : solutions) {
			if (solution != null) {
				if (firstSolution == null) {
					firstSolution = solution.clone();
				} else {
					firstSolution = firstSolution.intersect(solution);	
				}
			}
		}
		
		return firstSolution;
	}
	
	public int getID () {
		return ID;
	}
	
	
	public String toSring() {
		
		StringBuilder output = new StringBuilder();
		Formatter fm = new Formatter (output);
		
		T solution = this.getSolution();
		
		fm.format(cid + "INPUT:      [%d]\n", ID);
		fm.format(cid + "EDGES:      [%d]\n", solutions.size());
		fm.format(cid + "CONSISTENT: [%s]\n", this.isConsistent() ? "YES" : "NO");
		fm.format(cid + "COUNT:      [%d]", solution.modelCount());
		
		fm.close();
		
		
		
		
		return output.toString();
	}
	
	
	
}
