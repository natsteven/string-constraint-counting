package edu.boisestate.cs.solvers;

import java.math.BigInteger;
import java.util.Set;

import edu.boisestate.cs.BasicTimer;
import edu.boisestate.cs.automatonModel.A_Model;
import edu.boisestate.cs.automatonModel.AutomatonModelManager;

public class Solver_Count<T extends A_Model<T>> extends Solver<T> implements I_Solver_Count<T> {

    public Solver_Count(AutomatonModelManager modelFactory) {
        super(modelFactory);
    }

    public Solver_Count(AutomatonModelManager modelFactory,
                                  int initialBound) {
        super(modelFactory, initialBound);
    }

    /**
     * Enumerates all possible values for a given symbolic string
     *
     * @param id
     *         the identifier of the symbolic string
     *
     * @return a set of all possible string values represented by the symbolic
     * string
     */
    @Override
    public Set<String> getAllVales(int id) {

        // get model from id
        T model = this.symbolicStringMap.get(id);

        // return finite strings of model
        return model.getFiniteStrings();
    }

    /**
     * Get the number of solutions represented by the symbolic string model.
     *
     * @param id
     *         the identifier of the symbolic string
     *
     * @return number of solutions for a given node in the graph
     */
    @Override
    public long getModelCount(int id) {

        // get model from id
        T model = this.symbolicStringMap.get(id);

        // start timer
        BasicTimer.start();

        // get model count as big integer
        BigInteger count = model.modelCount();

        // stop timer
        BasicTimer.stop();

        // return model count as integer
        return count.longValue();
    }
}
