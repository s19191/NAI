import java.util.ArrayList;
import java.util.List;

public class Perceptron {
    double alpha;
    double threshold;
    double[] weight;
    List<int[]> training;

    public Perceptron() {
        alpha = 0.1;
        threshold = Math.random()*10-5;
        weight = new double[26];
        for (int i = 0; i < 26; i++) {
            weight[i] = Math.random()*10-5;
        }
        training = new ArrayList<>();
    }

    public void learn(String text){
        addTrining(text);
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
                if (y == 1.0) {
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
    public void addTrining(String text) {
        int[] letters = new int[26];
        text = text.toUpperCase();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i)>64 && text.charAt(i)<91) {
                letters[text.charAt(i) -65]++;
            }
        }
        training.add(letters);
    }
}
