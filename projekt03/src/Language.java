import java.util.ArrayList;
import java.util.List;

public class Language {
    String name;
    List<String> texts;
    Perceptron p;

    public Language(String name) {
        this.name = name;
        texts = new ArrayList<>();
        p = new Perceptron();
    }

    public void addText(String newText) {
        texts.add(newText);
        p.learn(newText);
    }

    @Override
    public String toString() {
        return "Language{" +
                "name='" + name + '\'' +
                ", texts=" + texts +
                ", p=" + p +
                '}';
    }
}
