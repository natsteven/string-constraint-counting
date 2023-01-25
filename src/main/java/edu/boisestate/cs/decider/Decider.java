package edu.boisestate.cs.decider;

import edu.boisestate.cs.automatonModel.A_Model;
import edu.boisestate.cs.graph.PrintConstraint;
import edu.boisestate.cs.graph.PrintConstraintComparator;
import edu.boisestate.cs.graph.SymbolicEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.EdgeReversedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.TopologicalOrderIterator;

/**
 * Some functionality is moved from A_Reporter
 * Decider is the core algorithm for traversing
 * the constraint graph.
 * Unlike implementation in A_Reporter, it does not
 * triggered by the first predicate encountered in the
 * graph traversion, but start solving all nodes at
 * the same time
 * @author elenasherman
 *
 * @param <T> the type of solver
 */

public class Decider <T extends A_Model<T>>{
	

}
