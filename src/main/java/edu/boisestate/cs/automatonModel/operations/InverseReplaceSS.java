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
     * replaced by a <tt>d</tt> transition.
     *
     * @param a input automaton
     * @return resulting automaton
     */
    @Override
    public Automaton op(Automaton a) {
        Automaton b = a.clone();
        // nps - we will check every state, and for each transition check for the first character of the replacement(d) sequence
        // if it matches we will continue to follow transitions until we find the last character of the replacement sequence
        // then we will add transitions from the source to destination states found that match the replaced(c) sequence
        for (State s : b.getStates()) {
            Set<Transition> transitions = s.getTransitions();
            int i =0;
            State next = s;
            while (i < d.length){
                for (Transition t : new ArrayList<Transition>(next.getTransitions())) {
                    char min = t.getMin();
                    char max = t.getMax();
                    State next = t.getDest();
                    // check if transition is in the replacement sequence, then follow
                    if (min <= d[i] && d[i] <= max) {
                        next = t.getDest();
                        i++;
                        break;
                    }
                }
            }
            // now we have the last state of the replacement sequence, we will add transitions for the replaced sequence

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

//    @Override
//    public CharSet charsetTransfer(CharSet a) {
//        if (a.contains(c)) {
//            return a.remove(c).add(d);
//        } else {
//            return a;
//        }
//    }

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

