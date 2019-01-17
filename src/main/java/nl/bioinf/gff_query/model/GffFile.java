package nl.bioinf.gff_query.model;

import nl.bioinf.gff_query.control.GffLine;

import java.util.ArrayList;
import java.util.HashMap;

public class GffFile {
    private ArrayList<GffLine> rawLines = new ArrayList<>();
    private ArrayList<GffLine> lines = new ArrayList<>();
    private boolean sorted = false;
    private String summary = "";

    public GffFile(ArrayList<String> fileLines, String path) {
        for(String gffLine : fileLines){
            if (!gffLine.startsWith("#")) {
                try {
                    GffLine temporaryGffLineObject = new GffLine(gffLine);
                    this.rawLines.add(temporaryGffLineObject);
                }
                catch (ArrayIndexOutOfBoundsException ex) {}
            }
        }
        // Make summary of file.
        this.makeSummary(path);
    }

    public void applyFilters(String[] filters){
        if (!this.rawLines.isEmpty()) {
            for(GffLine line : this.rawLines){
                if (line.amICorrect(filters)){
                    lines.add(line);
                }
            }
        }
        this.sorted = true;
    }

    @Override
    public String toString() {
        if (this.sorted) {
            String finalstring = "";
            for (GffLine line : this.lines) {
                finalstring = finalstring + line.getRaw() + "\n";
            }
            return (finalstring);
        }
        else {
            return "Lines not sorted yet!";
        }
    }

    public void makeSummary(String path) {
        String[] split_path = path.split("/");
        String filename = split_path[split_path.length-1];
        this.summary = "file\t"+filename+"\ntotal number of features\t%s\nmolecules with features:\n";
        ArrayList<String> checkedMolecules = new ArrayList<>();
        int totalNoFeatures = this.rawLines.size();
        // for each molecule build a string with information.
        if (!this.rawLines.isEmpty()) {
            for (GffLine gffLine : this.rawLines) {
                String currectMolecule = gffLine.getSeqname();
                // check if molecule does not exists in check_molecules.
                if (!checkedMolecules.contains(currectMolecule)) {
                    // if it does not, add it
                    checkedMolecules.add(currectMolecule);
                    // make info vars if it is the same seqname.
                    int noFeatures = 0;
                    HashMap<String,Integer> featureCounts = new HashMap();
                    // walk through each line with the same name and add information
                    for (GffLine gffLineSingleMol : this.rawLines) {
                        if (currectMolecule.equals(gffLineSingleMol.getSeqname())) {
                            // add to tempinfo
                            noFeatures += 1;
                            if (!featureCounts.containsKey(gffLineSingleMol.getFeature())) {
                                featureCounts.put(gffLineSingleMol.getFeature(), 1);
                            }
                            else {
                                // update hashmap if feature exists within.
                                featureCounts.put(gffLineSingleMol.getFeature(),
                                        featureCounts.get(gffLineSingleMol.getFeature())+1);
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

    public void printSummary() {
        if (this.summary.isEmpty()) {
            System.out.println("Empty or corrupt file, could not make summary.");
        }
        else {
            System.out.println(this.summary);
        }

    }
}
