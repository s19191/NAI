public class Iris {

    double[] values;
    String name;
    int numberOfAttributes;

    public Iris(String[] values, int numberOfAttributes) {
        this.values = new double[numberOfAttributes];
        name = values[numberOfAttributes];
        for (int i = 0; i < this.values.length; i++) {
            this.values[i] = Double.parseDouble(values[i].replace(",","."));
        }
        this.numberOfAttributes = numberOfAttributes;
    }

    public Iris(String[] values) {
        this.values = new double[values.length];
        for (int i = 0; i < this.values.length; i++) {
            this.values[i] = Double.parseDouble(values[i].replace(",","."));
        }
        this.numberOfAttributes = values.length;
    }
}
