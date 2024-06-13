package edu.boisestate.cs.graph;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.EdgeReversedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

import java.util.*;
import java.util.Map.Entry;

public class InvDefaultDirectedGraph extends DefaultDirectedGraph<PrintConstraint, SymbolicEdge> {

	private Map<PrintConstraint, Set<PrintConstraint>> predDepend;
	private Map<Integer, Set<Integer>> predDependID;

	public InvDefaultDirectedGraph(Class<? extends SymbolicEdge> edgeClass) {
		super(edgeClass);
		predDepend = new HashMap<PrintConstraint, Set<PrintConstraint>>();
		predDependID = new HashMap<Integer, Set<Integer>>();
	}

	public void computePredicateDependencies() {
		//get all the sinks that are predicates and create map entry for them
		//get all the symbolic sources
		for(PrintConstraint c : vertexSet()) {
			if(this.outDegreeOf(c) == 0 && (c.getActualVal().equals("false") || c.getActualVal().equals("true"))) {
				HashSet<PrintConstraint> dependSet = new HashSet<PrintConstraint>();
				HashSet<Integer> dependSetID = new HashSet<Integer>();
				dependSet.add(c);
				dependSetID.add(c.getId());
				predDepend.put(c, dependSet);
				predDependID.put(c.getId(), dependSetID);
			}
		}
		
		//get all the symbolic sources
		Set<PrintConstraint> sources = new HashSet<PrintConstraint>();
		for(PrintConstraint s : vertexSet()) {
			if(this.inDegreeOf(s) == 0 && s.getSplitValue().startsWith("r")) {
				sources.add(s);
				//System.out.println(s.getSplitValue());
			}
		}

		//System.out.println(sources);

		//intermediate map that remember symbolic sources for each predicate
		Map<PrintConstraint, Set<PrintConstraint>> symbValPred = new HashMap<PrintConstraint, Set<PrintConstraint>>();
		//DFS for each sink
		for(PrintConstraint s : sources) {
//			System.out.println(s.getValue());
//			System.out.print("\t");
			Set<PrintConstraint> reachedPred = new HashSet<PrintConstraint>();
			Set<Integer> reachedPredID = new HashSet<Integer>();
			DepthFirstIterator<PrintConstraint, SymbolicEdge> dfi = new DepthFirstIterator<PrintConstraint, SymbolicEdge>(this, s);
			while(dfi.hasNext()) {
				PrintConstraint n = dfi.next();
				if(this.outDegreeOf(n) == 0 && (n.getActualVal().equals("false") || n.getActualVal().equals("true"))) {
//					System.out.print(n.getId() + " ");
					reachedPred.add(n);
					reachedPredID.add(n.getId());
				}
			}
//			System.out.println();
			//iterate over predicates and add them to each other
			for(PrintConstraint p : reachedPred) {
				predDepend.get(p).addAll(reachedPred);
			}
			
			for(Integer p : reachedPredID) {
				predDependID.get(p).addAll(reachedPredID);
			}
			
		}

//		//resulting map
//		for(Entry<PrintConstraint, Set<PrintConstraint>> e : predDepend.entrySet()) {
//			//System.out.println(e);
//			System.out.println(e.getKey().getId());
//			System.out.print("\t");
//			for(PrintConstraint p : e.getValue()) {
//				System.out.print(p.getId() + " ");
//			}
//			System.out.println();
//		}
	}
	
	public Set<Integer> getDependedPredicates(Integer id){
		Set<Integer> ret = new HashSet<Integer>();
		ret.addAll(predDependID.get(id));
		
		return ret;
	}
	
	public Set<Integer> getAncestors(PrintConstraint start){
		Set<Integer> ret = new HashSet<Integer>();	
		EdgeReversedGraph<PrintConstraint, SymbolicEdge> reversedGraph = new EdgeReversedGraph<PrintConstraint, SymbolicEdge>(this);
		BreadthFirstIterator<PrintConstraint, SymbolicEdge>breadthFirstIterator =
				new BreadthFirstIterator<PrintConstraint, SymbolicEdge>(reversedGraph, start);
		while (breadthFirstIterator.hasNext()) {
			ret.add(breadthFirstIterator.next().getId());
		}
		
		return ret;
	}

	public int getNumPredicates() {
		return predDepend.size();
	}

	public int getNumSymbolicInputs() {
		int ret = 0;
		for(PrintConstraint c : vertexSet()) {
			if(c.getOp() == Operation.INIT_SYM) {
				System.out.println(c);
				ret++;
			}
		}
		return ret;
	}

	public Set<PrintConstraint> computeMinimalSpanningPredicates() {
		Set<Integer> ret = new HashSet<Integer>();
		Set<Integer> retDepends = new HashSet<Integer>();
		Map<Integer, Set<Integer>> unsortedMap = new HashMap<>(predDependID);

		List<Entry<Integer, Set<Integer>>> sortedPreds = new ArrayList<>(unsortedMap.entrySet());
		sortedPreds.sort(Entry.comparingByValue(Comparator.comparingInt(Set::size)));

		// greedy set-cover algorithm works fine
		for (Entry<Integer, Set<Integer>> entry : sortedPreds) {
			if (!retDepends.containsAll(entry.getValue())) {
				ret.add(entry.getKey());
				retDepends.addAll(entry.getValue());
			}
		}
		Set<PrintConstraint> retCon = new HashSet<PrintConstraint>();
		for (PrintConstraint c : vertexSet()) {
			if (ret.contains(c.getId())) {
				retCon.add(c);
			}
		}
		return retCon;
	}
}
