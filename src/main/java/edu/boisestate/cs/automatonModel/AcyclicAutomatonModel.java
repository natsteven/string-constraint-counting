package edu.boisestate.cs.automatonModel;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;
import dk.brics.string.stringoperations.*;
import edu.boisestate.cs.Alphabet;
import edu.boisestate.cs.automatonModel.operations.*;

import java.math.BigInteger;
import java.util.Set;

public class AcyclicAutomatonModel
        extends AutomatonModel {
	
	
	private Automaton automaton;
	
	public String getAutomaton(){
		return automaton.toString();
	}

    AcyclicAutomatonModel(Automaton automaton,
                          Alphabet alphabet,
                          int boundLength) {
        super(alphabet, boundLength);

        this.automaton = automaton;
        this.modelManager =
                new AcyclicAutomatonModelManager(alphabet, boundLength);
    }

    AcyclicAutomatonModel(Automaton automaton, Alphabet alphabet) {
        super(alphabet, 0);

        this.automaton = automaton;
    }

    private static Automaton getAutomatonFromAcyclicModel(AutomatonModel model) {
        return ((AcyclicAutomatonModel)model).automaton;
    }

    @Override
    public AutomatonModel assertContainedInOther(AutomatonModel containingModel) {
        ensureAcyclicModel(containingModel);

        // get containing automaton
        Automaton containing = getAutomatonFromAcyclicModel(containingModel);

        // if either automata is  empty
        if (this.automaton.isEmpty() || containing.isEmpty()) {
            return new AcyclicAutomatonModel(BasicAutomata.makeEmpty(), this.alphabet, 0);
        }

        // get all substrings
        Automaton substrings = performUnaryOperation(containing, new Substring(), this.alphabet);

        // get resulting automaton
        Automaton result =  this.automaton.intersection(substrings);

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @Override
    public AutomatonModel assertContainsOther(AutomatonModel containedModel) {
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
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @Override
    public AutomatonModel assertEmpty() {
        // get resulting automaton
        Automaton result = this.automaton.intersection(BasicAutomata.makeEmptyString());

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result, this.alphabet, 0);
    }

    @Override
    public AutomatonModel assertEndsOther(AutomatonModel containingModel) {
        ensureAcyclicModel(containingModel);

        // get containing automaton
        Automaton containing = getAutomatonFromAcyclicModel(containingModel);

        // if either automata is  empty
        if (this.automaton.isEmpty() || containing.isEmpty()) {
            return new AcyclicAutomatonModel(BasicAutomata.makeEmpty(), this.alphabet, 0);
        }

        // get all suffixes
        Automaton suffixes = performUnaryOperation(containing, new Postfix(), this.alphabet);

        // get resulting automaton
        Automaton result =  this.automaton.intersection(suffixes);

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public AutomatonModel assertEndsWith(AutomatonModel endingModel) {
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
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @Override
    public AutomatonModel assertEquals(AutomatonModel equalModel) {
        ensureAcyclicModel(equalModel);

        // concatenate with contained automaton
        Automaton equal = getAutomatonFromAcyclicModel(equalModel);

        // get resulting automaton
        Automaton result =  this.automaton.intersection(equal);

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @Override
    public AutomatonModel assertEqualsIgnoreCase(AutomatonModel equalModel) {
        ensureAcyclicModel(equalModel);

        // concatenate with contained automaton
        Automaton equal = getAutomatonFromAcyclicModel(equalModel);
        Automaton equalIgnoreCase = performUnaryOperation(equal, new IgnoreCase(), this.alphabet);

        // get resulting automaton
        Automaton result =  this.automaton.intersection(equalIgnoreCase);

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @Override
    public AutomatonModel assertHasLength(int min, int max) {
        // check min and max
        if (min > max) {
            return new AcyclicAutomatonModel(BasicAutomata.makeEmpty(), this.alphabet, 0);
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
        return new AcyclicAutomatonModel(result, this.alphabet, newBoundLength);
    }

    @Override
    public AutomatonModel assertNotContainedInOther(AutomatonModel notContainingModel) {
        ensureAcyclicModel(notContainingModel);

        // get containing automaton
        Automaton notContaining = getAutomatonFromAcyclicModel(notContainingModel);

        // if not containing automaton is  empty
        if (notContaining.isEmpty() || automaton.isEmpty() || automaton.isEmptyString()) {
            return new AcyclicAutomatonModel(BasicAutomata.makeEmpty(), alphabet, 0);
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
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @Override
    public AutomatonModel assertNotContainsOther(AutomatonModel notContainedModel) {
        ensureAcyclicModel(notContainedModel);

        // get not contained automaton
        Automaton notContained = getAutomatonFromAcyclicModel(notContainedModel);

        // if not containing automaton is  empty
        if (notContained.isEmpty() || automaton.isEmpty()) {
            return new AcyclicAutomatonModel(BasicAutomata.makeEmpty(), alphabet, 0);
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
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @Override
    public AutomatonModel assertNotEmpty() {
        // get resulting automaton
        Automaton result = this.automaton.minus(BasicAutomata.makeEmptyString());

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @Override
    public AutomatonModel assertNotEndsOther(AutomatonModel notContainingModel) {
        ensureAcyclicModel(notContainingModel);

        // get containing automaton
        Automaton notContaining = getAutomatonFromAcyclicModel(notContainingModel);

        // if not containing automaton is  empty
        if (notContaining.isEmpty() || automaton.isEmpty() || automaton.isEmptyString()) {
            return new AcyclicAutomatonModel(BasicAutomata.makeEmpty(), alphabet, 0);
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
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public AutomatonModel assertNotEndsWith(AutomatonModel notEndingModel) {
        ensureAcyclicModel(notEndingModel);

        Automaton notEnding = getAutomatonFromAcyclicModel(notEndingModel);

        // if not containing automaton is  empty
        if (notEnding.isEmpty() || automaton.isEmpty()) {
            return new AcyclicAutomatonModel(BasicAutomata.makeEmpty(), alphabet, 0);
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
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @Override
    public AutomatonModel assertNotEquals(AutomatonModel notEqualModel) {
        ensureAcyclicModel(notEqualModel);

        // get not equal automaton
        Automaton notEqual = getAutomatonFromAcyclicModel(notEqualModel);

        // if not containing automaton is  empty
        if (notEqual.isEmpty() || automaton.isEmpty()) {
            return new AcyclicAutomatonModel(BasicAutomata.makeEmpty(), alphabet, 0);
        }

        // if not equal automaton is a singleton
        Automaton result = automaton;
        if (notEqual.getFiniteStrings(1) != null) {
            // get resulting automaton
            result = this.automaton.minus(notEqual);
        }

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @Override
    public AutomatonModel assertNotEqualsIgnoreCase(AutomatonModel notEqualModel) {
        ensureAcyclicModel(notEqualModel);

        // get not equal automaton
        Automaton notEqual = getAutomatonFromAcyclicModel(notEqualModel);

        // if not containing automaton is  empty
        if (notEqual.isEmpty() || automaton.isEmpty()) {
            return new AcyclicAutomatonModel(BasicAutomata.makeEmpty(), alphabet, 0);
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
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @Override
    public AutomatonModel assertNotStartsOther(AutomatonModel notContainingModel) {
        ensureAcyclicModel(notContainingModel);

        // get containing automaton
        Automaton notContaining = getAutomatonFromAcyclicModel(notContainingModel);

        // if not containing automaton is  empty
        if (notContaining.isEmpty() || automaton.isEmpty() || automaton.isEmptyString()) {
            return new AcyclicAutomatonModel(BasicAutomata.makeEmpty(), alphabet, 0);
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
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public AutomatonModel assertNotStartsWith(AutomatonModel notStartsModel) {
        ensureAcyclicModel(notStartsModel);

        Automaton notStarting = getAutomatonFromAcyclicModel(notStartsModel);

        // if not containing automaton is  empty
        if (notStarting.isEmpty() || automaton.isEmpty()) {
            return new AcyclicAutomatonModel(BasicAutomata.makeEmpty(), alphabet, 0);
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
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @Override
    public AutomatonModel assertStartsOther(AutomatonModel containingModel) {
        ensureAcyclicModel(containingModel);

        // get containing automaton
        Automaton containing = getAutomatonFromAcyclicModel(containingModel);

        // if either automata is  empty
        if (this.automaton.isEmpty() || containing.isEmpty()) {
            return new AcyclicAutomatonModel(BasicAutomata.makeEmpty(), this.alphabet, 0);
        }

        // get all prefixes
        Automaton prefixes = performUnaryOperation(containing, new Prefix(), this.alphabet);

        // get resulting automaton
        Automaton result =  this.automaton.intersection(prefixes);

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public AutomatonModel assertStartsWith(AutomatonModel startingModel) {
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
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public AutomatonModel clone() {
        // create new model from existing automata
        Automaton cloneAutomaton = this.automaton.clone();
        return new AcyclicAutomatonModel(cloneAutomaton,
                                         this.alphabet,
                                         this.boundLength);
    }

    @Override
    public AutomatonModel concatenate(AutomatonModel argModel) {
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
        return new AcyclicAutomatonModel(result, this.alphabet, boundLength);
    }

    @Override
    public boolean containsString(String actualValue) {
        return this.automaton.run(actualValue);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public AutomatonModel delete(int start, int end) {

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
        return new AcyclicAutomatonModel(result, this.alphabet, newBoundLength);
    }

    @Override
    public boolean equals(AutomatonModel arg) {

        // check if arg model is bounded automaton model
        if (arg instanceof AcyclicAutomatonModel) {

            // cast arg model
            AcyclicAutomatonModel argModel = (AcyclicAutomatonModel) arg;

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
    public AutomatonModel insert(int offset, AutomatonModel argModel) {
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
        return new AcyclicAutomatonModel(result, this.alphabet, newBoundLength);
    }

    @Override
    public AutomatonModel intersect(AutomatonModel arg) {
        ensureAcyclicModel(arg);

        // cast arg model
        AcyclicAutomatonModel argModel = (AcyclicAutomatonModel) arg;

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
        return new AcyclicAutomatonModel(result, this.alphabet, boundLength);
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
    public AutomatonModel replace(char find, char replace) {
        // perform operation
        Automaton result = performUnaryOperation(automaton, new Replace1(find, replace), this.alphabet);

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result,
                                         this.alphabet,
                                         this.boundLength);
    }

    @Override
    public AutomatonModel replace(String find, String replace) {

        // perform operation
        Replace6 replaceOp = new Replace6(find, replace);
        Automaton result = performUnaryOperation(automaton, replaceOp, this.alphabet);

        // determine new bound length
        int boundDiff = find.length() - replace.length();
        int newBoundLength = this.boundLength - boundDiff;

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result, this.alphabet, newBoundLength);
    }

    @Override
    public AutomatonModel replaceChar() {

        // perform operation
        Automaton result = performUnaryOperation(automaton, new Replace4(), this.alphabet);

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result,
                                         this.alphabet,
                                         this.boundLength);
    }

    @Override
    public AutomatonModel replaceFindKnown(char find) {

        // perform operation
        Automaton result = performUnaryOperation(automaton, new Replace2(find), this.alphabet);

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result,
                                         this.alphabet,
                                         this.boundLength);
    }

    @Override
    public AutomatonModel replaceReplaceKnown(char replace) {

        // perform operation
        Automaton result = performUnaryOperation(automaton, new Replace3(replace), this.alphabet);

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result,
                                         this.alphabet,
                                         this.boundLength);
    }

    @Override
    public AutomatonModel reverse() {
        // if automaton is empty
        if (this.automaton.isEmpty()) {
            return new AcyclicAutomatonModel(BasicAutomata.makeEmpty(), this.alphabet, 0);
        }

        // perform operation
        Automaton result = performUnaryOperation(automaton, new Reverse(), this.alphabet);

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result, this.alphabet, this.boundLength);
    }

    @Override
    public AutomatonModel setCharAt(int offset, AutomatonModel argModel) {
        ensureAcyclicModel(argModel);

        // get automata for operations
        Automaton arg = getAutomatonFromAcyclicModel(argModel);

        // get resulting automaton
        PreciseSetCharAt operation = new PreciseSetCharAt(offset);
        Automaton result = operation.op(automaton, arg);
        result.minimize();

        // return unbounded model from automaton
        return new AcyclicAutomatonModel(result, this.alphabet, boundLength);
    }

    @Override
    public AutomatonModel setLength(int length) {

        // add null to new alphabet
        Set<Character> symbolSet = alphabet.getSymbolSet();
        symbolSet.add('\u0000');
        Alphabet newAlphabet = new Alphabet(symbolSet);

        // get resulting automaton
        Automaton result = performUnaryOperation(automaton,
                                                 new PreciseSetLength(length),
                                                 newAlphabet);

        // return unbounded model from automaton
        return new AcyclicAutomatonModel(result, this.alphabet, length);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public AutomatonModel substring(int start, int end) {
        // get resulting automaton
        Automaton result = performUnaryOperation(automaton, new PreciseSubstring(start, end), this.alphabet);

        // get new bound length
        int newBoundLength = end - start;

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result, this.alphabet, newBoundLength);
    }

    @Override
    public AutomatonModel suffix(int start) {

        // perform operation
        Automaton result = performUnaryOperation(automaton, new PreciseSuffix(start), this.alphabet);

        // determine new bound length
        int newBoundLength = this.boundLength - start;

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result, this.alphabet, newBoundLength);
    }

    @Override
    public AutomatonModel toLowercase() {

        // perform operation
        Automaton result = performUnaryOperation(automaton, new ToLowerCase(), this.alphabet);

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result,
                                         this.alphabet,
                                         this.boundLength);
    }

    @Override
    public AutomatonModel toUppercase() {

        // perform operation
        Automaton result = performUnaryOperation(automaton, new ToUpperCase(), this.alphabet);

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result,
                                         this.alphabet,
                                         this.boundLength);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public AutomatonModel trim() {

        // perform operation
        Automaton result = performUnaryOperation(automaton, new PreciseTrim(), this.alphabet);

        // return new model from resulting automaton
        return new AcyclicAutomatonModel(result,
                                         this.alphabet,
                                         this.boundLength);
    }

    private void ensureAcyclicModel(AutomatonModel arg) {
        // check if automaton model is bounded
        if (!(arg instanceof AcyclicAutomatonModel)) {

            throw new UnsupportedOperationException(
                    "The AcyclicAutomatonModel only supports binary " +
                    "operations with other AcyclicAutomatonModels.");
        }
    }
}
