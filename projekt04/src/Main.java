import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(new File("iris_training.txt").toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Żołnierz> armia = new ArrayList<>();
        int numberOfAttributes = lines.get(0).trim().split("\\s+").length -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().split("\\s+").length == numberOfAttributes + 1) {
                armia.add(new Żołnierz(lines.get(i).trim().split("\\s+"), numberOfAttributes));
            }
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj parametr k");
        int k = getterFromScanner(scanner,"k");
        List<Generał> generałowie = awansuj(armia, k);
        List<Pluton> plutony = stwórzPlutony(armia, generałowie, k);
        for (int i = 0; i < plutony.size(); i++) {
            System.out.println(plutony.get(i));
        }
        for (int i = 0; i < plutony.size(); i++) {
            plutony.get(i).countEntrophy();
        }
    }

    public static List<Pluton> stwórzPlutony(List<Żołnierz> armia, List<Generał> generałowie, int k) {
        List<Pluton> plutony;
        do {
            armia.get(0).czyByłaZmiana = false;
            for (int i = 0; i < armia.size(); i++) {
                double[] odległości = new double[generałowie.size()];
                for (int j = 0; j < generałowie.size(); j++) {
                    odległości[j] = armia.get(i).obliczOdległość(generałowie.get(j));
                }
                double min = odległości[0];
                int który = 0;
                for (int j = 1; j < odległości.length; j++) {
                    if (odległości[j] < min) {
                        min = odległości[j];
                        który = j;
                    }
                }
                armia.get(i).wybierzGenerała(generałowie.get(który));
            }
            plutony = new ArrayList<>();
            for (int i = 0; i < generałowie.size(); i++) {
                plutony.add(new Pluton(generałowie.get(i), k));
            }
            for (int i = 0; i < armia.size(); i++) {
                for (int j = 0; j < plutony.size(); j++) {
                    if (plutony.get(j).generał.equals(armia.get(i).obecnyGenerał)) {
                        plutony.get(j).addSoldier(armia.get(i));
                    }
                }
            }
            System.out.println("*************************następna iteracja*************************");
            for (int i = 0; i < plutony.size(); i++) {
                plutony.get(i).changeGeneral();
            }
            for (int i = 1; i < plutony.size() +1; i++) {
                double sum = 0.0;
                for (int j = 0; j < plutony.get(i - 1).żołnierze.size(); j++) {
                    for (int l = 0; l < plutony.get(i - 1).generał.values.length; l++) {
                        sum += Math.pow(plutony.get(i - 1).generał.values[l] - plutony.get(i - 1).żołnierze.get(j).wartości[l], 2);
                    }
                }
                System.out.println("Pluton: " + i + ", liczność plutonu = " + plutony.get(i - 1).żołnierze.size());
                System.out.println("Pluton: " + i + ", suma kwadratów odległości żołnierzy (puntków) do  generała (środka grupy): " + sum);
            }
        }
        while (armia.get(0).czyByłaZmiana);
        System.out.println("*************************koniec pętli*************************");
        return plutony;
    }

    public static List<Generał> awansuj(List<Żołnierz> army, int k) {
        List<Generał> generałowie = new ArrayList<>();
        int[] który = new int[k];
        for (int i = 0; i < k; i++) {
            int wybór = (int) (Math.random() * army.size());
            if (!czyJużJest(który,wybór)) {
                który[i] = wybór;
            } else {
                i--;
            }
        }
        for (int i = 0; i < który.length; i++) {
            generałowie.add(new Generał(army.get(który[i])));
        }
        return generałowie;
    }

    public static boolean czyJużJest(int[] który, int choose) {
        boolean czyJużJest = false;
        for (int i = 0; i < który.length; i++) {
            if (który[i] == choose) {
                czyJużJest = true;
            }
        }
        return czyJużJest;
    }

    public static int getterFromScanner(Scanner scanner, String whatIsSearching) {
        int parametr = -1;
        try {
            parametr = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e){
            System.out.println("Nie prawidłowa wartość, nie została podana liczba naturalna");
            System.out.println("Podaj ponownie paramtr "+whatIsSearching);
            parametr = getterFromScanner(scanner, whatIsSearching);
        }
        return parametr;
    }
}
