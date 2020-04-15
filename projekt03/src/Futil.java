import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.readAllLines;

public class Futil {
    public Futil() { }

    static List<Language> processDir(String dirName) {
        List<Language> languages = new ArrayList<>();
        try {
            Stream<Path> streamPath= Files.walk(new File(dirName).toPath());
            for (Path p: streamPath.filter(Files::isDirectory).collect(Collectors.toList())) {
                if (!p.toString().equals("training")) {
                    Language tmp = new Language(p.getFileName().toString());
                    try {
                        Files.walkFileTree(p, new SimpleFileVisitor<Path>() {
                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                                try {
                                    List<String> lines = readAllLines(file, Charset.forName("UTF-8"));
                                    String text = "";
                                    for (String s : lines) {
                                        text += s;
                                    }
                                    tmp.addText(text);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return FileVisitResult.CONTINUE;
                            }
                        });
                        languages.add(tmp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return languages;
    }
}