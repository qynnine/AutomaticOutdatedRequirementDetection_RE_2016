package simplex_demo;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by niejia on 16/1/20.
 */
public class Demo {

    // max , x+y
    public static void main(String[] args) {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new
                double[]{0.3, 0.4}, 0);
        Collection<LinearConstraint> constraints = new
                ArrayList<LinearConstraint>();
        constraints.add(new LinearConstraint(new double[]{1, 1},
                Relationship.EQ, 1));

        constraints.add(new LinearConstraint(new double[]{1, 0},
                Relationship.GEQ, 0.1));

        constraints.add(new LinearConstraint(new double[]{1, 0},
                Relationship.LEQ, 1));

        constraints.add(new LinearConstraint(new double[]{0, 1},
                Relationship.GEQ, 0.1));

        constraints.add(new LinearConstraint(new double[]{0, 1},
                Relationship.LEQ, 1));

        constraints.add(new LinearConstraint(new double[]{1, -1},
                Relationship.GEQ, 0));

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, new
                        LinearConstraintSet(constraints),
                GoalType.MAXIMIZE, new
                        NonNegativeConstraint(true));


        double x = solution.getPoint()[0];
        double y = solution.getPoint()[1];
        double min = solution.getValue();
        System.out.println(x + " " + y + " " + min);
    }
}
