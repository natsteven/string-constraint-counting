package edu.boisestate.cs.reporting;

import edu.boisestate.cs.BasicTimer;
import edu.boisestate.cs.Parser;
import edu.boisestate.cs.Parser_2;
import edu.boisestate.cs.automatonModel.A_Model;
import edu.boisestate.cs.graph.InvDefaultDirectedGraph;
import edu.boisestate.cs.graph.PrintConstraint;
import edu.boisestate.cs.graph.PrintConstraintComparator;
import edu.boisestate.cs.graph.SymbolicEdge;
//import edu.boisestate.cs.solvers.ExtendedSolver;
import edu.boisestate.cs.solvers.Solver;

import org.jgrapht.DirectedGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.util.*;

/**
 * 
 * @author 
 *
 * @param <T>
 */
abstract public class A_Reporter <T extends A_Model<T>> {

    protected final DirectedGraph<PrintConstraint, SymbolicEdge> graph;
    protected final Parser_2<T> parser;
    protected final boolean debug;
    protected final Solver<T> solver;
    protected final Map<Integer, String[]> operationsMap;
    protected final Map<Integer, Long> timerMap;

    /**
     * 
     * @param graph
     * @param parser
     * @param solver
     * @param debug
     */
    protected A_Reporter (DirectedGraph<PrintConstraint, SymbolicEdge> 	graph,
                       	   	Parser_2<T> 									parser,
                       	   	Solver<T> 										solver,
                       	   	boolean 										debug) {

        this.graph 		= graph;
        this.parser 	= parser;
        this.debug 		= debug;
        this.solver 	= solver;
        operationsMap 	= new HashMap<>();
        timerMap 		= new HashMap<>();
    }

    public void run() {

        // output header
        this.outputHeader();

        // initialize sets
        Set<PrintConstraint> leaves = new HashSet<>();
        Set<PrintConstraint> roots = new HashSet<>();
        //all outgoing constraints of a constraints (node)
        Map<PrintConstraint, Set<PrintConstraint>> unfinishedOutEdges = new HashMap<>();
        //all incoming constraints of a constraint (node)
        Map<PrintConstraint, Set<PrintConstraint>> unfinishedInEdges = new HashMap<>();

        // NPS - minimal spanning preds (only for inverse and may/will break other solvings)
        Set<PrintConstraint> minimalPreds = ((InvDefaultDirectedGraph)this.graph).computeMinimalSpanningPredicates();

        int maxId = 0;

        // populate root and end sets
        for (PrintConstraint constraint : graph.vertexSet()) {
        	int in = graph.inDegreeOf(constraint);
        	int out = graph.outDegreeOf(constraint);
        	
        	if (debug) {
        		System.out.printf("all ID: %d I:%d O:%d  %s\n",constraint.getId(),in,out,constraint.getValue());
        	}
        	
            // if no in paths, node is a root node
            if (graph.inDegreeOf(constraint) == 0) {
                roots.add(constraint);
            }

            // if no out paths, node is an end node
            if (graph.outDegreeOf(constraint) == 0) {
                leaves.add(constraint);
            }

            // update max id if necessary
            if (constraint.getId() > maxId) {
                maxId = constraint.getId();
            }
        }
        
        
        if (debug) {
        	for (PrintConstraint pc : roots) {
        		System.out.println("reporter-root ID: " + pc.getId() + "  " + pc.getValue());
        	}

        	for (PrintConstraint pc : leaves) {
        		System.out.println("reporter-end ID: " + pc.getId() + "  " + pc.getValue());
        	}
        }
        
        // set max id in parser
        this.parser.setMaxGraphId(maxId);

        // create priority queue structure for topological iteration
        Queue<PrintConstraint> queue = new PriorityQueue<>(1, new PrintConstraintComparator());

        // create topological iterator for graph
        TopologicalOrderIterator<PrintConstraint, SymbolicEdge> iterator = new TopologicalOrderIterator<>(this.graph, queue);

        // while processing constraints in topological order
        while (iterator.hasNext()) {

            // get constraint
            PrintConstraint constraint = iterator.next();
            int constraintId = constraint.getId();
            //System.out.println("c " + constraint);
            // add to unfinished edges
            Set<PrintConstraint> unfinishedOutSet = new HashSet<>();
            for (SymbolicEdge e : graph.outgoingEdgesOf(constraint)) {
                PrintConstraint target = (PrintConstraint) e.getATarget();

                unfinishedOutSet.add(target);

                //updating incoming edges of the target of constraint, which is source
                Set<PrintConstraint> unfinishedInSet = unfinishedInEdges.get(target);
                if (unfinishedInSet == null) {
                    unfinishedInSet = new HashSet<>();
                }
                unfinishedInSet.add(constraint);
                unfinishedInEdges.put(target, unfinishedInSet);
                //System.out.println("unfn in " + unfinishedInSet);
            }
            unfinishedOutEdges.put(constraint, unfinishedOutSet);
            
            //System.out.println("unfn out " + unfinishedOutSet);
            

            // initialize constraint source map
            Map<String, Integer> sourceMap = new HashMap<>();

            // declare target id
            int targetId = 0;

            // for each incoming edge of the constraint
            for (SymbolicEdge edge : graph.incomingEdgesOf(constraint)) {

                // get the source constraint
                PrintConstraint source = (PrintConstraint) edge.getASource();

                // add the edge type and source id to the source map
                sourceMap.put(edge.getType(), source.getId());

                // if source is target, set id
                if (edge.getType().equals("t")) {
                    targetId = source.getId();
                }
            }

            // set the constraint source map
            constraint.setSourceMap(sourceMap);

            // if constraint is a leaf node

            // NPS - we need to wait until we are at any of the deepest level of predicates.
            // best way may be to wait until a predicate has all other predicates as dependencies
            // main issue would any kind of disconnected graph, which is handleable but arguably shuold just be seperate graphs.
            // i dont think there are cases in connected graphs where a deepest leaf doesn't have all other predicates as dependencies.
            if (leaves.contains(constraint)) {

                // add end
                boolean isBoolFunc = parser.addEnd(constraint);//parses the constraint

                if (isBoolFunc) {
                    // NPS 6/13/24 - not 100 percent certain we don't need the outlying code in this branch
                    // so we'll just check that the contraint's depended predicates includes all leaves


                    if (minimalPreds.contains(constraint)) {
                        this.calculateStats(constraint); //invokes prints and stats computations, also sat checks
                    }
                }

                finishEdges(unfinishedInEdges, unfinishedOutEdges, constraint);
            }
            // if constraint is a root node
            else if (roots.contains(constraint)) {

                // add root
                String init = parser.addRoot(constraint);

                long lastTime = BasicTimer.getRunTime();
                init += "{" + lastTime + "}";

                // add initialization operation
                String[] ops = new String[] {init};
                this.operationsMap.put(constraintId, ops);

                // add operation time to map
                timerMap.put(constraintId, lastTime);

                finishEdges(unfinishedInEdges, unfinishedOutEdges, constraint);
            }
            // constraint is op node
            else {

                // add operation
                String operation = parser.addOperation(constraint);

                // get previous operations
                String[] prevOps = this.operationsMap.get(targetId);

                long lastTime = BasicTimer.getRunTime();
                operation = String.format("[%s]%s{%d}", constraintId, operation, lastTime);

                // create ops array for current operation
                String[] currentOps = Arrays.copyOf(prevOps, prevOps.length + 1);
                currentOps[currentOps.length - 1] = operation;

                // add operations to map
                this.operationsMap.put(constraintId, currentOps);

                // get previous time
                long prevTime = 0;
                if (timerMap.containsKey(targetId)) {
                    prevTime = timerMap.get(targetId);
                }

                // add operation time to map
                long currTime = lastTime + prevTime;
                timerMap.put(constraintId, currTime);

                finishEdges(unfinishedInEdges, unfinishedOutEdges, constraint);
            }
        }
       
        //solveInputs();
        
        // shut down solver
        solver.shutDown();
        
    } // end run

