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

		String targetAutomaton = "(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z)b";
		String targetRegex = "b";
		String replacement = "X";
		Model_Bounded replaceFirstOptimizedTest = mFactory.createFromRegex(targetAutomaton);
		DotToGraph.outputDotFileAndPng(replaceFirstOptimizedTest.toDot(), "before");
		System.out.println("Optimized:");
		DotToGraph.outputDotFileAndPng(
				replaceFirstOptimizedTest.superOptimizedReplaceFirst(targetRegex, replacement).toDot(),
				"optimizedAfter");
		System.out.println("Brute forces:");
		DotToGraph.outputDotFileAndPng(replaceFirstOptimizedTest.replaceFirst(targetRegex, replacement).toDot(),
				"unoptimizedAfter");
	}

}
