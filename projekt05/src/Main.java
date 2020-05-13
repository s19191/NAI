import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        List<Iris> training = load("iris_training.txt");
        List<Iris> test = load("iris_test.txt");
        Map<String, List<Iris>> grouped = group(training);
        Map<String, Double> mistakesMatrix = createMistakesMatrix();
        double count = 0.0;
        for (int i = 0; i < test.size(); i++) {
            count += createProbability(test.get(i), grouped, mistakesMatrix);
        }
        double precision = count/test.size();
        System.out.println();
        System.out.println("Precyzja: " + precision * 100 + "%");
        System.out.println();
        soutForMistakesMatrix(mistakesMatrix);
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
            for (String type : grouped.keySet()) {
                List<Double> partProbability = new ArrayList<>();
                for (int i = 0; i < iris.numberOfAttributes; i++) {
                    if (i == 0) {
                        Double counter = 1.0;
                        for (Iris trainingIris : grouped.get(type)) {
                            if (iris.values[i] == trainingIris.values[i]) {
                                counter++;
                            }
                        }
                        partProbability.add(counter / (grouped.get(type).size() + grouped.keySet().size()));
                    } else {
                        Double counter = 0.0;
                        for (Iris trainingIris : grouped.get(type)) {
                            if (iris.values[i] == trainingIris.values[i]) {
                                counter++;
                            }
                        }
                        partProbability.add(counter / (grouped.get(type).size() + grouped.keySet().size()));
                    }
                }
                Double finalProbability = partProbability.get(0);
                for (int i = 1; i < partProbability.size(); i++) {
                    finalProbability *= partProbability.get(i);
                }
                probability.put(type, finalProbability);
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

    public static double createProbability(Iris iris, Map<String, List<Iris>> grouped, Map<String, Double> mistakesMatrix) {
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
            for (String type : grouped.keySet()) {
                List<Double> partProbability = new ArrayList<>();
                for (int i = 0; i < iris.numberOfAttributes; i++) {
                    if (i == 0) {
                        Double counter = 1.0;
                        for (Iris trainingIris : grouped.get(type)) {
                            if (iris.values[i] == trainingIris.values[i]) {
                                counter++;
                            }
                        }
                        partProbability.add(counter / (grouped.get(type).size() + grouped.keySet().size()));
                    } else {
                        Double counter = 0.0;
                        for (Iris trainingIris : grouped.get(type)) {
                            if (iris.values[i] == trainingIris.values[i]) {
                                counter++;
                            }
                        }
                        partProbability.add(counter / (grouped.get(type).size() + grouped.keySet().size()));
                    }
                }
                Double finalProbability = partProbability.get(0);
                for (int i = 1; i < partProbability.size(); i++) {
                    finalProbability *= partProbability.get(i);
                }
                probability.put(type, finalProbability);
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
            putTrue(maxName, mistakesMatrix);
        } else {
            putFalse(iris.name, maxName, mistakesMatrix);
        }
        return result;
    }

    public static Map<String, Double> createMistakesMatrix(){
        Map<String, Double> mistakesMatrix = new LinkedHashMap<>();
        mistakesMatrix.put("sese",0.0);
        mistakesMatrix.put("seve",0.0);
        mistakesMatrix.put("sevi",0.0);
        mistakesMatrix.put("vese",0.0);
        mistakesMatrix.put("veve",0.0);
        mistakesMatrix.put("vevi",0.0);
        mistakesMatrix.put("vise",0.0);
        mistakesMatrix.put("vive",0.0);
        mistakesMatrix.put("vivi",0.0);
        return mistakesMatrix;
    }

    public static void putTrue(String name, Map<String,Double> mistakesMatrix) {
        switch (name) {
            case "Iris-setosa": {
                mistakesMatrix.put("sese",mistakesMatrix.get("sese") + 1);
                break;
            }
            case "Iris-versicolor": {
                mistakesMatrix.put("veve",mistakesMatrix.get("veve") + 1);
                break;
            }
            case "Iris-virginica": {
                mistakesMatrix.put("vivi",mistakesMatrix.get("vivi") + 1);
                break;
            }
        }
    }

    public static void putFalse(String trueName, String falseName, Map<String,Double> mistakesMatrix) {
        String tmp = trueName + falseName;
        switch (tmp) {
            case "Iris-setosaIris-versicolor": {
                mistakesMatrix.put("seve",mistakesMatrix.get("seve") + 1);
                break;
            }
            case "Iris-setosaIris-virginica": {
                mistakesMatrix.put("sevi",mistakesMatrix.get("sevi") + 1);
                break;
            }
            case "Iris-versicolorIris-setosa": {
                mistakesMatrix.put("vese",mistakesMatrix.get("vese") + 1);
                break;
            }
            case "Iris-versicolorIris-virginica": {
                mistakesMatrix.put("vevi",mistakesMatrix.get("vevi") + 1);
                break;
            }
            case "Iris-virginicaIris-setosa": {
                mistakesMatrix.put("vise",mistakesMatrix.get("vise") + 1);
                break;
            }
            case "Iris-virginicaIris-versicolor": {
                mistakesMatrix.put("vive",mistakesMatrix.get("vive") + 1);
                break;
            }
        }
    }

    public static void soutForMistakesMatrix(Map<String, Double> mistakesMatrix){
        System.out.println("\t\t\tIris-setosa\tIris-versicolor\tIris-virginica");
        System.out.println("Iris-setosa\t\t" + mistakesMatrix.get("sese") + "\t\t" + mistakesMatrix.get("seve") +"\t\t\t\t" + mistakesMatrix.get("sevi"));
        System.out.println("Iris-versicolor\t" + mistakesMatrix.get("vese") + "\t\t\t" + mistakesMatrix.get("veve") +"\t\t\t\t" + mistakesMatrix.get("vevi"));
        System.out.println("Iris-virginica\t" + mistakesMatrix.get("vise") + "\t\t\t" + mistakesMatrix.get("vive") +"\t\t\t\t" + mistakesMatrix.get("vivi"));
        System.out.println();
        System.out.println("\tIris-setosa\tIris-versicolor\tIris-virginica");
        Double seP = mistakesMatrix.get("sese") / (mistakesMatrix.get("sese") + mistakesMatrix.get("vese") + mistakesMatrix.get("vise"));
        Double veP = mistakesMatrix.get("veve") / (mistakesMatrix.get("veve") + mistakesMatrix.get("seve") + mistakesMatrix.get("vive"));
        Double viP = mistakesMatrix.get("vivi") / (mistakesMatrix.get("vivi") + mistakesMatrix.get("sevi") + mistakesMatrix.get("vevi"));
        Double seR = mistakesMatrix.get("sese") / (mistakesMatrix.get("sese") + mistakesMatrix.get("seve") + mistakesMatrix.get("sevi"));
        Double veR = mistakesMatrix.get("veve") / (mistakesMatrix.get("veve") + mistakesMatrix.get("vese") + mistakesMatrix.get("vevi"));
        Double viR = mistakesMatrix.get("vivi") / (mistakesMatrix.get("vivi") + mistakesMatrix.get("vise") + mistakesMatrix.get("vive"));
        Double seF = (2 * seP * seR) / (seP + seR);
        Double veF = (2 * veP * veR) / (veP + veR);
        Double viF = (2 * viP * viR) / (viP + viR);
        System.out.println("P\t\t" + seP + "\t\t\t" + veP +"\t\t\t\t" + viP);
        System.out.println("R\t\t" + seR + "\t\t\t" + veR +"\t\t\t\t" + viR);
        System.out.println("F\t\t" + seF + "\t\t\t" + veF +"\t\t\t\t" + viF);
        System.out.println();
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