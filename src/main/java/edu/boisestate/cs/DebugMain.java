package edu.boisestate.cs;

import java.util.HashMap;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import edu.boisestate.cs.automatonModel.Model_Bounded;
import edu.boisestate.cs.automatonModel.Model_Bounded_Manager;

public class DebugMain {

	public static void main(String[] args) {
		// regular expression to be converted
		String regex = "aab(ab|c)d";
		// alphabet to be used
		Alphabet alpha = new Alphabet("a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z");
		// create manager with given alphabet and an initial bound of 5
		Model_Bounded_Manager mFactory = new Model_Bounded_Manager(alpha, 5);
		Model_Bounded model = mFactory.createFromRegex(regex);
		Automaton a1 = new RegExp("a(b|c)deeef").toAutomaton();
		Automaton a2 = new RegExp("(cde|(e)*)").toAutomaton();
		System.out.println(model.findConcreteString(a1.getInitialState(), a2.getInitialState(), "", new HashMap<Integer, Integer>()));
	}

}
