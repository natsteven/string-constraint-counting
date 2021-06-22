/**
 * InverseLowerString.java
 * Mar 17, 2020
 */
package edu.boisestate.cs.automatonModel.operations;

//package dk.brics.string.stringoperations;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import dk.brics.string.charset.CharSet;
import dk.brics.string.stringoperations.*;

import java.util.ArrayList;
import java.util.Set;

/**
 * Automaton operation for {@link String#toUpperCase()}.
 */
public class InverseReplaceCC extends UnaryOperation {
	char c, d;
    /**
     * Constructs new operation object.
     */
    public InverseReplaceCC(char c, char d) {
        this.c = c;
        this.d = d;
    }

    /**
     * Automaton operation.
     * Constructs new automaton as copy of <tt>a</tt> where all <tt>c</tt> transitions are
     * replaced by <tt>d</tt> transitions.
     *
     * @param a input automaton
     * @return resulting automaton
     */
    @Override
    public Automaton op(Automaton a) {
        Automaton b = a.clone();
        for (State s : b.getStates()) {
            Set<Transition> transitions = s.getTransitions();
            for (Transition t : new ArrayList<Transition>(transitions)) {
                char min = t.getMin();
                char max = t.getMax();
                State dest = t.getDest();
                if (min <= c && c <= max) {
                   //transitions.remove(t);
                    transitions.add(new Transition(d, dest));
                    if (min < c) {
                        transitions.add(new Transition(min, (char) (c - 1), dest));
                    }
                    if (c < max) {
                        transitions.add(new Transition((char) (c + 1), max, dest));
                    }
                }
            }
        }
        b.setDeterministic(false);
        b.reduce();
        b.minimize();
        return b;
    }

    @Override
    public String toString() {
        return "InverseReplaceCC[" + c + "," + d + "]";
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public CharSet charsetTransfer(CharSet a) {
        if (a.contains(c)) {
            return a.remove(c).add(d);
        } else {
            return a;
        }
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() + c + d;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InverseReplaceCC) {
        	InverseReplaceCC o = (InverseReplaceCC) obj;
            return c == o.c && d == o.d;
        } else {
            return false;
        }
    }
}

