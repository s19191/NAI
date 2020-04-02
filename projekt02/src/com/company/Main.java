package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class Main {
    //nie używane, ale żeby było wiadomo co jest co
    static final int irysSetosa = 1;
    static final int ortherIrys = 0;
    static final double alpha = 0.1;

    public static void main(String[] args) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(new File("D:\\NAI\\projekt02\\iris_training.txt").toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String[]> irysyTraining = new ArrayList<>();
        int numberOfAttributes = lines.get(0).trim().split("\\s+").length;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().split("\\s+").length == numberOfAttributes) {
                irysyTraining.add(lines.get(i).trim().split("\\s+"));
            }
        }
        double[] perceptron = learning(irysyTraining);
        //wypisuje współrzędne wag, pierwsze 4 to wagi a 5 to próg
        for (int i = 0; i <perceptron.length ; i++) {
            if(i != perceptron.length -1) {
                System.out.println("Waga " + i +": " +perceptron[i]);
            } else {
                System.out.println("Próg: " + perceptron[i]);
            }
        }
        try {
            lines = Files.readAllLines(new File("D:\\NAI\\projekt02\\iris_test.txt").toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String[]> irysyTest = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().split("\\s+").length == numberOfAttributes) {
                irysyTest.add(lines.get(i).trim().split("\\s+"));
            }
        }
        double per100 = irysyTest.size();
        double perPositive = 0;
        for (int i = 0; i < irysyTest.size(); i++) {
            perPositive += isTrue(irysyTest.get(i),perceptron);
        }
        double perResoult = perPositive/per100*100;
        System.out.println("Ilość prawidłowo zakwalifikowanych: "+perPositive+", dokładność eksperymentu: "+perResoult+"%");
        boolean ifContinue = true;
        Scanner scanner = new Scanner(System.in);
        while (ifContinue) {
            System.out.println("Jeżeli chcesz sprawdzać swój własny wektor wpisz 1, jak chcesz zakończyć działanie tego programu wpisz 0");
            int what = getterFromScanner(scanner,"jednoznacznie identyfikujący co ma wykonać program, dla przypomnienia: \nJeżeli chcesz sprawdzać swój własny wektor wpisz 1, jak chcesz zakończyć działanie tego programu wpisz 0");
            switch (what){
                case 0: {
                    ifContinue = false;
                    break;
                }
                case 1: {
                    System.out.println("Podaj wektor (w naszym przypadku ma on mieć 4 atrybuty, ale to wynika z pliku któy wczytaliśmy): ");
                    System.out.println("Podawaj 4 liczby oddzielone białym znakiem, przyjmijmy znak spacji");
                    String[] vecotor = scanner.nextLine().trim().split("\\s+");
                    String classified = clasiffy(vecotor,perceptron);
                    System.out.println("Według naszego programu podany vector został zaklasyfikowany do: "+classified);
                    break;
                }
                default: {
                    System.out.println("Wpisano liczbę, która to nie jest uznawana jako komenda naszego programu, proszę spróbować ponownie");
                    break;
                }
            }
        }
        scanner.close();
    }

    public static double[] learning(List<String[]> training) {
        double[] w = new double[training.get(0).length -1];
//        for (int i = 0; i < w.length; i++) {
//            w[i] = Math.random()*10-5;
//        }
//        for (int i = 0; i < w.length; i++) {
//            w[i] = Math.random()*4-2;
//        }
//        double threshold = Math.random()*10-5;
        double threshold = 0.0;
//        double threshold = Math.random()*4-2;
        int allGood = 0;
        while (allGood != training.size()) {
            for (int j = 0; j < training.size(); j++) {
                String trueIris = training.get(j)[training.get(j).length-1];
                double sum = 1 * threshold;
//                double sum = 0;
                for (int i = 0; i < training.get(j).length-1; i++) {
                    sum += (Double.parseDouble(training.get(j)[i].replace(",","."))*w[i]);
                }
                double y;
                double oppositeY;
                if (sum>=0) {
                    y = 1.0;
                    oppositeY = 0.0;
                } else {
                    y = 0.0;
                    oppositeY = 1.0;
                }
                if (y == 1.0 && trueIris.equals("Iris-setosa") || y == 0.0 && !trueIris.equals("Iris-setosa")) {
                    allGood++;
                } else {
                    threshold = threshold + alpha*(oppositeY - y)*1;
                    for (int i = 0; i < w.length; i++) {
                        w[i] = w[i] + alpha*(oppositeY - y)*Double.parseDouble(training.get(j)[i].replace(",","."));
                    }
                }
                if (j == training.size() -1 && allGood != training.size()) {
                    allGood = 0;
                }
            }
        }
        double[] perceptron = new double[w.length +1];
        for (int i = 0; i < w.length; i++) {
            perceptron[i] = w[i];
        }
        perceptron[perceptron.length -1] = threshold;
        return perceptron;
    }

    public static String clasiffy(String[] vector, double[] perceptron) {
        String result;
        double sum = perceptron[perceptron.length -1]*1;
//        double sum = 0;
        for (int i = 0; i < vector.length; i++) {
            sum += (Double.parseDouble(vector[i].replace(",","."))*perceptron[i]);
        }
        if (sum>=0) {
            result = "Iris-setosa";
        } else {
            result = "Nie Iris-setosa";
        }
        return result+sum;
    }

    public static int isTrue(String [] test, double[] perceptron){
        int result = 0;
        String trueIris = test[test.length -1];
        double sum = perceptron[perceptron.length -1]*1;
//        double sum = 0;
        for (int i = 0; i < test.length -1; i++) {
            sum += (Double.parseDouble(test[i].replace(",","."))*perceptron[i]);
        }
        double y;
        if (sum>=0) {
            y = 1.0;
        } else {
            y = 0.0;
        }
        if (y == 1.0 && trueIris.equals("Iris-setosa") || y == 0.0 && !trueIris.equals("Iris-setosa")) {
            result = 1;
        }
        return result;
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