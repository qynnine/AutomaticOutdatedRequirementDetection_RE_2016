package core.metrics;

import java.util.LinkedHashMap;

/**
 * Created by niejia on 16/1/18.
 */
public class FmeasureCutCurve extends LinkedHashMap<String, Double> {
    private String name;
    private double cutParameter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCutParameter() {
        return cutParameter;
    }

    public void setCutParameter(double cutParameter) {
        this.cutParameter = cutParameter;
    }
}
