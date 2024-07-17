package edu.boisestate.cs.solvers;

import edu.boisestate.cs.Alphabet;
import edu.boisestate.cs.BasicTimer;
import edu.boisestate.cs.automatonModel.A_Model;
import edu.boisestate.cs.automatonModel.A_Model_Manager;
import edu.boisestate.cs.util.Tuple;
import edu.boisestate.cs.automatonModel.Model_Acyclic;

public class Solver<T extends A_Model<T>> extends A_Solver_Extended<T> implements I_Solver<T> {

    public final A_Model_Manager<T> modelManager;

    public Solver(A_Model_Manager<T> modelManager) {
        super();

        // initialize factory from parameter
        this.modelManager = modelManager;
    }

    public Solver(A_Model_Manager<T> modelManager,
                                int initialBound) {
        super(initialBound);

        // initialize factory from parameter
        this.modelManager = modelManager;
    }

    @Override
    public void append(int id, int base, int arg, int start, int end) {

        // get models
        T baseModel = this.symbolicStringMap.get(base);
        T argModel = this.symbolicStringMap.get(arg);

        // start timer
        BasicTimer.start();

        // get substring model
        T substrModel = argModel.substring(start, end);

        // append substring model to base model
        baseModel = baseModel.concatenate(substrModel);

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);
    }

    @Override
    public void append(int id, int base, int arg) {

//    	System.out.println(id + " " + base + " " + arg);
//    	System.out.println("sybmolicStringMap " + symbolicStringMap);
        // get models
        T baseModel = this.symbolicStringMap.get(base);
        T argModel = this.symbolicStringMap.get(arg);

        // start timer
        BasicTimer.start();
        //System.out.println("bM " + baseModel.getAutomaton().toString() + " aM " + argModel.getAutomaton().toString());
        // perform operation
        baseModel = baseModel.concatenate(argModel);
        
        //System.out.println("Append " + baseModel + " id " + id);

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);
    }

    @Override
    public void contains(boolean result, int base, int arg) {

        // get models
        T baseModel = this.symbolicStringMap.get(base);
        T argModel = this.symbolicStringMap.get(arg);

       // System.out.println("base " + base + " m\t" + baseModel + "\tresult " + result);
       // System.out.println("arg " + arg + " m\t "  + argModel);
        // true branch
        if (result) {

            // start timer
            BasicTimer.start();

            // get satisfying base model
            baseModel = baseModel.assertContainsOther(argModel);
           // System.out.println("Done with baseModel");
            // get satisfying arg model
            argModel = argModel.assertContainedInOther(baseModel);

            //System.exit(2);
            // stop timer
            BasicTimer.stop();

        } else {

            // start timer
            BasicTimer.start();

            // get satisfying base model as temp
           T tempModel = baseModel.assertNotContainsOther(argModel);

            // get satisfying arg model
            argModel = argModel.assertNotContainedInOther(baseModel);

            // set base model from temp
            baseModel = tempModel;
            //System.exit(2);
            // stop timer
            BasicTimer.stop();
        }

        
        // store result models
        this.symbolicStringMap.put(base, baseModel);
        this.symbolicStringMap.put(arg, argModel);
    }

    @Override
    public void deleteCharAt(int id, int base, int loc) {

        // delegate to delete method with start and end based on loc
        this.delete(id, base, loc, loc + 1);
    }

    @Override
    public void delete(int id, int base, int start, int end) {

        // get model
        T baseModel = this.symbolicStringMap.get(base);

        // start timer
        BasicTimer.start();

        // perform delete
        baseModel = baseModel.delete(start, end);

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);
    }

    @Override
    public void endsWith(boolean result, int base, int arg) {

        // get models
        T baseModel = this.symbolicStringMap.get(base);
        T argModel = this.symbolicStringMap.get(arg);

        if (result) {

            // start timer
            BasicTimer.start();

            // get satisfying base model
            baseModel = baseModel.assertEndsWith(argModel);

            // get satisfying arg model
            argModel = argModel.assertEndsOther(baseModel);

            // stop timer
            BasicTimer.stop();
        } else {

            // start timer
            BasicTimer.start();

            // get satisfying base model as temp
            T tempModel = baseModel.assertNotEndsWith(argModel);

            // get satisfying arg model
            argModel = argModel.assertNotEndsOther(baseModel);

            // set base model from temp
            baseModel = tempModel;

            // stop timer
            BasicTimer.stop();
        }

        // store result models
        this.symbolicStringMap.put(base, baseModel);
        this.symbolicStringMap.put(arg, argModel);
    }

    @Override
    public void equals(boolean result, int base, int arg) {

        // get models
        T baseModel = this.symbolicStringMap.get(base);
        T argModel = this.symbolicStringMap.get(arg);
//        System.out.println("argModel " + arg + "\n" + argModel.getAutomaton());
//        System.out.println("result " + result);
        // perform equals
        if (result) {

            // start timer
            BasicTimer.start();
            //System.out.println(baseModel + " id " + base);
            // get satisfying base model
            baseModel = baseModel.assertEquals(argModel);

            // get satisfying arg model
            argModel = argModel.assertEquals(baseModel);

            // stop timer
            BasicTimer.stop();
        } else {

            // start timer
            BasicTimer.start();

            // get satisfying base model as temp
            T tempModel = baseModel.assertNotEquals(argModel);

            // get satisfying arg model
            argModel = argModel.assertNotEquals(baseModel);

            // set base model from temp
            baseModel = tempModel;

            // stop timer
            BasicTimer.stop();
        }

        // store result models
        this.symbolicStringMap.put(base, baseModel);
        this.symbolicStringMap.put(arg, argModel);
    }

    @Override
    public void equalsIgnoreCase(boolean result, int base, int arg) {

        // get models
        T baseModel = this.symbolicStringMap.get(base);
        T argModel = this.symbolicStringMap.get(arg);

        // perform equals
        if (result) {

            // start timer
            BasicTimer.start();

            // get satisfying base model
            baseModel = baseModel.assertEqualsIgnoreCase(argModel);

            // get satisfying arg model
            argModel = argModel.assertEqualsIgnoreCase(baseModel);

            // stop timer
            BasicTimer.stop();
        } else {

            // start timer
            BasicTimer.start();

            // get satisfying base model as temp
            T tempModel = baseModel.assertNotEqualsIgnoreCase(argModel);

            // get satisfying arg model
            argModel = argModel.assertNotEqualsIgnoreCase(baseModel);

            // set base model from temp
            baseModel = tempModel;

            // stop timer
            BasicTimer.stop();
        }

        // store result models
        this.symbolicStringMap.put(base, baseModel);
        this.symbolicStringMap.put(arg, argModel);
    }

    @Override
    public String getSatisfiableResult(int id) {

        // get model
        T model = this.symbolicStringMap.get(id);
        return model.getAcceptedStringExample();
    }

    @Override
    public void insert(int id, int base, int arg, int offset) {

        // get models
        T baseModel = this.symbolicStringMap.get(base);
        T argModel = this.symbolicStringMap.get(arg);

        // start timer
        BasicTimer.start();

        // perform insert
        baseModel = baseModel.insert(offset, argModel);

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);
    }

    @Override
    public void insert(int id,
                       int base,
                       int arg,
                       int offset,
                       int start,
                       int end) {

        // get models
        T baseModel = this.symbolicStringMap.get(base);
        T argModel = this.symbolicStringMap.get(arg);

        // start timer
        BasicTimer.start();

        // get substring from arg model
        T substrModel = argModel.substring(start, end);

        // perform insert
        baseModel = baseModel.insert(offset, substrModel);

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);
    }

    @Override
    public void isEmpty(boolean result, int base) {

        // get model
        T baseModel = this.symbolicStringMap.get(base);

        if (result) {

            // start timer
            BasicTimer.start();

            // get satisfying automaton
            baseModel = baseModel.assertEmpty();

            // stop timer
            BasicTimer.stop();

        } else {

            // start timer
            BasicTimer.start();

            // get satisfying automaton
            baseModel = baseModel.assertNotEmpty();

            // stop timer
            BasicTimer.stop();

        }

        // store result models
        this.symbolicStringMap.put(base, baseModel);
    }

    @Override
    public boolean isSatisfiable(int id) {

        // get model
        T model = this.symbolicStringMap.get(id);
        //System.out.println("model " + model + " id " + id);
        // return true if not empty
        return !model.isEmpty();
    }

    @Override
    public boolean isSingleton(int id, String actualValue) {

        // get model
        T model = this.symbolicStringMap.get(id);
        //System.out.println("model " + model + " id " + id);
        //System.out.println(model.getAutomaton() + " val " + actualValue);

        // return singleton status
        return model.containsString(actualValue) && model.isSingleton();
    }

    @Override
    public boolean isSingleton(int id) {
    	// System.out.println("singleton " + id);
        // get model
        T model = this.symbolicStringMap.get(id);

        // return singleton status
        return model.isSingleton();
    }

    @Override
    public boolean isSound(int id, String actualValue) {

        // get model
        T model = this.symbolicStringMap.get(id);
        //why do we intersect? Why not just check whether 
        //the automaton accepts the string?
        //System.out.println("M " + model.getAutomaton() + " id " + id + " val " + actualValue);
        boolean ret = true;
        if(actualValue.equals("true") || actualValue.equals("false")){
        	//since the actual program execution went either true 
        	//or false then the resulting automaton should not be empty
        	ret = !model.isEmpty();
        } else {
        	ret = model.containsString(actualValue);
        }
        return ret;
        
        /* eas 10-20-18 old code 

        // get value model
        AutomatonModel value =
                this.modelManager.createString(actualValue);

        // intersect models
        AutomatonModel intersection = model.intersect(value);  
        // sound if intersection is not empty
        return !intersection.isEmpty();
        */
     
    }

    @Override
    public void newConcreteString(int id, String string) {
        // start timer
        BasicTimer.start();

        // create new automaton model from string
        T model = this.modelManager.createString(string);

        //System.out.println("newConcreteString " + id + " : " + string + " " + model.getClass());
        // stop timer
        BasicTimer.stop();

        // store new model
        this.symbolicStringMap.put(id, model);
        
       // System.out.println("m " + "\n" + model.getAutomaton());

        // store string value
        this.concreteStringMap.put(id, string);
    }

    @Override
    public void newSymbolicString(int id) {
        // start timer
        BasicTimer.start();

        // create new symbolic string
        T model =
                this.modelManager.createAnyString(this.initialBound);

        // stop timer
        BasicTimer.stop();
        //System.out.println("M " + model + " id " + id);
        // store new model
        this.symbolicStringMap.put(id, model);
    }

    @Override
    public void propagateSymbolicString(int id, int base) {
        // get model
        T model = this.symbolicStringMap.get(base);

        // start timer
        BasicTimer.start();

        // clone model
        T clone = model.clone();

        // stop timer
        BasicTimer.stop();

        // store clone
        this.symbolicStringMap.put(id, clone);
    }

    @Override
    public void replaceCharFindKnown(int id, int base, char find) {

        // get model
        T baseModel = this.symbolicStringMap.get(base);

        // start timer
        BasicTimer.start();

        // perform replace string operation
        baseModel = baseModel.replaceFindKnown(find);

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);

    }

    @Override
    public void replaceCharKnown(int id, int base, char find, char replace) {

        // get model
        T baseModel = this.symbolicStringMap.get(base);

        // start timer
        BasicTimer.start();
        // perform replace string operation
        baseModel = baseModel.replace(find, replace);

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);

    }

    @Override
    public void replaceCharReplaceKnown(int id, int base, char replace) {

        // get model
        T baseModel = this.symbolicStringMap.get(base);

        // start timer
        BasicTimer.start();

        // perform replace string operation
        baseModel = baseModel.replaceReplaceKnown(replace);

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);
    }

    @Override
    public void replaceCharUnknown(int id, int base) {

        // get model
        T baseModel = this.symbolicStringMap.get(base);

        // start timer
        BasicTimer.start();

        // perform replace string operation
        baseModel = baseModel.replaceChar();

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);

    }

    @Override
    public String replaceEscapes(String value) {

        // all unicode characters supported
        return value;
    }
    
    
    /**
     * This should be working
     * @param id
     * @param base
     * @param argOne
     * @param argTwo
     */
    public void replaceAll(int id, int base, int argOne, int argTwo) {
    	// get models
    	T baseModel = this.symbolicStringMap.get(base);
    	if (baseModel.getClass() != Model_Acyclic.class)
    		return;
    	String arg1String = this.concreteStringMap.get(argOne);
    	String arg2String = this.concreteStringMap.get(argTwo);
//    	System.out.println("Before:\n" + baseModel.getFiniteStrings());
//    	System.out.println("\n==========\n\nStarting Automaton:\n\n" + baseModel.getAutomaton().toString() + "\n\n========\n\n");
    	// start timer
    	BasicTimer.start();
    	// perform replaceFirst string operation
    	baseModel = baseModel.replaceAll(arg1String, arg2String);
    	// stop timer
    	BasicTimer.stop();
//    	System.out.println("After:\n" + baseModel.getFiniteStrings());
//    	System.out.println("\n==========\n\nFinished Automaton:\n\n" + baseModel.getAutomaton().toString() + "\n\n========\n\n");
    	// store result model
    	this.symbolicStringMap.put(id, baseModel);
    }
    
    
    /**
     * This should be working
     * 
     * @param id
     * @param base
     * @param argOne
     * @param argTwo
     */
    public void replaceFirst(int id, int base, int argOne, int argTwo) {
    	// get models
    	T baseModel = this.symbolicStringMap.get(base);
//    	if (baseModel.getClass() != Model_Acyclic.class)
//    		return;
    	String arg1String = this.concreteStringMap.get(argOne);
    	String arg2String = this.concreteStringMap.get(argTwo);
    	System.out.println("Before:\n" + baseModel.getFiniteStrings());
    	// start timer
    	BasicTimer.start();
    	// perform replaceFirst string operation
    	baseModel = baseModel.replaceFirst(arg1String, arg2String);
    	// stop timer
    	BasicTimer.stop();
    	System.out.println("After:\n" + baseModel.getFiniteStrings());
    	// store result model
    	this.symbolicStringMap.put(id, baseModel);
    }

    @Override
    public void replaceStrings(int id, int base, int argOne, int argTwo) {

        // get models
        T baseModel = this.symbolicStringMap.get(base);
        String arg1String = this.concreteStringMap.get(argOne);
        String arg2String = this.concreteStringMap.get(argTwo);

        // start timer
        BasicTimer.start();

        // perform replace string operation
        baseModel = baseModel.replace(arg1String, arg2String);

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);
    }

    @Override
    public void reverse(int id, int base) {

        // get model
        T baseModel = this.symbolicStringMap.get(base);

        // start timer
        BasicTimer.start();

        // perform operation
        baseModel = baseModel.reverse();

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);
    }

    @Override
    public void setCharAt(int id, int base, int arg, int offset) {

        // get models
        T baseModel = this.symbolicStringMap.get(base);
        T argModel = this.symbolicStringMap.get(arg);

        // start timer
        BasicTimer.start();

        // perform set char
        baseModel = baseModel.setCharAt(offset, argModel);

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);
    }

    @Override
    public void setLength(int id, int base, int length) {

        // get model
        T baseModel = this.symbolicStringMap.get(base);

        // start timer
        BasicTimer.start();

        // perform set length
        baseModel = baseModel.setLength(length);

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);
    }

    @Override
    public void shutDown() {
        // nothing needed
    }

    @Override
    public void startsWith(boolean result, int base, int arg) {

        // get models
        T baseModel = this.symbolicStringMap.get(base);
        T argModel = this.symbolicStringMap.get(arg);

        if (result) {

            // start timer
            BasicTimer.start();

            // get satisfying base model
            baseModel = baseModel.assertStartsWith(argModel);

            // get satisfying arg model
            argModel = argModel.assertStartsOther(baseModel);

            // stop timer
            BasicTimer.stop();

        } else {

            // start timer
            BasicTimer.start();

            // get satisfying base model as temp
            T tempModel = baseModel.assertNotStartsWith(argModel);

            // get satisfying arg model
            argModel = argModel.assertNotStartsOther(baseModel);

            // set base model from temp
            baseModel = tempModel;

            // stop timer
            BasicTimer.stop();
        }

        // store result models
        this.symbolicStringMap.put(base, baseModel);
        this.symbolicStringMap.put(arg, argModel);
    }

    @Override
    public void substring(int id, int base, int start) {

        // get model
        T baseModel = this.symbolicStringMap.get(base);

        // start timer
        BasicTimer.start();

        // perform operation
        baseModel = baseModel.suffix(start);

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);
    }

    @Override
    public void substring(int id, int base, int start, int end) {

        // get model
        T baseModel = this.symbolicStringMap.get(base);

        // start timer
        BasicTimer.start();

        // perform operation
        baseModel = baseModel.substring(start, end);

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);
    }

    @Override
    public void toLowerCase(int id, int base) {

        // get model
        T baseModel = this.symbolicStringMap.get(base);

        // start timer
        BasicTimer.start();

        // perform operation
        baseModel = baseModel.toLowercase();

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);
    }

    @Override
    public void toUpperCase(int id, int base) {

        // get model
        T baseModel = this.symbolicStringMap.get(base);

        // start timer
        BasicTimer.start();

        // perform operation
        baseModel = baseModel.toUppercase();

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);
    }

    @Override
    public void trim(int id, int base) {

        // get model
        T baseModel = this.symbolicStringMap.get(base);

        // start timer
        BasicTimer.start();

        // perform operation
        baseModel = baseModel.trim();

        // stop timer
        BasicTimer.stop();

        // store result model
        this.symbolicStringMap.put(id, baseModel);
    }

    private Tuple<Character, Boolean> getCharFromString(String string) {

        // initialize result variables
        boolean isKnown = true;
        char charValue;

        // attempt to parse char value from string
        try {

            // parse string to int
            int tempVal = Integer.parseInt(string);

            // if value falls between 0 and 9, not known char value
            if (tempVal >= 0 || tempVal < 10) {
                isKnown = false;
            }

            // set char value via cast
            charValue = (char) tempVal;

        } catch (NumberFormatException e) {

            // if string is not empty
            if (!string.isEmpty()) {

                // set value to first char in string
                charValue = string.charAt(0);

            } else {

                // set value to first value from alphabet
                Alphabet alphabet = this.modelManager.getAlphabet();
                charValue = alphabet.getSymbolSet().iterator().next();

            }
        }

        // return results
        return new Tuple<>(charValue, isKnown);
    }

}
