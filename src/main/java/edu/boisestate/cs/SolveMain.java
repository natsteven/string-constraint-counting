/**
 * The processor. Traverses the inputed flow graph using a temporal depth first
 * search to create PCs and pass them to the constraint solvers using the
 * argument.
 *
 * @author Scott Kausler, Andrew Harris
 */
package edu.boisestate.cs;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.boisestate.cs.Settings.ReportType;
import edu.boisestate.cs.Settings.SolverType;
import edu.boisestate.cs.automatonModel.AutomatonModelManager;
import edu.boisestate.cs.automatonModel.Model_Acyclic;
import edu.boisestate.cs.automatonModel.Model_Acyclic_Inverse;
import edu.boisestate.cs.automatonModel.Model_Acyclic_Inverse_Manager;
import edu.boisestate.cs.automatonModel.Model_Acyclic_Manager;
import edu.boisestate.cs.automatonModel.Model_Acyclic_Weighted;
import edu.boisestate.cs.automatonModel.Model_Acyclic_Weighted_Manager;
import edu.boisestate.cs.automatonModel.Model_Bounded;
import edu.boisestate.cs.automatonModel.Model_Bounded_Manager;
import edu.boisestate.cs.automatonModel.Model_Concrete_Singleton;
import edu.boisestate.cs.automatonModel.Model_Concrete_Singleton_Manager;
import edu.boisestate.cs.decider.Decider;
import edu.boisestate.cs.graph.InvDefaultDirectedGraph;
import edu.boisestate.cs.graph.PrintConstraint;
import edu.boisestate.cs.graph.SymbolicEdge;
import edu.boisestate.cs.reporting.MCReporter;
import edu.boisestate.cs.reporting.Reporter;
import edu.boisestate.cs.reporting.Reporter_Count;
import edu.boisestate.cs.reporting.Reporter_Inverse;
import edu.boisestate.cs.reporting.Reporter_Inverse_BFS;
import edu.boisestate.cs.reporting.Reporter_SAT;
import edu.boisestate.cs.reporting.SATReporter;
import edu.boisestate.cs.solvers.*;
import edu.boisestate.cs.util.LambdaVoid1;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.io.File;
import java.io.IOException;
import java.util.*;

//import javax.swing.plaf.synth.SynthSeparatorUI;

@SuppressWarnings({ "unchecked" })
public class SolveMain {
	private static int initialBound = 0;
	private static int defaultBound = 3;

	private static boolean reduce = false;
	private static boolean debug = false;
	private static boolean header = true;

	private static String inputFile;
	private static Alphabet alpha;
	
