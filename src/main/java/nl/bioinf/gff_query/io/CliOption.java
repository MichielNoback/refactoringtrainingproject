package nl.bioinf.gff_query.io;

public enum CliOption {
    HELP("h", "help", "Gives usage instructions"),
    INFILE("i", "infile", "The input gff3 file, as absolute or relative path");

    private final String shortOpt;
    private final String longOpt;
    private final String description;

    CliOption(String shortOpt, String longOpt, String description) {
        this.shortOpt = shortOpt;
        this.longOpt = longOpt;
        this.description = description;
    }

    public String shortOpt() {
        return shortOpt;
    }

    public String longOpt() {
        return longOpt;
    }

    public String description() {
        return description;
    }
}
