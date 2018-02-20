package nl.bioinf.gff_query.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileReader {
    public ArrayList<String> readFile(String raw_path) {
        ArrayList<String> fileArrayList = new ArrayList<>();
        Path path = Paths.get(raw_path);
        try (BufferedReader reader = Files.newBufferedReader(
                path,
                Charset.defaultCharset())) {
            String lineFromFile;
            while ((lineFromFile = reader.readLine()) != null) {
                fileArrayList.add(lineFromFile);
            }
        } catch (IOException exception) {
            System.out.println("Error while reading file, \"IOExceptio\", Did you give the correct location / does the " +
                "file exist?");
        }

        return fileArrayList;
    }
}
