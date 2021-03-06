package simplex_demo;

import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by niejia on 16/1/20.
 */
public class Main {
    public static void main(String[] args) {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new
                double[] { 2, 2, 1 }, 0);
        Collection<LinearConstraint> constraints = new
                ArrayList<LinearConstraint>();
        constraints.add(new LinearConstraint(new double[] { 1, 1, 0 },
                Relationship.GEQ,  1));
        constraints.add(new LinearConstraint(new double[] { 1, 0, 1 },
                Relationship.GEQ,  1));
        constraints.add(new LinearConstraint(new double[] { 0, 1, 0 },
                Relationship.GEQ,  1));

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new MaxIter(100), f, new
                        LinearConstraintSet(constraints),
                GoalType.MINIMIZE, new
                        NonNegativeConstraint(true));
        System.out.println(solver.toString());
    }
}
