package edu.boisestate.cs;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import edu.boisestate.cs.automatonModel.Model_Acyclic;
import edu.boisestate.cs.automatonModel.Model_Acyclic_Manager;
import edu.boisestate.cs.automatonModel.Model_Bounded;
import edu.boisestate.cs.automatonModel.Model_Bounded_Manager;

public class DebugMain {

	public static void main(String[] args) {
		// regular expression to be converted
//		String regex = "(a|b)ca";
		String regex = "(c|a)(g|e)f";
		// alphabet to be used
		Alphabet alpha = new Alphabet("a,b,c,d,e,f,g,h,x");
//		Alphabet alpha = new Alphabet("a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z");
		// create manager with given alphabet and an initial bound of 5
		Model_Acyclic_Manager mFactory = new Model_Acyclic_Manager(alpha, 5);
		Model_Acyclic model = mFactory.createFromRegex(regex);
		System.out.println(model.getFiniteStrings());
		System.out.println(model.getAutomaton());
		Automaton test = new RegExp("(c|a)e").toAutomaton();
		Automaton test2 = new RegExp(regex).toAutomaton();
		System.out.println(test.intersection(test2));
		model = model.replaceAllOptimized("e", "x");
		System.out.println(model.getFiniteStrings());
		System.out.println(model.getAutomaton());
	}

}
