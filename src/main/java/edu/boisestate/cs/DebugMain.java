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

		Model_Bounded model1 = mFactory.createFromRegex("(b|cc)d");
		Model_Bounded result1 = model1.replaceFirst("bd", "z");
		DotToGraph.outputDotFileAndPng(model1.toDot(), "model-1-before");
		DotToGraph.outputDotFileAndPng(result1.toDot(), "model-1-after");

		Model_Bounded model2 = mFactory.createFromRegex("(abc|xby)");
		Model_Bounded result2 = model2.replaceFirst("(a|x)", "f");
		DotToGraph.outputDotFileAndPng(model2.toDot(), "model-2-before");
		DotToGraph.outputDotFileAndPng(result2.toDot(), "model-2-after");

		Model_Bounded model3 = mFactory.createFromRegex("(bbz|byb)");
		Model_Bounded result3 = model3.replaceAll("b", "f");
		DotToGraph.outputDotFileAndPng(model3.toDot(), "model-3-before");
		DotToGraph.outputDotFileAndPng(result3.toDot(), "model-3-after");

		Model_Bounded model4 = mFactory.createFromRegex("a(b(c|d)|abc)d");
		Model_Bounded result4 = model4.replaceFirst("bc", "z");
		DotToGraph.outputDotFileAndPng(model4.toDot(), "model-4-before");
		DotToGraph.outputDotFileAndPng(result4.toDot(), "model-4-after");

		Model_Bounded model5 = mFactory.createFromRegex("abcbbb");
		Model_Bounded result5 = model5.replaceAll("(bcb|bbb)", "b");
		DotToGraph.outputDotFileAndPng(model5.toDot(), "model-5-before");
		DotToGraph.outputDotFileAndPng(result5.toDot(), "model-5-after");

		Automaton a = new RegExp("(b|cc)d").toAutomaton();
		DotToGraph.outputDotFileAndPng(a.toDot(), "stateTestBefore");
		remove('b', a.getInitialState());
		DotToGraph.outputDotFileAndPng(a.toDot(), "stateTestAfter");

		Model_Bounded genius1 = mFactory.createFromRegex("(bd|ccd)");
		DotToGraph.outputDotFileAndPng(genius1.toDot(), "genius-before");

		Model_Bounded genius2 = mFactory.createFromRegex("(d|ccd)");
		DotToGraph.outputDotFileAndPng(genius2.toDot(), "genius-after");

		Model_Bounded singletonExample = mFactory.createFromRegex("abc");
		DotToGraph.outputDotFileAndPng(singletonExample.toDot(), "singletonExample");

		Model_Bounded independentSolutionExample = mFactory.createFromRegex("(b|cc)d");
		DotToGraph.outputDotFileAndPng(independentSolutionExample.toDot(), "independentSolutionExample");

		Automaton targetAutomaton = new RegExp("a(bcd|bce)").toAutomaton();
		Automaton regexAutomaton = new RegExp("bc").toAutomaton();
		Set<String> s = new HashSet<String>();
		s.add("abcd");
//		s.add("abce");
		System.out.println(independentSolutionExample.findConcreteString(targetAutomaton.getInitialState(),
				regexAutomaton.getInitialState(), "", new HashMap<Integer, Integer>(), false, s));
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
