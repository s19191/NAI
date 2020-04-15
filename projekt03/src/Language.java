import java.util.ArrayList;
import java.util.List;

public class Language {
    private String name;
    private List<String> texts;
    private Perceptron p;

    public Language(String name) {
        this.name = name;
        texts = new ArrayList<>();
        p = new Perceptron();
    }

    void addText(String newText, String languageName) {
        texts.add(newText);
        if (languageName.equals(name)) {
            p.learn(newText, 1);
        } else {
            p.learn(newText, 0);
        }
    }

    boolean test(String testText){
        return p.test(testText);
    }

    String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Language{" +
                "name='" + name + '\'' +
                ", p=" + p +
                '}';
    }
}