	public static void main(String[] args) {

		Settings settings = CommandLine.processArgs(args);

		// ensure arguments processed properly before continuing
		if (settings == null) {
			return;
		}
			
		inputFile = settings.getGraphFilePath();
		initialBound = settings.getInitialBoundingLength();
		
		/*
		 * If solver is inverse, we can ignore the reporter and automata types, load the 
		 * graph and run the acyclic inverse method. 
		 */
		if (settings.getSolverType() == SolverType.INVERSE) {

			printHeader(inputFile, initialBound, "Inverse", "Inverse", "Acyclic");

			reduce = true;
			InvDefaultDirectedGraph graph = (InvDefaultDirectedGraph) loadGraph(inputFile);
			System.out.println("=".repeat(42) + "GRAPH STATS" + "=".repeat(42));
			System.out.println("NUM CONSTRAINTS:\t" + graph.vertexSet().size());
			System.out.println("NUM PREDICATES:\t\t" + graph.getNumPredicates());
			System.out.println("NUM SYMBOLIC INPUTS:\t" + graph.getNumSymInputs());
			System.out.println("=".repeat(95));
			run_Acyclic_Inverse_r3(graph);


		/*
		 * If solver is jsa, we need to run the correct method based on the reporter and automata types.
		 */
		} else if (settings.getSolverType() == SolverType.JSA) {

			if (settings.getReportType() == ReportType.MODEL_COUNT) {

				if (settings.getAutomatonModelVersion() == 1) {
					// jsa, bounded, count
					printHeader(inputFile, initialBound, "JSA", "Model Count", "Bounded");

					DirectedGraph<PrintConstraint, SymbolicEdge> graph = loadGraph(inputFile);
					run_Bounded_Count(graph);

				}

				if (settings.getAutomatonModelVersion() == 2) {
					// jsa, acyclic, count
					printHeader(inputFile, initialBound, "JSA", "Model Count", "Acyclic");

					DirectedGraph<PrintConstraint, SymbolicEdge> graph = loadGraph(inputFile);
					run_Acyclic_Count(graph);

				}

				if (settings.getAutomatonModelVersion() == 3) {
					// jsa, weighted, count
					printHeader(inputFile, initialBound, "JSA", "Model Count", "Acyclic Weighted");

					DirectedGraph<PrintConstraint, SymbolicEdge> graph = loadGraph(inputFile);
					run_Weighted_Count(graph);

				}

			}

			if (settings.getReportType() == ReportType.SAT) {

				if (settings.getAutomatonModelVersion() == 1) {
					// jsa, bounded, sat
					printHeader(inputFile, initialBound, "JSA", "SAT", "Bounded");

					DirectedGraph<PrintConstraint, SymbolicEdge> graph = loadGraph(inputFile);
					run_Bounded_SAT(graph);

				}

				if (settings.getAutomatonModelVersion() == 2) {
					// jsa, acyclic, sat
					printHeader(inputFile, initialBound, "JSA", "SAT", "Acyclic");

					DirectedGraph<PrintConstraint, SymbolicEdge> graph = loadGraph(inputFile);
					run_Acyclic_SAT(graph);

				}
				//eas: there is not point of having acyclic weighted for SAT - it will have the same
				//precision as Acyclic but would take much more time - only use for testing purposes
				if (settings.getAutomatonModelVersion() == 3) {
					// jsa, weighted, sat
					printHeader(inputFile, initialBound, "JSA", "SAT", "Acyclic Weighted");

					DirectedGraph<PrintConstraint, SymbolicEdge> graph = loadGraph(inputFile);
					run_Weighted_SAT(graph);

				}

			}

		} else if (settings.getSolverType() == SolverType.CONCRETE) {
			if (settings.getReportType() == ReportType.SAT) {
				if(settings.getAutomatonModelVersion() == 1) {
					//explicitly encodes sets of strings as an acyclic automaton would
					printHeader(inputFile, initialBound, "Concrete", "SAT", "Acyclic");
					DirectedGraph<PrintConstraint, SymbolicEdge> graph = loadGraph(inputFile);
					//run_Concrete_Acyclic_SAT(graph);
				} else if (settings.getAutomatonModelVersion() == 2) {//eas: only this one is supported for now to do the testing
					printHeader(inputFile, initialBound, "Concrete", "SAT", "Singleton");
					DirectedGraph<PrintConstraint, SymbolicEdge> graph = loadGraph(inputFile);
					run_Concrete_Singleton_SAT(graph);
				}
			} else if (settings.getReportType() == ReportType.MODEL_COUNT) {
				if(settings.getAutomatonModelVersion() == 1) {
					//explicitly encodes sets of strings as an acyclic automaton would
					printHeader(inputFile, initialBound, "Concrete", "SAT", "Acyclic");
					DirectedGraph<PrintConstraint, SymbolicEdge> graph = loadGraph(inputFile);
					//run_Concrete_Acyclic_MC(graph);
				} else if (settings.getAutomatonModelVersion() == 2) {
					printHeader(inputFile, initialBound, "Concrete", "SAT", "Singleton");
				}
			}
			
			/*
			 * The remaining types are the concrete and blank solvers, which currently use the non-typed classes.
			 */
		} else {

			// initialize components object
			Components components = new Components();

			// load constraint graph
			loadGraph(components, settings);

			// load alphabet
			loadAlphabet(components, settings);

			// load solver
			loadSolver(components, settings);

			// if graph or parser not loaded, abort program
			if (components.getGraph() == null || components.getSolver() == null) {
				return;
			}

			// load parser
			loadParser(components, settings);

			// load reporter
			loadReporter(components, settings);

			// if reporter not loaded, abort program
			if (components.getReporter() == null) {
				return;
			}

			// run reporter
			components.getReporter().run();

		} // end other solver type

	}


	/*
	 * loadAlphabet for concrete and blank solvers.
	 */
	private static void loadAlphabet(Components components, Settings settings) {

		// declare alphabet variable
		Alphabet alphabet = null;

		// if alphabet declared
		if (settings.getAlphabetDeclaration() != null) {

			// create alphabet from declaration
			alphabet = new Alphabet(settings.getAlphabetDeclaration());

			// if alphabet is not superset of minimal alphabet
			if (!alphabet.isSuperset(settings.getMinAlphabet())) {

				// reset alphabet to null
				alphabet = null;
			}
		}

		// if alphabet not already set
		if (alphabet == null) {

			// create alphabet from minimum required alphabet
			alphabet = new Alphabet(settings.getMinAlphabet());
		}

		// store alphabet
		components.setAlphabet(alphabet);

	}

