import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DataSet {

    double[] sizes;
    double[] vals;
    static int counter = 1;
    int number;
    int capacity;
    int length;

    public DataSet(double[] sizes, double[] vals, int capacity) {
        this.sizes = sizes;
        this.vals = vals;
        number = counter;
        counter++;
        this.capacity = capacity;
        length = vals.length;
    }

    @Override
    public String toString() {
        return "DataSet{" +
                "sizes=" + Arrays.toString(sizes) +
                ", vals=" + Arrays.toString(vals) +
                ", number=" + number +
                '}';
    }

    public String findTheBest() {
        long time = System.nanoTime();
        double maxVal = 0.0;
        double maxCapacity = 0.0;
        List<Integer> whichOnes = new LinkedList<>();
        List<Double> choosenOnesSizes = new LinkedList<>();
        List<Double> choosenOnesVals = new LinkedList<>();
        for (int i = 0; i < (int)(Math.pow(2.0,length)); i++) {
            String tmp = Integer.toBinaryString(i);
            List<Integer> whichOnesTmp = new LinkedList<>();
            List<Double> choosenOnesSizesTmp = new LinkedList<>();
            List<Double> choosenOnesValsTmp = new LinkedList<>();
            double sumSize = 0.0;
            double sumVal = 0.0;
            for (int j = tmp.length() -1; j >= 0; j--) {
                if (tmp.charAt(j) == '1') {
                    whichOnesTmp.add(j + 1);
                    choosenOnesSizesTmp.add(sizes[j]);
                    choosenOnesValsTmp.add(vals[j]);
                    sumSize += sizes[j];
                    sumVal += vals[j];
                }
            }
            if (sumSize <= capacity && sumVal > maxVal) {
                maxVal = sumVal;
                maxCapacity = sumSize;
                whichOnes = whichOnesTmp;
                choosenOnesSizes = choosenOnesSizesTmp;
                choosenOnesVals = choosenOnesValsTmp;
            }
        }
        time = System.nanoTime()-time;
        String result = "Zajęta pojemność: " + maxCapacity +"\nCałkowita wartość: " + maxVal +"\nCzas wykonania: " + TimeUnit.SECONDS.convert(time,TimeUnit.NANOSECONDS) + "s\n";
        for (int i = whichOnes.size() -1; i >= 0; i--) {
            result += "[nr przedmiotu: " + whichOnes.get(i) + ", size: " + choosenOnesSizes.get(i) + ", value: " + choosenOnesVals.get(i) + "]\n";
        }
        return result;
    }
}
