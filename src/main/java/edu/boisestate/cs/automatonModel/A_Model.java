package edu.boisestate.cs.automatonModel;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import dk.brics.string.stringoperations.UnaryOperation;
import edu.boisestate.cs.Alphabet;
import edu.boisestate.cs.MinMaxPair;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * @author 
 *
 * @param <T>
 */
public abstract class A_Model <T extends A_Model <T>> implements Cloneable, I_Model <T> {

    protected final Alphabet alphabet;
    protected int boundLength;
    protected A_Model_Manager<T> modelManager;
    
    /**
     * 
     * @param alphabet
     * @param initialBoundLength
     */
    protected A_Model(Alphabet alphabet, int initialBoundLength) {

        this.alphabet = alphabet;
        this.boundLength = initialBoundLength;
    }
    
    
    @Override
	public abstract String getAcceptedStringExample();

    @Override
	public int getBoundLength() {
        return boundLength;
    }
    

    @Override
	public abstract Set<String> getFiniteStrings();

    @Override
	public abstract boolean isEmpty();

    @Override
	public abstract boolean isSingleton();

    @Override
	public void setBoundLength(int boundLength) {
        this.boundLength = boundLength;
    }



    static Automaton performUnaryOperation(Automaton automaton, UnaryOperation operation, Alphabet alphabet) {
        
    	// use operation
        Automaton result = operation.op(automaton);

        // bound resulting automaton to alphabet
        String charSet = alphabet.getCharSet();
        Automaton anyChar = BasicAutomata.makeCharSet(charSet).repeat();
        result = result.intersection(anyChar);

        //eas: even so the operation return the minimized automaton
        //the intersection might mess it up.
        //no need to call determinize since a minimize does
        //call that method first - only deterministic FA
        //can be minimized.
        result.minimize();

        // return resulting automaton
        return result;
    }

    @Override
	public abstract T assertContainedInOther(T containingModel);

    @Override
	public abstract T assertContainsOther(T containedModel);

    @Override
	public abstract T assertEmpty();

    @Override
	public abstract T assertEndsOther(T baseModel);

    @Override
	public abstract T assertEndsWith(T endingModel);

    @Override
	public abstract T assertEquals(T equalModel);

    @Override
	public abstract T assertEqualsIgnoreCase(T equalModel);

    @Override
	public abstract T assertHasLength(int min, int max);

    @Override
	public abstract T assertNotContainedInOther(T notContainingModel);

    @Override
	public abstract T assertNotContainsOther(T notContainedModel);

    @Override
	public abstract T assertNotEmpty();

    @Override
	public abstract T assertNotEndsOther(T notEndingModel);

    @Override
	public abstract T assertNotEndsWith(T notEndingModel);

    @Override
	public abstract T assertNotEquals(T notEqualModel);

    @Override
	public abstract T assertNotEqualsIgnoreCase(T notEqualModel);

    @Override
	public abstract T assertNotStartsOther(T notStartingModel);

    @Override
	public abstract T assertNotStartsWith(T notStartsModel);

    @Override
	public abstract T assertStartsOther(T startingModel);

    @Override
	public abstract T assertStartsWith(T startingModel);

    @Override
	public abstract T concatenate(T arg);

    @Override
	public abstract boolean containsString(String actualValue);

    @Override
	public abstract T delete(int start, int end);

    @Override
	public abstract boolean equals(T arg);

    @Override
	public abstract T intersect(T arg);

    @Override
	public abstract T insert(int offset, T argModel);

    @Override
	public abstract BigInteger modelCount();

    @Override
	public abstract T replace(char find, char replace);

    @Override
	public abstract T replace(String find, String replace);

    @Override
	public abstract T replaceChar();

    @Override
	public abstract T replaceFindKnown(char find);
    
	@Override
	public abstract T replaceAll(String arg1String, String arg2String);
	
    @Override
    public abstract T replaceFirst(String regexString, String replacementString);

    @Override
	public abstract T replaceReplaceKnown(char replace);

    @Override
	public abstract T reverse();

    @Override
	public abstract T substring(int start, int end);

    @Override
	public abstract T setCharAt(int offset, T argModel);

    @Override
	public abstract T setLength(int length);

    @Override
	public abstract T suffix(int start);

    @Override
	public abstract T toLowercase();

    @Override
	public abstract T toUppercase();

    @Override
	public abstract T trim();

    @Override
	public abstract T clone();

    static Automaton getRequiredCharAutomaton(Automaton a, Alphabet alphabet, int boundLength) {
        // if initial state is accepting
        State initialState = a.getInitialState();
        if (initialState.isAccept() && initialState.getTransitions().isEmpty()) {
            return BasicAutomata.makeEmptyString();
        }

        // initialize required char map
        Map<Integer, Character> requiredCharMap = new HashMap<>();

        // initialize state set
        Set<State> states = new TreeSet<>();
        states.add(initialState);

        // walk automaton up to bound length
        int accept = -1;
        for (int i = 0; i < boundLength && accept < 0; i++) {
            // initialize flag as true
            boolean isSame = true;

            // initialize current char to unused value
            char c = Character.MAX_VALUE;
            Set<State> newStates = new TreeSet<>();
            for (State s : states) {
                // if no transitions
                if (s.getTransitions().size() != 1) {
                    isSame = false;
                    continue;
                }
                // check if transition destination is an accepting state
                for (Transition t : s.getTransitions()) {
                    newStates.add(t.getDest());
                    if (t.getDest().isAccept()) {
                        accept = i;
                    }
                    // if transitions allow more than one character at length i
                    if (t.getMin() != t.getMax() ||
                        (c != Character.MAX_VALUE && c != t.getMin())) {
                        isSame = false;
                        continue;
                    }

                    // set current char to single char from transition
                    c = t.getMin();
                }
            }

            // if single char for transition at lenght i
            if (isSame && c != Character.MAX_VALUE) {
                requiredCharMap.put(i, c);
            }

            // update state set
            states = newStates;
        }

        // if no required single characters
        if (requiredCharMap.isEmpty()) {
            return BasicAutomata.makeEmpty();
        }

        // initialize initial state and current state variable
        State initial = new State();
        State s = initial;

        // create required char automaton
        int length = boundLength;
        if (accept >= 0) {
            length = accept + 1;
        }
        for (int i = 0; i < length; i ++) {
            // create new destination state
            State dest = new State();

            // if single character at length i
            if (requiredCharMap.containsKey(i)) {
                // add single char transition
                s.addTransition(new Transition(requiredCharMap.get(i), dest));
            } else {
                // add transition for all chars in alphabet
                for (MinMaxPair pair : alphabet.getCharRanges()) {
                    s.addTransition(new Transition(pair.getMin(), pair.getMax(), dest));
                }
            }

            // update current state
            s = dest;
        }

        // initialize return automaton and set initial and accepting states
        Automaton returnAutomaton = new Automaton();
        returnAutomaton.setInitialState(initial);
        s.setAccept(true);

        // return automaton
        return returnAutomaton;
    }
    
    @Override
	public abstract String getAutomaton();
}
