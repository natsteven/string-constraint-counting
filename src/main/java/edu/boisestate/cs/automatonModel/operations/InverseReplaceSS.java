/**
 * InverseReplaceSS.java
 * Jul 18, 2024
 */
package edu.boisestate.cs.automatonModel.operations;

//package dk.brics.string.stringoperations;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import dk.brics.string.charset.CharSet;
import dk.brics.string.stringoperations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * Inverse Automaton operation for {@link String#replace(CharSequence, CharSequence)} ()}.
 */
public class InverseReplaceSS extends UnaryOperation {
    char[] c, d;
    /**
     * Constructs new operation object.
     */
    public InverseReplaceSS(char[] c, char[] d) {
        this.c = c;
        this.d = d;
    }

    /**
     * Automaton operation.
     * Constructs new automaton as copy of <tt>a</tt> where all <tt>c</tt> transition sequences are
     * replaced by a <tt>d</tt> transition sequence.
     *
     * @param a input automaton
     * @return resulting automaton
     */
    @Override
    public Automaton op(Automaton a) {
        Automaton b = a.clone();
        State src = null;
        State dest = null;
        // nps - we will check every state, and for each transition check for the first character of the replacement(d) sequence
        // if it matches we will continue to follow transitions until we find the last character of the replacement sequence
        // then we will add transitions from the source to destination states found that match the replaced(c) sequence
        for (State s : b.getStates()) {
            dest = followSubstring(s); // this return destination state of the replacement sequence
            if (dest!=null){
                src = s;
                break;
            }
        }
        // no replaced string found
        if (src == null || dest == null){
            return b;
        }
        Set<Transition> transitions = src.getTransitions();
        // now we have the first and last state of the replacement sequence
        // we will add transitions and states from the source to dest state
        for (int i = 0; i < c.length - 1 ; i++){
            State to = new State();
            transitions.add(new Transition(c[i], to));
            transitions = to.getTransitions();
        }
        transitions.add(new Transition((c[c.length - 1]), dest));
        b.setDeterministic(false);
        b.reduce();
        b.minimize();
        return b;
    }

    /**
     * Follows the replacement sequence from the specified state to find the destination
     * state that can be followed by the replacmeent sequence, else return snull
     * @param s
     * @return
     */
    private State followSubstring(State s) {
        int i =0;
        State next = s;
        boolean found = false;
        while (i < d.length){
            for (Transition t : new ArrayList<Transition>(next.getTransitions())) {
                char min = t.getMin();
                char max = t.getMax();
                // check if transition is in the replacement sequence, then follow
                if (min <= d[i] && d[i] <= max) {
                    next = t.getDest();
                    i++;
                    found = true;
                    break;
                }
            }
            if (!found) return null; // no transition to follow
        }
        return next;
    }

    @Override
    public String toString() {
        return "InverseReplaceSS[" + c + "," + d + "]";
    }
//    no idea what the below two methods are for
    @Override
    public int getPriority() {
        return 3;
    }

    // not sure this is correctly done
    // in fact pretty sure it isnt correct, but we can't just replace all characters in d in a with characters in c...
    @Override
    public CharSet charsetTransfer(CharSet a) {
        for (char ch : c) {
            a = a.add(ch);
        }
        return a;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() + Arrays.hashCode(c) + Arrays.hashCode(d);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InverseReplaceSS) {
            InverseReplaceSS o = (InverseReplaceSS) obj;
            return c == o.c && d == o.d;
        } else {
            return false;
        }
    }
}

