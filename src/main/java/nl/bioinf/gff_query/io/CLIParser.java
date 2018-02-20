package nl.bioinf.gff_query.io;

import org.apache.commons.cli.*;
import java.io.File;

public class CLIParser {
    private Options options;
    private Options help_options;
    private CommandLine cmd;
    private CommandLine help_cmd;
    private DefaultParser parser;
    private HelpFormatter formatter;

    public CLIParser() {
        // Create parser object
        this.parser = new DefaultParser();
        // create Options object
        this.options = new Options();
        this.help_options = new Options();
        // add cli options

        Option helpOption = Option.builder("h")
                .longOpt("help")
                .required(false)
                .hasArg(false)
                .desc("Gives program help.")
                .build();
        this.help_options.addOption(helpOption);
        this.options.addOption(helpOption);
        Option infileOption = Option.builder("i")
                .longOpt("infile")
                .required(true)
                .hasArg(true)
                .desc("input gff3 file, e.g. \"gff3_sample.gff3\"")
                .build();
        this.options.addOption(infileOption);
        Option summaryOption = Option.builder("s")
                .longOpt("summary")
                .required(false)
                .hasArg(false)
                .desc("makes a summary of the gff3 file.")
                .build();
        this.options.addOption(summaryOption);
        Option fetchtypeOption = Option.builder("ft")
                .longOpt("fetch_type")
                .required(false)
                .hasArg(true)
                .desc("fetches type, e.g. \"CDS\"")
                .build();
        this.options.addOption(fetchtypeOption);
        Option fetchregionOption = Option.builder("fr")
                .longOpt("fetch_region")
                .required(false)
                .hasArg(true)
                .desc("fetches region in between start..end, e.g. \"250000..260000\"")
                .build();
        this.options.addOption(fetchregionOption);
        Option fetchchildrenOption = Option.builder("fc")
                .longOpt("fetch_children")
                .required(false)
                .hasArg(true)
                .desc("input which Parent group attributes to sort on, e.g. \"PGSC0003DMT400039136\".")
                .build();
        this.options.addOption(fetchchildrenOption);
        Option findwildcardOption = Option.builder("fw")
                .longOpt("find_wildcard")
                .required(false)
                .hasArg(true)
                .desc("input which wildcard, e.g. \"[dD]efesin\" to match on. (gff3 name attribute)")
                .build();
        this.options.addOption(findwildcardOption);
        Option filterOption = Option.builder("f")
                .longOpt("filter")
                .required(false)
                .hasArg(true)
                .desc("miscellaneous filters, defined like" +
                        " so: \"source|score|orientation|maximum_length|minimum_length\"")
                .build();
        this.options.addOption(filterOption);
        // create help
        this.formatter = new HelpFormatter();
    }

    public void cliParse(String[] args) throws ParseException {
        this.cmd = this.parser.parse(this.options, args);
    }

    public boolean checkInputs() {
        // check which inputs exist and check them (if neccesary)
        // check infile
        if (this.cmd.hasOption("infile")) {
            File f = new File(this.cmd.getOptionValue("infile"));
            if(!(f.exists() && !f.isDirectory())) {
                // raise error that file does not exist.
                return false;
            }
        }
        // check filter
        if (this.cmd.hasOption("filter")) {
            // check if filter has correct formatting. If it does not, return false
            // <SOURCE, SCORE, ORIENTATION MAXIMUM AND/OR MINIMUM LENGTH>
            if (!this.cmd.getOptionValue("filter").matches(
                    "(.+\\|(\\d+|\\*|\\.)\\|(\\.|\\+|\\-|\\*)\\|(\\d+|\\*|\\.)\\|(\\d+|\\*|\\.))")) {
                return false;
            }
        }
        // check fetch region
        if (this.cmd.hasOption("fetch_region")) {
            // check if fetch region has correct formatting.
            if (!this.cmd.getOptionValue("fetch_region").matches("\\d+\\.\\.\\d+")){
                return false;
            }
        }

        if (this.cmd.hasOption("fetch_type")) {
            // check if not a bool.
            if (this.cmd.getOptionValue("fetch_type").matches("(true|false)")) {
                return false;
            }
        }
        if (this.cmd.hasOption("fetch_children")) {
            // check if not a bool.
            if (this.cmd.getOptionValue("fetch_children").matches("(true|false)")) {
                return false;
            }
        }
        if (this.cmd.hasOption("find_wildcard")) {
            // check if not a bool.
            if (this.cmd.getOptionValue("find_wildcard").matches("(true|false)")) {
                return false;
            }
        }
        return true;
    }

    public String[] returnArguments() {
        String[] parsed_args = new String[7];
        // build parsed_args array.
        // it is filled with options/null respectively like so;
        // infile, summary, fetch_type, fetch_region, fetch_children, find_wildcard, filter
        // Options without arguments, will be false if not present, and true when present.
        // Options with arguments, will also be false if not present, and their getOptionValue when present.
        // keep all values strings to be consistent.
        if (this.cmd.hasOption("infile")) {parsed_args[0] = this.cmd.getOptionValue("infile");}
        else {parsed_args[0] = "false";}
        if (this.cmd.hasOption("summary")) {parsed_args[1] = "true";}
        else {parsed_args[1] = "false";}
        if (this.cmd.hasOption("fetch_type")) {parsed_args[2] = this.cmd.getOptionValue("fetch_type");}
        else {parsed_args[2] = "false";}
        if (this.cmd.hasOption("fetch_region")) {parsed_args[3] = this.cmd.getOptionValue("fetch_region");}
        else {parsed_args[3] = "false";}
        if (this.cmd.hasOption("fetch_children")) {parsed_args[4] = this.cmd.getOptionValue("fetch_children");}
        else {parsed_args[4] = "false";}
        if (this.cmd.hasOption("find_wildcard")) {parsed_args[5] = this.cmd.getOptionValue("find_wildcard");}
        else {parsed_args[5] = "false";}
        if (this.cmd.hasOption("filter")) {parsed_args[6] = this.cmd.getOptionValue("filter");}
        else {parsed_args[6] = "false";}

        return parsed_args;
    }

    public boolean cliHelpParse(String[] args) throws ParseException {
        this.help_cmd = new DefaultParser().parse(this.help_options, args, true);
        if(this.help_cmd.hasOption("help") || this.help_cmd.getArgs().length == 0) {
            return true;
        }
        return false;
    }

    public void printHelp() {
        // print the help or the version there. return true because help is printed
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
}