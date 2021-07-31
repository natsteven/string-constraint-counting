package edu.boisestate.cs.graph;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
public class InvDefaultDirectedGraph extends DefaultDirectedGraph<PrintConstraint, SymbolicEdge> {

	private Map<PrintConstraint, Set<PrintConstraint>> predDepend;

	public InvDefaultDirectedGraph(Class<? extends SymbolicEdge> edgeClass) {
		super(edgeClass);
		predDepend = new HashMap<PrintConstraint, Set<PrintConstraint>>();
	}

	public void computePredicateDependencies() {
		//get all the sinks that are predicates and create map entry for them
		//get all the symbolic sources
		for(PrintConstraint c : vertexSet()) {
			if(this.outDegreeOf(c) == 0 && (c.getActualVal().equals("false") || c.getActualVal().equals("true"))) {
				HashSet<PrintConstraint> dependSet = new HashSet<PrintConstraint>();
				dependSet.add(c);
				predDepend.put(c, dependSet);
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
			DepthFirstIterator<PrintConstraint, SymbolicEdge> dfi = new DepthFirstIterator<PrintConstraint, SymbolicEdge>(this, s);
			while(dfi.hasNext()) {
				PrintConstraint n = dfi.next();
				if(this.outDegreeOf(n) == 0 && (n.getActualVal().equals("false") || n.getActualVal().equals("true"))) {
//					System.out.print(n.getId() + " ");
					reachedPred.add(n);
				}
			}
//			System.out.println();
			//iterate over predicates and add them to each other
			for(PrintConstraint p : reachedPred) {
				predDepend.get(p).addAll(reachedPred);
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

}
