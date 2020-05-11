public class Iris {

    double[] values;
    String name;

    public Iris(String[] values, int numberOfAttributes) {
        this.values = new double[numberOfAttributes];
        name = values[numberOfAttributes];
        for (int i = 0; i < this.values.length; i++) {
            this.values[i] = Double.parseDouble(values[i].replace(",","."));
        }
    }
}
