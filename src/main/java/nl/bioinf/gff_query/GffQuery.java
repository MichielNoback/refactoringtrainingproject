package nl.bioinf.gff_query;

import nl.bioinf.gff_query.io.FileReader;
import nl.bioinf.gff_query.io.GffAnalysisOptions;
import nl.bioinf.gff_query.model.GffFile;
import nl.bioinf.gff_query.io.CLIParser;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.ArrayList;

public class GffQuery {
    public static void main(String[] args) throws IOException {
        CLIParser cliParser = new CLIParser();
        if (cliParser.isHelpRequested(args)) {
            cliParser.printHelp();
            return;
        }
        try {
            cliParser.parseCommandLineArguments(args);
        } catch(ParseException ex) {
            cliParser.printHelp();
            return;
        }
        GffAnalysisOptions analysisOptions = cliParser.getAnalysisOptions();

        //TODO it would be really nice to proceed from here with refactoring

        // if this worked, go on and grab all the arguments.
        // but first check if the inputs are correct.
        boolean correctInputs = analysisOptions.checkCorrectnessOfInputs();
        if (!correctInputs) {
            System.out.println("Something went wrong when checking inputs, some may be incorrect, please " +
                    "refer to the help page or check if your input file is correct.");
        }
        else {
            // handle stuff based on the case.
            int mycase;
            if (analysisOptions.isSummaryRequested()) {
                // case 1, print summary.
                mycase = 1;
            }
            else {
                // case 2, actually apply filters and stuff.
                mycase = 2;
            }
            // before running the switch, read in file and make a GffFile object
            String path = analysisOptions.getInFile();
            FileReader filereader = new FileReader();
            // read in the raw file and catch the resulting ArrayList
            ArrayList fileArrayList = filereader.readFile(path);
            if (fileArrayList.isEmpty()) {
                mycase = 3;
            }
            // parse it to a GffFile object.
            GffFile myGffFile = new GffFile(fileArrayList, path);
            switch (mycase) {
                case 1:
                    // do summary, not implemented as of yet.
                    myGffFile.printSummary();
                    break;
                case 2:
                    // apply all filters.
                    // create filters array
                    String[] filters = new String[5];
                    // filters has all filters like so;
                    // fetch_type, fetch_region, filter, fetch_children, find_wildcard
                    // parsedArguments had this;
                    // infile, summary, fetch_type, fetch_region, fetch_children, find_wildcard, filter
                    filters[0] = analysisOptions.getSearchType();
                    filters[1] = analysisOptions.getSearchRegion();
                    filters[2] = analysisOptions.getSearchFilter();
                    filters[3] = analysisOptions.getSearchChildren();
                    filters[4] = analysisOptions.getSearchWildcard();
                    // put in filters and print results
                    myGffFile.applyFilters(filters);
                    if (myGffFile.toString().isEmpty()) {
                        System.out.println("Oops, no lines found! Try to apply different filters. or double " +
                                "check them. It might also be possible that the file did not contain any" +
                                " valid gff3 formatted lines.");
                    }
                    System.out.print(myGffFile.toString());
                    break;
                case 3:
                    System.out.println("empty or corrupt file.");
                    break;
            }

        }
    }



}
