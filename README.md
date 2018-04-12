# knapsack-heuristics
Implementation of constructive and perturbative local search algorithms for the Multidimensional 0/1 Knapsack Problem.

## Benchmarking

### Constructive heuristics
```bash
 java -jar benchmarks.jar cTestToyodaCH bTestGreedyCH aTestRandomCH -rff jmh-ch-results.csv -rf csv -wi 0 | tee jmh-ch-results.log
 ```
### Best Improvement
```bash
 java -jar benchmarks.jar fTestToyodaBI eTestGreedyBI dTestRandomBI -rff jmh-bi-results.csv -rf csv -wi 0 | tee jmh-bi-results.log
 ```
### First Improvement
```bash
 java -jar benchmarks.jar iTestToyodaFI hTestGreedyFI gTestRandomFI -rff jmh-fi-results.csv -rf csv -wi 0 | tee jmh-fi-results.log
 ```
### VND Improvement
```bash
 java -jar benchmarks.jar lTestToyodaVND -rff jmh-vnd-results-nornd.csv -rf csv -wi 0 -f 0 | tee jmh-vnd-results-nornd.log
 ```
```bash
 java -jar benchmarks.jar lTestToyodaVND -rff jmh-vnd-results-toyoda-10x250-0.50_1.csv -rf csv -wi 0 -f 0 | tee jmh-vnd-results-toyoda-10x250-0.50_1.log
 ```
```bash
 java -jar benchmarks.jar kTestGreedyVND -rff jmh-vnd-results-greedy-10x250-0.50_1.csv -rf csv -wi 0 -f 0 | tee jmh-vnd-results-greedy--10x250-0.50_1.log
 ```
```bash
 java -jar benchmarks.jar jTestRandomVND -rff jmh-vnd-rnd-results-10x250-0.50_1.csv -rf csv -wi 0 -i 1 -f 0 | tee jmh-vnd-rnd-results--10x250-0.50_1.log
 ```