package edu.boisestate.cs.automatonModel;

import edu.boisestate.cs.Alphabet;

public abstract class A_Model_Manager <T extends A_Model <T>> implements I_Model_Manager <T>  {

//    static protected I_Model_Manager instance = null;
    protected Alphabet alphabet;
    protected int initialBoundLength;

    @Override
	public Alphabet getAlphabet() {
        return this.alphabet;
    }
    
    public A_Model_Manager (Alphabet alphabet, int initialBoundLength) {
    	this.alphabet = alphabet;
    	this.initialBoundLength = initialBoundLength;
    	
    	
    }

//    static public I_Model_Manager getInstance(Alphabet alphabet,
//                                                    int modelVersion,
//                                                    int initialBoundLength) {
//
//        if (modelVersion == 1) {
//            BoundedAutomatonModelManager.setInstance(alphabet,
//                                                       initialBoundLength);
//        } else if (modelVersion == 2) {
//            AcyclicAutomatonModelManager.setInstance(alphabet,
//                                                     initialBoundLength);
////        } else if (modelVersion == 3) {
////            AggregateAutomatonModelManager.setInstance(alphabet,
////                                                       initialBoundLength);
////        } else if (modelVersion == 4) {
////            WeightedAutomatonModelManager.setInstance(alphabet,
////                                                      initialBoundLength);
////        } else if (modelVersion == 3) {
////        	AcyclicWeightedAutomatonModelManager.setInstance(alphabet, 
////        											initialBoundLength);
//        }
//
//        return instance;
//    }

    /**
     * Create a new automaton model from a concrete string
     * @param string
     * @return
     */
    @Override
	public abstract T createString(String string);

    /**
     * Create a new symbolic string from 0 to up to a certain length
     * @param initialBound the upper bound of the lenght (inlcusive)
     * @return
     */
    @Override
	public abstract T createAnyString(int initialBound);

    /**
     * A string with no upper bound - for unbounded models
     * @return
     */
    @Override
	public abstract T createAnyString();

    /**
     * Creates a symbolic string with length from min to max (both inclusive)
     * @param min
     * @param max
     * @return
     */
    @Override
	public abstract T createAnyString(int min, int max);
}
