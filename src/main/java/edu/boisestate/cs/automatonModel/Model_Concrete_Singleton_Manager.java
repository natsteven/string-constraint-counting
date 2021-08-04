package edu.boisestate.cs.automatonModel;

import java.util.Random;

import edu.boisestate.cs.Alphabet;

public class Model_Concrete_Singleton_Manager extends A_Model_Manager<Model_Concrete_Singleton> {
	

	public Model_Concrete_Singleton_Manager(Alphabet alphabet, int initialBoundLength) {
		super(alphabet, initialBoundLength);
	}

	@Override
	public Model_Concrete_Singleton createAnyString(int initialBound) {
		//create a random string with a random bound up to initialBound
		Random r = new Random();
		int length = r.nextInt(initialBound);
		String ret = "";
		while(length >= 0) {
			ret+= alphabet.randomChar();
			length--;
		}
		return new Model_Concrete_Singleton(ret);
	}

	@Override
	public Model_Concrete_Singleton createAnyString() {
		Random r = new Random();
		int length = r.nextInt(initialBoundLength);
		String ret = "";
		while(length >= 0) {
			ret+= alphabet.randomChar();
			length--;
		}
		return new Model_Concrete_Singleton(ret);
	}

	@Override
	public Model_Concrete_Singleton createAnyString(int min, int max) {
		Random r = new Random();
		int length = r.nextInt(max-min +1) + min - 1;
		String ret = "";
		while(length >= 0) {
			ret+= alphabet.randomChar();
			length--;
		}
		return new Model_Concrete_Singleton(ret);
	}

	@Override
	public Model_Concrete_Singleton createString(String string) {
		// TODO Auto-generated method stub
		return new Model_Concrete_Singleton(string);
	}

}
