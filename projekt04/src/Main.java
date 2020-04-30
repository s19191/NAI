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
        List<Soldier> army = new ArrayList<>();
        int numberOfAttributes = lines.get(0).trim().split("\\s+").length -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().split("\\s+").length == numberOfAttributes + 1) {
                army.add(new Soldier(lines.get(i).trim().split("\\s+"), numberOfAttributes));
            }
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj parametr k");
        int k = getterFromScanner(scanner,"k");
        List<General> generals = promote(army, k);
        while (army.get(0).ifChanged) {
            for (int i = 0; i < generals.size(); i++) {
                for (int j = 0; j < army.size(); j++) {
                    army.get(j).distance(generals.get(i));
                }
            }
        }
    }
    public static List<General> promote(List<Soldier> army, int k) {
        List<General> generals = new ArrayList<>();
        int[] which = new int[k];
        for (int i = 0; i < k; i++) {
            int choose = (int) (Math.random() * army.size());
            if (!ifContains(which,choose)) {
                which[i] = choose;
            } else {
                i--;
            }
        }
        for (int i = 0; i < which.length; i++) {
            generals.add(new General(army.get(which[i])));
        }
        return generals;
    }

    public static boolean ifContains(int[] which, int choose) {
        boolean ifContains = false;
        for (int i = 0; i < which.length; i++) {
            if (which[i] == choose) {
                ifContains = true;
            }
        }
        return ifContains;
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
