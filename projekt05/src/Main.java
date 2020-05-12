import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        List<Iris> training = load("iris_training.txt");
        List<Iris> test = load("iris_test.txt");
        Map<String, Integer> irisTypes = createPropablility(training);
    }

    public static Map<String, Integer> createPropablility(List<Iris> irises) {
        Map<String, Integer> irisesMap = new HashMap<>();
        for (int i = 0; i < irises.size(); i++) {
            if (irisesMap.containsKey(irises.get(i).name)) {
                Integer tmp = irisesMap.get(irises.get(i).name);
                irisesMap.remove(irises.get(i).name);
                irisesMap.put(irises.get(i).name, tmp++);
            } else {
                irisesMap.put(irises.get(i).name, 1);
            }
        }
        return irisesMap;
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
}