package nl.bioinf.gff_query.io;

import org.apache.commons.cli.*;
import java.io.File;

public class CLIParser {
    static final String OPTION_HELP = "help";
    static final String OPTION_INFILE = "infile";
    static final String OPTION_SUMMARY = "summary";
    static final String OPTION_FETCH_TYPE = "fetch_type";
    static final String OPTION_FETCH_REGION = "fetch_region";
    static final String OPTION_FETCH_CHILDREN = "fetch_children";
    static final String OPTION_FIND_WILDCARD = "find_wildcard";
    static final String OPTION_FILTER = "filter";
    /*private -> testing*/ Options options;
    /*private -> testing*/ CommandLine commandLine;
    private DefaultParser parser;
    private HelpFormatter formatter;
    private Options helpOptions;

    public CLIParser() {
        this.parser = new DefaultParser();
        buildOptions();
    }

    private void buildOptions() {
        this.options = new Options();
        this.helpOptions = new Options();
        /*builds the options*/
        Option helpOption = Option.builder("h")
                .longOpt(OPTION_HELP)
                .required(false)
                .hasArg(false)
                .desc("Gives usage instructions")
                .build();
        options.addOption(helpOption);
        helpOptions.addOption(helpOption);
        Option infileOption = Option.builder("i")
                .longOpt(OPTION_INFILE)
                .required(true)
                .hasArg(true)
                .desc("The input gff3 file, as absolute or relative path")
                .build();
        options.addOption(infileOption);
        Option summaryOption = Option.builder("s")
                .longOpt(OPTION_SUMMARY)
                .required(false)
                .hasArg(false)
                .desc("lists a summary of the gff3 file")
                .build();
        options.addOption(summaryOption);
        Option fetchtypeOption = Option.builder("ft")
                .longOpt(OPTION_FETCH_TYPE)
                .required(false)
                .hasArg(true)
                .desc("Lists all features of the requested type, e.g. \"CDS\"")
                .build();
        options.addOption(fetchtypeOption);
        Option fetchregionOption = Option.builder("fr")
                .longOpt(OPTION_FETCH_REGION)
                .required(false)
                .hasArg(true)
                .desc("Lists all the features that reside completely within the given region; specified as start..end, e.g. \"250000..260000\"")
                .build();
        options.addOption(fetchregionOption);
        Option fetchchildrenOption = Option.builder("fc")
                .longOpt(OPTION_FETCH_CHILDREN)
                .required(false)
                .hasArg(true)
                .desc("Lists all child features of the given feature ID, e.g. \"PGSC0003DMT400039136\"")
                .build();
        options.addOption(fetchchildrenOption);
        Option findwildcardOption = Option.builder("fw")
                .longOpt(OPTION_FIND_WILDCARD)
                .required(false)
                .hasArg(true)
                .desc("Lists all features for which the \"name\" attribute matches the given wildcard, e.g. \"[dD]efesin\"")
                .build();
        options.addOption(findwildcardOption);
        Option filterOption = Option.builder("f")
                .longOpt(OPTION_FILTER)
                .required(false)
                .hasArg(true)
                .desc("Lists all features that pass all of the given filters dspecified in the format string defined as" +
                        ": \"source|score|orientation|maximum_length|minimum_length\" where the filters should be relevant to the " +
                        "given attribute")
                .build();
        options.addOption(filterOption);
    }

    public void printHelp() {
        this.formatter = new HelpFormatter();
        String footer = "\nfor path, both windows and linux paths work. When specifying a relative path from the " +
                "currect working directory however, watch out that you DO NOT prepend you path with a \"/\". e.g.:" +
                " NOT /data/gene_sample.gff3 but data/gene_sample.gff3\n\n" +
                "When specifying the filter argument, it has to in the following format;" +
                " source|score|orientation|maximum_length|minimum_length\", where suppression of an individual " +
                "filter is indicated using an asterisk (*)." +
                "\tSOURCE should filter on source attribute" +
                "\tSCORE should filter on score attribute" +
                "\tORIENTATION should be defined using a \"+\", \"-\" or \".\" character" +
                "\tMINIMUM LENGTH should be defined using an integer" +
                "\tMAXIMUM LENGTH should be defined using an integer";

        this.formatter.printHelp("GffQuery", "Version: 1.1-SNAPSHOT", this.options, footer, true);
    }

    public boolean isHelpRequested(String[] args) {
        try {
            CommandLine commandLine = this.parser.parse(this.options, args);
            return (this.commandLine.hasOption(OPTION_HELP));
        } catch (ParseException e) {
            return true;
        }
    }

    public void parseCommandLineArguments(String[] args) throws ParseException {
        this.commandLine = this.parser.parse(this.options, args);
    }

