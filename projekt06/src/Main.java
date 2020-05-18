import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Zestaw> training = load("plecak.txt");
    }

    public static List<Zestaw> load (String path) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(new File(path).toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Zestaw> zestawy = new ArrayList<>();
        int numberOfAttributes = lines.get(0).trim().split("\\s+").length -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().split("\\s+").length == numberOfAttributes + 1) {
//                zestawy.add(new Iris(lines.get(i).trim().split("\\s+"), numberOfAttributes));
            }
        }
        return zestawy;
    }
}
