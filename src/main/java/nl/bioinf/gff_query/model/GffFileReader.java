package nl.bioinf.gff_query.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GffFileReader {
    private final String gffFilePath;
    private ArrayList<GffElement> rawLines = new ArrayList<>();
    private ArrayList<GffElement> lines = new ArrayList<>();
    private boolean sorted = false;
    private String summary = "";
    private List<String> fileArrayList;

    public GffFileReader(String gffFilePath) {
        this.gffFilePath = Objects.requireNonNull(gffFilePath);
    }

    public void readFile() {
        this.fileArrayList = new ArrayList<>();
        Path path = Paths.get(this.gffFilePath);
        try (BufferedReader reader = Files.newBufferedReader(
                path,
                Charset.defaultCharset())) {
            String lineFromFile;
            while ((lineFromFile = reader.readLine()) != null) {
                if (!lineFromFile.startsWith("#")) {
                    try {
                        GffElement temporaryGffElementObject = new GffElement(lineFromFile);
                        this.rawLines.add(temporaryGffElementObject);
                    }
                    catch (ArrayIndexOutOfBoundsException ex) {
                        /*TODO auch!*/
                    }
                }
            }
        } catch (IOException exception) {
            System.out.println("Error while reading file, \"IOExceptio\", Did you give the correct location / does the " +
                    "file exist?");
        }
    }


    public void applyFilters(String[] filters){
        if (!this.rawLines.isEmpty()) {
            for(GffElement line : this.rawLines){
                if (line.amICorrect(filters)){
                    lines.add(line);
                }
            }
        }
        this.sorted = true;
    }

    public boolean emptyDataFile() {
        // before running the switch, read in file and make a GffFileReader object
        String path = analysisOptions.getInFile();
        FileReader filereader = new FileReader();
        // read in the raw file and catch the resulting ArrayList
        filereader.readFile(path);
        if (fileArrayList.isEmpty()) {
            return true;
        }
        return false;
    }


    public void applyFilters(GffAnalysisOptions analysisOptions) {
        /*TODO replace method applyFilters(String[] filters)*/
        //        filters[0] = analysisOptions.getSearchType();
        //        filters[1] = analysisOptions.getSearchRegion();
        //        filters[2] = analysisOptions.getSearchFilter();
        //        filters[3] = analysisOptions.getSearchChildren();
        //        filters[4] = analysisOptions.getSearchWildcard();

    }


    @Override
    public String toString() {
        if (this.sorted) {
            String finalstring = "";
            for (GffElement line : this.lines) {
                finalstring = finalstring + line.getRaw() + "\n";
            }
            return (finalstring);
        }
        else {
            return "Lines not sorted yet!";
        }
    }

    public void printSummary() {
        String[] split_path = this.gffFilePath.split("/");
        String filename = split_path[split_path.length-1];
        this.summary = "file\t"+filename+"\ntotal number of features\t%s\nmolecules with features:\n";
        ArrayList<String> checkedMolecules = new ArrayList<>();
        int totalNoFeatures = this.rawLines.size();
        // for each molecule build a string with information.
        if (!this.rawLines.isEmpty()) {
            for (GffElement gffElement : this.rawLines) {
                String currectMolecule = gffElement.getSeqname();
                // check if molecule does not exists in check_molecules.
                if (!checkedMolecules.contains(currectMolecule)) {
                    // if it does not, add it
                    checkedMolecules.add(currectMolecule);
                    // make info vars if it is the same seqname.
                    int noFeatures = 0;
                    HashMap<String,Integer> featureCounts = new HashMap();
                    // walk through each line with the same name and add information
                    for (GffElement gffElementSingleMol : this.rawLines) {
                        if (currectMolecule.equals(gffElementSingleMol.getSeqname())) {
                            // add to tempinfo
                            noFeatures += 1;
                            if (!featureCounts.containsKey(gffElementSingleMol.getFeature())) {
                                featureCounts.put(gffElementSingleMol.getFeature(), 1);
                            }
                            else {
                                // update hashmap if feature exists within.
                                featureCounts.put(gffElementSingleMol.getFeature(),
                                        featureCounts.get(gffElementSingleMol.getFeature())+1);
                            }
                        }
                    }
                    // after the for loop, dump all info of the current molecule in the summary string.
                    this.summary += currectMolecule+"\n";
                    this.summary += "\tnumber of features\t"+noFeatures+"\n";
                    for (String key : featureCounts.keySet()) {
                        int value = featureCounts.get(key);
                        this.summary += "\t\t"+key+"\t\t\t\t"+Integer.toString(value)+"\n";
                    }
                }
            }
            // after the complete for loop, add up the total number of featues into the summary.
            this.summary = String.format(this.summary, Integer.toString(totalNoFeatures));
        }
        else {
            // if file is empty, make summary empty too.
            this.summary = "";
        }
    }

}
