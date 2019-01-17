package nl.bioinf.gff_query.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

public class GffLine {
    private String rawLine = "";
    private String seqname = "";
    private String source = "";
    private String feature = "";
    private int start = 0;
    private int end = 0;
    private int size = 0;
    private String score = "";
    private char strand = ' ';
    private char frame  = ' ';
    private HashMap<String, String> groupingAttr = new HashMap<>();


    public GffLine(String line) {
        this.rawLine = line;
        String[] splitLine = line.split("\t| ");
        // filter out empty items
        List<String> list = new ArrayList<>();
        for(String s : splitLine) {
            if(s != null && s.length() > 0) {
                list.add(s);
            }
        }
        // define all separate attributes.
        splitLine = list.toArray(new String[list.size()]);
        this.seqname = splitLine[0];
        this.source = splitLine[1];
        this.feature = splitLine[2];
        this.start = parseInt(splitLine[3]);
        this.end = parseInt(splitLine[4]);
        this.size = this.end-this.start;
        this.score = splitLine[5];
        this.strand = splitLine[6].charAt(0);
        this.frame = splitLine[7].charAt(0);
        for (String attribute: splitLine[8].split(";")) {
            String[] splitted_attribute = attribute.split("=");
            String my_key = splitted_attribute[0].toUpperCase();
            String my_value = splitted_attribute[1];
            this.groupingAttr.put(my_key, my_value);
        }
    }

    public String getRaw() {
        return this.rawLine;
    }

    private boolean checkFetchType(String fetchType) {
        return (this.feature.equals(fetchType) || fetchType.equals("false"));
    }

    private boolean checkFetchRegion(String fetchRegion) {
        if (fetchRegion.equals("false")) {
            return true;
        }
        // match regex.
        String[] fetchRegionSplit = fetchRegion.split("\\.\\.");
        int regionStart = parseInt(fetchRegionSplit[0]);
        int regionEnd = parseInt(fetchRegionSplit[1]);
        // check if region is within scope.
        return (regionStart <= this.start && regionEnd >= this.end);
    }

    private boolean checkFilter(String filter) {
        if (filter.equals("false")) {
            return true;
        }
        // The filter should be specified using the format "source|score|orientation|maximum_length|minimum_length",
        // where suppression of an individual filter is indicated using an asterisk (*).
        String[] filterSplit = filter.split("\\|");
        //System.out.println("filterSplit = " + Arrays.toString(filterSplit));
        String filterSource = filterSplit[0];
        String filterScore = filterSplit[1];
        String filterOrientation = filterSplit[2];
        String filterMaximumLength = filterSplit[3];
        String filterMinimumLength = filterSplit[4];
        // check if source matches
        if (!filterSource.equals(this.source) && !filterSource.equals("*")) {
            return false;
        }
        // check if score matches
        if (!filterScore.equals(this.score) && !filterScore.equals("*")) {
            return false;
        }
        // check if the orientation matches
        if (filterOrientation.charAt(0) != this.strand && !filterOrientation.equals("*")) {
            return false;
        }
        // check if the maxlength is within scope.
        if (!filterMaximumLength.equals("*")) {
            if (parseInt(filterMaximumLength) < this.size) {
                return false;
            }
        }
        // check if the minlength is within scope.
        if (!filterMinimumLength.equals("*")) {
            if (parseInt(filterMinimumLength) > this.size) {
                return false;
            }
        }
        // if no checks are triggered, everything is alright, return true.
        return true;
    }

    private boolean checkFetchChildren(String fetchChildren) {
        if (fetchChildren.equals("false")) {
            return true;
        }
        // first check if it contains the parent key at all
        if (!this.groupingAttr.containsKey("PARENT")) {
            return false;
        }
        // if it does, check if it matches.
        else {
            if (!this.groupingAttr.get("PARENT").equals(fetchChildren)) {
                return false;
            }
        }
        // return true when all checks don't raise any problems.
        return true;
    }

    private boolean checkFindWildcard(String findWildcard) {
        if (findWildcard.equals("(.*)false(.*)")) {
            return true;
        }
        // first check if it contains the name key at all
        if (!this.groupingAttr.containsKey("NAME")) {
            return false;
        }
        // if it does, check if it matches.
        else{
            if (!this.groupingAttr.get("NAME").matches(findWildcard)) {
                return false;
            }
        }
        return true;
    }

    public boolean amICorrect(String[] filters) {
        // defined filters loosely for easier calling later on.
        String fetchType = filters[0];
        String fetchRegion = filters[1];
        String filter = filters[2];
        String fetchChildren = filters[3];
        String findWildcard = "(.*)"+filters[4]+"(.*)";

        // do all checks, return false if the line does not comply. return true otherwise at the end (default)
        // everything that is the string "false" is correct, since this means that the argument was not given.
        boolean correct = true;
        // check fetch type.
        correct = checkFetchType(fetchType);
        // check fetch region
        if (correct) {correct = checkFetchRegion(fetchRegion);}
        // check filter.
        if (correct) {correct = checkFilter(filter);}
        // check fetchChildren
        if (correct) {correct = checkFetchChildren(fetchChildren);}
        // check find wildcard
        if (correct) {correct = checkFindWildcard(findWildcard);}

        // when passed through all filters, return.
        return correct;
    }

    public String getSeqname() {
        return this.seqname;
    }

    public String getFeature() {
        return this.feature;
    }
}
