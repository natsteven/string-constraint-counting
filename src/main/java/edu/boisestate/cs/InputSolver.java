package edu.boisestate.cs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;

import com.fasterxml.jackson.databind.ObjectMapper;

//import edu.boisestate.cs.automatonModel.Model_Acyclic_Inverse;
//import edu.boisestate.cs.automatonModel.Model_Acyclic_Inverse_Manager;
import edu.boisestate.cs.automatonModel.AutomatonModelManager;
import edu.boisestate.cs.automatonModel.Model_Acyclic;
import edu.boisestate.cs.automatonModel.Model_Acyclic_Inverse;
import edu.boisestate.cs.automatonModel.Model_Acyclic_Inverse_Manager;
import edu.boisestate.cs.automatonModel.Model_Acyclic_Manager;
import edu.boisestate.cs.automatonModel.Model_Bounded;
import edu.boisestate.cs.automatonModel.Model_Bounded_Manager;
import edu.boisestate.cs.automatonModel.Model_Weighted;
import edu.boisestate.cs.automatonModel.Model_Weighted_Manager;
import edu.boisestate.cs.graph.PrintConstraint;
import edu.boisestate.cs.graph.SymbolicEdge;
import edu.boisestate.cs.reporting.MCReporter;
import edu.boisestate.cs.reporting.Reporter;
import edu.boisestate.cs.reporting.Reporter_Count;
import edu.boisestate.cs.reporting.Reporter_Inverse;
//import edu.boisestate.cs.reporting.Reporter_Inverse;
import edu.boisestate.cs.reporting.Reporter_SAT;
import edu.boisestate.cs.reporting.SATReporter;
import edu.boisestate.cs.solvers.AutomatonModelSolver;
import edu.boisestate.cs.solvers.BlankSolver;
import edu.boisestate.cs.solvers.ConcreteSolver;
import edu.boisestate.cs.solvers.ExtendedSolver;
import edu.boisestate.cs.solvers.MCAutomatonModelSolver;
import edu.boisestate.cs.solvers.ModelCountSolver;
import edu.boisestate.cs.solvers.Solver;
import edu.boisestate.cs.solvers.Solver_Count;
import edu.boisestate.cs.solvers.Solver_Inverse;
//import edu.boisestate.cs.solvers.Solver_Inverse;
import edu.boisestate.cs.util.LambdaVoid1;

public class InputSolver {
	private static int initialBound = 0;
	private static int defaultBound = 3;

	private static boolean reduce = false;
	private static boolean debug = false;

	private static String inputFile;

	private static Alphabet alpha;

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: ");
			System.out.println("inputSolver <required: graph file> <optional: input length>");
		} else if (args.length < 2) {
			System.out.println("[IGEN] Using input graph : " + args[0]);
			inputFile = args[0];
		} else {
			System.out.println("[IGEN] Using input graph : " + args[0]);
			inputFile = args[0];
			initialBound = Integer.parseInt(args[1]);
			System.out.println("[IGEN] Using input length : " + initialBound);
		}
		reduce = true;
		DirectedGraph<PrintConstraint, SymbolicEdge> graph = loadGraph(inputFile);
		
		//run_Bounded_Count(graph);
		//run_Bounded_SAT(graph);
		//run_Acyclic_Inverse_r3(graph); 
		//run_Acyclic_Count(graph);
		//run_Acyclic_SAT(graph);
		run_Weighted_Count(graph);
		
	}

	// TODO: create Model_Bounded_Inverse
	// TODO: create Solver_Inverse
	// TODO: create Model_Bounded_Inverse_Manager
	// TODO: create Reporter_Inverse_2