	private static void loadGraph(Components components, final Settings settings) {
		// store graph as component
		LambdaVoid1<String> setMinAlphabet = new LambdaVoid1<String>() {
			@Override
			public void execute(String s) {
				settings.setMinAlphabet(s);
			}
		};
		components.setGraph(loadGraph(settings.getGraphFilePath(), setMinAlphabet));
	}

	/*
	 * loadGraph for the concrete and blank solvers, needs to be public for dotgenerator, methodcount and cleargraph classes
	 */
	public static DirectedGraph<PrintConstraint, SymbolicEdge> loadGraph(String graphPath,
			LambdaVoid1<String> setMinAlphabet) {

		// initialize graph object as null
		DirectedGraph<PrintConstraint, SymbolicEdge> graph = new DefaultDirectedGraph<>(SymbolicEdge.class);

		// create json object mapper
		ObjectMapper mapper = new ObjectMapper();

		// initialize json file object
		File graphFile = new File(graphPath);

		// initialize lists for processing
		Map<Integer, PrintConstraint> constraintMap = new HashMap<>();
		Map<PrintConstraint, List<Integer>> sourceConstraintMap = new HashMap<>();
		List<Map<String, Object>> edgeData = new LinkedList<>();

		try {

			// get graph data from json file
			Map<String, Object> graphData = mapper.readValue(graphFile, Map.class);

			// add alphabet data to settings
			Map<String, Object> alphabetData = (Map<String, Object>) graphData.get("alphabet");
			String minAlphabet = (String) alphabetData.get("declaration");
			setMinAlphabet.execute(minAlphabet);

			// get constraint data from graph data
			List<Map<String, Object>> vertexData = (List<Map<String, Object>>) graphData.get("vertices");
			// System.out.println("VD " + vertexData);
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

					// add to edge list
					incomingEdge.put("target", id);
					edgeData.add(incomingEdge);
				}
			}

			// set sourceConstraints for each constraint
			for (PrintConstraint constraint : sourceConstraintMap.keySet()) {

				// for each constraint id
				for (int id : sourceConstraintMap.get(constraint)) {

					// get source constraint
					PrintConstraint sourceConstraint = constraintMap.get(id);

					// set source constraint as source for current constraint
					constraint.setSource(sourceConstraint);
				}

				// add constraint to graph
				graph.addVertex(constraint);
			}

