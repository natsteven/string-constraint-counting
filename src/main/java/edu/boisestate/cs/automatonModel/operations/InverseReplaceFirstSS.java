///**
// * InverseReplaceFirstSS.java
// * Jul 29, 2024
// */
//package edu.boisestate.cs.automatonModel.operations;
//
////package dk.brics.string.stringoperations;
//
//import dk.brics.automaton.Automaton;
//import dk.brics.automaton.RegExp;
//import dk.brics.automaton.State;
//import dk.brics.automaton.Transition;
//import dk.brics.string.charset.CharSet;
//import dk.brics.string.stringoperations.*;
//
//import java.util.*;
//
///**
// * Inverse Automaton operation for {@link String#replaceFirst(String, String)} ()}.
// */
//public class InverseReplaceFirstSS extends UnaryOperation {
//    String c, d;
//    /**
//     * Constructs new operation object.
//     */
//    public InverseReplaceFirstSS(String c, String d) {
//        this.c = c;
//        this.d = d;
//    }
//
//    /**
//     * Automaton operation.
//     * Constructs new automaton as copy of <tt>a</tt> where greedy first matches of teh regex pattern <tt>c</tt> are
//     * replaced by <tt>d</tt>.
//     *
//     * @param a input automaton
//     * @return resulting automaton
//     */
//    @Override
//    public Automaton op(Automaton a) {
//        Automaton b = a.clone();
//
//        // construct an automota from the regex pattern c, and find if matches exist
//        Automaton cAutomaton = new RegExp(c).toAutomaton();
//        Automaton matchAutomaton = getAny().concatenate(cAutomaton).concatenate(getAny());
//        matchAutomaton = matchAutomaton.intersection(b);
//        if (matchAutomaton.isEmpty()) return b; // no matches found
//
//        // find all prefixes of the automaton that occur before any replacement sequence/pattern
//        Automaton matching = matchAutomaton.clone();
//        HashMap<State, Set<Automaton>> prefixMap = new HashMap<>();
//        HashMap<State, Set<Automaton>> suffixMap = new HashMap<>();
//        Stack<State> stack = new Stack<>();
////        Stack<Stack<State>> toProcess = new Stack<>();
//        // check all matches
//        while (!matching.isEmpty()){
//            stack.push(matching.getInitialState());
//            // go through states until first match found
//            while (!stack.isEmpty()){ // incorrect condition for while loop
//                State qs = stack.peek(); //
//                Automaton matchSearch= matching.clone();
//                State newStart = null;
//                // set all states to accept/final and init to qs
//                for (State s : matchSearch.getStates()){
//                    s.setAccept(true);
//                    if (s.equals(qs)) newStart = s;
//                }
//                // new automata with new start state and all states as final
//                matchSearch = getNewStart(matchSearch, newStart);
//
//                Automaton inter = matchSearch.intersection(cAutomaton.concatenate(getAny()));
//                if (inter.isEmpty()) {
//                    stack.push(qs.getTransitions().iterator().next().getDest());
//                    // no matches from current state, so push a child and save other children
////                    Stack<State> children = new Stack<>(); //doesnt need to be stack just convenient
////                    for (Transition t : qs.getTransitions()){
////                        children.push(t.getDest());
////                    }
//                    // i dont know that htis is necesary, every path should have a match?
////                    if (children.isEmpty()) break; //? nothing left to process, impossible branch or something?
////                    stack.push(children.pop()); // the child we will process next
////                    toProcess.push(children); // more optimized? for backtracking i think technically the outer while would achieve the same though....
//                } else {
//                    prefixMap.computeIfAbsent(qs, k -> new HashSet<Automaton>());
//                    Automaton prefix = getAutomatonFromStack(stack);
//                    prefixMap.get(qs).add(prefix);
//
//                    suffixMap.computeIfAbsent(qs, k -> new HashSet<Automaton>());
//                    Automaton suffix = getNewStart(matching, qs).intersection(cAutomaton.concatenate(getAny()));
//                    suffixMap.get(qs).add(suffix);
//
//                    matching = matching.minus(prefix.concatenate(suffix));
//                    break;
////                    // backtrack
////                    if (toProcess.isEmpty()){
////                        stack.pop();
////                    } else {
////                        Stack<State> children = toProcess.pop();
////                        if (!children.isEmpty()){
////                            stack.push(children.pop());
////                        } else {
////                            stack.pop();
////                        }
////                    }
//                }
//            }
//        }
//
//        // now we need to extract the longest pattern and the actual suffix from the current suffix automatons
//        // so we can insert the string in its place along with the other automata
//
//        HashMap<State, Set<Automaton>> realSuffixMap = new HashMap<>();
//
//        for ( Map.Entry<State, Set<Automaton>> entry : suffixMap.entrySet()){
//            State s = entry.getKey();
//            Set<Automaton> suffixes = entry.getValue();
////            Set<Automaton> prefixes = prefixMap.get(s);
//            Set<Automaton> realSuffixes = new HashSet<>();
//
//            Stack<State> matched = new Stack<>();
//            Stack<State> search = new Stack<>();
//            for (Automaton suffix : suffixes) {
//                Automaton RS = suffix.clone();
//                for (State state : RS.getStates()) {
//                    state.setAccept(false);
//                }
//                RS.getInitialState().setAccept(true); // unclear why this is necessary to me
//                while (!RS.isEmpty()) {
//                    search.push(RS.getInitialState());
//                    while (!search.isEmpty()) {
//                        State qs = search.pop();
//                        Automaton inter = RS.intersection(cAutomaton);
//                        if (!inter.isEmpty()) matched.push(qs);
//                        if (qs.getTransitions() != null) {
//                            search.push(qs.getTransitions().iterator().next().getDest());
//                        }
//                    }
//                    Automaton realSuffix = getNewStart(RS, matched.pop()); // greedy match
//                    realSuffixes.add(realSuffix);
//                    RS = RS.minus(realSuffix);
//                }
//
//            }
//            realSuffixMap.put(s, realSuffixes);
//        }
//
//        // construct new automaton, from all the pairs of prefixes and suffixes concatted with the replacement string
//
//        // coalesce automaton sets for a given state
//        for (State s : prefixMap.keySet()){
//            Set<Automaton> prefixes = prefixMap.get(s);
//            Set<Automaton> suffixes = realSuffixMap.get(s);
////            for (Automaton prefix : prefixes){
////                for (Automaton suffix : suffixes){
////                    Automaton replacement = new RegExp(d).toAutomaton();
////                    Automaton replacementAutomaton = prefix.concatenate(replacement).concatenate(suffix);
////                    b = b.minus(prefix.concatenate(cAutomaton).concatenate(suffix));
////                    b = b.union(replacementAutomaton);
////                }
////            }
//        }
//        return b;
//    }
//
//    private Automaton getAutomatonFromStack(Stack<State> st) {
//        // for each state in reverse order create copy and then reduce
//        Stack<State> stack = (Stack<State>) st.clone();
//        HashMap<State, State> corresponds = new HashMap<>();
//        Automaton a = new Automaton();
//        Collections.reverse(stack);
//        // get corresponding states so transitions can be correctly cloned
//        for (State s : stack){
//            State ns = new State();
//            corresponds.put(s, ns);
//        }
//        // clone transitions with corresponding states
//        for (State s : stack) {
//            for (Transition t : s.getTransitions()) {
//                State dest = t.getDest();
//                if (corresponds.get(dest) != null){
//                    corresponds.get(s).getTransitions().add(new Transition(t.getMin(), t.getMax(), corresponds.get(dest)));
//                }
//            }
//        }
//        State start = corresponds.get(stack.pop());
//        a.setInitialState(start);
//        a.reduce();
//        return a;
//    }
//
//    private Automaton getNewStart(Automaton a, State newStart){
//        Automaton b = a.clone();
//        b.setInitialState(newStart);
//        b.reduce();
//        return b;
//    }
//
////    private Automaton getAutomatonFromStack(Stack<State> stack) {
////        Automaton a = new Automaton();
////        Collections.reverse(stack);
////        State s = stack.pop();
////        a.setInitialState(s);
////        while (!stack.isEmpty()){
////            State next = stack.pop();
////            s.getTransitions().add(new Transition(next));
////            s = next;
////        }
////        return a;
////    }
//
//    /**
//     * Follows the replacement sequence from the specified state to find the destination
//     * state that can be followed by the replacmeent sequence, else return snull
//     * @param s
//     * @return
//     */
//    private State followSubstring(State s) {
//        int i =0;
//        State next = s;
//        boolean found = false;
//        while (i < d.length){
//            for (Transition t : new ArrayList<Transition>(next.getTransitions())) {
//                char min = t.getMin();
//                char max = t.getMax();
//                // check if transition is in the replacement sequence, then follow
//                if (min <= d[i] && d[i] <= max) {
//                    next = t.getDest();
//                    i++;
//                    found = true;
//                    break;
//                }
//            }
//            if (!found) return null; // no transition to follow
//        }
//        return next;
//    }
//
//    @Override
//    public String toString() {
//        return "InverseReplaceSS[" + c + "," + d + "]";
//    }
//    //    no idea what the below two methods are for
//    @Override
//    public int getPriority() {
//        return 3;
//    }
//
//    // not sure this is correctly done
//    // in fact pretty sure it isnt correct, but we can't just replace all characters in d in a with characters in c...
//    @Override
//    public CharSet charsetTransfer(CharSet a) {
//        for (char ch : c) {
//            a = a.add(ch);
//        }
//        return a;
//    }
//
//    @Override
//    public int hashCode() {
//        return getClass().hashCode() + Arrays.hashCode(c) + Arrays.hashCode(d);
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof InverseReplaceSS) {
//            InverseReplaceSS o = (InverseReplaceSS) obj;
//            return c == o.c && d == o.d;
//        } else {
//            return false;
//        }
//    }
//
//    public Automaton getAny() {
//        return new RegExp(".*").toAutomaton();
//    }
//}
//
