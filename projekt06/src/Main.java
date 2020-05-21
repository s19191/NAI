import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(new File("plecak.txt").toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<DataSet> dataSets = new ArrayList<>();
        int length = 0;
        int capacity = 0;
        Pattern p = Pattern.compile("length - (\\d{2}), capacity (\\d{2})");
        Matcher m = p.matcher(lines.get(0));
        if (m.matches()) {
            length = Integer.parseInt(m.group(1));
            capacity = Integer.parseInt(m.group(2));
        }
        int tmp = -1;
        double[] tmpSizes = new double[length];
        double[] tmpVals = new double[length];
        for (int i = 0; i < lines.size(); i++) {
            Pattern p1 = Pattern.compile("");
            Matcher m1 = p1.matcher(lines.get(i));
            if (m1.matches()) {
                tmp = 0;
                tmpSizes = new double[length];
                tmpVals = new double[length];
            } else {
                Pattern p2 = Pattern.compile("sizes = \\{(.+)\\} ");
                Matcher m2 = p2.matcher(lines.get(i));
                if (m2.matches()) {
                    String[] tmpTab = m2.group(1).split(",");
                    for (int j = 0; j < tmpTab.length; j++) {
                        tmpSizes[j] = Integer.parseInt(tmpTab[j].trim());
                    }
                }
                Pattern p3 = Pattern.compile("vals = {1,2}\\{(.+)\\} ");
                Matcher m3 = p3.matcher(lines.get(i));
                if (m3.matches()) {
                    String[] tmpTab = m3.group(1).split(",");
                    for (int j = 0; j < tmpTab.length; j++) {
                        tmpVals[j] = Integer.parseInt(tmpTab[j].trim());
                    }
                }
                tmp++;
                if (tmp >= 3) {
                    dataSets.add(new DataSet(tmpSizes, tmpVals, capacity));
                }
            }
        }
        Random r = new Random();
        int whichOne = r.nextInt(15);
        DataSet ourSet = dataSets.get(whichOne);
        System.out.println("Wybrany DataSet: " + ourSet.number);
        System.out.println(ourSet.findTheBest());
    }
}
