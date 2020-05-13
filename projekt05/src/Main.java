import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        List<Iris> training = load("iris_training.txt");
        List<Iris> test = load("iris_test.txt");
        //Map<String, Integer> irisTypes = createTypes(training);
        Map<String, List<Iris>> grouped = group(training);
        double count = 0.0;
        for (int i = 0; i < test.size(); i++) {
            count += createProbability(test.get(i), grouped);
        }
        double precision = count/test.size();
        System.out.println(precision);
        Scanner scanner = new Scanner(System.in);
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
                    System.out.println("Podawaj 4 liczby oddzielone białym znakiem, przyjmijmy znak spacji");
                    String[] vecotor = scanner.nextLine().trim().split("\\s+");
                    String classified = clasiffy(new Iris(vecotor), grouped);
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
    public static String clasiffy(Iris iris, Map<String, List<Iris>> grouped) {
        Map<String, Double> probability = new HashMap<>();
        boolean zero = false;
        System.out.println("*************************************************************Prawdopodobieństwa przed wygładzeniem*************************************************************");
        for (String type : grouped.keySet()) {
            List<Double> partProbability = new ArrayList<>();
            for (int i = 0; i < iris.numberOfAttributes; i++) {
                Double counter = 0.0;
                for (Iris trainingIris : grouped.get(type)) {
                    if (iris.values[i] == trainingIris.values[i]) {
                        counter++;
                    }
                }
                if (counter == 0.0) {
                    zero = true;
                }
                partProbability.add(counter / (grouped.get(type).size() + grouped.keySet().size()));
            }
            Double finalProbability = partProbability.get(0);
            for (int i = 1; i < partProbability.size(); i++) {
                finalProbability *= partProbability.get(i);
            }
            probability.put(type,finalProbability);
        }
        System.out.println(probability);
        probability.clear();
        if (zero) {
            System.out.println("*************************************************************Prawdopodobieństwa po wygładzeniu całości*************************************************************");
            for (String type : grouped.keySet()) {
                List<Double> partProbability = new ArrayList<>();
                for (int i = 0; i < iris.numberOfAttributes; i++) {
                    Double counter = 1.0;
                    for (Iris trainingIris : grouped.get(type)) {
                        if (iris.values[i] == trainingIris.values[i]) {
                            counter++;
                        }
                    }
                    partProbability.add(counter / (grouped.get(type).size() + grouped.keySet().size()));
                }
                Double finalProbability = partProbability.get(0);
                for (int i = 1; i < partProbability.size(); i++) {
                    finalProbability *= partProbability.get(i);
                }
                probability.put(type,finalProbability);
            }
            System.out.println(probability);
        } else {
            System.out.println("*************************************************************Prawdopodobieństwa po wygładzeniu tylko pierwszego atrybutu*************************************************************");
            int checker = 0;
            for (String type : grouped.keySet()) {
                if (checker == 0) {
                    checker++;
                    List<Double> partProbability = new ArrayList<>();
                    for (int i = 0; i < iris.numberOfAttributes; i++) {
                        Double counter = 1.0;
                        for (Iris trainingIris : grouped.get(type)) {
                            if (iris.values[i] == trainingIris.values[i]) {
                                counter++;
                            }
                        }
                        partProbability.add(counter / (grouped.get(type).size() + grouped.keySet().size()));
                    }
                    Double finalProbability = partProbability.get(0);
                    for (int i = 1; i < partProbability.size(); i++) {
                        finalProbability *= partProbability.get(i);
                    }
                    probability.put(type, finalProbability);
                } else {
                    List<Double> partProbability = new ArrayList<>();
                    for (int i = 0; i < iris.numberOfAttributes; i++) {
                        Double counter = 0.0;
                        for (Iris trainingIris : grouped.get(type)) {
                            if (iris.values[i] == trainingIris.values[i]) {
                                counter++;
                            }
                        }
                        partProbability.add(counter / grouped.get(type).size());
                    }
                    Double finalProbability = partProbability.get(0);
                    for (int i = 1; i < partProbability.size(); i++) {
                        finalProbability *= partProbability.get(i);
                    }
                    probability.put(type,finalProbability);
                }
            }
            System.out.println(probability);
        }
        Double max = 0.0;
        String result ="";
        for (String name : probability.keySet()) {
            if (probability.get(name) >= max) {
                max = probability.get(name);
                result = name;
            }
        }
        return result;
    }

    public static double createProbability(Iris iris, Map<String, List<Iris>> grouped) {
        double result = 0;
        Map<String, Double> probability = new HashMap<>();
        boolean zero = false;
        System.out.println("*************************************************************Prawdopodobieństwa przed wygładzeniem*************************************************************");
        for (String type : grouped.keySet()) {
            List<Double> partProbability = new ArrayList<>();
            for (int i = 0; i < iris.numberOfAttributes; i++) {
                Double counter = 0.0;
                for (Iris trainingIris : grouped.get(type)) {
                    if (iris.values[i] == trainingIris.values[i]) {
                        counter++;
                    }
                }
                if (counter == 0.0) {
                    zero = true;
                }
                partProbability.add(counter / (grouped.get(type).size() + grouped.keySet().size()));
            }
            Double finalProbability = partProbability.get(0);
            for (int i = 1; i < partProbability.size(); i++) {
                finalProbability *= partProbability.get(i);
            }
            probability.put(type,finalProbability);
        }
        System.out.println(probability);
        probability.clear();
        if (zero) {
            System.out.println("*************************************************************Prawdopodobieństwa po wygładzeniu całości*************************************************************");
            for (String type : grouped.keySet()) {
                List<Double> partProbability = new ArrayList<>();
                for (int i = 0; i < iris.numberOfAttributes; i++) {
                    Double counter = 1.0;
                    for (Iris trainingIris : grouped.get(type)) {
                        if (iris.values[i] == trainingIris.values[i]) {
                            counter++;
                        }
                    }
                    partProbability.add(counter / (grouped.get(type).size() + 1));
                }
                Double finalProbability = partProbability.get(0);
                for (int i = 1; i < partProbability.size(); i++) {
                    finalProbability *= partProbability.get(i);
                }
                probability.put(type,finalProbability);
            }
            System.out.println(probability);
        } else {
            System.out.println("*************************************************************Prawdopodobieństwa po wygładzeniu tylko pierwszego atrybutu*************************************************************");
            int checker = 0;
            for (String type : grouped.keySet()) {
                if (checker == 0) {
                    checker++;
                    List<Double> partProbability = new ArrayList<>();
                    for (int i = 0; i < iris.numberOfAttributes; i++) {
                        Double counter = 1.0;
                        for (Iris trainingIris : grouped.get(type)) {
                            if (iris.values[i] == trainingIris.values[i]) {
                                counter++;
                            }
                        }
                        partProbability.add(counter / (grouped.get(type).size() + grouped.keySet().size()));
                    }
                    Double finalProbability = partProbability.get(0);
                    for (int i = 1; i < partProbability.size(); i++) {
                        finalProbability *= partProbability.get(i);
                    }
                    probability.put(type, finalProbability);
                } else {
                    List<Double> partProbability = new ArrayList<>();
                    for (int i = 0; i < iris.numberOfAttributes; i++) {
                        Double counter = 0.0;
                        for (Iris trainingIris : grouped.get(type)) {
                            if (iris.values[i] == trainingIris.values[i]) {
                                counter++;
                            }
                        }
                        partProbability.add(counter / grouped.get(type).size());
                    }
                    Double finalProbability = partProbability.get(0);
                    for (int i = 1; i < partProbability.size(); i++) {
                        finalProbability *= partProbability.get(i);
                    }
                    probability.put(type,finalProbability);
                }
            }
            System.out.println(probability);
        }
        Double max = 0.0;
        String maxName ="";
        for (String name : probability.keySet()) {
            if (probability.get(name) > max) {
                max = probability.get(name);
                maxName = name;
            }
        }
        if (iris.name.equals(maxName)) {
            result = 1;
        }
        return result;
    }
    public static Map<String, List<Iris>> group(List<Iris> irises) {
        Map<String, List<Iris>> sorted = new HashMap<>();
        for (Iris iris : irises) {
            if (!sorted.containsKey(iris.name)) {
                sorted.put(iris.name, new ArrayList<>());
                sorted.get(iris.name).add(iris);
            } else {
                sorted.get(iris.name).add(iris);
            }
        }
        return sorted;
    }

//    public static Map<String, Integer> createTypes(List<Iris> irises) {
//        Map<String, Integer> irisesMap = new HashMap<>();
//        for (int i = 0; i < irises.size(); i++) {
//            if (irisesMap.containsKey(irises.get(i).name)) {
//                Integer tmp = irisesMap.get(irises.get(i).name);
//                irisesMap.remove(irises.get(i).name);
//                irisesMap.put(irises.get(i).name, ++tmp);
//            } else {
//                irisesMap.put(irises.get(i).name, 1);
//            }
//        }
//        return irisesMap;
//    }

    public static List<Iris> load (String path) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(new File(path).toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Iris> irises = new ArrayList<>();
        int numberOfAttributes = lines.get(0).trim().split("\\s+").length -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().split("\\s+").length == numberOfAttributes + 1) {
                irises.add(new Iris(lines.get(i).trim().split("\\s+"), numberOfAttributes));
            }
        }
        return irises;
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