//	public static void run_Bounded_Inverse(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
//		Model_Bounded_Inverse_Manager mFactory = new Model_Bounded_Inverse_Manager(alpha, initialBound);
//		Solver_Inverse<Model_Bounded> mSolver = new Solver_Inverse<Model_Bounded>(mFactory, initialBound);
//		Parser_2<Model_Bounded_Inverse> mParser = new Parser_2<Model_Bounded_Inverse>(mSolver, debug);
//		Reporter_Inverse_2<Model_Bounded_Inverse> mReporter = new Reporter_Inverse_2<Model_Bounded_Inverse>(graph,
//				mParser, mSolver, debug);
//		mSolver.setReduce(reduce);
//		mReporter.run();
//	}

	public static void run_Bounded_Count(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
		Model_Bounded_Manager mFactory = new Model_Bounded_Manager(alpha, initialBound);
		Solver_Count<Model_Bounded> mSolver = new Solver_Count<Model_Bounded>(mFactory, initialBound);
		Parser_2<Model_Bounded> mParser = new Parser_2<Model_Bounded>(mSolver, debug);
		Reporter_Count<Model_Bounded> mReporter = new Reporter_Count<Model_Bounded>(graph, mParser, mSolver, debug);
		mReporter.run();
	}

	public static void run_Bounded_SAT(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
		Model_Bounded_Manager mFactory = new Model_Bounded_Manager(alpha, initialBound);
		Solver<Model_Bounded> mSolver = new Solver<Model_Bounded>(mFactory, initialBound);
		Parser_2<Model_Bounded> mParser = new Parser_2<Model_Bounded>(mSolver, debug);
		Reporter_SAT<Model_Bounded> mReporter = new Reporter_SAT<Model_Bounded>(graph, mParser, mSolver, debug);
		mReporter.run();
	}

	public static void run_Acyclic_Count(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
		Model_Acyclic_Manager mFactory = new Model_Acyclic_Manager(alpha, initialBound);
		Solver_Count<Model_Acyclic> mSolver = new Solver_Count<Model_Acyclic>(mFactory, initialBound);
		Parser_2<Model_Acyclic> mParser = new Parser_2<Model_Acyclic>(mSolver, debug);
		Reporter_Count<Model_Acyclic> mReporter = new Reporter_Count<Model_Acyclic>(graph, mParser, mSolver, debug);
		mReporter.run();
	}
	
	public static void run_Acyclic_SAT(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
		Model_Acyclic_Manager mFactory = new Model_Acyclic_Manager(alpha, initialBound);
		Solver<Model_Acyclic> mSolver = new Solver<Model_Acyclic>(mFactory, initialBound);
		Parser_2<Model_Acyclic> mParser = new Parser_2<Model_Acyclic>(mSolver, debug);
		Reporter_SAT<Model_Acyclic> mReporter = new Reporter_SAT<Model_Acyclic>(graph, mParser, mSolver, debug);
		mReporter.run();
	}
	
	public static void run_Weighted_Count(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
		Model_Weighted_Manager mFactory = new Model_Weighted_Manager(alpha, initialBound);
		Solver_Count<Model_Weighted> mSolver = new Solver_Count<Model_Weighted>(mFactory, initialBound);
		Parser_2<Model_Weighted> mParser = new Parser_2<Model_Weighted>(mSolver, debug);
		Reporter_Count<Model_Weighted> mReporter = new Reporter_Count<Model_Weighted>(graph, mParser, mSolver, debug);
		mReporter.run();
	}

	// TODO: create Model_Acyclic_Inverse_Manager
	// TODO: create Model_Acyclic_Inverse