    public GffAnalysisOptions getAnalysisOptions() {
        GffAnalysisOptions gffAnalysisOptions = new GffAnalysisOptions();
        if (this.commandLine.hasOption(OPTION_INFILE)) {
            gffAnalysisOptions.setInFile(this.commandLine.getOptionValue("infile"));
        }
        if (this.commandLine.hasOption(OPTION_SUMMARY)) {
            gffAnalysisOptions.setSummary(true);
        }
        if (this.commandLine.hasOption(OPTION_FETCH_TYPE)) {
            gffAnalysisOptions.setSearchType(this.commandLine.getOptionValue("fetch_type"));
        }
//        if (this.commandLine.hasOption(OPTION_FETCH_REGION)) {parsed_args[3] = this.commandLine.getOptionValue("fetch_region");}
//        else {parsed_args[3] = "false";}
//        if (this.commandLine.hasOption(OPTION_FETCH_REGION)) {parsed_args[4] = this.commandLine.getOptionValue("fetch_children");}
//        else {parsed_args[4] = "false";}
//        if (this.commandLine.hasOption(OPTION_FIND_WILDCARD)) {parsed_args[5] = this.commandLine.getOptionValue("find_wildcard");}
//        else {parsed_args[5] = "false";}
//        if (this.commandLine.hasOption(OPTION_FILTER)) {parsed_args[6] = this.commandLine.getOptionValue("filter");}
//        else {parsed_args[6] = "false";}

        return null;
    }



    public String[] returnArguments() {
        String[] parsed_args = new String[7];
        // build parsed_args array.
        // it is filled with options/null respectively like so;
        // infile, summary, fetch_type, fetch_region, fetch_children, find_wildcard, filter
        // Options without arguments, will be false if not present, and true when present.
        // Options with arguments, will also be false if not present, and their getOptionValue when present.
        // keep all values strings to be consistent.
        if (this.commandLine.hasOption(OPTION_INFILE)) {parsed_args[0] = this.commandLine.getOptionValue("infile");}
        else {parsed_args[0] = "false";}
        if (this.commandLine.hasOption(OPTION_SUMMARY)) {parsed_args[1] = "true";}
        else {parsed_args[1] = "false";}
        if (this.commandLine.hasOption(OPTION_FETCH_TYPE)) {parsed_args[2] = this.commandLine.getOptionValue("fetch_type");}
        else {parsed_args[2] = "false";}
        if (this.commandLine.hasOption(OPTION_FETCH_REGION)) {parsed_args[3] = this.commandLine.getOptionValue("fetch_region");}
        else {parsed_args[3] = "false";}
        if (this.commandLine.hasOption(OPTION_FETCH_REGION)) {parsed_args[4] = this.commandLine.getOptionValue("fetch_children");}
        else {parsed_args[4] = "false";}
        if (this.commandLine.hasOption(OPTION_FIND_WILDCARD)) {parsed_args[5] = this.commandLine.getOptionValue("find_wildcard");}
        else {parsed_args[5] = "false";}
        if (this.commandLine.hasOption(OPTION_FILTER)) {parsed_args[6] = this.commandLine.getOptionValue("filter");}
        else {parsed_args[6] = "false";}

        return parsed_args;
    }


    public boolean checkInputs() {
        // check which inputs exist and check them (if neccesary)
        // check infile
        if (this.commandLine.hasOption(OPTION_INFILE)) {
            File f = new File(this.commandLine.getOptionValue(OPTION_INFILE));
            if(!(f.exists() && !f.isDirectory())) {
                // raise error that file does not exist.
                return false;
            }
        }
        // check filter
        if (this.commandLine.hasOption(OPTION_FILTER)) {
            // check if filter has correct formatting. If it does not, return false
            // <SOURCE, SCORE, ORIENTATION MAXIMUM AND/OR MINIMUM LENGTH>
            if (!this.commandLine.getOptionValue("filter").matches(
                    "(.+\\|(\\d+|\\*|\\.)\\|(\\.|\\+|\\-|\\*)\\|(\\d+|\\*|\\.)\\|(\\d+|\\*|\\.))")) {
                return false;
            }
        }
        // check fetch region
        if (this.commandLine.hasOption(OPTION_FETCH_REGION)) {
            // check if fetch region has correct formatting.
            if (!this.commandLine.getOptionValue(OPTION_FETCH_REGION).matches("\\d+\\.\\.\\d+")){
                return false;
            }
        }

        if (this.commandLine.hasOption(OPTION_FETCH_TYPE)) {
            // check if not a bool.
            if (this.commandLine.getOptionValue(OPTION_FETCH_TYPE).matches("(true|false)")) {
                return false;
            }
        }
        if (this.commandLine.hasOption(OPTION_FETCH_CHILDREN)) {
            // check if not a bool.
            if (this.commandLine.getOptionValue(OPTION_FETCH_CHILDREN).matches("(true|false)")) {
                return false;
            }
        }
        if (this.commandLine.hasOption(OPTION_FIND_WILDCARD)) {
            // check if not a bool.
            if (this.commandLine.getOptionValue(OPTION_FIND_WILDCARD).matches("(true|false)")) {
                return false;
            }
        }
        return true;
    }
}