package nl.bioinf.gff_query.io;

import nl.bioinf.gff_query.model.GffAnalysisOptions;
import org.apache.commons.cli.*;

public class CLIParser {
//    these two replaced by enum example
//    public static final String OPTION_HELP = "help";
//    public static final String OPTION_INFILE = "infile";
    public static final String OPTION_SUMMARY = "summary";
    public static final String OPTION_FETCH_TYPE = "fetch_type";
    public static final String OPTION_FETCH_REGION = "fetch_region";
    public static final String OPTION_FETCH_CHILDREN = "fetch_children";
    public static final String OPTION_FIND_WILDCARD = "find_wildcard";
    public static final String OPTION_FILTER = "filter";
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
        Option helpOption = Option.builder(CliOption.HELP.shortOpt())
                .longOpt(CliOption.HELP.longOpt())
                .required(false)
                .hasArg(false)
                .desc(CliOption.HELP.description())
                .build();
        options.addOption(helpOption);
        helpOptions.addOption(helpOption);
        Option infileOption = Option.builder(CliOption.INFILE.shortOpt())
                .longOpt(CliOption.INFILE.longOpt())
                .required(true)
                .hasArg(true)
                .desc(CliOption.INFILE.description())
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
                .desc("Lists all features for which the \"name\" attribute matches the given wildcard, specified as regex pattern, " +
                        "e.g. \"[dD]efesin\"")
                .build();
        options.addOption(findwildcardOption);
        Option filterOption = Option.builder("f")
                .longOpt(OPTION_FILTER)
                .required(false)
                .hasArg(true)
                .desc("Lists all features that pass all of the given filters specified in the format string defined as" +
                        ": \"source|score|orientation|maximum_length|minimum_length\" where the filters should be relevant to the " +
                        "given attribute. Suppression of an individual filter is indicated using an asterisk (*)." +
                        "ORIENTATION should be defined using a \"+\", \"-\" or \".\" character")
                .build();
        options.addOption(filterOption);
    }

    public void printHelp() {
        this.formatter = new HelpFormatter();
        String footer = "\nPaths are platform independent.";
        this.formatter.printHelp("GffQuery", "Version: 1.1-SNAPSHOT", this.options, footer, true);
    }

    public boolean isHelpRequested(String[] args) {
        try {
            CommandLine commandLine = this.parser.parse(this.options, args);
            return (commandLine.hasOption(CliOption.HELP.shortOpt()));
        } catch (ParseException e) {
            return true;
        }
    }

    public void parseCommandLineArguments(String[] args) throws ParseException {
        this.commandLine = this.parser.parse(this.options, args);
    }

    public GffAnalysisOptions getAnalysisOptions() {
        GffAnalysisOptions gffAnalysisOptions = new GffAnalysisOptions();
        if (this.commandLine.hasOption(CliOption.INFILE.longOpt())) {
            gffAnalysisOptions.setInFile(this.commandLine.getOptionValue(CliOption.INFILE.longOpt()));
        }
        if (this.commandLine.hasOption(OPTION_SUMMARY)) {
            gffAnalysisOptions.setSummary(true);
        }
        if (this.commandLine.hasOption(OPTION_FETCH_TYPE)) {
            gffAnalysisOptions.setSearchType(this.commandLine.getOptionValue(OPTION_FETCH_TYPE));
        }
        if (this.commandLine.hasOption(OPTION_FETCH_REGION)) {
            gffAnalysisOptions.setSearchRegion(this.commandLine.getOptionValue(OPTION_FETCH_REGION));
        }
        if (this.commandLine.hasOption(OPTION_FETCH_CHILDREN)) {
            gffAnalysisOptions.setSearchChildren(this.commandLine.getOptionValue(OPTION_FETCH_CHILDREN));
        }
        if (this.commandLine.hasOption(OPTION_FIND_WILDCARD)) {
            gffAnalysisOptions.setSearchWildcard(this.commandLine.getOptionValue(OPTION_FIND_WILDCARD));
        }
        if (this.commandLine.hasOption(OPTION_FILTER)) {
            gffAnalysisOptions.setSearchFilterAsString(this.commandLine.getOptionValue(OPTION_FILTER));
        }
        return gffAnalysisOptions;
    }

}