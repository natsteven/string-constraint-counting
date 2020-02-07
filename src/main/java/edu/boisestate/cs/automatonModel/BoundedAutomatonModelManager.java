package edu.boisestate.cs.automatonModel;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;
import dk.brics.string.stringoperations.*;
import edu.boisestate.cs.Alphabet;
import edu.boisestate.cs.automatonModel.operations.*;

import java.math.BigInteger;
import java.util.Set;

public class BoundedAutomatonModelManager
        extends AutomatonModelManager {

    private final int boundLength;

    BoundedAutomatonModelManager(Alphabet alphabet,
                                           int boundLength) {
        this.alphabet = alphabet;
        this.boundLength = boundLength;

        // set automaton minimization as huffman
        Automaton.setMinimization(0);
    }

    static void setInstance(Alphabet alphabet, int initialBoundLength) {
        instance = new BoundedAutomatonModelManager(alphabet,
                                                      initialBoundLength);
    }

    @Override
    public AutomatonModel createAnyString(int initialBound) {
        return this.createAnyString();
    }

    @Override
    public AutomatonModel createAnyString(int min, int max) {

        // create any string automaton from alphabet
        String charSet = this.alphabet.getCharSet();
        Automaton anyChar = BasicAutomata.makeCharSet(charSet);

        // create bounded automaton
        Automaton boundedAutomaton = anyChar.repeat(min, max);

        // return model from bounded automaton
        return new BoundedAutomatonModel(boundedAutomaton,
                                           this.alphabet,
                                           this.boundLength);
    }

    @Override
    public AutomatonModel createAnyString() {

        // create any string automaton from alphabet
        String charSet = this.alphabet.getCharSet();
        Automaton anyString = BasicAutomata.makeCharSet(charSet)
                                           .repeat();

        // return model from automaton
        return new BoundedAutomatonModel(anyString,
                                           this.alphabet,
                                           this.boundLength);
    }

    @Override
    public AutomatonModel createString(String string) {
        // create string automaton
        Automaton stringAutomaton = BasicAutomata.makeString(string);
        // get string length as bound length
        int length = string.length();
        // return model from automaton
        return new BoundedAutomatonModel(stringAutomaton,
                                           this.alphabet,
                                           length);
    }
}
