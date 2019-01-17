package nl.bioinf.gff_query.io;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CLIParserTest {
    private CLIParser cliParser;
    @BeforeEach
    void init(){
        cliParser = new CLIParser();
    }

    @Test
    void buildOptions() {
        final Options options = this.cliParser.options;
        assert(options.getOption(CLIParser.OPTION_FILTER) != null);
        assert(options.getOption(CLIParser.OPTION_FILTER).getLongOpt().equals(CLIParser.OPTION_FILTER));
        assert(options.getOption(CLIParser.OPTION_FILTER).getOpt().equals("f"));
        //many assertions can be added here for a full test suite of the option building process
    }

    @Test
    void isHelpRequestedEmpty() {
        String[] empty = {};
        assertTrue(cliParser.isHelpRequested(empty));
    }

    @Test
    void isHelpRequestedReal() {
        String[] args = {"-h"};
        assertTrue(cliParser.isHelpRequested(args));
        String[] args2 = {"--help"};
        assertTrue(cliParser.isHelpRequested(args2));
    }

    @Test
    void parseCommandLineArgumentsEmpty() {
        String[] empty = {};
        Executable executable = () -> cliParser.parseCommandLineArguments(empty);
        assertThrows(ParseException.class, executable);
    }

    @Test
    void parseCommandLineArgumentsHelp() throws ParseException {
        String[] args = {"-h"};
        Executable executable = () -> cliParser.parseCommandLineArguments(args);
        assertThrows(ParseException.class, executable);

        String[] args2 = {"--help"};
        executable = () -> cliParser.parseCommandLineArguments(args2);
        assertThrows(ParseException.class, executable);
    }

    @Test
    void parseCommandLineArgumentsNoInputFile() throws ParseException {
        String[] args = {"-s"};
        Executable executable = () -> cliParser.parseCommandLineArguments(args);
        assertThrows(ParseException.class, executable);

        String[] args2 = {"--summary"};
        executable = () -> cliParser.parseCommandLineArguments(args2);
        assertThrows(ParseException.class, executable);
    }

    @Test
    void parseCommandLineArgumentsSmallFileSummary() throws ParseException {
        cliParser.parseCommandLineArguments(CommandLineArgsExamples.ARGS_REQUESTING_SUMMARY_OF_SMALL_FILE);
        assertTrue(cliParser.commandLine.hasOption(CLIParser.OPTION_SUMMARY));
        assertTrue(cliParser.commandLine.hasOption(CLIParser.OPTION_INFILE));
    }

    @Test
    void testReturnArguments() throws ParseException {
        cliParser.parseCommandLineArguments(CommandLineArgsExamples.ARGS_REQUESTING_SUMMARY_OF_SMALL_FILE);
        System.out.println("cliParser.returnArguments() = " + Arrays.toString(cliParser.returnArguments()));
    }

}