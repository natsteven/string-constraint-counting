package edu.boisestate.cs;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.boisestate.cs.automatonModel.*;
import edu.boisestate.cs.graph.PrintConstraint;
import edu.boisestate.cs.graph.SymbolicEdge;
//import edu.boisestate.cs.reporting.A_Reporter;
//import edu.boisestate.cs.reporting.Reporter_Count;
import edu.boisestate.cs.reporting.Reporter_Inverse;
//import edu.boisestate.cs.reporting.Reporter_SAT;
import edu.boisestate.cs.solvers.*;
//import edu.boisestate.cs.util.LambdaVoid1;





public class SPFFileRunner {

	
//	private static int lowerBound = 1;
//	private static int upperBound = 3;
	private static int initialBound = 3;
	private static boolean reduce = true;
	private static boolean debug = false;
	
//	private static Alphabet THREE_CHAR = new Alphabet("A-C,a-c");
//	private static Alphabet THREE_CHAR_UC = new Alphabet("A-C");
//	private static Alphabet FULL_CHAR = new Alphabet("A-Z,a-z");
	private static Alphabet alpha;
	
	private static String solutionFile;
	private static Map<Integer,String> IDtoSPFsym = new HashMap<Integer,String>(); 
	

	@SuppressWarnings("unchecked")
	public String solveGraph (String inputFile) {
		
		StringBuilder outputFile = new StringBuilder(inputFile);
		outputFile.insert(outputFile.indexOf("."),"_solution");
		
		DirectedGraph<PrintConstraint, SymbolicEdge> graph = loadGraph(inputFile);
		
		System.out.println("[SPFFileRunner] Graph Loaded");
		
		solutionFile = outputFile.toString();
		
		// debug output matching ID to SPF symbolic string ...
//		for (Integer i : IDtoSPFsym.keySet()) {
//			System.out.println("Solution ID: " + i + " -> SPF symbolic string: " + IDtoSPFsym.get(i));
//		}
		
		System.out.println("[SPFFileRunner] Calling Solver");
		
		run_Acyclic_Inverse(graph);
		
		// add code to open solution file, populate SPFsym with SPF symbolic string matching ID
        // create json object mapper
        ObjectMapper mapper = new ObjectMapper();
  		mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // initialize json file object
        File SPFSolutions = new File(solutionFile);
        
        System.out.println("[SPFFileRunner] Adding SPF Symbolic String Names to Solution File");
        try {
			Map<String, Object> solutions = mapper.readValue(SPFSolutions, Map.class);
			
			// get constraint data from graph data
			List<Map<String, Object>> solutionData = (List<Map<String, Object>>) solutions.get("inputs");
			
			for (Map<String, Object> obj : solutionData) {
				int ID = (Integer) obj.get("ID");
				String SPFVar = IDtoSPFsym.get(ID);
				//SPFVar = SPFVar.substring(0, SPFVar.indexOf('_'));
				obj.replace("SPFsym", SPFVar);
			}
			
			mapper.writeValue(SPFSolutions, solutions);
			
			
			
			
			
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        
		
		
		return solutionFile;
	}
	
	
	public static void run_Acyclic_Inverse (DirectedGraph<PrintConstraint, SymbolicEdge> graph) {
		
		Model_Acyclic_Inverse_Manager mFactory = 				new Model_Acyclic_Inverse_Manager (alpha, initialBound);
		Solver_Inverse<Model_Acyclic_Inverse> mSolver = 		new Solver_Inverse<Model_Acyclic_Inverse> (mFactory, initialBound);
		Parser_2<Model_Acyclic_Inverse> mParser = 				new Parser_2<Model_Acyclic_Inverse> (mSolver, debug);
		Reporter_Inverse<Model_Acyclic_Inverse> mReporter = 	new Reporter_Inverse<Model_Acyclic_Inverse> (graph,mParser,mSolver,debug);	
		
		mSolver.setReduce(reduce);
		//mReporter.setSaveOption(true,"shorty7");
		mReporter.solutionFile = solutionFile;
		mReporter.run();
	
	}
	
	
	
    @SuppressWarnings("unchecked")
	public static DirectedGraph<PrintConstraint, SymbolicEdge> loadGraph(String graphPath) {

    	System.out.println("[SPFFileRunner] Loading Graph: " + graphPath);
    	
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
            alpha = new Alphabet(minAlphabet);
            //setMinAlphabet.execute(minAlphabet);
            
            if (graphData.get("inputLength") != null) {
            	int inputLength = (int) graphData.get("inputLength");
            	initialBound = inputLength;
            	System.out.println("[SPFFileRunner] Using initial bound from JSON File: " + initialBound);
            } else {
            	System.out.println("[SPFFileRunner] Using initial bound default ... " + initialBound);
            }


            // get constraint data from graph data
            List<Map<String, Object>> vertexData = (List<Map<String, Object>>) graphData.get("vertices");
            
            //System.out.println("VD " + vertexData);
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
                
                String SPFsym = (String) obj.get("SPFsym");
                if (SPFsym != "") {
                	IDtoSPFsym.put(id, SPFsym);
                }

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
                
            } // end vertexData for loop

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
                
            } // end sourceConstraint for loop

            for (Map<String, Object> obj : edgeData) {

                // get symbolic edge data
                int sourceId = (Integer) obj.get("source");
                PrintConstraint source = constraintMap.get(sourceId);
                int targetId = (Integer) obj.get("target");
                PrintConstraint target = constraintMap.get(targetId);
                String type = (String) obj.get("type");
                //System.out.println("src " + source);
                //System.out.println("trgt " + target);
                // create symbolic edge in graph from data
                SymbolicEdge edge = graph.addEdge(source, target);
              // System.out.println(edge);
                edge.setType(type);
                
            } // end edge data for loop

        } catch (IOException i) {
            i.printStackTrace();
        }

        // return graph
        return graph;
    } // end loadGraph
	
	
	
	
}
