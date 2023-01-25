package edu.boisestate.cs.decider;

import edu.boisestate.cs.Parser_2;
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

<<<<<<< HEAD
public class Decider <T extends A_Model<T>>{
	protected final DirectedGraph<PrintConstraint, SymbolicEdge> graph;
	   protected final Parser_2<T> parser;
	
	
	public Decider (DirectedGraph<PrintConstraint, SymbolicEdge> graph, Parser_2<T> parser) {
		this.graph = graph;
		this.parser = parser;
	}
	
	public boolean decide() {
		boolean ret = false;
		
		//pre-condition: the nodes id in the input graph are ordered in BSF from smallest (roots) to largest (leaves)
		//if id_n1 < id_n2 then n2 cannot depend on n1, but n1 might depends on n2
		
		//since predicate restrict the values of its target nodes, that is, updates right away without
		//any need for propagation, then we need to solve those first that have more then one outgoing edge
		//One edge is going to the predicate and another somewhere else in the graph
		//This would at least restrict the computation
		
		//Step1. Finding all predicates children with more than one outgoing nodes
		//The comparator puts them based on their BSF id since we would like to solve those first
		//on which other predChild2Out depend
		Queue<PrintConstraint> predChild2Out = new PriorityQueue<>(1, new PrintConstraintComparator());
		//stores to which predicate predChild2Out element corresponds to
		Map<PrintConstraint, PrintConstraint> nodeToPred = new HashMap<PrintConstraint, PrintConstraint>();
		
		//get the leaves -- should be predicates, but we will check
		Iterator<PrintConstraint> iterVert = graph.vertexSet().iterator();
		while(iterVert.hasNext()) {
			PrintConstraint node = iterVert.next();
			if(graph.outDegreeOf(node) == 0) {
				//found a leaf
				//check if it is a predicate
				if(parser.isPredicate(node)) {
					//get its children
					Iterator<SymbolicEdge> iterEdges = graph.incomingEdgesOf(node).iterator();
					while(iterEdges.hasNext()) {
						SymbolicEdge nodeInEdge = iterEdges.next();
						//get the source of that edge, node is the target of that edge and check the number 
						//of its outgoing edges
						PrintConstraint childNode = (PrintConstraint)nodeInEdge.getASource();
						if(graph.outDegreeOf(childNode) > 1) {
							predChild2Out.add(childNode);
							nodeToPred.put(childNode, node);
						}
					}
				}
			}//end finding predicates
		}//end iterVert.hasNext();
		
		//let's see them 
		System.out.println("predChild2Out " + predChild2Out); 
		
		//create a reverse graph for better iteration
		EdgeReversedGraph<PrintConstraint, SymbolicEdge> revGraph = new EdgeReversedGraph<PrintConstraint, SymbolicEdge> (graph);
		//for optimization -- remember all nodes and corresponding edges that were removed due to 
		//search optimization
		Set<PrintConstraint> reEnumNodes = new HashSet<PrintConstraint>();
		Set<SymbolicEdge> reEnumEdges = new HashSet<SymbolicEdge>();
		
		int idCntr = 0;
		//go through all of them to find those nodes that should be re-enumerated
		for(PrintConstraint child2Out : predChild2Out) {
			//use BSF to find all ancestors of child2Out -- we would have to enumerate them
			System.out.println("pred " + nodeToPred.get(child2Out));
			BreadthFirstIterator<PrintConstraint, SymbolicEdge> bfsIter = new BreadthFirstIterator<PrintConstraint, SymbolicEdge>(revGraph, child2Out);
			List<PrintConstraint> bfsNodes = new ArrayList<PrintConstraint>(); //for enumeration order
			bfsNodes.add(nodeToPred.get(child2Out));//the last, bfs add child2Out to its search
			System.out.println(graph.edgeSet().size() + "\t" + graph.vertexSet().size());
			while(bfsIter.hasNext()) {
				PrintConstraint bfsNode = bfsIter.next();
				System.out.println("bfs " + bfsNode);
				bfsNodes.add(0,bfsNode);//so the roots are the first one in the list to start re-enumerating them
			}
			//re-enumerate nodes
			System.out.println("bfsNodes " + bfsNodes);
			for(PrintConstraint node : bfsNodes) {
				node.setID(idCntr);
				System.out.println("node id " + node);
				reEnumEdges.addAll(graph.edgesOf(node));
				idCntr++;
			}
			//remember those to add back
			reEnumNodes.addAll(bfsNodes);
			//remove those nodes from the revGraph so we don't iteration over them again
			revGraph.removeAllVertices(bfsNodes);
			System.out.println(graph.edgeSet().size() + "\t" + graph.vertexSet().size());
		}//end for each child2Out
		
	
		//now we need to re-enumerate the rest of the nodes in the graph so no collisions exist
		//go over all nodes that are left in the revGraph and re-enumerate them in BFS manner to preserve the original property
		BreadthFirstIterator<PrintConstraint, SymbolicEdge> bfsIter = new BreadthFirstIterator<PrintConstraint, SymbolicEdge>(revGraph);
		while(bfsIter.hasNext()) {
			PrintConstraint node = bfsIter.next();
			node.setID(idCntr);
			idCntr++;
		}
		
		//add back reEnum nodes and their edges back to the graph
		reEnumNodes.forEach(s -> graph.addVertex(s));
		reEnumEdges.forEach(s -> graph.addEdge((PrintConstraint)s.getASource(), (PrintConstraint)s.getATarget()));
		 // create priority queue structure for topological iteration
        Queue<PrintConstraint> queue = new PriorityQueue<>(1, new PrintConstraintComparator());

        // create topological iterator for graph
        TopologicalOrderIterator<PrintConstraint, SymbolicEdge> iter = new TopologicalOrderIterator<>(this.graph, queue);
        
        
        
        //more likely need to add some pre-processing to separate into independent graphs
        //eas: 4/9/22 - the correct order is now in place
        //next, we need to invoke parser on those nodes to solve constraints
        //and after that figure out the inverse part
        //would be greate to have inverse on the operation that Phillip did
        while(iter.hasNext()) {
        	PrintConstraint currConstr = iter.next();
        	System.out.println("cnstr " + currConstr);
        }
		
		
		
		
		return ret;
	}
=======
public abstract class Decider <T extends A_Model<T>>{
>>>>>>> branch 'mjr/solvemain' of https://github.com/BoiseState/string-constraint-counting.git

}
