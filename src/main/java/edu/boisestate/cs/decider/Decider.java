package edu.boisestate.cs.decider;

import edu.boisestate.cs.automatonModel.A_Model;

/**
 * Some functionality is moved from A_Reporter
 * Decider is the core algorithm for traversing
 * the constraint graph.
 * Unlike implementation in A_Reporter, it does not
 * triggered by the first predicate encountered in the
 * graph traversion, but start solving all nodes at
 * the same time
 * @author elenasherman
 *
 * @param <T> the type of solver
 */

public abstract class Decider <T extends A_Model<T>>{

}
