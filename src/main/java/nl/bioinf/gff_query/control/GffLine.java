package nl.bioinf.gff_query.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

public class GffLine {
    private String raw_line = "";
    private String seqname = "";
    private String source = "";
    private String feature = "";
    private int start = 0;
    private int end = 0;
    private int size = 0;
    private String score = "";
    private char strand = ' ';
    private char frame  = ' ';
    private HashMap<String, String> grouping_attr = new HashMap<>();


    public GffLine(String line) {
        this.raw_line = line;
        String[] split_line = line.split("\t| ");
        // filter out empty items
        List<String> list = new ArrayList<>();
        for(String s : split_line) {
            if(s != null && s.length() > 0) {
                list.add(s);
            }
        }
        // define all separate attributes.
        split_line = list.toArray(new String[list.size()]);
        this.seqname = split_line[0];
        this.source = split_line[1];
        this.feature = split_line[2];
        this.start = parseInt(split_line[3]);
        this.end = parseInt(split_line[4]);
        this.size = this.end-this.start;
        this.score = split_line[5];
        this.strand = split_line[6].charAt(0);
        this.frame = split_line[7].charAt(0);
        for (String attribute: split_line[8].split(";")) {
            String[] splitted_attribute = attribute.split("=");
            String my_key = splitted_attribute[0].toUpperCase();
            String my_value = splitted_attribute[1];
            this.grouping_attr.put(my_key, my_value);
        }
    }

    public String getRaw() {
        return this.raw_line;
    }

    private boolean check_fetch_type(String fetch_type) {
        return (this.feature.equals(fetch_type) || fetch_type.equals("false"));
    }

    private boolean check_fetch_region(String fetch_region) {
        if (fetch_region.equals("false")) {
            return true;
        }
        // match regex.
        String[] fetch_region_split = fetch_region.split("\\.\\.");
        int region_start = parseInt(fetch_region_split[0]);
        int region_end = parseInt(fetch_region_split[1]);
        // check if region is within scope.
        return (region_start <= this.start && region_end >= this.end);
    }

    private boolean check_filter(String filter) {
        if (filter.equals("false")) {
            return true;
        }
        // The filter should be specified using the format "source|score|orientation|maximum_length|minimum_length",
        // where suppression of an individual filter is indicated using an asterisk (*).
        String[] filter_split = filter.split("\\|");
        //System.out.println("filter_split = " + Arrays.toString(filter_split));
        String filter_source = filter_split[0];
        String filter_score = filter_split[1];
        String filter_orientation = filter_split[2];
        String filter_maximum_length = filter_split[3];
        String filter_minimum_length = filter_split[4];
        // check if source matches
        if (!filter_source.equals(this.source) && !filter_source.equals("*")) {
            return false;
        }
        // check if score matches
        if (!filter_score.equals(this.score) && !filter_score.equals("*")) {
            return false;
        }
        // check if the orientation matches
        if (filter_orientation.charAt(0) != this.strand && !filter_orientation.equals("*")) {
            return false;
        }
        // check if the maxlength is within scope.
        if (!filter_maximum_length.equals("*")) {
            if (parseInt(filter_maximum_length) < this.size) {
                return false;
            }
        }
        // check if the minlength is within scope.
        if (!filter_minimum_length.equals("*")) {
            if (parseInt(filter_minimum_length) > this.size) {
                return false;
            }
        }
        // if no checks are triggered, everything is alright, return true.
        return true;
    }

    private boolean check_fetch_children(String fetch_children) {
        if (fetch_children.equals("false")) {
            return true;
        }
        // first check if it contains the parent key at all
        if (!this.grouping_attr.containsKey("PARENT")) {
            return false;
        }
        // if it does, check if it matches.
        else {
            if (!this.grouping_attr.get("PARENT").equals(fetch_children)) {
                return false;
            }
        }
        // return true when all checks don't raise any problems.
        return true;
    }

    private boolean check_find_wildcard(String find_wildcard) {
        if (find_wildcard.equals("(.*)false(.*)")) {
            return true;
        }
        // first check if it contains the name key at all
        if (!this.grouping_attr.containsKey("NAME")) {
            return false;
        }
        // if it does, check if it matches.
        else{
            if (!this.grouping_attr.get("NAME").matches(find_wildcard)) {
                return false;
            }
        }
        return true;
    }

    public boolean amICorrect(String[] filters) {
        // defined filters loosely for easier calling later on.
        String fetch_type = filters[0];
        String fetch_region = filters[1];
        String filter = filters[2];
        String fetch_children = filters[3];
        String find_wildcard = "(.*)"+filters[4]+"(.*)";

        // do all checks, return false if the line does not comply. return true otherwise at the end (default)
        // everything that is the string "false" is correct, since this means that the argument was not given.
        boolean correct = true;
        // check fetch type.
        correct = check_fetch_type(fetch_type);
        // check fetch region
        if (correct) {correct = check_fetch_region(fetch_region);}
        // check filter.
        if (correct) {correct = check_filter(filter);}
        // check fetch_children
        if (correct) {correct = check_fetch_children(fetch_children);}
        // check find wildcard
        if (correct) {correct = check_find_wildcard(find_wildcard);}

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
