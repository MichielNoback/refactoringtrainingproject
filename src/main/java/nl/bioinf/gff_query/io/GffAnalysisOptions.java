package nl.bioinf.gff_query.io;

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

    public boolean isSummary() {
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

    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
    }
}
