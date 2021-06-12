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

import edu.boisestate.cs.automatonModel.AutomatonModelManager;
import edu.boisestate.cs.automatonModel.Model_Bounded;
import edu.boisestate.cs.automatonModel.Model_Bounded_Manager;
import edu.boisestate.cs.graph.PrintConstraint;
import edu.boisestate.cs.graph.SymbolicEdge;
import edu.boisestate.cs.reporting.MCReporter;
import edu.boisestate.cs.reporting.Reporter;
import edu.boisestate.cs.reporting.Reporter_Count_2;
import edu.boisestate.cs.reporting.SATReporter;
import edu.boisestate.cs.solvers.AutomatonModelSolver;
import edu.boisestate.cs.solvers.BlankSolver;
import edu.boisestate.cs.solvers.ConcreteSolver;
import edu.boisestate.cs.solvers.ExtendedSolver;
import edu.boisestate.cs.solvers.MCAutomatonModelSolver;
import edu.boisestate.cs.solvers.ModelCountSolver;
import edu.boisestate.cs.solvers.Solver_Count;
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
		run_Acyclic_Inverse_r3(graph);
	}
	
	public static void run_Bounded_Inverse(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
		Model_Bounded_Inverse_Manager mFactory = new Model_Bounded_Inverse_Manager(alpha, initialBound);
		Solver_Inverse<Model_Bounded> mSolver = new Solver_Inverse<Model_Bounded>(mFactory, initialBound);
		Parser_2<Model_Bounded_Inverse> mParser = new Parser_2<Model_Bounded_Inverse>(mSolver, debug);
		Reporter_Inverse_2<Model_Bounded_Inverse> mReporter = new Reporter_Inverse_2<Model_Bounded_Inverse>(graph, mParser, mSolver, debug);
		mSolver.setReduce(reduce);
		mReporter.run();
	}
	
	public static void run_Bounded_Count(DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
		Model_Bounded_Manager mFactory = new Model_Bounded_Manager(alpha, initialBound);
		Solver_Count<Model_Bounded> mSolver = new Solver_Count<Model_Bounded>(mFactory, initialBound);
		Parser_2<Model_Bounded> mParser = new Parser_2<Model_Bounded>(mSolver, debug);
		Reporter_Count_2<Model_Bounded> mReporter = new Reporter_Count_2<Model_Bounded>(graph, mParser, mSolver, debug);
		
	}
}
