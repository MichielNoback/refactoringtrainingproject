### README ###
#### GffQuery - Casper Peters (Berghopper) ####

This is my repo for the final assignment gff query, only raw code will be submitted here along with gradle files to build a jar.

The data folder contains example files for testing the script. Info on the gff format can be found [here](https://github.com/The-Sequence-Ontology/Specifications/blob/master/gff3.md)

After the jar is built, you can run it.

Usage; usage: `java -jar GffQuery-Jar-1.1-SNAPSHOT.jar [-f <arg>] [-fc <arg>] [-fr <arg>] [-ft <arg>] [-fw <arg>]
              [-h] -i <arg> [-s]`

For further explanation, use the help defined below;

```
 -f,--filter <arg>            miscellaneous filters, defined like so:
                              "source|score|orientation|maximum_length|min
                              imum_length"
 -fc,--fetch_children <arg>   input which Parent group attributes to sort
                              on, e.g. "PGSC0003DMT400039136".
 -fr,--fetch_region <arg>     fetches region in between start..end, e.g.
                              "250000..260000"
 -ft,--fetch_type <arg>       fetches type, e.g. "CDS"
 -fw,--find_wildcard <arg>    input which wildcard, e.g. "[dD]efesin" to
                              match on. (gff3 name attribute)
 -h,--help                    Gives program help.
 -i,--infile <arg>            input gff3 file, e.g. "gff3_sample.gff3"
 -s,--summary                 makes a summary of the gff3 file.

for path, both windows and linux paths work. When specifying a relative
path from the currect working directory however, watch out that you DO NOT
prepend you path with a "/". e.g.: NOT /data/gene_sample.gff3 but
data/gene_sample.gff3

When specifying the filter argument, it has to in the following format;
source|score|orientation|maximum_length|minimum_length", where suppression
of an individual filter is indicated using an asterisk (*).
SOURCE should filter on source attribute
SCORE should filter on score attribute
ORIENTATION should be defined using a "+", "-" or "." character
MINIMUM LENGTH should be defined using an integer
MAXIMUM LENGTH should be defined using an integer
```