			for (Map<String, Object> obj : edgeData) {

				// get symbolic edge data
				int sourceId = (Integer) obj.get("source");
				PrintConstraint source = constraintMap.get(sourceId);
				int targetId = (Integer) obj.get("target");
				PrintConstraint target = constraintMap.get(targetId);
				String type = (String) obj.get("type");
				// System.out.println("src " + source);
				// System.out.println("trgt " + target);
				// create symbolic edge in graph from data
				SymbolicEdge edge = graph.addEdge(source, target);
				// System.out.println(edge);
				edge.setType(type);
			}

		} catch (IOException i) {
			i.printStackTrace();
		}

		// return graph
		return graph;
	}

	/*
	 * loadParser for the concrete and blank solvers
	 */
	private static void loadParser(Components components, Settings settings) {

		// create and store parser as component
		components.setParser(new Parser(components.getSolver(), settings.getDebug()));

	}

	/*
	 * loadReporter for the concrete and blank solvers
	 */
	private static void loadReporter(Components components, Settings settings) {

		// get values from settings
		Settings.ReportType reportType = settings.getReportType();
		boolean debug = settings.getDebug();
		DirectedGraph<PrintConstraint, SymbolicEdge> graph = components.getGraph();
		Parser parser = components.getParser();
		ExtendedSolver solver = components.getSolver();

		// initialize reporter as null
		Reporter reporter = null;

		if (reportType == Settings.ReportType.MODEL_COUNT) {

			// ensure solver is model count solver
			if (solver instanceof ModelCountSolver) {

				// cast solver
				ModelCountSolver mcSolver = (ModelCountSolver) solver;

				// create reporter from parameters
				reporter = new MCReporter(graph, parser, solver, debug, mcSolver);
			}

		} else if (reportType == Settings.ReportType.SAT) {

			// create reporter from parameters
			reporter = new SATReporter(graph, parser, solver, debug);
		}

		// store reporter
		components.setReporter(reporter);
	}

	/*
	 * loadSolver for use with concrete and blank solvers
	 */
	private static void loadSolver(Components components, Settings settings) {

		// get needed info from settings object
		Settings.SolverType selectedSolver = settings.getSolverType();
		Settings.ReportType reportType = settings.getReportType();
		int modelVersion = settings.getAutomatonModelVersion();
		int boundingLength = settings.getInitialBoundingLength();
		Alphabet alphabet = components.getAlphabet();

		// initialize extend solver as null
		ExtendedSolver solver = null;

		// create specified solver for parser
		if (selectedSolver == Settings.SolverType.BLANK) {

			solver = new BlankSolver();

		} else if (selectedSolver == Settings.SolverType.CONCRETE) {

			solver = new ConcreteSolver(alphabet, boundingLength);

		} else if (selectedSolver == Settings.SolverType.JSA) {

			// get model manager instance
			AutomatonModelManager modelManager = AutomatonModelManager.getInstance(alphabet, modelVersion,
					boundingLength);

			if (reportType == Settings.ReportType.SAT) {

				solver = new AutomatonModelSolver(modelManager, boundingLength);

			} else if (reportType == Settings.ReportType.MODEL_COUNT) {

				solver = new MCAutomatonModelSolver(modelManager, boundingLength);
			}

		}

		// store created solver
		components.setSolver(solver);
	}

	/*
	 * loadGraph for jsa and inverse solvers
	 */
	private static DirectedGraph<PrintConstraint, SymbolicEdge> loadGraph(String graphPath) {
		// initialize variables

		// init null graph object
		DirectedGraph<PrintConstraint, SymbolicEdge> graph = new DefaultDirectedGraph<>(SymbolicEdge.class);
		//eas just a shadow graph for now
		InvDefaultDirectedGraph graphExtra = new InvDefaultDirectedGraph(SymbolicEdge.class);

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
//					System.out.println("[IGEN] Using initial bound from JSON file .. " + initialBound);
				} else {
					initialBound = defaultBound;
//					System.out.println("[IGEN] Using initial bound default ..." + initialBound);
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
				graphExtra.addVertex(constraint);
				
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
				SymbolicEdge edgeExtra = graphExtra.addEdge(source, target);
				edgeExtra.setType(type);
			} // end edgeData for loop
		} catch (IOException i) {
			i.printStackTrace();
		}

		//eas for efficient processing several predicates, we
		//create a dependency map that indicate on which predicates a predicate is dependent
		//we say that two predicates are dependent if there is a symbolic node among their common ancestors
		
		graphExtra.computePredicateDependencies();
		//System.exit(1);
		return graphExtra;
	} // end loadGraph
	
	/*
	 * Run inverse solver
	 */
	private static void run_Acyclic_Inverse_r3(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
		Model_Acyclic_Inverse_Manager mFactory 					= new Model_Acyclic_Inverse_Manager(alpha, initialBound);
		Solver_Inverse<Model_Acyclic_Inverse> mSolver 		= new Solver_Inverse<Model_Acyclic_Inverse>(mFactory,	initialBound);
		Parser_2<Model_Acyclic_Inverse> mParser 				= new Parser_2<Model_Acyclic_Inverse>(mSolver, debug);
		Reporter_Inverse<Model_Acyclic_Inverse> mReporter 	= new Reporter_Inverse_BFS<Model_Acyclic_Inverse>(graph, mParser, mSolver, debug); //new Reporter_Inverse<Model_Acyclic_Inverse>(graph, mParser, mSolver, debug);
		////Reporter_Inverse<Model_Acyclic_Inverse> mReporter 	= new Reporter_Inverse<Model_Acyclic_Inverse>(graph, mParser, mSolver, debug);
		mSolver.setReduce(reduce);
		mReporter.run();
		//Decider<Model_Acyclic_Inverse> mDecider = new Decider<Model_Acyclic_Inverse>(graph, mParser);
		//mDecider.decide();
	}
	
	/*
	 * Solver = jsa, Automata = bounded, Reporter = model count
	 */
	private static void run_Bounded_Count(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
		Model_Bounded_Manager mFactory = new Model_Bounded_Manager(alpha, initialBound);
		Solver_Count<Model_Bounded> mSolver = new Solver_Count<Model_Bounded>(mFactory, initialBound);
		Parser_2<Model_Bounded> mParser = new Parser_2<Model_Bounded>(mSolver, debug);
		Reporter_Count<Model_Bounded> mReporter = new Reporter_Count<Model_Bounded>(graph, mParser, mSolver, debug);
		mReporter.run();
	}

	/*
	 * Solver = jsa, Automata = bounded, Reporter = sat
	 */
	private static void run_Bounded_SAT(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
		Model_Bounded_Manager mFactory = new Model_Bounded_Manager(alpha, initialBound);
		Solver<Model_Bounded> mSolver = new Solver<Model_Bounded>(mFactory, initialBound);
		Parser_2<Model_Bounded> mParser = new Parser_2<Model_Bounded>(mSolver, debug);
		Reporter_SAT<Model_Bounded> mReporter = new Reporter_SAT<Model_Bounded>(graph, mParser, mSolver, debug);
		mReporter.run();
	}

	/*
	 * Solver = jsa, Automata = acyclic, Reporter = model count
	 */
	private static void run_Acyclic_Count(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
		Model_Acyclic_Manager mFactory = new Model_Acyclic_Manager(alpha, initialBound);
		Solver_Count<Model_Acyclic> mSolver = new Solver_Count<Model_Acyclic>(mFactory, initialBound);
		Parser_2<Model_Acyclic> mParser = new Parser_2<Model_Acyclic>(mSolver, debug);
		Reporter_Count<Model_Acyclic> mReporter = new Reporter_Count<Model_Acyclic>(graph, mParser, mSolver, debug);
		mReporter.run();
	}
	
	/*
	 * Solver = jsa, Automata = acyclic, Reporter = sat
	 */
	private static void run_Acyclic_SAT(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
		Model_Acyclic_Manager mFactory = new Model_Acyclic_Manager(alpha, initialBound);
		Solver<Model_Acyclic> mSolver = new Solver<Model_Acyclic>(mFactory, initialBound);
		Parser_2<Model_Acyclic> mParser = new Parser_2<Model_Acyclic>(mSolver, debug);
		Reporter_SAT<Model_Acyclic> mReporter = new Reporter_SAT<Model_Acyclic>(graph, mParser, mSolver, debug);
		mReporter.run();
	}
	
	/*
	 * Solver = jsa, Automata = acyclic weighted, Reporter = model count
	 */
	private static void run_Weighted_Count(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
		Model_Acyclic_Weighted_Manager mFactory = new Model_Acyclic_Weighted_Manager(alpha, initialBound);
		Solver_Count<Model_Acyclic_Weighted> mSolver = new Solver_Count<Model_Acyclic_Weighted>(mFactory, initialBound);
		Parser_2<Model_Acyclic_Weighted> mParser = new Parser_2<Model_Acyclic_Weighted>(mSolver, debug);
		Reporter_Count<Model_Acyclic_Weighted> mReporter = new Reporter_Count<Model_Acyclic_Weighted>(graph, mParser, mSolver, debug);
		mReporter.run();
	}
	
	/*
	 * Solver = jsa, Automata = acyclic weighted, Reporter = sat
	 */
	private static void run_Weighted_SAT(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
		Model_Acyclic_Weighted_Manager mFactory = new Model_Acyclic_Weighted_Manager(alpha, initialBound);
		Solver_Count<Model_Acyclic_Weighted> mSolver = new Solver_Count<Model_Acyclic_Weighted>(mFactory, initialBound);
		Parser_2<Model_Acyclic_Weighted> mParser = new Parser_2<Model_Acyclic_Weighted>(mSolver, debug);
		Reporter_SAT<Model_Acyclic_Weighted> mReporter = new Reporter_SAT<Model_Acyclic_Weighted>(graph, mParser, mSolver, debug);
		mReporter.run();
	}
	
	
	/**
	 * Solver = concrete, Automata = singleton, Reporter = sat
	 * @param graph
	 */
	private static void run_Concrete_Singleton_SAT(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
		//TODO: for eas
		//the input values will be obtained from the graph itself
		Model_Concrete_Singleton_Manager mFactory = new Model_Concrete_Singleton_Manager(alpha, initialBound);
		Solver<Model_Concrete_Singleton> mSolver = new Solver<Model_Concrete_Singleton>(mFactory, initialBound);
		Parser_2<Model_Concrete_Singleton> mParser = new Parser_2<Model_Concrete_Singleton>(mSolver, debug);
		Reporter_SAT<Model_Concrete_Singleton> mReporter = new Reporter_SAT<Model_Concrete_Singleton>(graph, mParser, mSolver, debug);
		mReporter.run();
		
	}
	
	private static void printHeader (String graph, int length, String solver, String reporter, String automata) {
		
		if (header) {
		System.out.println("Using input graph.... : " + graph);
		System.out.println("Using input length... : " + length);
		System.out.println("Using solver......... : " + solver);
		System.out.println("Using reporter....... : " + reporter);
		System.out.println("Using automata....... : " + automata + "\n");
		}
		
	}
}
