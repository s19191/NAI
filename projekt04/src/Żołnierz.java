public class Żołnierz {

    double[] wartości;
    Generał obecnyGenerał;
    Generał poprzedniGenerał;
    static boolean czyByłaZmiana;
    String nazwa;

    public Żołnierz(String[] wartości, int ilośćAtrybutów) {
        this.wartości = new double[ilośćAtrybutów];
        nazwa = wartości[ilośćAtrybutów];
        for (int i = 0; i < this.wartości.length; i++) {
            this.wartości[i] = Double.parseDouble(wartości[i].replace(",","."));
        }
        poprzedniGenerał = null;
        czyByłaZmiana = false;
    }

    public void wybierzGenerała(Generał generał) {
        poprzedniGenerał = obecnyGenerał;
        obecnyGenerał = generał;
        if (poprzedniGenerał == null) {
            czyByłaZmiana = true;
        } else if (!poprzedniGenerał.equals(obecnyGenerał)) {
            czyByłaZmiana = true;
        }
    }

    public double obliczOdległość(Generał generał) {
        double odległość = 0.0;
        for (int i = 0; i < wartości.length; i++) {
            odległość += Math.pow(generał.values[i] - wartości[i], 2);
        }
        return odległość;
    }

    @Override
    public String toString() {
        return "Żołnierz: " +
                ", obecny generał = " + obecnyGenerał;
    }
}
