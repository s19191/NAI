import java.util.List;

public class Examiner {
    List<Language> languages;

    public Examiner(List<Language> languages) {
        this.languages = languages;
    }
    void examin(String testText) {
        StringBuilder sb = new StringBuilder();
        for (Language l : languages) {
            if (l.test(testText)) {
                System.out.println("Podany tekst program zakwalifikował do języka: " + l.getName());
            }
        }
    }
}
