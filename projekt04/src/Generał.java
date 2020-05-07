public class Generał {

    double[] values;
    static int ilośćGenerałów = 1;
    int numerGenerała;

    public Generał(int numberOfAttributes) {
        this.values = new double[numberOfAttributes];
        for (int i = 0; i < this.values.length; i++) {
            this.values[i] = Math.random() * 10;
        }
        numerGenerała = ilośćGenerałów;
        ilośćGenerałów++;
    }

    public Generał(Żołnierz żołnierz) {
        this.values = żołnierz.wartości;
        numerGenerała = ilośćGenerałów;
        ilośćGenerałów++;
    }

    @Override
    public String toString() {
        return "Generał: " + numerGenerała;
    }
}
