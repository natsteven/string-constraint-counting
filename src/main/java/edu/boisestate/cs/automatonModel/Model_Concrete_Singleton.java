package edu.boisestate.cs.automatonModel;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import edu.boisestate.cs.Alphabet;

public class Model_Concrete_Singleton extends A_Model<Model_Concrete_Singleton > {
	
	private String singleton;
	
	protected Model_Concrete_Singleton(Alphabet alphabet, int initialBoundLength) {
		super(alphabet, initialBoundLength);
	}
	
	protected Model_Concrete_Singleton(String string) {
		super(new Alphabet(string==null?"": string), string==null?0:string.length());
		singleton = string;
		
	}
	
	public String getSingleton() {
		return singleton;
	}

	@Override
	public String getAcceptedStringExample() {
		// TODO Auto-generated method stub
		return singleton;
	}

	@Override
	public Set<String> getFiniteStrings() {
		// TODO Auto-generated method stub
		Set<String> ret = new HashSet<String>();
		ret.add(singleton);
		return ret;
	}

	@Override
	public boolean isEmpty() {
		//must always have a concrete string, even the string is empty ""
		return singleton==null? true : false;
	}

	@Override
	public boolean isSingleton() {
		// Singleton solver always operates only on one single string
		return true; //singleton==null? false : true;
	}

	@Override
	public Model_Concrete_Singleton assertContainedInOther(Model_Concrete_Singleton containingModel) {
	
		return containingModel.singleton!=null && containingModel.singleton.contains(singleton)? new Model_Concrete_Singleton(singleton): new Model_Concrete_Singleton(null);
	}

	@Override
	public Model_Concrete_Singleton assertContainsOther(Model_Concrete_Singleton containedModel) {
		
		return singleton != null && singleton.contains(containedModel.getAutomaton())? new Model_Concrete_Singleton(singleton): new Model_Concrete_Singleton(null);
	}

	@Override
	public Model_Concrete_Singleton assertEmpty() {
		return singleton != null && singleton.isEmpty()? new Model_Concrete_Singleton(singleton): new Model_Concrete_Singleton(null);
	}

	@Override
	public Model_Concrete_Singleton assertEndsOther(Model_Concrete_Singleton baseModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton assertEndsWith(Model_Concrete_Singleton endingModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton assertEquals(Model_Concrete_Singleton equalModel) {
		return singleton.equals(equalModel.singleton)? new Model_Concrete_Singleton(singleton): new Model_Concrete_Singleton(null);
	}

	@Override
	public Model_Concrete_Singleton assertEqualsIgnoreCase(Model_Concrete_Singleton equalModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton assertHasLength(int min, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton assertNotContainedInOther(Model_Concrete_Singleton notContainingModel) {
		//System.out.println("notContaining");
		return notContainingModel.singleton!=null && !notContainingModel.singleton.contains(singleton)? new Model_Concrete_Singleton(singleton): new Model_Concrete_Singleton(null);
	}

	@Override
	public Model_Concrete_Singleton assertNotContainsOther(Model_Concrete_Singleton notContainedModel) {
		//System.out.println("notContains");
		return singleton!=null && !singleton.contains(notContainedModel.getAutomaton())? new Model_Concrete_Singleton(singleton): new Model_Concrete_Singleton(null);
	}

	@Override
	public Model_Concrete_Singleton assertNotEmpty() {
		return singleton != null && !singleton.isEmpty()? new Model_Concrete_Singleton(singleton): new Model_Concrete_Singleton(null);
	}

	@Override
	public Model_Concrete_Singleton assertNotEndsOther(Model_Concrete_Singleton notEndingModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton assertNotEndsWith(Model_Concrete_Singleton notEndingModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton assertNotEquals(Model_Concrete_Singleton notEqualModel) {
		// TODO Auto-generated method stub
		return !singleton.equals(notEqualModel.singleton)? new Model_Concrete_Singleton(singleton): new Model_Concrete_Singleton(null);
	}

	@Override
	public Model_Concrete_Singleton assertNotEqualsIgnoreCase(Model_Concrete_Singleton notEqualModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton assertNotStartsOther(Model_Concrete_Singleton notStartingModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton assertNotStartsWith(Model_Concrete_Singleton notStartsModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton assertStartsOther(Model_Concrete_Singleton startingModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton assertStartsWith(Model_Concrete_Singleton startingModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton concatenate(Model_Concrete_Singleton arg) {
		//System.out.println("concat");
		return new Model_Concrete_Singleton(singleton.concat(arg.singleton));
	}

	@Override
	public boolean containsString(String actualValue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Model_Concrete_Singleton delete(int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Model_Concrete_Singleton arg) {
		return singleton.equals(arg.singleton);
	}

	@Override
	public Model_Concrete_Singleton intersect(Model_Concrete_Singleton arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton insert(int offset, Model_Concrete_Singleton argModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger modelCount() {
		// TODO Auto-generated method stub
		return singleton == null? BigInteger.valueOf(0) : BigInteger.valueOf(1);
	}

	@Override
	public Model_Concrete_Singleton replace(char find, char replace) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton replace(String find, String replace) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton replaceChar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton replaceFindKnown(char find) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton replaceReplaceKnown(char replace) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton reverse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton substring(int start, int end) {
		
		return new Model_Concrete_Singleton(singleton.substring(start, end));
	}

	@Override
	public Model_Concrete_Singleton setCharAt(int offset, Model_Concrete_Singleton argModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton setLength(int length) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton suffix(int start) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model_Concrete_Singleton toLowercase() {
		//System.out.println("toLowerCase");
		return new Model_Concrete_Singleton(singleton.toLowerCase());
	}

	@Override
	public Model_Concrete_Singleton toUppercase() {
		return new Model_Concrete_Singleton(singleton.toUpperCase());
	}

	@Override
	public Model_Concrete_Singleton trim() {
		return new Model_Concrete_Singleton(singleton.trim());
	}

	@Override
	public Model_Concrete_Singleton clone() {
		return new Model_Concrete_Singleton(singleton);
	}

	@Override
	public String getAutomaton() {
		return singleton;
	}
	

}
