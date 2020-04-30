import com.sun.org.apache.bcel.internal.generic.GETFIELD;

import java.util.*;

public class Soldier {

    double[] values;
    Map<General,Double> generalsMap;
    General mainGeneral;
    General previousGeneral;
    static boolean ifChanged;

    public Soldier(String[] values, int numberOfAttributes) {
        this.values = new double[numberOfAttributes];
        for (int i = 0; i < this.values.length; i++) {
            this.values[i] = Double.parseDouble(values[i].replace(",","."));
        }
        generalsMap = new HashMap<>();
        ifChanged = false;
    }

    public void distance(General general) {
        double distance = 0.0;
        for (int i = 0; i < values.length; i++) {
            distance += Math.pow(general.values[i] - values[i], 2);
        }
        generalsMap.put(general,distance);
        Double min = this.findMin();
        for (General g : generalsMap.keySet()) {
            if (generalsMap.get(g) == min) {
                if (previousGeneral == null) {
                    mainGeneral = g;
                    previousGeneral = g;
                } else {
                    mainGeneral = g;
                }
            }
        }
        if (mainGeneral.equals(previousGeneral)) {
            ifChanged = true;
        }
    }
    public Double findMin() {
        List<Double> generals = new LinkedList<>(generalsMap.values());
        Double min = generals.get(0);
        for (int i = 1; i < generals.size(); i++) {
            if (generals.get(i) < min) {
                min = generals.get(i);
            }
        }
        return min;
    }
}
