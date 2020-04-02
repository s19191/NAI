package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(new File("D:\\NAI\\projekt01\\iris_training.txt").toPath(), StandardCharsets.UTF_8);
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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj parametr k");
        int k = getterFromScanner(scanner,"k");
        try {
            lines = Files.readAllLines(new File("D:\\NAI\\projekt01\\iris_test.txt").toPath(), StandardCharsets.UTF_8);
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
            perPositive += isTrue(irysyTest.get(i),irysyTraining,k);
        }
        double perResoult = perPositive/per100*100;
        System.out.println("Ilość prawidłowo zakwalifikowanych: "+perPositive+", dokładność eksperymentu: "+perResoult+"%");
        boolean ifContinue = true;
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
                    System.out.println("Pamiętaj, że parametr k podany wcześniej pozostał nie zmieniony!, czyli jest to: "+k);
                    System.out.println("Podawaj 4 liczby oddzielone białym znakiem, przyjmijmy znak spacji");
                    String[] vecotor = scanner.nextLine().trim().split("\\s+");
                    String classified = clasiffy(vecotor,irysyTraining,k);
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

    public static List<String> createResult(String [] values, List<String[]> training, int k) {
        List<vectorLengthPlusName> vectorLengthPlusNames = new ArrayList<>();
        double tmp = 0.0;
        boolean mistake = false;
        for (int i = 0; i < training.size(); i++) {
            for (int j = 0; j < values.length - 1; j++) {
                try {
                    tmp += Math.pow((Double.parseDouble(values[j].replace(',', '.')) - Double.parseDouble(training.get(i)[j].replace(',', '.'))), 2);
                } catch (NumberFormatException e) {
                    mistake = true;
                    String mistakeTmp ="";
                    for (int l = 0; l < values.length; l++) {
                        mistakeTmp += values[l]+" ";
                    }
                    System.out.println("Podano nie właściwe dane, linijka z błędem:" + mistakeTmp);
                }
            }
            if (!mistake) {
                vectorLengthPlusNames.add(new vectorLengthPlusName(training.get(i)[training.get(i).length - 1], tmp));
            }
            tmp = 0.0;
            mistake = false;
        }
        vectorLengthPlusNames.sort((o1, o2) -> {
            if (o1.vectorLength > o2.vectorLength) {
                return 1;
            } else if (o1.vectorLength < o2.vectorLength) {
                return -1;
            } else {
                return 0;
            }
        });
        List<String> result = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            result.add(vectorLengthPlusNames.get(i).name);
        }
        return result;
    }

    public static String clasiffy(String[] vector, List<String[]> training, int k) {
        List<String> result = createResult(vector,training,k);
        Map<String, Long> groupped = result.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        List<String> keyList = new ArrayList<>(groupped.keySet());
        String pretender = keyList.get(0);
        long howManytoTrue = groupped.get(pretender);
        int counter = 0;
        List<String> theSame = new ArrayList<>();
        for (int i = 1; i < keyList.size(); i++) {
            if (groupped.get(keyList.get(i)) > howManytoTrue) {
                pretender = keyList.get(i);
                howManytoTrue = groupped.get(pretender);
                counter = 0;
                theSame.clear();
            } else if (groupped.get(keyList.get(i)) == howManytoTrue) {
                counter++;
                theSame.add(keyList.get(i));
            }
        }
        if (counter != 0) {
            for (int i = 0; i < result.size(); i++) {
                for (int j = 0; j < theSame.size(); j++) {
                    if (result.get(i).equals(pretender)) {
                        break;
                    } else if (result.get(i).equals(theSame.get(j))) {
                        pretender = result.get(i);
                        break;
                    }
                }
            }
        }
        return pretender;
    }

    public static int isTrue(String [] test, List<String[]> training, int k){
        String trueClasification = test[test.length-1];
        List<String> result = createResult(test,training,k);
        Map<String, Long> groupped = result.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Set<String> keySet = groupped.keySet();
        long howManytoTrue;
        int trueOrFalse = 0;
        String pretender = null;
        if (groupped.containsKey(trueClasification)) {
            howManytoTrue = groupped.get(trueClasification);
            int counter = 0;
            List<String> theSame = new ArrayList<>();
            for (String s : keySet) {
                if (groupped.get(s) > howManytoTrue) {
                    pretender = s;
                    howManytoTrue = groupped.get(s);
                    counter = 0;
                    theSame.clear();
                } else if (groupped.get(s) == howManytoTrue && !s.equals(trueClasification) && !s.equals(pretender)) {
                    counter++;
                    theSame.add(s);
                }
            }
            if (pretender == null && counter == 0) {
                trueOrFalse = 1;
            } else if (pretender != null && counter == 0) {
                trueOrFalse = 0;
            } else if (pretender == null && counter != 0) {
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i).equals(trueClasification)) {
                        trueOrFalse = 1;
                        break;
                    } else {
                        boolean czyPrzerwać = false;
                        for (int j = 0; j < theSame.size(); j++) {
                            if (result.get(i).equals(theSame.get(j))) {
                                trueOrFalse = 0;
                                czyPrzerwać = true;
                                break;
                            }
                        }
                        if (czyPrzerwać) {
                            break;
                        }
                    }
                }
            }
        } else {
            trueOrFalse = 0;
        }
        return trueOrFalse;
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
