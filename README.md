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
│   ├── report.pdf
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
                  -sls=ga|sa
                  -maxtime=100.00
                  -slsseed=1234
                  -gamr=2
                  -gaps=50
                  -sait=100.00
                  -saft=0.00001
                  -sadf=0.845
                  -sarpt=10
 ```
> `-f` means the file with the instances  
> `-ch` the constructive heuristic you want execute  
> `-s` the random seed you want to test for the radom ch  
> `-imp` the improvement you want to test with the ch as initial solution  
> `-is`  the random seed you want to use in the shuffle pashe of the improvement  
> `-sls` ga for the Genetic algoirthm and sa for the simmulated annealing  
> `-max` the maximum amount of time to run the SLS  
> `-sls` the seed used to the random parts in the SLS methods  
> `-gamr` mutation rate for the GA  
> `-gap` population size for the GA  
> `-sai` initial temprature for the SA  
> `-saf` final temperature for the SA  
> `-sad` temperature control for the SA  
> `-sar` rounds per temperature for the SA  

for example to run the SA with a random inital solution you can run:
```
java -jar mkp.jar -f="../instances/OR10x250-0.75_1.dat" -ch="random" -is=46193 -sls="sa" -maxtime=100 -slsseed=4567
```

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

### Simulated Annealing (simple SLS method)

#### 10x100 Instances
```bash
 java -jar benchmarks.jar sa10x100Instances -rff jmh-sa10x100Instances.csv -rf csv -wi 0 -f 1 | tee sa10x100Instances.log
 ```
#### 10x250 Instances 
```bash
 java -jar benchmarks.jar sa10x250Instances -rff jmh-sa10x250Instances.csv -rf csv -wi 0 -f 1 | tee sa10x250Instances.log
 ```
#### first 5 10x250 Instances
```bash
 java -jar benchmarks.jar saF510x250Instances -rff jmh-saF510x250Instances.csv -rf csv -wi 0 -f 1 | tee saF510x250Instances.log
 ```

### Genetic Algorithm (population-based SLS method)

#### 10x100 Instances
```bash
 java -jar benchmarks.jar ga10x100Instances -rff jmh-ga10x100Instances.csv -rf csv -wi 0 -f 1 | tee ga10x100Instances.log
 ```
#### 10x250 Instances 
```bash
 java -jar benchmarks.jar ga10x250Instances -rff jmh-ga10x250Instances.csv -rf csv -wi 0 -f 1 | tee ga10x250Instances.log
 ```
#### first 5 10x250 Instances
```bash
 java -jar benchmarks.jar gaF510x250Instances -rff jmh-gaF510x250Instances.csv -rf csv -wi 0 -f 1 | tee gaF510x250Instances.log
 ```

## Build

The project has been develped with Java 1.8, Maven and Netbeans. To build you can do it from netbeans or execute the following command in the terminal from the `knapsack-problem` folder.
```bash
mvn install
```
The respective jars are going to be inside the `target` folder.