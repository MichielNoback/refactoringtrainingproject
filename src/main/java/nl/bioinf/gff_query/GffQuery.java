package nl.bioinf.gff_query;

import nl.bioinf.gff_query.model.GffAnalysisOptions;
import nl.bioinf.gff_query.model.GffFileReader;
import nl.bioinf.gff_query.io.CLIParser;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.ArrayList;

public class GffQuery {
    private GffAnalysisOptions analysisOptions;
    private boolean optionsProcessedIncorrectly = true;
//    private ProgramBehaviour programBehaviour;
    private GffFileReader myGffFileReader;
    private ArrayList<String> fileArrayList;

    public static void main(String[] args) throws IOException {
        //GET OUT OF STATIC!
        GffQuery gffQuery = new GffQuery();
        gffQuery.start(args);
    }

    private void start(String[] args) {
        processCommandLine(args);
        if (this.optionsProcessedIncorrectly) {
            return;
        }

        try {
            analysisOptions.checkCorrectnessOfInputs();
        } catch (IllegalArgumentException ex) {
            System.err.println("An error occurred. Avalaible info: " + ex.getMessage()
                    + "\nTry '--help' for instructions");
            return;
        }
        if (analysisOptions.isSummaryRequested()) {
            //this.programBehaviour = new SummaryPrinter();
            myGffFileReader.printSummary();
            return;
        }

        /*TODO refactor use of fileArrayList and FileReader away*/
        this.myGffFileReader = new GffFileReader(analysisOptions.getInFile());
        myGffFileReader.readFile();

        if (myGffFileReader.emptyDataFile()) {
            System.err.println("empty or corrupt file.");
            return;
        }

        myGffFileReader.applyFilters(analysisOptions);
    }

    private void processCommandLine(String[] args) {
        CLIParser cliParser = new CLIParser();
        if (cliParser.isHelpRequested(args)) {
            cliParser.printHelp();
            this.optionsProcessedIncorrectly = true;
            return;
        }
        try {
            cliParser.parseCommandLineArguments(args);
        } catch(ParseException ex) {
            cliParser.printHelp();
            this.optionsProcessedIncorrectly = true;
            return;
        }
        this.analysisOptions = cliParser.getAnalysisOptions();
    }


//    private interface ProgramBehaviour {
//        void execute();
//    }
//
//    private final class SummaryPrinter implements ProgramBehaviour {
//        @Override
//        public void execute() {
//            myGffFileReader.printSummary();
//        }
//    }

}
