import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Perceptron {
    private double alpha;
    private double threshold;
    private double[] weight;
    private List<int[]> training;
    private List<Integer> checker;

    public Perceptron() {
        alpha = 0.1;
        threshold = Math.random()*2-1;
//        threshold = -1.0;
        weight = new double[26];
        for (int i = 0; i < 26; i++) {
            weight[i] = Math.random()*2-1;
        }
        training = new ArrayList<>();
        checker = new ArrayList<>();
    }

    void learn(String text, int newChecker){
        int[] letters = lettersQuantity(text);
        training.add(letters);
        checker.add(newChecker);
        int allGood = 0;
        while (allGood != training.size()){
            for (int i = 0; i < training.size(); i++) {
                double sum = 1 * threshold;
                for (int j = 0; j < training.get(i).length; j++) {
                    sum += training.get(i)[j] * weight[j];
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
                if (y == 1.0 && checker.get(i) == 1 || y == 0.0 && checker.get(i) == 0) {
                    allGood++;
                } else {
                    threshold = threshold + alpha*(oppositeY - y)*1;
                    for (int j = 0; j < weight.length; j++) {
                        weight[j] = weight[j] + alpha*(oppositeY - y)*training.get(i)[j];
                    }
                }
                if (i == training.size() -1 && allGood != training.size()) {
                    allGood = 0;
                }
            }
        }
    }
    
    int[] lettersQuantity(String text) {
        int[] letters = new int[26];
        text = text.toUpperCase();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i)>64 && text.charAt(i)<91) {
                letters[text.charAt(i) -65]++;
            }
        }
        return letters;
    }
    
    boolean test(String testText){
        int[] letters = lettersQuantity(testText);
        double sum = 1 * threshold;
        for (int i = 0; i < weight.length; i++) {
            sum += letters[i] * weight[i];
        }
        if (sum>=0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Perceptron{" +
                "alpha=" + alpha +
                ", threshold=" + threshold +
                ", weight=" + Arrays.toString(weight) +
                '}';
    }
}
