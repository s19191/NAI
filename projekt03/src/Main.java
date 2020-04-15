import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Language> languages = Futil.processDir("training");
//        for (Language l : languages) {
//            System.out.println(l.toString());
//        }
        Examiner examiner = new Examiner(languages);
        examiner.examin("ene due like fake");
    }
}
