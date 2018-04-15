# knapsack-heuristics
Implementation of constructive and perturbative local search algorithms for the Multidimensional 0/1 Knapsack Problem.

## How to run

In the runnable folder you can to find the jars for the simple execution and benchmark.

### Folder structure

```
├── experiment
│   ├── instances
│   │   ├── *.dat
│   ├── runnable
│   │   ├── benchmarks.jar
│   │   ├── mkp.jar
│   ├── knapsack-problem
│   │   ├── src/main/java/be/ac/ulb/kubedaar/knapsack/problem
│   │   │   ├── berk
│   │   │   │   ├── MyBenchmark.java
│   │   │   ├── impl
│   │   │   │   ├── InstanceProblem.java
│   │   │   │   ├── Solution.java
│   │   │   │   ├── Improvement.java
│   │   │   │   ├── RandomSolution.java
│   │   │   │   ├── GreedySolution.java
│   │   │   │   ├── ToyodaSolution.java
│   │   │   │   ├── FirstImprovement.java
│   │   │   │   ├── BestImprovement.java
│   │   │   │   ├── VNDImprovement.java
│   │   │   ├── test
│   │   │   │   ├── Main.java
│   │   │   │   ├── Test.java
│   │   ├── pom.xml
│   │   ├── debug-instance.dat
│   │   ├── target/
│   ├── aws-results/
│   ├── README.md
```

`note:` Inside the `aws-results` you can find more detail (logs, tables, etc.) about the final results of the benchmark.

### Run from terminal
go to the runnable folder (`cd runnable`) and execute the following commands:
```bash
java -jar mkp.jar -f="instances/xxxxx.dat" 
                  -ch=random|greedy|toyoda 
                  -s=1234 
                  -imp=bi|fi|vnd 
                  -is=1236
 ```
> `-f` means the file with the instances  
> `-ch` the constructive heuristic you want execute  
> `-s` the random seed you want to test for the radom ch  
> `-imp` the improvement you want to test with the ch as initial solution  
> `-is`  the random seed you want to use in the shuffle pashe of the improvement  



## Benchmarking

go to the runnable folder (`cd runnable`) and execute the following commands:

### Machine where the testes were done.
- Compute optimized AWS EC2 mc5.large
- Intel Xeon Platinum 8124M 2 cores
- Clock speed 3 GHz
- 4GB RAM
- The Amazon Linux AMI OS
- Java 1.8

### Constructive heuristics
```bash
 java -jar benchmarks.jar cTestToyodaCH bTestGreedyCH aTestRandomCH -rff jmh-ch-results.csv -rf csv -wi 0 -f 0 | tee jmh-ch-results.log
 ```
### Best Improvement
```bash
 java -jar benchmarks.jar fTestToyodaBI eTestGreedyBI dTestRandomBI -rff jmh-bi-results.csv -rf csv -wi 0 -f 0 | tee jmh-bi-results.log
 ```
### First Improvement
```bash
 java -jar benchmarks.jar iTestToyodaFI hTestGreedyFI gTestRandomFI -rff jmh-fi-results.csv -rf csv -wi 0 -f 0 | tee jmh-fi-results.log
 ```
### VND Improvement
```bash
 java -jar benchmarks.jar lTestToyodaVND -rff jmh-vnd-results-nornd.csv -rf csv -wi 0 -f 0 | tee jmh-vnd-results-nornd.log
 ```
```bash
 java -jar benchmarks.jar kTestGreedyVND -rff jmh-vnd-results-greedy.csv -rf csv -wi 0 -f 0 | tee jmh-vnd-results-greedy.log
 ```
```bash
 java -jar benchmarks.jar jTestRandomVND -rff jmh-vnd-results-rnd.csv -rf csv -wi 0 -f 0 | tee jmh-vnd-results-rnd.log
 ```


## Build

The project has been develped with Java 1.8, Maven and Netbeans. To build you can do it from netbeans or execute the following command in the terminal from the `knapsack-problem` folder.
```bash
mvn install
```
The respective jars are going to be inside the `target` folder.