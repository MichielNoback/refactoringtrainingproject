package nl.bioinf.gff_query.io;

import java.io.File;

public class GffAnalysisOptions {

    private String inFile;
    private boolean summary;
    private String searchType;
    private String searchRegion;
    private String searchChildren;
    private String searchWildcard;
    private String searchFilter;

    public String getInFile() {
        return inFile;
    }

    public void setInFile(String inFile) {
        this.inFile = inFile;
    }

    public boolean isSummaryRequested() {
        return summary;
    }

    public void setSummary(boolean summary) {
        this.summary = summary;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchRegion() {
        return searchRegion;
    }

    public void setSearchRegion(String searchRegion) {
        this.searchRegion = searchRegion;
    }

    public String getSearchChildren() {
        return searchChildren;
    }

    public void setSearchChildren(String searchChildren) {
        this.searchChildren = searchChildren;
    }

    public String getSearchWildcard() {
        return searchWildcard;
    }

    public void setSearchWildcard(String searchWildcard) {
        this.searchWildcard = searchWildcard;
    }

    public String getSearchFilter() {
        return searchFilter;
    }

    public void setSearchFilterAsString(String searchFilter) {
        this.searchFilter = searchFilter;
    }
    /**
     * check correctness of options that were provided
     * @return
     */
    public void checkCorrectnessOfInputs() {
        if (this.inFile != null) {
            File f = new File(this.inFile);
            if(!(f.exists() && !f.isDirectory())) {
                throw new IllegalArgumentException("File " + inFile
                        + " does not exist or is a directory");
            }
        }
        if (this.searchFilter != null
                && ! this.searchFilter.matches("(.+\\|(\\d+|\\*|\\.)\\|(\\.|\\+|\\-|\\*)\\|(\\d+|\\*|\\.)\\|(\\d+|\\*|\\.))")) {
            // <SOURCE, SCORE, ORIENTATION MAXIMUM AND/OR MINIMUM LENGTH>
            throw new IllegalArgumentException("The search filter is not specified correctly: "
                    + searchFilter);
        }
//        // check fetch region
//        if (this.commandLine.hasOption(OPTION_FETCH_REGION)) {
//            // check if fetch region has correct formatting.
//            if (!this.commandLine.getOptionValue(OPTION_FETCH_REGION).matches("\\d+\\.\\.\\d+")){
//                return false;
//            }
//        }
//
//        if (this.commandLine.hasOption(OPTION_FETCH_TYPE)) {
//            // check if not a bool.
//            if (this.commandLine.getOptionValue(OPTION_FETCH_TYPE).matches("(true|false)")) {
//                return false;
//            }
//        }
//        if (this.commandLine.hasOption(OPTION_FETCH_CHILDREN)) {
//            // check if not a bool.
//            if (this.commandLine.getOptionValue(OPTION_FETCH_CHILDREN).matches("(true|false)")) {
//                return false;
//            }
//        }
//        if (this.commandLine.hasOption(OPTION_FIND_WILDCARD)) {
//            // check if not a bool.
//            if (this.commandLine.getOptionValue(OPTION_FIND_WILDCARD).matches("(true|false)")) {
//                return false;
//            }
//        }
    }

}
