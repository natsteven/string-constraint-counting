package edu.boisestate.cs.automatonModel;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import dk.brics.string.stringoperations.*;
import edu.boisestate.cs.Alphabet;
import edu.boisestate.cs.automatonModel.operations.*;

import java.math.BigInteger;
import java.util.Set;
import java.util.Stack;

/**
 * 
 * @author
 *
 */
public class Model_Acyclic extends A_Model <Model_Acyclic> {
	
	
	private Automaton automaton;
	

   /**
    * Constructor 1: Requires *ACYCLIC* automata as argument. <br>
    * For use within Model_Acyclic_Manager <br>
    * Has no safeguards against incorrect automata being passed. <br>
    * 
    * @param automaton - ACYCLIC Automaton
    * @param alphabet - Alphabet
    * @param initialBoundLength - Initial bound, should match Automaton length
    */
	protected Model_Acyclic(Automaton automaton, Alphabet alphabet, int boundLength) {
       
    	super(alphabet, boundLength);

        this.automaton = automaton;
        
        this.modelManager = new Model_Acyclic_Manager (alphabet, boundLength);
    }
	
	/**
	 * 
	 * @param automaton
	 * @param alphabet
	 */
	protected Model_Acyclic(Automaton automaton, Alphabet alphabet) {
        
		super (alphabet, 0);

        this.automaton = automaton;
        
        this.modelManager = new Model_Acyclic_Manager (alphabet, 0);
    }
    
   	public String getAutomaton(){
		return automaton.toString();
	}

    private static Automaton getAutomatonFromAcyclicModel(Model_Acyclic model) {
        return model.automaton;
    }

    @Override
    public Model_Acyclic assertContainedInOther(Model_Acyclic containingModel) {
        ensureAcyclicModel(containingModel);

        // get containing automaton
        Automaton containing = getAutomatonFromAcyclicModel(containingModel);

        // if either automata is  empty
        if (this.automaton.isEmpty() || containing.isEmpty()) {
            return new Model_Acyclic(BasicAutomata.makeEmpty(), this.alphabet, 0);
        }

        // get all substrings
        Automaton substrings = performUnaryOperation(containing, new Substring(), this.alphabet);

        // get resulting automaton
        Automaton result =  this.automaton.intersection(substrings);

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }

    @Override
    public Model_Acyclic assertContainsOther(Model_Acyclic containedModel) {
        ensureAcyclicModel(containedModel);

        // create any string automata
        Automaton anyString1 =
                BasicAutomata.makeCharSet(this.alphabet.getCharSet()).repeat();
        Automaton anyString2 =
                BasicAutomata.makeCharSet(this.alphabet.getCharSet()).repeat();

        // concatenate with contained automaton
        Automaton contained = getAutomatonFromAcyclicModel(containedModel);
        Automaton x = anyString1.concatenate(contained).concatenate(anyString2);

        // get resulting automaton
        Automaton result =  this.automaton.intersection(x);
        result.minimize();

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }

    @Override
    public Model_Acyclic assertEmpty() {
        // get resulting automaton
        Automaton result = this.automaton.intersection(BasicAutomata.makeEmptyString());

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, 0);
    }

    @Override
    public Model_Acyclic assertEndsOther(Model_Acyclic containingModel) {
        ensureAcyclicModel(containingModel);

        // get containing automaton
        Automaton containing = getAutomatonFromAcyclicModel(containingModel);

        // if either automata is  empty
        if (this.automaton.isEmpty() || containing.isEmpty()) {
            return new Model_Acyclic(BasicAutomata.makeEmpty(), this.alphabet, 0);
        }

        // get all suffixes
        Automaton suffixes = performUnaryOperation(containing, new Postfix(), this.alphabet);

        // get resulting automaton
        Automaton result =  this.automaton.intersection(suffixes);

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }


    @Override
    public Model_Acyclic assertEndsWith(Model_Acyclic endingModel) {
        ensureAcyclicModel(endingModel);

        // create any string automata
        Automaton anyString =
                BasicAutomata.makeCharSet(this.alphabet.getCharSet()).repeat();

        // concatenate with ending automaton
        Automaton end = getAutomatonFromAcyclicModel(endingModel);
        Automaton x = anyString.concatenate(end);

        // get bounded resulting automaton
        Automaton result =  this.automaton.intersection(x);
        result.minimize();

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }

    @Override
    public Model_Acyclic assertEquals(Model_Acyclic equalModel) {
        ensureAcyclicModel(equalModel);

        // concatenate with contained automaton
        Automaton equal = getAutomatonFromAcyclicModel(equalModel);

        // get resulting automaton
        Automaton result =  this.automaton.intersection(equal);

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }

    @Override
    public Model_Acyclic assertEqualsIgnoreCase(Model_Acyclic equalModel) {
        ensureAcyclicModel(equalModel);

        // concatenate with contained automaton
        Automaton equal = getAutomatonFromAcyclicModel(equalModel);
        Automaton equalIgnoreCase = performUnaryOperation(equal, new IgnoreCase(), this.alphabet);

        // get resulting automaton
        Automaton result =  this.automaton.intersection(equalIgnoreCase);

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }

    @Override
    public Model_Acyclic assertHasLength(int min, int max) {
        // check min and max
        if (min > max) {
            return new Model_Acyclic(BasicAutomata.makeEmpty(), this.alphabet, 0);
        }

        // get any string with length between min and max
        Automaton minMax = BasicAutomata.makeCharSet(this.alphabet.getCharSet()).repeat(min, max);

        // get resulting automaton
        Automaton result =  this.automaton.intersection(minMax);

        // get new bound length
        int newBoundLength = max;
        if (this.boundLength < max) {
            newBoundLength = this.boundLength;
        }

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, newBoundLength);
    }

    @Override
    public Model_Acyclic assertNotContainedInOther(Model_Acyclic notContainingModel) {
        ensureAcyclicModel(notContainingModel);

        // get containing automaton
        Automaton notContaining = getAutomatonFromAcyclicModel(notContainingModel);

        // if not containing automaton is  empty
        if (notContaining.isEmpty() || automaton.isEmpty() || automaton.isEmptyString()) {
            return new Model_Acyclic(BasicAutomata.makeEmpty(), alphabet, 0);
        }

        // get automaton of required chars from not containing automaton
        notContaining = getRequiredCharAutomaton(notContaining, alphabet, boundLength);

        Automaton result = automaton;
        if (!notContaining.isEmpty()) {

            // get all substrings
            Automaton substrings = performUnaryOperation(notContaining,
                                                         new Substring(),
                                                         this.alphabet);

            // get resulting automaton
            result = this.automaton.minus(substrings);
        }

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }

    @Override
    public Model_Acyclic assertNotContainsOther(Model_Acyclic notContainedModel) {
        ensureAcyclicModel(notContainedModel);

        // get not contained automaton
        Automaton notContained = getAutomatonFromAcyclicModel(notContainedModel);

        // if not containing automaton is  empty
        if (notContained.isEmpty() || automaton.isEmpty()) {
            return new Model_Acyclic(BasicAutomata.makeEmpty(), alphabet, 0);
        }

        notContained = getRequiredCharAutomaton(notContained, alphabet, boundLength);

        Automaton result = automaton;
        if (!notContained.isEmpty()) {
            // create any string automata
            Automaton anyString1 =
                    BasicAutomata.makeCharSet(this.alphabet.getCharSet())
                                 .repeat();
            Automaton anyString2 =
                    BasicAutomata.makeCharSet(this.alphabet.getCharSet())
                                 .repeat();

            // concatenate with not contained automaton
            Automaton x = anyString1.concatenate(notContained)
                                    .concatenate(anyString2);

            // get resulting automaton
            result = this.automaton.minus(x);
        }

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }

    @Override
    public Model_Acyclic assertNotEmpty() {
        // get resulting automaton
        Automaton result = this.automaton.minus(BasicAutomata.makeEmptyString());

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }

    @Override
    public Model_Acyclic assertNotEndsOther(Model_Acyclic notContainingModel) {
        ensureAcyclicModel(notContainingModel);

        // get containing automaton
        Automaton notContaining = getAutomatonFromAcyclicModel(notContainingModel);

        // if not containing automaton is  empty
        if (notContaining.isEmpty() || automaton.isEmpty() || automaton.isEmptyString()) {
            return new Model_Acyclic(BasicAutomata.makeEmpty(), alphabet, 0);
        }

        // get automaton of required chars from not containing automaton
        notContaining = getRequiredCharAutomaton(notContaining, alphabet, boundLength);

        Automaton result = automaton;
        if (!notContaining.isEmpty()) {

            // get all suffixes
            Automaton suffixes = performUnaryOperation(notContaining,
                                                       new Postfix(),
                                                       this.alphabet);

            // get resulting automaton
            result = this.automaton.minus(suffixes);
        }

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }


    @Override
    public Model_Acyclic assertNotEndsWith(Model_Acyclic notEndingModel) {
        ensureAcyclicModel(notEndingModel);

        Automaton notEnding = getAutomatonFromAcyclicModel(notEndingModel);

        // if not containing automaton is  empty
        if (notEnding.isEmpty() || automaton.isEmpty()) {
            return new Model_Acyclic(BasicAutomata.makeEmpty(), alphabet, 0);
        }

        notEnding = getRequiredCharAutomaton(notEnding, alphabet, boundLength);

        Automaton result = automaton;
        if (!notEnding.isEmpty()) {

            // create any string automata
            Automaton anyString =
                    BasicAutomata.makeCharSet(this.alphabet.getCharSet())
                                 .repeat();

            // concatenate with not ending automaton
            Automaton x = anyString.concatenate(notEnding);

            // get resulting automaton
            result = this.automaton.minus(x);
        }

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }

    @Override
    public Model_Acyclic assertNotEquals(Model_Acyclic notEqualModel) {
        ensureAcyclicModel(notEqualModel);

        // get not equal automaton
        Automaton notEqual = getAutomatonFromAcyclicModel(notEqualModel);

        // if not containing automaton is  empty
        if (notEqual.isEmpty() || automaton.isEmpty()) {
            return new Model_Acyclic(BasicAutomata.makeEmpty(), alphabet, 0);
        }

        // if not equal automaton is a singleton
        Automaton result = automaton;
        if (notEqual.getFiniteStrings(1) != null) {
            // get resulting automaton
            result = this.automaton.minus(notEqual);
        }

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }

    @Override
    public Model_Acyclic assertNotEqualsIgnoreCase(Model_Acyclic notEqualModel) {
        ensureAcyclicModel(notEqualModel);

        // get not equal automaton
        Automaton notEqual = getAutomatonFromAcyclicModel(notEqualModel);

        // if not containing automaton is  empty
        if (notEqual.isEmpty() || automaton.isEmpty()) {
            return new Model_Acyclic(BasicAutomata.makeEmpty(), alphabet, 0);
        }

        // if not equal automaton is a singleton
        Automaton result = automaton;
        if (notEqual.getFiniteStrings(1) != null) {
            Automaton equalIgnoreCase = performUnaryOperation(notEqual,
                                                              new IgnoreCase(),
                                                              this.alphabet);

            // get resulting automaton
            result = this.automaton.minus(equalIgnoreCase);
        }

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }

    @Override
    public Model_Acyclic assertNotStartsOther(Model_Acyclic notContainingModel) {
        ensureAcyclicModel(notContainingModel);

        // get containing automaton
        Automaton notContaining = getAutomatonFromAcyclicModel(notContainingModel);

        // if not containing automaton is  empty
        if (notContaining.isEmpty() || automaton.isEmpty() || automaton.isEmptyString()) {
            return new Model_Acyclic(BasicAutomata.makeEmpty(), alphabet, 0);
        }

        // get automaton of required chars from not containing automaton
        notContaining = getRequiredCharAutomaton(notContaining, alphabet, boundLength);

        Automaton result = automaton;
        if (!notContaining.isEmpty()) {

            // get all prefixes
            Automaton prefixes = performUnaryOperation(notContaining,
                                                       new Prefix(),
                                                       this.alphabet);

            // get resulting automaton
            result = this.automaton.minus(prefixes);
        }

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }


    @Override
    public Model_Acyclic assertNotStartsWith(Model_Acyclic notStartsModel) {
        ensureAcyclicModel(notStartsModel);

        Automaton notStarting = getAutomatonFromAcyclicModel(notStartsModel);

        // if not containing automaton is  empty
        if (notStarting.isEmpty() || automaton.isEmpty()) {
            return new Model_Acyclic(BasicAutomata.makeEmpty(), alphabet, 0);
        }

        notStarting = getRequiredCharAutomaton(notStarting, alphabet, boundLength);

        Automaton result = automaton;
        if (!notStarting.isEmpty()) {
            // create any string automata
            Automaton anyString =
                    BasicAutomata.makeCharSet(this.alphabet.getCharSet())
                                 .repeat();

            // concatenate with not starts automaton
            Automaton x = notStarting.concatenate(anyString);

            // get resulting automaton
            result = this.automaton.minus(x);
        }

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }

    @Override
    public Model_Acyclic assertStartsOther(Model_Acyclic containingModel) {
        ensureAcyclicModel(containingModel);

        // get containing automaton
        Automaton containing = getAutomatonFromAcyclicModel(containingModel);

        // if either automata is  empty
        if (this.automaton.isEmpty() || containing.isEmpty()) {
            return new Model_Acyclic(BasicAutomata.makeEmpty(), this.alphabet, 0);
        }

        // get all prefixes
        Automaton prefixes = performUnaryOperation(containing, new Prefix(), this.alphabet);

        // get resulting automaton
        Automaton result =  this.automaton.intersection(prefixes);

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }


    @Override
    public Model_Acyclic assertStartsWith(Model_Acyclic startingModel) {
        ensureAcyclicModel(startingModel);

        // create any string automata
        Automaton anyString =
                BasicAutomata.makeCharSet(this.alphabet.getCharSet()).repeat();

        // concatenate with contained automaton
        Automaton start = getAutomatonFromAcyclicModel(startingModel);
        Automaton x = start.concatenate(anyString);

        // get resulting automaton
        Automaton result =  this.automaton.intersection(x);
        result.minimize();

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }


    @Override
    public Model_Acyclic clone() {
        // create new model from existing automata
        Automaton cloneAutomaton = this.automaton.clone();
        return new Model_Acyclic(cloneAutomaton,
                                         this.alphabet,
                                         this.boundLength);
    }

    @Override
    public Model_Acyclic concatenate(Model_Acyclic argModel) {
        ensureAcyclicModel(argModel);

        // get arg automaton
        Automaton arg = getAutomatonFromAcyclicModel(argModel);

        // get concatenation of automata
        Automaton result = this.automaton.concatenate(arg);

        // minimize result automaton
        result.minimize();

        // calculate new bound length
        int boundLength = this.boundLength + argModel.boundLength;

        // return bounded model from automaton
        return new Model_Acyclic(result, this.alphabet, boundLength);
    }

    @Override
    public boolean containsString(String actualValue) {
        return this.automaton.run(actualValue);
    }


    @Override
    public Model_Acyclic delete(int start, int end) {

        // perform operation
        Automaton result = performUnaryOperation(automaton, new PreciseDelete(start, end), this.alphabet);

        // determine new bound length
        int newBoundLength;
        if (this.boundLength < start) {
            newBoundLength = 0;
        } else if (this.boundLength < end) {
            newBoundLength = start;
        } else {
            int charsDeleted = end - start;
            newBoundLength = this.boundLength - charsDeleted;
        }

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, newBoundLength);
    }

    @Override
    public boolean equals(Model_Acyclic arg) {

        // check if arg model is bounded automaton model
        if (arg instanceof Model_Acyclic) {

            // cast arg model
            Model_Acyclic argModel = (Model_Acyclic) arg;

            // check underlying automaton models for equality
            return this.automaton.equals(argModel.automaton);
        }

        return false;
    }

    @Override
    public String getAcceptedStringExample() {
        return this.automaton.getShortestExample(true);
    }

    @Override
    public Set<String> getFiniteStrings() {

        // return finite strings from automaton
        return automaton.getFiniteStrings();
    }

    @Override
    public Model_Acyclic insert(int offset, Model_Acyclic argModel) {
        ensureAcyclicModel(argModel);

        // get automata for operations
        Automaton arg = getAutomatonFromAcyclicModel(argModel);

        // get resulting automaton
        PreciseInsert insert = new PreciseInsert(offset);
        Automaton result = insert.op(automaton, arg);
        result.minimize();

        // calculate new bound length
        int newBoundLength = this.boundLength + argModel.boundLength;

        // return unbounded model from automaton
        return new Model_Acyclic(result, this.alphabet, newBoundLength);
    }

    @Override
    public Model_Acyclic intersect(Model_Acyclic arg) {
        ensureAcyclicModel(arg);

        // cast arg model
        Model_Acyclic argModel = (Model_Acyclic) arg;

        // get intersection of automata
        Automaton result = this.automaton.intersection(argModel.automaton);

        // minimize result automaton
        result.minimize();

        // calculate new bound length
        int boundLength = this.boundLength;
        if (argModel.boundLength < this.boundLength) {
            boundLength = argModel.boundLength;
        }

        // return bounded model from automaton
        return new Model_Acyclic(result, this.alphabet, boundLength);
    }

    @Override
    public boolean isEmpty() {
    	/* eas 10-31-18 why, why is EmptyString() ??? */
        //return this.automaton.isEmptyString();
    	//the correct code
        return this.automaton.isEmpty();
      
    }

    @Override
    public boolean isSingleton() {
        // get one finite string, null if more
        Set<String> strings = this.automaton.getFiniteStrings(1);

        // return if single non-null string in automaton
        return strings != null &&
               strings.size() == 1 &&
               strings.iterator().next() != null;
    }

    @Override
    public BigInteger modelCount() {
        // return model count of automaton
        return StringModelCounter.ModelCount(automaton);
    }

    @Override
    public Model_Acyclic replace(char find, char replace) {
        // perform operation
        Automaton result = performUnaryOperation(automaton, new Replace1(find, replace), this.alphabet);

        // return new model from resulting automaton
        return new Model_Acyclic(result,
                                         this.alphabet,
                                         this.boundLength);
    }

    @Override
    public Model_Acyclic replace(String find, String replace) {

        // perform operation
        Replace6 replaceOp = new Replace6(find, replace);
        Automaton result = performUnaryOperation(automaton, replaceOp, this.alphabet);

        // determine new bound length
        int boundDiff = find.length() - replace.length();
        int newBoundLength = this.boundLength - boundDiff;

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, newBoundLength);
    }

    @Override
    public Model_Acyclic replaceChar() {

        // perform operation
        Automaton result = performUnaryOperation(automaton, new Replace4(), this.alphabet);

        // return new model from resulting automaton
        return new Model_Acyclic(result,
                                         this.alphabet,
                                         this.boundLength);
    }

    @Override
    public Model_Acyclic replaceFindKnown(char find) {

        // perform operation
        Automaton result = performUnaryOperation(automaton, new Replace2(find), this.alphabet);

        // return new model from resulting automaton
        return new Model_Acyclic(result,
                                         this.alphabet,
                                         this.boundLength);
    }

    @Override
    public Model_Acyclic replaceReplaceKnown(char replace) {

        // perform operation
        Automaton result = performUnaryOperation(automaton, new Replace3(replace), this.alphabet);

        // return new model from resulting automaton
        return new Model_Acyclic(result,
                                         this.alphabet,
                                         this.boundLength);
    }

    @Override
    public Model_Acyclic reverse() {
        // if automaton is empty
        if (this.automaton.isEmpty()) {
            return new Model_Acyclic(BasicAutomata.makeEmpty(), this.alphabet, 0);
        }

        // perform operation
        Automaton result = performUnaryOperation(automaton, new Reverse(), this.alphabet);

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, this.boundLength);
    }

    @Override
    public Model_Acyclic setCharAt(int offset, Model_Acyclic argModel) {
        ensureAcyclicModel(argModel);

        // get automata for operations
        Automaton arg = getAutomatonFromAcyclicModel(argModel);

        // get resulting automaton
        PreciseSetCharAt operation = new PreciseSetCharAt(offset);
        Automaton result = operation.op(automaton, arg);
        result.minimize();

        // return unbounded model from automaton
        return new Model_Acyclic(result, this.alphabet, boundLength);
    }

    @Override
    public Model_Acyclic setLength(int length) {

        // add null to new alphabet
        Set<Character> symbolSet = alphabet.getSymbolSet();
        symbolSet.add('\u0000');
        Alphabet newAlphabet = new Alphabet(symbolSet);

        // get resulting automaton
        Automaton result = performUnaryOperation(automaton,
                                                 new PreciseSetLength(length),
                                                 newAlphabet);

        // return unbounded model from automaton
        return new Model_Acyclic(result, this.alphabet, length);
    }


    @Override
    public Model_Acyclic substring(int start, int end) {
        // get resulting automaton
        Automaton result = performUnaryOperation(automaton, new PreciseSubstring(start, end), this.alphabet);

        // get new bound length
        int newBoundLength = end - start;

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, newBoundLength);
    }

    @Override
    public Model_Acyclic suffix(int start) {

        // perform operation
        Automaton result = performUnaryOperation(automaton, new PreciseSuffix(start), this.alphabet);

        // determine new bound length
        int newBoundLength = this.boundLength - start;

        // return new model from resulting automaton
        return new Model_Acyclic(result, this.alphabet, newBoundLength);
    }

    @Override
    public Model_Acyclic toLowercase() {

        // perform operation
        Automaton result = performUnaryOperation(automaton, new ToLowerCase(), this.alphabet);

        // return new model from resulting automaton
        return new Model_Acyclic(result,
                                         this.alphabet,
                                         this.boundLength);
    }

    @Override
    public Model_Acyclic toUppercase() {

        // perform operation
        Automaton result = performUnaryOperation(automaton, new ToUpperCase(), this.alphabet);

        // return new model from resulting automaton
        return new Model_Acyclic(result,
                                         this.alphabet,
                                         this.boundLength);
    }

    @Override
    public Model_Acyclic trim() {

        // perform operation
        Automaton result = performUnaryOperation(automaton, new PreciseTrim(), this.alphabet);

        // return new model from resulting automaton
        return new Model_Acyclic(result,
                                         this.alphabet,
                                         this.boundLength);
    }

    private void ensureAcyclicModel(Model_Acyclic arg) {
        // check if automaton model is bounded
        if (!(arg instanceof Model_Acyclic)) {

            throw new UnsupportedOperationException(
                    "The Acyclic Automaton Model only supports binary " +
                    "operations with other Acyclic Automaton Models.");
        }
    }

    public Model_Acyclic union (Model_Acyclic argModel) {
   	 ensureAcyclicModel(argModel);
   	 Automaton arg = getAutomatonFromAcyclicModel(argModel);
   	 Automaton result = this.automaton.union(arg);
   	 result.minimize();
        int boundLength = this.boundLength;
        if (argModel.boundLength > this.boundLength) {
            boundLength = argModel.boundLength;
        }
        return new Model_Acyclic(result, this.alphabet, boundLength);
   }

	/**
	 * Checks if the automaton contained in this object is a subset of the provided
	 * regex.
	 * 
	 * @param regexString - Regular expression to be checked
	 * @return True if they match, false if otherwise.
	 */
	public boolean matches(String regexString) {
		// create an Automaton from the regexString
		RegExp regex = new RegExp(regexString);
		Automaton regexAutomaton = regex.toAutomaton();
		// return if the automaton are subsets of each other
		return this.automaton.subsetOf(regexAutomaton);
	}

	/**
	 * Given a regex and a replacement String, iterate through all possible
	 * solutions of the target automaton. For each solution, run
	 * String.replaceFirst(regex, String). Convert this String into an automaton,
	 * then union said automaton with the result automaton. Return the final
	 * automaton.
	 * 
	 * @param regexString - String storing the regex to be used
	 * @param replacement - String to replace occurrences of the regex
	 * @return - Model_Acyclic object containing the resulting automaton
	 */
	public Model_Acyclic replaceFirstBruteForce(String regexString, String replacement) {
		// get the set of all possible finite solutions for the automaton
		Set<String> solutions = this.automaton.getFiniteStrings();
		// if the automaton as an infinite language, return the unmodified automaton
		if (solutions == null)
			return new Model_Acyclic(this.automaton, this.alphabet, this.boundLength);
		// start with an empty automaton
		Automaton result = Automaton.makeEmpty();
		// for each solution, replace the first occurrence of the regex and concatenate
		// it with the result automaton
		for (String str : solutions) {
			Automaton a = BasicAutomata.makeString(str.replaceFirst(regexString, replacement));
			if (result.isEmpty())
				result = a;
			else
				result = result.union(a);
		}
		result.minimize();
		// return the result automaton in a Model_Acyclic wrapper
		return new Model_Acyclic(result, this.alphabet, this.boundLength);
	}

	public Model_Acyclic replaceAllBruteForce(String regexString, String replacement) {
		// get the set of all possible finite solutions for the automaton
		Set<String> solutions = this.automaton.getFiniteStrings();
		// if the automaton as an infinite language, return the unmodified automaton
		if (solutions == null)
			return new Model_Acyclic(this.automaton, this.alphabet, this.boundLength);
		// start with an empty automaton
		Automaton result = Automaton.makeEmpty();
		// for each solution, replace the first occurrence of the regex and concatenate
		// it with the result automaton
		for (String str : solutions) {
			Automaton a = BasicAutomata.makeString(str.replaceAll(regexString, replacement));
			if (result.isEmpty())
				result = a;
			else
				result = result.union(a);
		}
		result.minimize();
		// return the result automaton in a Model_Acyclic wrapper
		return new Model_Acyclic(result, this.alphabet, this.boundLength);
	}

	/**
	 * Given a regex and replacement String, this method performs a replaceAll
	 * operation on all Strings in the language of the target Automaton.
	 * 
	 * The intersection of the target Automaton and all Strings containing the
	 * provided regex is found and saved as intersection. Then, a depth-first search
	 * is performed on intersection to find each prefix containing the regex. The
	 * prefix and suffix (i.e. following State which contains its suffixes) are
	 * saved. The method calls itself on the suffix to find any further matches.
	 * Once the suffix is returned, it is concatenated with the prefix and unioned
	 * with the target automaton. Once the intersection is empty, the target
	 * automaton is returned wrapped in a Model_Acyclic object.
	 * 
	 * @param regexString       - String representation of the regex to be found in
	 *                          the target Automaton
	 * @param replacementString - String to replace the substring which satisfies
	 *                          the target Automaton
	 * @return - The modified Automaton
	 */
	public Model_Acyclic replaceAllOptimized(String regexString, String replacementString) {
		// initialize automata
		Automaton targetAutomaton = Automaton.minimize(this.automaton.clone());
		Automaton regexAutomaton = new RegExp(regexString).toAutomaton();
		Automaton containsRegex = Automaton.makeAnyString().union(regexAutomaton).union(Automaton.makeAnyString());
		Automaton intersection = targetAutomaton.intersection(containsRegex);
		targetAutomaton = targetAutomaton.minus(containsRegex);
		// initialize target states
		State targetState = intersection.getInitialState();
		State regexState = regexAutomaton.getInitialState();
		// initialize the prefixandString tracker
		StringBuilder prefix = new StringBuilder();
		// initialize stateStack
		Stack<State> stateStack = new Stack<State>();
		while (true) {
			// if we have already visited the targetState, then the Automaton is cyclic
			if (stateStack.contains(targetState))
				throw new IllegalArgumentException("Cannot run cyclic automata in Model_Acyclic");
			// if we are at a satisfactory point in the regex
			if (regexState.isAccept()) {
				// check if there is a path to continue
				String transition = null;
				boolean solution = false;
				for (Transition t : targetState.getTransitions()) {
					for (Transition r : regexState.getTransitions()) {
						transition = getSharedTransition(t, r);
						if (transition != null) {
							// if there is a potential path, follow it
							State subTargetState = t.getDest();
							State subRegexState = r.getDest();
							StringBuilder subPrefix = new StringBuilder(transition);
							stateStack.push(targetState);
							while (true) {
								transition = null;
								// if there is a further solution, update parent variables
								if (subRegexState.isAccept()) {
									targetState = subTargetState;
									prefix.append(subPrefix.toString());
									subPrefix.delete(0, subPrefix.length());
									// find out if there is an even further solution
									for (Transition subT : subTargetState.getTransitions()) {
										for (Transition subR : subRegexState.getTransitions()) {
											transition = getSharedTransition(subT, subR);
											if (transition != null) {
												subTargetState = subT.getDest();
												subRegexState = subR.getDest();
												subPrefix.append(transition);
												break;
											}
										}
										if (transition != null)
											break;
									}
									if (transition == null)
										break;
								} else {
									// if current state is not a solution, find out if there is a potential further
									// path
									for (Transition subT : subTargetState.getTransitions()) {
										for (Transition subR : subRegexState.getTransitions()) {
											transition = getSharedTransition(subT, subR);
											if (transition != null) {
												subTargetState = subT.getDest();
												subRegexState = subR.getDest();
												subPrefix.append(transition);
												break;
											}
										}
										if (transition != null)
											break;
									}
									if (transition == null)
										break;
								}
							}
							break;
						}
					}
					if (solution)
						break;
				}
				// create prefix
				String modifiedPrefix = prefix.toString().replaceAll(regexString, replacementString);
				// minus target prefix from intersection
				intersection = Automaton.minimize(intersection
						.minus(Automaton.makeString(prefix.toString()).concatenate(Automaton.makeAnyString())));
				// create suffix automaton
				Automaton suffix = Automaton.makeEmpty();
				suffix.setInitialState(targetState);
				// call self on suffix
				Model_Acyclic suffixModel = new Model_Acyclic(suffix, this.alphabet, this.boundLength)
						.replaceAllOptimized(regexString, replacementString);
				// concat prefix and suffix
				Automaton modifiedAutomaton = concatState(Automaton.makeString(modifiedPrefix),
						suffixModel.automaton.getInitialState());
				// union modifiedAutomaton with targetAutomaton and minimize
				targetAutomaton = Automaton.minimize(targetAutomaton.union(modifiedAutomaton));
				// reset temp states and state stack
				targetState = intersection.getInitialState();
				regexState = regexAutomaton.getInitialState();
				stateStack.clear();
				prefix.delete(0, prefix.length());
				// if there are no further matches to operate on, return the modified automaton
				if (intersection.isEmpty()) {
					return new Model_Acyclic(targetAutomaton, this.alphabet, this.boundLength);
				}
			} else {
				// else check if any of the next target transitions match the regex
				boolean found = false;
				for (Transition t : targetState.getTransitions()) {
					for (Transition r : regexState.getTransitions()) {
						String transition = getSharedTransition(r, t);
						// if a match is found
						if (transition != null) {
							found = true;
							// update Stacks
							stateStack.push(targetState);
							prefix.append(transition);
							// update State objects
							targetState = t.getDest();
							regexState = r.getDest();
							// break out of loop
							break;
						}
					}
					if (found)
						break;
				}
				// if no matching transition was found, continue down the next available path
				if (!found) {
					// if there are no possible following Transitions
					if (targetState.getTransitions().isEmpty()) {
						if (targetState.isAccept()) {
							return new Model_Acyclic(targetAutomaton.union(intersection), this.alphabet,
									this.boundLength);
						}
						throw new ArithmeticException("Invalid String found in intersection.");
					}
					// save next Transition
					Transition t = (Transition) targetState.getTransitions().toArray()[0];
					// update prefix and State counters
					prefix.append(t.getMin());
					stateStack.push(targetState);
					// update State objects
					regexState = regexAutomaton.getInitialState();
					targetState = t.getDest();
				}
			}
		}
	}

	/**
	 * Given a regex and replacement String, this method performs a replaceFirst
	 * operation on all Strings in the language of the target Automaton.
	 * 
	 * The intersection of the target Automaton and all Strings containing the
	 * provided regex is found and saved as intersection. Then, a depth-first search
	 * is performed on intersection to find each prefix containing the regex. The
	 * prefix and following State (which contains its suffixes) are saved. The
	 * prefix is modified with the String.replaceFirst method before being
	 * concatenated with its suffixes. The resulting Automaton is then unioned with
	 * the original Automaton. After all possible prefixes are found and
	 * intersection is empty, the resulting Automaton is returned.
	 * 
	 * @param regexString       - String representation of the regex to be found in
	 *                          the target Automaton
	 * @param replacementString - String to replace the substring which satisfies
	 *                          the target Automaton
	 * @return - The modified Automaton
	 */
	@Override
	public Model_Acyclic replaceFirstOptimized(String regexString, String replacementString) {
		long start = System.currentTimeMillis();
		// initialize Automata
		Automaton regexAutomaton = new RegExp(regexString).toAutomaton();
		Automaton originalAutomaton = Automaton.minimize(this.automaton.clone());
		Automaton anyPrefixAndSuffix = Automaton.makeAnyString().concatenate(regexAutomaton)
				.concatenate(Automaton.makeAnyString());
		// Automaton containing all Strings in the originalAutomaton's language which
		// contain a substring which satisfies the regex
		Automaton intersection = Automaton.minimize(originalAutomaton.intersection(anyPrefixAndSuffix));
		originalAutomaton = Automaton.minimize(originalAutomaton.minus(anyPrefixAndSuffix));
		// if there are no matches to operate on, return the originalAutomaton
		if (intersection.isEmpty())
			return new Model_Acyclic(originalAutomaton, this.alphabet, this.boundLength);
		// initialize temporary States
		State targetState = intersection.getInitialState();
		State regexState = regexAutomaton.getInitialState();
		// Stack containing visited States to check for cyclic Automata
		Stack<State> stateStack = new Stack<State>();
		// StringBuilder to contain the ongoing prefix
		StringBuilder prefix = new StringBuilder();
		while (true) {
			// if we have already visited the targetState, then the Automaton is cyclic
			if (stateStack.contains(targetState))
				throw new IllegalArgumentException("Cannot run cyclic automata in Model_Acyclic");
			// if a satisfactory regex state has been reached
			if (regexState.isAccept()) {
				// check if there is a path to continue
				String transition = null;
				boolean solution = false;
				for (Transition t : targetState.getTransitions()) {
					for (Transition r : regexState.getTransitions()) {
						transition = getSharedTransition(t, r);
						if (transition != null) {
							// if there is a potential path, follow it
							State subTargetState = t.getDest();
							State subRegexState = r.getDest();
							StringBuilder subPrefix = new StringBuilder(transition);
							stateStack.push(targetState);
							while (true) {
								transition = null;
								// if there is a further solution, update parent variables
								if (subRegexState.isAccept()) {
									targetState = subTargetState;
									prefix.append(subPrefix.toString());
									subPrefix.delete(0, subPrefix.length());
									// find out if there is an even further solution
									for (Transition subT : subTargetState.getTransitions()) {
										for (Transition subR : subRegexState.getTransitions()) {
											transition = getSharedTransition(subT, subR);
											if (transition != null) {
												subTargetState = subT.getDest();
												subRegexState = subR.getDest();
												subPrefix.append(transition);
												break;
											}
										}
										if (transition != null)
											break;
									}
									if (transition == null)
										break;
								} else {
									// if current state is not a solution, find out if there is a potential further
									// path
									for (Transition subT : subTargetState.getTransitions()) {
										for (Transition subR : subRegexState.getTransitions()) {
											transition = getSharedTransition(subT, subR);
											if (transition != null) {
												subTargetState = subT.getDest();
												subRegexState = subR.getDest();
												subPrefix.append(transition);
												break;
											}
										}
										if (transition != null)
											break;
									}
									if (transition == null)
										break;
								}
							}
							break;
						}
					}
					if (solution)
						break;
				}
				// concat prefix and all possible suffixes
				Automaton modifiedPrefix = concatState(
						Automaton.makeString(prefix.toString().replaceFirst(regexString, replacementString)),
						targetState);
				// minus target prefix from intersection
				intersection = intersection
						.minus(Automaton.makeString(prefix.toString()).concatenate(Automaton.makeAnyString()));
				// union modifiedPrefix with originalAutomaton
				originalAutomaton = originalAutomaton.union(modifiedPrefix);
				// minimize Automata
				intersection = Automaton.minimize(intersection);
				originalAutomaton = Automaton.minimize(originalAutomaton);
				// reset temp states and state stack
				targetState = intersection.getInitialState();
				regexState = regexAutomaton.getInitialState();
				stateStack.clear();
				prefix.delete(0, prefix.length());
				// if there are no further matches to operate on, return the modified automaton
				if (intersection.isEmpty())
					return new Model_Acyclic(originalAutomaton, this.alphabet, this.boundLength);
			} else {
				// else check if any of the next target transitions match the regex
				boolean found = false;
				for (Transition t : targetState.getTransitions()) {
					for (Transition r : regexState.getTransitions()) {
						String transition = getSharedTransition(r, t);
						// if a match is found
						if (transition != null) {
							found = true;
							// update Stacks
							stateStack.push(targetState);
							prefix.append(transition);
							// update State objects
							targetState = t.getDest();
							regexState = r.getDest();
							// break out of loop
							break;
						}
					}
					if (found)
						break;
				}
				// if no matching transition was found, continue down the next available path
				if (!found) {
					// if there are no possible following Transitions
					if (targetState.getTransitions().isEmpty()) {
						throw new ArithmeticException("Invalid String found in intersection.");
					}
					// save next Transition
					Transition t = (Transition) targetState.getTransitions().toArray()[0];
					// update prefix and State counters
					prefix.append(t.getMin());
					stateStack.push(targetState);
					// update State objects
					regexState = regexAutomaton.getInitialState();
					targetState = t.getDest();
				}
			}
		}
	}

	/**
	 * Concatenates the given State s onto the end of the given Automaton a.
	 * 
	 * @param a - Automaton to be the prefix of the concatenation
	 * @param s - State and its contained children to be the suffix
	 * @return - Resulting Automaton after the two objects have been concatenated
	 */
	private Automaton concatState(Automaton a, State s) {
		if (a.getSingleton() == null)
			return null;
		Automaton result = Automaton.minimize(a.clone());
		if (s.getTransitions().size() == 0)
			return result;
		State state = result.getInitialState();
		while (!state.isAccept())
			state = ((Transition) state.getTransitions().toArray()[0]).getDest();
		for (Transition t : s.getTransitions())
			state.addTransition(t);
		state.setAccept(false);
		result.minimize();
		return result;
	}

	/**
	 * Checks if the two transitions have any shared character in their ranges. If
	 * they do, the first shared character is returned as a String.
	 * 
	 * @param trans1 - First transition to be checked
	 * @param trans2 - Second transition to be checked
	 * @return - Shared character if it exists. Otherwise, null
	 */
	private String getSharedTransition(Transition trans1, Transition trans2) {
		// get range of each transition as a String
		String range1 = getCharRange(trans1.getMin(), trans1.getMax());
		String range2 = getCharRange(trans2.getMin(), trans2.getMax());
		if (range1 == null || range2 == null)
			return null;
		// for each character in range 1, check if it is also contained in range2
		for (int i = 0; i < range1.length(); i++)
			if (range2.contains(range1.substring(i, i + 1)))
				// if the character at index i is contained in both strings, then return the
				// character
				return range1.substring(i, i + 1);
		// if the transitions have no shared characters, return null
		return null;
	}

	/**
	 * Gets the range between the min and max characters and returns it as a String.
	 * 
	 * @param min - Minimum char value
	 * @param max - Maximum char value
	 * @return - Range in String form if possible, otherwise null
	 */
	private String getCharRange(char min, char max) {
		if (min > max)
			return null;
		String result = "";
		while (min <= max)
			result += min++;
		return result;
	}

	/**
	 * Returns the Dot representation of this automaton.
	 * 
	 * @return - Dot representation of this automaton
	 */
	public String toDot() {
		return this.automaton.toDot();
	}
    
}