//	public static void run_Acyclic_Inverse(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
//		Model_Acyclic_Inverse_Manager mFactory = new Model_Acyclic_Inverse_Manager(alpha, initialBound);
//		Solver_Inverse<Model_Acyclic_Inverse> mSolver = new Solver_Inverse<Model_Acyclic_Inverse>(mFactory,
//				initialBound);
//		Parser_2<Model_Acyclic_Inverse> mParser = new Parser_2<Model_Acyclic_Inverse>(mSolver, debug);
//		Reporter_Inverse_2<Model_Acyclic_Inverse> mReporter = new Reporter_Inverse_2<Model_Acyclic_Inverse>(graph, mParser,
//				mSolver, debug);
//		mSolver.setReduce(reduce);
//		mReporter.run();
//	}

	// TODO: create Model_Acyclic_Inverse_Manager
	// TODO: create Model_Acyclic_Inverse
	// TODO: create Solver_Inverse
	// TODO: create Reporter_Inverse
	public static void run_Acyclic_Inverse_r3(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
		Model_Acyclic_Inverse_Manager mFactory 					= new Model_Acyclic_Inverse_Manager(alpha, initialBound);
		Solver_Inverse<Model_Acyclic_Inverse> mSolver 		= new Solver_Inverse<Model_Acyclic_Inverse>(mFactory,	initialBound);
		Parser_2<Model_Acyclic_Inverse> mParser 				= new Parser_2<Model_Acyclic_Inverse>(mSolver, debug);
		Reporter_Inverse<Model_Acyclic_Inverse> mReporter 	= new Reporter_Inverse<Model_Acyclic_Inverse>(graph, mParser, mSolver, debug);
		mSolver.setReduce(reduce);
		mReporter.run();
	}

	@SuppressWarnings("unchecked")
	public static DirectedGraph<PrintConstraint, SymbolicEdge> loadGraph(String graphPath) {
		// initialize variables

		// init null graph object
		DirectedGraph<PrintConstraint, SymbolicEdge> graph = new DefaultDirectedGraph<>(SymbolicEdge.class);

		// create json object mapper
		ObjectMapper mapper = new ObjectMapper();

		// init json file object
		File graphFile = new File(graphPath);

		// init lists for processing
		Map<Integer, PrintConstraint> constraintMap = new HashMap<>();
		Map<PrintConstraint, List<Integer>> sourceConstraintMap = new HashMap<>();
		List<Map<String, Object>> edgeData = new LinkedList<>();

		try {
			// get graph data from json file
			Map<String, Object> graphData = mapper.readValue(graphFile, Map.class);

			// add alphabet data to settings
			Map<String, Object> alphabetData = (Map<String, Object>) graphData.get("alphabet");
			String minAlphabet = (String) alphabetData.get("declaration");
			alpha = new Alphabet(minAlphabet);

			// determine initialBound
			if (initialBound == 0) {
				if (graphData.get("inputLength") != null) {
					int inputLength = (int) graphData.get("inputLength");
					initialBound = inputLength;
					System.out.println("[IGEN] Using initial bound from JSON file .. " + initialBound);
				} else {
					initialBound = defaultBound;
					System.out.println("[IGEN] Using initial bound default ..." + initialBound);
				}
			}

			// get constraint data from graph data
			List<Map<String, Object>> vertexData = (List<Map<String, Object>>) graphData.get("vertices");

			// add constraints from file to constraintMap
			// for each vertex
			for (Map<String, Object> obj : vertexData) {
				// get constraint vertex data
				int id = (Integer) obj.get("id");
				String actualValue = (String) obj.get("actualValue");
				int num = (Integer) obj.get("num");
				long timeStamp;

				try {
					timeStamp = (Long) obj.get("timeStamp");
				} catch (ClassCastException e) {
					timeStamp = (Integer) obj.get("timeStamp");
				}

				int type = (Integer) obj.get("type");
				String value = (String) obj.get("value");

				// create constraint from vertex data
				PrintConstraint constraint = new PrintConstraint(id, actualValue, num, timeStamp, type, value);

				// add constraint to map
				constraintMap.put(id, constraint);

				// get source constraint list and add to map
				List<Integer> sourceConstraints = (List<Integer>) obj.get("sourceConstraints");
				sourceConstraintMap.put(constraint, sourceConstraints);

				// get incoming edges
				List<Map<String, Object>> incomingEdges = (List<Map<String, Object>>) obj.get("incomingEdges");

				for (Map<String, Object> incomingEdge : incomingEdges) {
					// add edge to list
					incomingEdge.put("target", id);
					edgeData.add(incomingEdge);
				}
			} // end vertexData for loop

			// add sourceConstraints from file to their constraint in constraintMap
			// create vertices in graph
			// set sourceConstraints for each constraint
			for (PrintConstraint constraint : sourceConstraintMap.keySet()) {
				// for each constraint id
				for (int id : sourceConstraintMap.get(constraint)) {
					// get source constraint
					PrintConstraint sourceConstraint = constraintMap.get(id);
					// set source constraint as source for current constraints
					constraint.setSource(sourceConstraint);
				}
				// add constraint to graph
				graph.addVertex(constraint);
			} // end sourceConstraint for loop

			// create edges in graph from edgeData
			for (Map<String, Object> obj : edgeData) {
				// get symbolic edge data
				int sourceId = (Integer) obj.get("source");
				PrintConstraint source = constraintMap.get(sourceId);
				int targetId = (Integer) obj.get("target");
				PrintConstraint target = constraintMap.get(targetId);
				String type = (String) obj.get("type");

				// create symbolic edge in graph from data
				SymbolicEdge edge = graph.addEdge(source, target);
				edge.setType(type);
			} // end edgeData for loop
		} catch (IOException i) {
			i.printStackTrace();
		}

		return graph;
	} // end loadGraph
}
