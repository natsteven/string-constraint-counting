package edu.boisestate.cs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import edu.boisestate.cs.automatonModel.Model_Bounded;
import edu.boisestate.cs.automatonModel.Model_Bounded_Manager;
import edu.boisestate.cs.util.DotToGraph;

public class DebugMain {

	public static void main(String[] args) {
		// regular expression to be converted
		String regex = "aab(ab|c)d";
		// alphabet to be used
		Alphabet alpha = new Alphabet("a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z");
		// create manager with given alphabet and an initial bound of 5
		Model_Bounded_Manager mFactory = new Model_Bounded_Manager(alpha, 5);
		
		String targetAutomaton = "(abc|hbe|qby)gf";
		String targetRegex = "b";
		String replacement = "X";
		Model_Bounded replaceFirstOptimizedTest = mFactory.createFromRegex(targetAutomaton);
		DotToGraph.outputDotFileAndPng(replaceFirstOptimizedTest.toDot(), "before");
		DotToGraph.outputDotFileAndPng(replaceFirstOptimizedTest.replaceFirstOptimized(targetRegex, replacement).toDot(), "optimizedAfter");
		DotToGraph.outputDotFileAndPng(replaceFirstOptimizedTest.replaceFirst(targetRegex, replacement).toDot(), "unoptimizedAfter");
	}

	public static boolean remove(char targetTransition, State curr) {
		for (Transition t : curr.getTransitions()) {
			if (t.getMin() == targetTransition) {
				curr.getTransitions().remove(t);
				return true;
			} else {
				if (remove(targetTransition, t.getDest()))
					return true;
			}
		}
		return false;
	}

}
