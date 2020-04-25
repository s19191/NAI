import java.util.ArrayList;
import java.util.List;

public class Examiner {
    List<Language> languages;

    public Examiner(List<Language> languages) {
        this.languages = languages;
    }
    String examin(String testText) {
        String result = "";
        List<String> tmp = new ArrayList<>();
        for (Language l : languages) {
            if (l.test(testText)) {
                tmp.add(l.getName());
            }
        }
        for (int i = 0; i < tmp.size(); i++) {
            if (i == tmp.size()-1) {
                result += "Podany tekst program zakwalifikował do języka: " + tmp.get(i);
            } else {
                result += "Podany tekst program zakwalifikował do języka: " + tmp.get(i) + "\n";
            }
        }
        return result;
    }
}
