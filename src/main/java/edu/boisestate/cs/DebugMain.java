package edu.boisestate.cs;

import edu.boisestate.cs.automatonModel.Model_Bounded;
import edu.boisestate.cs.automatonModel.Model_Bounded_Manager;
import edu.boisestate.cs.util.DotToGraph;

public class DebugMain {

	public static void main(String[] args) {
		// alphabet to be used
		Alphabet alpha = new Alphabet("a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z");
		// create manager with given alphabet and an initial bound of 5
		Model_Bounded_Manager mFactory = new Model_Bounded_Manager(alpha, 10);

		// this one breaks brute force
//		String targetAutomaton = "(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z)(begfh|ndido(niob|hnudidhudibnido)|nudiofnuini|b)(iwonwoiwsnoi|nbsiuos|sjnmios(sjmiosbindondm|mndiopdndb)mdoipdmd)miodxpb";
		String targetAutomaton = "(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z)(begfh|ndido(niob|hnudidhudibnido)|nudiofnuini|b)(iwonwoiwsnoi|nbsiuos|sjnmios)";
		String targetRegex = "(di|n|g)";
		String replacement = "X";
//		Model_Bounded replaceFirstOptimizedTest = mFactory.createFromRegex(targetAutomaton);
//		DotToGraph.outputDotFileAndPng(replaceFirstOptimizedTest.toDot(), "before");
//		System.out.println("Optimized:");
//		DotToGraph.outputDotFileAndPng(
//				replaceFirstOptimizedTest.replaceFirstOptimized(targetRegex, replacement).toDot(),
//				"optimizedAfter");
//		System.out.println("Brute force:");
//		DotToGraph.outputDotFileAndPng(replaceFirstOptimizedTest.replaceFirstBruteForce(targetRegex, replacement).toDot(),
//				"unoptimizedAfter");
		
		Model_Bounded replaceAllTest = mFactory.createFromRegex("brbrbrb");
		DotToGraph.outputDotFileAndPng(replaceAllTest.toDot(), "replaceAllBefore");
		DotToGraph.outputDotFileAndPng(replaceAllTest.replaceAll("brb", "X").toDot(), "replaceAllBruteForce");
	}

}
