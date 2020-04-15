import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Language> languages = Futil.processDir("training");
        for (Language l : languages) {
            System.out.println(l.toString());
        }
    }
}
