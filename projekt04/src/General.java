public class General {

    double[] values;

    public General(int numberOfAttributes) {
        this.values = new double[numberOfAttributes];
        for (int i = 0; i < this.values.length; i++) {
            this.values[i] = Math.random() * 10;
        }
    }

    public General(Soldier soldier) {
        this.values = soldier.values;
    }
}
