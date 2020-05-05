import java.util.ArrayList;
import java.util.List;

public class Pluton {

    Generał generał;
    int k;
    List<Żołnierz> żołnierze;
    static int ilośćPlutonów = 1;
    int numerPlutonu;
    double entropia;

    public Pluton(Generał generał, int k) {
        this.generał = generał;
        this.k = k;
        żołnierze = new ArrayList<>();
        numerPlutonu = ilośćPlutonów;
        ilośćPlutonów++;
        if (ilośćPlutonów >= 4) {
            ilośćPlutonów = 1;
        }
        entropia = 0.0;
    }

    public void addSoldier(Żołnierz żołnierz) {
        żołnierze.add(żołnierz);
    }

    public void changeGeneral() {
        for (int i = 0; i < żołnierze.get(0).wartości.length; i++) {
            double sum = 0.0;
            for (int j = 0; j < żołnierze.size(); j++) {
                sum += żołnierze.get(j).wartości[i];
            }
            double result = sum/ żołnierze.size();
            generał.values[i] = result;
        }
    }
    public void countEntrophy(){
        if (żołnierze.size() != 0) {
            double[] prawdopodobieństwo = new double[k];
            for (int i = 0; i < żołnierze.size(); i++) {
                if (żołnierze.get(i).nazwa.equals("Iris-setosa")) {
                    prawdopodobieństwo[0]++;
                }
                if (żołnierze.get(i).nazwa.equals("Iris-versicolor")) {
                    prawdopodobieństwo[1]++;
                }
                if (żołnierze.get(i).nazwa.equals("Iris-virginica")) {
                    prawdopodobieństwo[2]++;
                }
            }
            prawdopodobieństwo[0] = prawdopodobieństwo[0]/ żołnierze.size();
            prawdopodobieństwo[1] = prawdopodobieństwo[1]/ żołnierze.size();
            prawdopodobieństwo[2] = prawdopodobieństwo[2]/ żołnierze.size();
            for (int i = 0; i < prawdopodobieństwo.length; i++) {
                if (prawdopodobieństwo[i] != 0.0 && prawdopodobieństwo[i] != 1) {
                    entropia += log2(prawdopodobieństwo[i]) * prawdopodobieństwo[i];
                }
            }
            if (entropia != 0) {
                entropia *= -1;
            }
            System.out.println("Pluton " + numerPlutonu + ", Entropia: " + entropia);
        } else {
            System.out.println("Pluton jest pusty");
        }
    }
    public static double log2(double x)
    {
        return (Math.log(x)/Math.log(2) +1e-10);
    }

    @Override
    public String toString() {
        return "Pluton: " + numerPlutonu +
                ", generał = " + generał +
                ", liczność plutonu = " + żołnierze.size();
    }
}
