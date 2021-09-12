package edu.boisestate.cs.automatonModel;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import dk.brics.string.stringoperations.Postfix;
import dk.brics.string.stringoperations.Prefix;
import dk.brics.string.stringoperations.Replace1;
import dk.brics.string.stringoperations.Replace2;
import dk.brics.string.stringoperations.Replace3;
import dk.brics.string.stringoperations.Replace4;
import dk.brics.string.stringoperations.Replace6;
import dk.brics.string.stringoperations.Reverse;
import dk.brics.string.stringoperations.Substring;
import dk.brics.string.stringoperations.ToLowerCase;
import dk.brics.string.stringoperations.ToUpperCase;
import edu.boisestate.cs.Alphabet;
import edu.boisestate.cs.automatonModel.operations.IgnoreCase;
import edu.boisestate.cs.automatonModel.operations.PreciseDelete;
import edu.boisestate.cs.automatonModel.operations.PreciseInsert;
import edu.boisestate.cs.automatonModel.operations.PreciseSetCharAt;
import edu.boisestate.cs.automatonModel.operations.PreciseSetLength;
import edu.boisestate.cs.automatonModel.operations.PreciseSubstring;
import edu.boisestate.cs.automatonModel.operations.PreciseSuffix;
import edu.boisestate.cs.automatonModel.operations.PreciseTrim;
import edu.boisestate.cs.automatonModel.operations.StringModelCounter;
import edu.boisestate.cs.util.DotToGraph;
import edu.ucsb.cs.www.vlab.stranger.StrangerLibrary.transition;

public class Model_Bounded extends A_Model<Model_Bounded> {

	private Automaton automaton;

	public String getAutomaton() {
		return this.automaton.toString();
	}

	public Model_Bounded(Automaton automaton, Alphabet alphabet, int initialBoundLength) {
		super(alphabet, initialBoundLength);

		// set automaton from parameter
		this.automaton = automaton;

		this.modelManager = new Model_Bounded_Manager(alphabet, initialBoundLength);
	}

