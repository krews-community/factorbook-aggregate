# Factorbook Aggregate

A Dockerized application used by Factorbook workflows to aggregate signal (from a bigWig) over peaks.

## Running

The built docker container can be found on docker hub as genomealmanac/factorbook-aggregate.

To run make sure the files you pass are accessible within the container and run the container with the command 
followed by the arguments you need:

`java -jar /app/aggregate.jar`

### Arguments

| Name |  Description |  Required | Default |
|---|---|---|---|
| `--peaks` | path to peaks in narrowPeak format | yes | |
| `--signal` | path to two-bit file for this assembly | yes | |
| `--chrom-info` | path to chromosome lengths for this assembly | yes | |
| `--offset` | Offset, in bp, to shift peaks |  no | 0 |
| `--output-dir` | Path to write output files | yes | |

Here's how a complete command with arguments should look:

```
java -jar /app/aggregate.jar \
  --peaks=/data/in/my-peaks.bed \
  --signal=/data/in/my-signal.bigWig \
  --chrom-info=/data/in/my-chrominfo.sizes \
  --output-dir=/data/out
```

## Procedure

-> Peaks (narrowPeak) \
-> Clean peaks (filter + rename) \
-> Summits (+/- 2000 from summit) \
-> Values (use bigwigvaluesoverbed, get values for each base in each peak, tsv) \
-> Aggregate (sum columns (bases), output as single-column tsv)

## File Inputs and Outputs

See `src/test/resources` for a complete example set of input and output files

### Chrom Info Files (Input)

The required chrom info file is a tab delimited file containing chromosome names and lengths in bp. For example:

```
chr1	1000000
chr2	2000000
```

## For Contributors

The scripts/ directory contains utilities you can use to build, test, and deploy

### Building

To build the docker container with a tag, use `scripts/build-image.sh`.

### Testing

To test run tests manually in IDE or use `scripts/test.sh`.

### Deploy

To deploy the image to our docker repository, use `scripts/push-image.sh`.