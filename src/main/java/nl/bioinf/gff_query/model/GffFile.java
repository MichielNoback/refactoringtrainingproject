package nl.bioinf.gff_query.model;

import nl.bioinf.gff_query.control.GffLine;

import java.util.ArrayList;
import java.util.HashMap;

public class GffFile {
    private ArrayList<GffLine> raw_lines = new ArrayList<>();
    private ArrayList<GffLine> lines = new ArrayList<>();
    private boolean sorted = false;
    private String summary = "";

    public GffFile(ArrayList<String> fileLines, String path) {
        for(String gff_line : fileLines){
            if (!gff_line.startsWith("#")) {
                try {
                    GffLine temporary_gff_line_object = new GffLine(gff_line);
                    this.raw_lines.add(temporary_gff_line_object);
                }
                catch (ArrayIndexOutOfBoundsException ex) {}
            }
        }
        // Make summary of file.
        this.makeSummary(path);
    }

    public void applyFilters(String[] filters){
        if (!this.raw_lines.isEmpty()) {
            for(GffLine line : this.raw_lines){
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
        ArrayList<String> checked_molecules = new ArrayList<>();
        int total_no_features = this.raw_lines.size();
        // for each molecule build a string with information.
        if (!this.raw_lines.isEmpty()) {
            for (GffLine gff_line : this.raw_lines) {
                String currect_molecule = gff_line.getSeqname();
                // check if molecule does not exists in check_molecules.
                if (!checked_molecules.contains(currect_molecule)) {
                    // if it does not, add it
                    checked_molecules.add(currect_molecule);
                    // make info vars if it is the same seqname.
                    int no_features = 0;
                    HashMap<String,Integer> feature_counts = new HashMap();
                    // walk through each line with the same name and add information
                    for (GffLine gff_line_single_mol : this.raw_lines) {
                        if (currect_molecule.equals(gff_line_single_mol.getSeqname())) {
                            // add to tempinfo
                            no_features += 1;
                            if (!feature_counts.containsKey(gff_line_single_mol.getFeature())) {
                                feature_counts.put(gff_line_single_mol.getFeature(), 1);
                            }
                            else {
                                // update hashmap if feature exists within.
                                feature_counts.put(gff_line_single_mol.getFeature(),
                                        feature_counts.get(gff_line_single_mol.getFeature())+1);
                            }
                        }
                    }
                    // after the for loop, dump all info of the current molecule in the summary string.
                    this.summary += currect_molecule+"\n";
                    this.summary += "\tnumber of features\t"+no_features+"\n";
                    for (String key : feature_counts.keySet()) {
                        int value = feature_counts.get(key);
                        this.summary += "\t\t"+key+"\t\t\t\t"+Integer.toString(value)+"\n";
                    }
                }
            }
            // after the complete for loop, add up the total number of featues into the summary.
            this.summary = String.format(this.summary, Integer.toString(total_no_features));
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