	@Override
	public Model_Bounded assertContainedInOther(Model_Bounded containingModel) {
		ensureUnboundedModel(containingModel);

		// get containing automaton
		Automaton containing = getAutomatonFromUnboundedModel(containingModel);

		// if either automata is empty
		if (this.automaton.isEmpty() || containing.isEmpty()) {
			return new Model_Bounded(BasicAutomata.makeEmpty(), this.alphabet, 0);
		}

		// get all substrings
		Automaton substrings = performUnaryOperation(containing, new Substring(), this.alphabet);

		// get resulting automaton
		Automaton result = this.automaton.intersection(substrings);

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	private static Automaton getAutomatonFromUnboundedModel(Model_Bounded model) {
		return ((Model_Bounded) model).automaton;
	}

	private void ensureUnboundedModel(Model_Bounded arg) {
		// check if automaton model is unbounded
		if (!(arg instanceof Model_Bounded)) {

			throw new UnsupportedOperationException(
					"The Model_Bounded only supports binary " + "operations with other Model_Boundeds.");
		}
	}

	@Override
	public Model_Bounded assertContainsOther(Model_Bounded containedModel) {
		ensureUnboundedModel(containedModel);

		// create any string automata
		Automaton anyString1 = BasicAutomata.makeCharSet(this.alphabet.getCharSet()).repeat();
		Automaton anyString2 = BasicAutomata.makeCharSet(this.alphabet.getCharSet()).repeat();

		// concatenate with contained automaton
		Automaton contained = getAutomatonFromUnboundedModel(containedModel);
		Automaton x = anyString1.concatenate(contained).concatenate(anyString2);

		// get resulting automaton
		Automaton result = this.automaton.intersection(x);
		result.minimize();

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded assertEmpty() {
		// get resulting automaton
		Automaton result = this.automaton.intersection(BasicAutomata.makeEmptyString());

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, 0);
	}

	@Override
	public Model_Bounded assertEndsOther(Model_Bounded containingModel) {
		ensureUnboundedModel(containingModel);

		// get containing automaton
		Automaton containing = getAutomatonFromUnboundedModel(containingModel);

		// if either automata is empty
		if (this.automaton.isEmpty() || containing.isEmpty()) {
			return new Model_Bounded(BasicAutomata.makeEmpty(), this.alphabet, 0);
		}

		// get all suffixes
		Automaton suffixes = performUnaryOperation(containing, new Postfix(), this.alphabet);

		// get resulting automaton
		Automaton result = this.automaton.intersection(suffixes);

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded assertEndsWith(Model_Bounded endingModel) {
		ensureUnboundedModel(endingModel);

		// create any string automata
		Automaton anyString = BasicAutomata.makeCharSet(this.alphabet.getCharSet()).repeat();

		// concatenate with ending automaton
		Automaton end = getAutomatonFromUnboundedModel(endingModel);
		Automaton x = anyString.concatenate(end);

		// get resulting automaton
		Automaton result = this.automaton.intersection(x);
		result.minimize();

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded assertEquals(Model_Bounded equalModel) {
		ensureUnboundedModel(equalModel);

		// get equal automaton
		Automaton equal = getAutomatonFromUnboundedModel(equalModel);

		// get resulting automaton
		Automaton result = this.automaton.intersection(equal);

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded assertEqualsIgnoreCase(Model_Bounded equalModel) {
		ensureUnboundedModel(equalModel);

		// get equal automaton ignoring case
		Automaton equal = getAutomatonFromUnboundedModel(equalModel);
		Automaton equalIgnoreCase = performUnaryOperation(equal, new IgnoreCase(), this.alphabet);

		// get resulting automaton
		Automaton result = this.automaton.intersection(equalIgnoreCase);

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded assertNotContainedInOther(Model_Bounded notContainingModel) {
		ensureUnboundedModel(notContainingModel);

		// get not containing automaton
		Automaton notContaining = getAutomatonFromUnboundedModel(notContainingModel);

		// if not containing automaton is empty
		if (notContaining.isEmpty() || automaton.isEmpty() || automaton.isEmptyString()) {
			return new Model_Bounded(BasicAutomata.makeEmpty(), alphabet, boundLength);
		}

		// get automaton of required chars from not containing automaton
		notContaining = getRequiredCharAutomaton(notContaining, alphabet, boundLength);

		Automaton result = automaton;
		if (!notContaining.isEmpty()) {

			// get all substrings
			Automaton substrings = performUnaryOperation(notContaining, new Substring(), this.alphabet);

			// get resulting automaton
			result = this.automaton.minus(substrings);
		}

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded assertNotContainsOther(Model_Bounded notContainedModel) {
		ensureUnboundedModel(notContainedModel);

		// get not contained automaton
		Automaton notContained = getAutomatonFromUnboundedModel(notContainedModel);

		// if not containing automaton is empty
		if (notContained.isEmpty() || automaton.isEmpty()) {
			return new Model_Bounded(BasicAutomata.makeEmpty(), alphabet, 0);
		}

		notContained = getRequiredCharAutomaton(notContained, alphabet, boundLength);

		Automaton result = automaton;
		if (!notContained.isEmpty()) {

			// create any string automata
			Automaton anyString1 = BasicAutomata.makeCharSet(this.alphabet.getCharSet()).repeat();
			Automaton anyString2 = BasicAutomata.makeCharSet(this.alphabet.getCharSet()).repeat();

			// concatenate with not contained automaton
			Automaton x = anyString1.concatenate(notContained).concatenate(anyString2);

			// get resulting automaton
			result = automaton.minus(x);
		}

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded assertNotEmpty() {
		// get resulting automaton
		Automaton result = this.automaton.minus(BasicAutomata.makeEmptyString());

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded assertNotEndsOther(Model_Bounded notContainingModel) {
		ensureUnboundedModel(notContainingModel);

		// get not containing automaton
		Automaton notContaining = getAutomatonFromUnboundedModel(notContainingModel);

		// if not containing automaton is empty
		if (notContaining.isEmpty() || automaton.isEmpty() || automaton.isEmptyString()) {
			return new Model_Bounded(BasicAutomata.makeEmpty(), alphabet, boundLength);
		}

		// get automaton of required chars from not containing automaton
		notContaining = getRequiredCharAutomaton(notContaining, alphabet, boundLength);

		Automaton result = automaton;
		if (!notContaining.isEmpty()) {

			// get all suffixes
			Automaton suffixes = performUnaryOperation(notContaining, new Postfix(), this.alphabet);

			// get resulting automaton
			result = this.automaton.minus(suffixes);
		}

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);

	}

	@Override
	public Model_Bounded assertNotEndsWith(Model_Bounded notEndingModel) {
		ensureUnboundedModel(notEndingModel);

		Automaton notEnding = getAutomatonFromUnboundedModel(notEndingModel);

		// if not containing automaton is empty
		if (notEnding.isEmpty() || automaton.isEmpty()) {
			return new Model_Bounded(BasicAutomata.makeEmpty(), alphabet, 0);
		}

		notEnding = getRequiredCharAutomaton(notEnding, alphabet, boundLength);

		Automaton result = automaton;
		if (!notEnding.isEmpty()) {

			// create any string automata
			Automaton anyString = BasicAutomata.makeCharSet(this.alphabet.getCharSet()).repeat();

			// concatenate with not ending automaton
			Automaton x = anyString.concatenate(notEnding);

			// get resulting automaton
			result = this.automaton.minus(x);
		}

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded assertNotEquals(Model_Bounded notEqualModel) {
		ensureUnboundedModel(notEqualModel);

		// get not equal automaton
		Automaton notEqual = getAutomatonFromUnboundedModel(notEqualModel);

		// if not containing automaton is empty
		if (notEqual.isEmpty() || automaton.isEmpty()) {
			return new Model_Bounded(BasicAutomata.makeEmpty(), alphabet, 0);
		}

		// if not equal automaton is a singleton
		Automaton result = automaton;
		if (notEqual.getFiniteStrings(1) != null) {

			// get resulting automaton
			result = this.automaton.minus(notEqual);
		}

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded assertNotEqualsIgnoreCase(Model_Bounded notEqualModel) {
		ensureUnboundedModel(notEqualModel);

		// get not equal automaton
		Automaton notEqual = getAutomatonFromUnboundedModel(notEqualModel);

		// if not containing automaton is empty
		if (notEqual.isEmpty() || automaton.isEmpty()) {
			return new Model_Bounded(BasicAutomata.makeEmpty(), alphabet, 0);
		}

		// if not equal automaton is a singleton
		Automaton result = automaton;
		if (notEqual.getFiniteStrings(1) != null) {
			Automaton equalIgnoreCase = performUnaryOperation(notEqual, new IgnoreCase(), this.alphabet);

			// get resulting automaton
			result = this.automaton.minus(equalIgnoreCase);
		}

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded assertNotStartsOther(Model_Bounded notContainingModel) {
		ensureUnboundedModel(notContainingModel);

		// get not containing automaton
		Automaton notContaining = getAutomatonFromUnboundedModel(notContainingModel);

		// if not containing automaton is empty
		if (notContaining.isEmpty() || automaton.isEmpty() || automaton.isEmptyString()) {
			return new Model_Bounded(BasicAutomata.makeEmpty(), alphabet, boundLength);
		}

		// get automaton of required chars from not containing automaton
		notContaining = getRequiredCharAutomaton(notContaining, alphabet, boundLength);

		Automaton result = automaton;
		if (!notContaining.isEmpty()) {

			// get all prefixes
			Automaton prefixes = performUnaryOperation(notContaining, new Prefix(), this.alphabet);

			// get resulting automaton
			result = this.automaton.minus(prefixes);
		}

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded assertNotStartsWith(Model_Bounded notStartsModel) {
		ensureUnboundedModel(notStartsModel);

		Automaton notStarting = getAutomatonFromUnboundedModel(notStartsModel);

		// if not containing automaton is empty
		if (notStarting.isEmpty() || automaton.isEmpty()) {
			return new Model_Bounded(BasicAutomata.makeEmpty(), alphabet, 0);
		}

		notStarting = getRequiredCharAutomaton(notStarting, alphabet, boundLength);

		Automaton result = automaton;
		if (!notStarting.isEmpty()) {
			// create any string automata
			Automaton anyString = BasicAutomata.makeCharSet(this.alphabet.getCharSet()).repeat();

			// concatenate with not starts automaton
			Automaton x = notStarting.concatenate(anyString);

			// get resulting automaton
			result = this.automaton.minus(x);
		}

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded assertStartsOther(Model_Bounded containingModel) {
		ensureUnboundedModel(containingModel);

		// get containing automaton
		Automaton containing = getAutomatonFromUnboundedModel(containingModel);

		// if either automata is empty
		if (this.automaton.isEmpty() || containing.isEmpty()) {
			return new Model_Bounded(BasicAutomata.makeEmpty(), this.alphabet, 0);
		}

		// get all prefixes
		Automaton prefixes = performUnaryOperation(containing, new Prefix(), this.alphabet);

		// get resulting automaton
		Automaton result = this.automaton.intersection(prefixes);

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded assertStartsWith(Model_Bounded startingModel) {
		ensureUnboundedModel(startingModel);

		// create any string automata
		Automaton anyString = BasicAutomata.makeCharSet(this.alphabet.getCharSet()).repeat();

		// concatenate with contained automaton
		Automaton start = getAutomatonFromUnboundedModel(startingModel);
		Automaton x = start.concatenate(anyString);

		// get resulting automaton
		Automaton result = this.automaton.intersection(x);
		result.minimize();

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded clone() {

		// create new model from existing automata
		Automaton cloneAutomaton = this.automaton.clone();
		return new Model_Bounded(cloneAutomaton, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded concatenate(Model_Bounded argModel) {

		ensureUnboundedModel(argModel);

		// get arg automaton
		Automaton arg = getAutomatonFromUnboundedModel(argModel);

		// get concatenation of automata
		Automaton result = this.automaton.concatenate(arg);

		// minimize result automaton
		result.minimize();

		// calculate new bound length
		int boundLength = this.boundLength + argModel.boundLength;

		// return unbounded model from automaton
		return new Model_Bounded(result, this.alphabet, boundLength);
	}

	@Override
	public boolean containsString(String actualValue) {
		return this.automaton.run(actualValue);
	}

	@Override
	public Model_Bounded delete(int start, int end) {

		// perform operation
		// System.out.println("start " + start + " end " + end +"\n Old \n" +
		// automaton);
		Automaton result = performUnaryOperation(automaton, new PreciseDelete(start, end), this.alphabet);

		// determine new bound length
		int newBoundLength;
		if (this.boundLength < start) {
			// automaton should already be empty after delete operation
			// algorithm
			newBoundLength = 0;
		} else if (this.boundLength < end) {
			newBoundLength = start;
		} else {
			int charsDeleted = end - start;
			newBoundLength = this.boundLength - charsDeleted;
		}
		// System.out.println("new bound length " + newBoundLength + " vs " +
		// boundLength + " " + result.toString());
		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, newBoundLength);
	}

	@Override
	public boolean equals(Model_Bounded argModel) {

		// check if arg model is unbounded automaton model
		if (argModel instanceof Model_Bounded) {

			// get arg automaton
			Automaton arg = getAutomatonFromUnboundedModel(argModel);

			// check underlying automaton models for equality
			return this.automaton.equals(arg);
		}

		return false;
	}

	@Override
	public String getAcceptedStringExample() {
		return this.automaton.getShortestExample(true);
	}

	@Override
	public Set<String> getFiniteStrings() {

		// get bounding automaton from current bound length
		int length = this.getBoundLength();
		Automaton bounding = BasicAutomata.makeAnyChar().repeat(0, length);

		// get bounded automaton
		Automaton bounded = automaton.intersection(bounding);

		// return finite strings from bounded automaton
		return bounded.getFiniteStrings();
	}

	@Override
	public Model_Bounded insert(int offset, Model_Bounded argModel) {
		ensureUnboundedModel(argModel);

		// get automata for operations
		Automaton arg = getAutomatonFromUnboundedModel(argModel);

		// get resulting automaton
		PreciseInsert insert = new PreciseInsert(offset);
		Automaton result = insert.op(automaton, arg);
		result.minimize();

		// calculate new bound length
		int newBoundLength = this.boundLength + argModel.boundLength;

		// return unbounded model from automaton
		return new Model_Bounded(result, this.alphabet, newBoundLength);
	}

	@Override
	public boolean isEmpty() {
		/* eas 10-31-18 why, why is EmptyString() ??? */
		// return this.automaton.isEmptyString();
		// the correct code
		return this.automaton.isEmpty();
	}

	@Override
	public boolean isSingleton() {

		// get one finite string, null if more
		Set<String> strings = this.automaton.getFiniteStrings(1);

		// return if single non-null string in automaton
		return strings != null && strings.size() == 1 && strings.iterator().next() != null;
	}

	@Override
	public BigInteger modelCount() {

		// get bound length from model
		int length = this.boundLength;

		// return model count of automaton
		return StringModelCounter.ModelCount(automaton, length);
	}

	@Override
	public Model_Bounded replace(char find, char replace) {

		// perform operation
		Automaton result = performUnaryOperation(automaton, new Replace1(find, replace), this.alphabet);

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded replace(String find, String replace) {

		// perform operation
		Automaton result = performUnaryOperation(automaton, new Replace6(find, replace), this.alphabet);

		// determine new bound length
		int boundDiff = find.length() - replace.length();
		int newBoundLength = this.boundLength - boundDiff;

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, newBoundLength);
	}

	@Override
	public Model_Bounded replaceChar() {

		// perform operation
		Automaton result = performUnaryOperation(automaton, new Replace4(), this.alphabet);

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded replaceFindKnown(char find) {

		// perform operation
		Automaton result = performUnaryOperation(automaton, new Replace2(find), this.alphabet);

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded replaceReplaceKnown(char replace) {

		// perform operation
		Automaton result = performUnaryOperation(automaton, new Replace3(replace), this.alphabet);

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.getBoundLength());
	}

	@Override
	public Model_Bounded reverse() {

		// perform operation
		Automaton result = performUnaryOperation(automaton, new Reverse(), this.alphabet);

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded setCharAt(int offset, Model_Bounded argModel) {
		ensureUnboundedModel(argModel);

		// get automata for operations
		Automaton arg = getAutomatonFromUnboundedModel(argModel);

		// get resulting automaton
		PreciseSetCharAt operation = new PreciseSetCharAt(offset);
		Automaton result = operation.op(automaton, arg);
		result.minimize();

		// return unbounded model from automaton
		return new Model_Bounded(result, this.alphabet, boundLength);
	}

	@Override
	public Model_Bounded setLength(int length) {

		// add null to new alphabet
		Set<Character> symbolSet = alphabet.getSymbolSet();
		symbolSet.add('\u0000');
		Alphabet newAlphabet = new Alphabet(symbolSet);

		// get resulting automaton
		Automaton result = performUnaryOperation(automaton, new PreciseSetLength(length), newAlphabet);

		// return unbounded model from automaton
		return new Model_Bounded(result, this.alphabet, length);
	}

	@Override
	public Model_Bounded substring(int start, int end) {
		// get resulting automaton
		Automaton result = performUnaryOperation(automaton, new PreciseSubstring(start, end), this.alphabet);

		// get new bound length
		int newBoundLength = end - start;

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, newBoundLength);
	}

	@Override
	public Model_Bounded suffix(int start) {

		// perform operation
		Automaton result = performUnaryOperation(automaton, new PreciseSuffix(start), this.alphabet);

		// determine new bound length
		int boundLength = this.getBoundLength() - start;

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, boundLength);
	}

	@Override
	public Model_Bounded toLowercase() {

		// perform operation
		Automaton result = performUnaryOperation(automaton, new ToLowerCase(), this.alphabet);

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded toUppercase() {

		// perform operation
		Automaton result = performUnaryOperation(automaton, new ToUpperCase(), this.alphabet);

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded trim() {

		// perform operation
		Automaton result = performUnaryOperation(automaton, new PreciseTrim(), this.alphabet);

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	@Override
	public Model_Bounded assertHasLength(int min, int max) {
		// check min and max
		if (min > max) {
			return new Model_Bounded(BasicAutomata.makeEmpty(), this.alphabet, 0);
		}

		// get any string with length between min and max
		Automaton minMax = BasicAutomata.makeCharSet(this.alphabet.getCharSet()).repeat(min, max);

		// get resulting automaton
		Automaton result = this.automaton.intersection(minMax);

		// get new bound length
		int newBoundLength = max;
		if (this.boundLength < max) {
			newBoundLength = this.boundLength;
		}

		// return new model from resulting automaton
		return new Model_Bounded(result, this.alphabet, newBoundLength);
	}

	@Override
	public Model_Bounded intersect(Model_Bounded argModel) {
		ensureUnboundedModel(argModel);

		// get arg automaton
		Automaton arg = getAutomatonFromUnboundedModel(argModel);
		System.out.println("this + " + automaton + " arg " + arg);
		// get intersection of automata
		Automaton result = this.automaton.intersection(arg);
		System.out.println("res + " + result);
		// minimize result automaton
		result.minimize();

		// calculate new bound length
		int boundLength = this.boundLength;
		if (argModel.boundLength < this.boundLength) {
			boundLength = argModel.boundLength;
		}

		// return unbounded model from automaton
		return new Model_Bounded(result, this.alphabet, boundLength);
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
	 * String.replaceAll(regex, String). Convert this String into an automaton, then
	 * union said automaton with the result automaton. Return the final automaton.
	 * 
	 * @param regexString - String storing the regex to be used
	 * @param replacement - String to replace occurrences of the regex
	 * @return - Model_Bounded object containing the resulting automaton
	 */
	public Model_Bounded replaceAll(String regexString, String replacement) {
		// get the set of all possible finite solutions for the automaton
		Set<String> solutions = this.automaton.getFiniteStrings();
		// if the automaton as an infinite language, return the unmodified automaton
		if (solutions == null)
			return new Model_Bounded(this.automaton, this.alphabet, this.boundLength);
		// start with an empty automaton
		Automaton result = Automaton.makeEmpty();
		// for each solution, replace all occurrence of the regex and concatenate
		// it with the result automaton
		for (String str : solutions) {
			Automaton a = BasicAutomata.makeString(str.replaceAll(regexString, replacement));
			if (result.isEmpty())
				result = a;
			else
				result = result.union(a);
		}
		result.minimize();
		// return the result automaton in a Model_Bounded wrapper
		return new Model_Bounded(result, this.alphabet, this.boundLength);
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
	 * @return - Model_Bounded object containing the resulting automaton
	 */
	public Model_Bounded replaceFirst(String regexString, String replacement) {
		// get the set of all possible finite solutions for the automaton
		Set<String> solutions = this.automaton.getFiniteStrings();
		// if the automaton as an infinite language, return the unmodified automaton
		if (solutions == null)
			return new Model_Bounded(this.automaton, this.alphabet, this.boundLength);
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
		// return the result automaton in a Model_Bounded wrapper
		return new Model_Bounded(result, this.alphabet, this.boundLength);
	}

	/**
	 * Given a regex and replacement String, perform a depth first search on the
	 * target automaton. Method searches for a string which both satisfies the
	 * target automaton, as well as contains a substring which satisfies the regex.
	 * Upon discovery, replace the first occurrence of said substring with the
	 * target replacement String. Repeat this process until no further matches are
	 * found.
	 * 
	 * @param regexString       - Regex to be found in the target Automaton
	 * @param replacementString - String to replace the regex substring
	 * @return - Modfied clone of target Automaton
	 */
	public Model_Bounded replaceFirstOptimized(String regexString, String replacementString) {
		// clone the target automaton
		Automaton targetAutomaton = this.automaton.clone();
		// minimize the target automaton
		targetAutomaton.minimize();
		// initialize regex automaton
		Automaton regexAutomaton = new RegExp(regexString).toAutomaton();
		// set of all previously replaced or solution strings
		Set<String> stringsToIgnore = new HashSet<String>();
		do {
			// find a string which contains a substring satisfying the regex
			String solution = findConcreteString(targetAutomaton.getInitialState(), regexAutomaton.getInitialState(),
					"", new HashMap<Integer, Integer>(), false, stringsToIgnore);
			// if no solution is found, break out of the loop
			if (solution == null)
				break;
			// account for the period at the end of the solution
			solution = solution.substring(0, solution.length() - 1);
			// add the solution and replaced string to stringsToIgnore for future loops
			stringsToIgnore.add(solution);
			stringsToIgnore.add(solution.replaceFirst(regexString, replacementString));
			// create singleton automaton from result of solution.replaceFirst
			Automaton modifiedString = BasicAutomata.makeString(solution.replaceFirst(regexString, replacementString));
			// remove solution string from original automaton
			targetAutomaton = targetAutomaton.minus(new RegExp(solution).toAutomaton());
			// add the modified string back to the target automaton
			targetAutomaton = targetAutomaton.union(modifiedString);
			// minimize the target automaton
			targetAutomaton.minimize();
		} while (true);
		return new Model_Bounded(targetAutomaton, this.alphabet, this.boundLength);
	}

	public Model_Bounded replaceFirstMoreOptimized(String regexString, String replacementString)
			throws IllegalArgumentException {
		// initialize automata
		Automaton targetAutomaton = Automaton.minimize(this.automaton.clone());
		Automaton regexAutomaton = new RegExp(regexString).toAutomaton();
		Automaton modifiedAutomaton = Automaton.makeEmpty();
		// initialize States
		State targetState = targetAutomaton.getInitialState();
		State regexState = regexAutomaton.getInitialState();
		// initialize Stack
		Stack<State> stateStack = new Stack<State>();
		// initialize prefixString
		String prefixString = "";
		while (true) {
			// if we have already visited the targetState, then the Automaton is cyclic
			if (stateStack.contains(targetState))
				throw new IllegalArgumentException("Cannot run cyclic automata in Model_Bounded");
			// if the current prefixString satisfies the regex
			if (regexState.isAccept()) {
				// save the prefix in tempAutomaton and concat prefix with all possible suffixes
				Automaton tempAutomaton = Automaton.makeString(prefixString);
				tempAutomaton = concatState(tempAutomaton, targetState);
				// union the resulting automaton with modifiedAutomaton and remove the old one from targetAutomaton
				modifiedAutomaton = modifiedAutomaton.union(tempAutomaton);
				targetAutomaton.minus(tempAutomaton);
				// reset target and regex States
				targetState = targetAutomaton.getInitialState();
				regexState = regexAutomaton.getInitialState();
				stateStack.clear();
			} else {
				String transition = "";
				// else check if any of the next target transitions match the regex
				for (Transition t : targetState.getTransitions()) {
					for (Transition r : regexState.getTransitions()) {
						String shared = getSharedTransition(r, t);
						if (shared != null) {
							transition = shared;
						}
					}
				}
				// if there is a transition next which matches
				if (transition != null) {
					stateStack.push(targetState);
					prefixString = prefixString.concat(transition);
					// TODO: handle going to next state with matching transition
				} else {
					// if there are no next transitions which match the regex
					// TODO: this
				}
			}
		}
		return new Model_Bounded(targetAutomaton, this.alphabet, this.boundLength);
	}

	private Automaton concatState(Automaton a, State s) {
		if (a.getSingleton() == null)
			return null;
		Automaton result = Automaton.minimize(a.clone());
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
	 * Finds a concrete string which matches the regex. In essence, finds a symbolic
	 * string (the regex) inside of another symbolic string (the target automata)
	 * and returns a shared concrete string.
	 * 
	 * Steps through individual states and transitions in both model and regex
	 * automaton to find matches between the two and account for transition ranges.
	 * 
	 * Initial call MUST start with str = "", regexSolutionUpstream = false, and
	 * visitedStates as an empty HashMap.
	 * 
	 * @param targetState           - Current state of target automata
	 * @param regexState            - Current state of regex automata
	 * @param str                   - Current accepted string value
	 * @param visitedStates         - HashMap of all visited states in the current
	 *                              iteration or branch
	 * @param regexSolutionUpstream - True if an upstream iteration has already
	 *                              reached an accepted regex state. Used to avoid
	 *                              loops
	 * @param replacedStrings       - Set containing all strings which have already
	 *                              been removed and replaced
	 * @param replacementStrings    - Set containing all strings which have been
	 *                              created via replaceFirst
	 * @return - Concrete string which satisfies the target automaton and contains a
	 *         substring which satisfies the regex. Returns "." at the end of any
	 *         solution. Returns null if there is no solution.
	 */
	public String findConcreteString(State targetState, State regexState, String str,
			HashMap<Integer, Integer> visitedStates, boolean regexSolutionUpstream, Set<String> stringsToIgnore)
			throws IllegalArgumentException {
		// if the targetState has been visited before, this means there is a cycle.
		// Throw an exception.
		if (visitedStates.get(targetState.hashCode()) != null && visitedStates.get(targetState.hashCode()) == 1)
			throw new IllegalArgumentException("Cannot run cyclic automata in Model_Bounded");
		// account for possibility of automata accepting the empty string
		if (str.equals("") && targetState.isAccept() && regexState.isAccept() && !stringsToIgnore.contains(str))
			return ".";
		// if the end of the target automata has been reached and a regex solution has
		// been found upstream, return "."
		if (regexSolutionUpstream && targetState.isAccept()) {
			if (stringsToIgnore.contains(str)) {
				if (targetState.getTransitions().isEmpty())
					return null;
			} else
				return ".";
		} else if (targetState.getTransitions().isEmpty())
			return null;
		// for each transition, check if its destination state has been visited,
		// or if it has already been visited twice
		for (Transition targetTrans : targetState.getTransitions()) {
			int targetHashCode = targetTrans.getDest().hashCode();
			Integer value = visitedStates.get(targetHashCode);
			if (value == null || value != 2) {
				// for each next regex transition, if the target transition is a subset of the
				// regex transition
				for (Transition regexTrans : regexState.getTransitions()) {
					// if there exists a transition value shared between the two transitions
					String sharedTransition = getSharedTransition(targetTrans, regexTrans);
					if (sharedTransition != null) {
						// call method on itself for next iteration
						String downstreamResult = findConcreteString(targetTrans.getDest(), regexTrans.getDest(),
								str + sharedTransition, visitState(targetState, visitedStates),
								regexSolutionUpstream || regexTrans.getDest().isAccept(), stringsToIgnore);
						// if recursive call returns a value, then a solution has been found. Return
						// the shared transition + this solution to the top
						if (downstreamResult != null)
							if (downstreamResult == ".") {
								// if the current solution has already been found before, return null and ignore
								// it
								if (!stringsToIgnore.contains(str + sharedTransition)
										&& !stringsToIgnore.contains(str + sharedTransition))
									return str + sharedTransition + downstreamResult;
							} else
								return downstreamResult;
					}
				}
			}
		}
		// if str is null, keep exploring any possibilities
		// for each possible transition, recursively call this method on the next state
		for (Transition targetTrans : targetState.getTransitions()) {
			// to account for ranged OR statements, loop through the entire range of
			// transition characters
			for (char c = targetTrans.getMin(); c <= targetTrans.getMax(); c++) {
				String downstreamResult = findConcreteString(targetTrans.getDest(), regexState, str + c,
						visitState(targetState, visitedStates), regexSolutionUpstream || regexState.isAccept(),
						stringsToIgnore);
				// if recursive call returns a value, then a solution has been found. Return
				// this transition + this solution to the top
				if (downstreamResult != null)
					if (downstreamResult == ".") {
						// if the current solution has already been found before, return null and ignore
						// it
						if (!stringsToIgnore.contains(str + c) && !stringsToIgnore.contains(str + c))
							return str + c + downstreamResult;
					} else
						return downstreamResult;
			}
		}
		// If this point has been reached in the method, it means there are no possible
		// solutions downstream from the current state. Return null to signify that this
		// path is dead
		return null;
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
	 * Returns an updates version of visitedStates to reflect visiting the current
	 * state. Returns null if the current state has already been visited twice.
	 * 
	 * @param state         - State to be visited
	 * @param visitedStates - HashMap of already visited states to be updated
	 * @return - Updated HashMap of visited states
	 */
	private static HashMap<Integer, Integer> visitState(State state, HashMap<Integer, Integer> visitedStates) {
		// if state has not already been visited twice
		if (visitedStates.get(state.hashCode()) == null || visitedStates.get(state.hashCode()) < 2) {
			// copy all entries from visitedStates to updatedVisitedStates
			HashMap<Integer, Integer> updatedVisitedStates = new HashMap<Integer, Integer>();
			Set<Entry<Integer, Integer>> entries = visitedStates.entrySet();
			for (Entry<Integer, Integer> entry : entries) {
				updatedVisitedStates.put(entry.getKey(), entry.getValue());
			}
			// add/increment state to be visited in the updated hash map
			if (updatedVisitedStates.get(state.hashCode()) == null)
				updatedVisitedStates.put(state.hashCode(), 1);
			else
				updatedVisitedStates.put(state.hashCode(), 2);
			return updatedVisitedStates;
		} else {
			// if the state has already been visited twice, it cannot be visited again.
			// return null
			return null;
		}
	}

	public String toDot() {
		return this.automaton.toDot();
	}
}