    protected void finishEdges(Map<PrintConstraint, Set<PrintConstraint>> inEdges,
                             Map<PrintConstraint, Set<PrintConstraint>> outEdges,
                             PrintConstraint vertex) {
        Set<PrintConstraint> parents = inEdges.get(vertex);
        inEdges.remove(vertex);
        if (parents != null) {
            for (PrintConstraint parent : parents) {
                Set<PrintConstraint> siblings = outEdges.get(parent);
                if (siblings == null) {
                    return;
                }
                siblings.remove(vertex);
                if (siblings.isEmpty()) {
                    siblings.remove(parent);
                   //solver.remove(parent.getId());
                }
            }
        }
    }

    protected String joinStrings(Iterable<String> strings, String separator) {
        StringBuilder head = new StringBuilder();
        Iterator<String> iter = strings.iterator();
        head.append(iter.next());
        while (iter.hasNext()) {
            head.append(separator).append(iter.next());
        }
        return head.toString();
    }

    protected void addBooleanOperation(int base,
                                       int arg,
                                       String constName,
                                       int const_id,
                                       boolean argWasSingleton) {

        long accTime = 0;
        if (timerMap.containsKey(base)) {
            accTime = timerMap.get(base);
        }

        if (constName.equals("isEmpty")) {

            // update base constraint operations
            String[] baseOps = this.operationsMap.get(base);

            // create ops array for current operation
            String[] newBaseOps = Arrays.copyOf(baseOps, baseOps.length + 1);
            newBaseOps[newBaseOps.length - 1] =
                    String.format("[%d]<S:%d>.isEmpty(){%s}", const_id, base, accTime);

            // add operations to map
            this.operationsMap.put(base, newBaseOps);

        } else {

            // get base constraint operations
            String[] baseOps = this.operationsMap.get(base);

            // create ops array for base operation
            String[] newBaseOps = Arrays.copyOf(baseOps, baseOps.length + 1);
            if (argWasSingleton) {
            	//System.out.println("singleton arg: " + arg + " : " + Parser_2.actualVals.get(arg));
                newBaseOps[newBaseOps.length - 1] =
                        String.format("[%d]<S:%d>.%s(\"%s\"){%d}",
                                      const_id,
                                      base,
                                      constName,
                                      Parser_2.actualVals.get(arg),
                                      accTime);
            } else {
                newBaseOps[newBaseOps.length - 1] =
                        String.format("[%d]<S:%d>.%s(<S:%d>){%d}",
                                      const_id,
                                      base,
                                      constName,
                                      arg,
                                      accTime);
            }

            // update base operations
            this.operationsMap.put(base, newBaseOps);

            // get arg constraint operations
            String[] argOps = this.operationsMap.get(arg);

            // create ops array for arg operation
            String[] newArgOps = Arrays.copyOf(argOps, argOps.length + 1);
            newArgOps[newArgOps.length - 1] =
                    String.format("[%d]<S:%d>.%s(<S:%d>){%d}", const_id, base, constName, arg, accTime);

            // update arg operations
            this.operationsMap.put(arg, newArgOps);

        }
    }

    protected abstract void outputHeader();

    protected abstract void calculateStats(PrintConstraint constraint);
    
    protected abstract void solveInputs();
    
}
