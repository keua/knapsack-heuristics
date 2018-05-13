/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package be.ac.ulb.kubedaar.knapsack.problem.berk;

import be.ac.ulb.kubedaar.knapsack.problem.impl.BestImprovement;
import be.ac.ulb.kubedaar.knapsack.problem.impl.FirstImprovement;
import be.ac.ulb.kubedaar.knapsack.problem.impl.GeneticAlgorithm;
import be.ac.ulb.kubedaar.knapsack.problem.impl.GreedySolution;
import be.ac.ulb.kubedaar.knapsack.problem.impl.ProblemInstance;
import be.ac.ulb.kubedaar.knapsack.problem.impl.RandomSolution;
import be.ac.ulb.kubedaar.knapsack.problem.impl.SimulatedAnnealing;
import be.ac.ulb.kubedaar.knapsack.problem.impl.Solution;
import be.ac.ulb.kubedaar.knapsack.problem.impl.ToyodaSolution;
import be.ac.ulb.kubedaar.knapsack.problem.impl.VNDImprovement;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(jvmArgs = "-Djmh.ignoreLock=true")
public class MyBenchmark {

    private final static Long IMPROVEMENT_SEED = 4619L;
    private final static Long RANDOM_SEEDS[] = {
        7440L, 1437L,
        1279L, 1263L, 1872L, 1252L, 1394L,
        1015L, 1009L, 3882L, 1130L, 4602L,
        1047L, 1353L, 2952L, 2177L, 1586L
    };

    @State(Scope.Benchmark)
    public static class ConstructiveHeuristicsState {

        @Param({"OR10x100-", "OR10x250-"})
        private String aResourcesXitem;
        @Param({"0.25", "0.50", "0.75"})
        private String bDifficulty;
        @Param({"_1", "_2", "_3", "_4", "_5", "_6", "_7", "_8", "_9", "_10"})
        private String cSample;

        RandomSolution rs;
        GreedySolution gs;
        ToyodaSolution ts;

        private ProblemInstance pIns;
        private final static String PATH = "../instances/";
        private int counter = 0;

        @Setup(Level.Trial)
        public void setup() {
            String fileName = aResourcesXitem + bDifficulty + cSample + ".dat";
            this.pIns = ProblemInstance.readInstance(PATH + fileName);
            counter = 0;
        }

        @Setup(Level.Iteration)
        public void initialzingSolutions(BenchmarkParams params) {
            String method = params.getBenchmark().toLowerCase();
            if (method.contains("random")) {
                rs = new RandomSolution(pIns, RANDOM_SEEDS[counter]);
                System.out.println("RS: " + RANDOM_SEEDS[counter]);
                counter++;
            } else if (method.contains("greedy")) {
                gs = new GreedySolution(pIns);
            } else if (method.contains("toyoda")) {
                ts = new ToyodaSolution(pIns);
            }
        }

        @TearDown(Level.Iteration)
        public void doTearDown(BenchmarkParams params) {
            String method = params.getBenchmark().toLowerCase();
            if (method.contains("random")) {
                System.out.println("Profit: " + rs.getValue());
            } else if (method.contains("greedy")) {
                System.out.println("Profit: " + gs.getValue());
            } else if (method.contains("toyoda")) {
                System.out.println("Profit: " + ts.getValue());
            }
        }
    }

    @State(Scope.Benchmark)
    public static class BestImprovementState {

        @Param({"OR10x100-", "OR10x250-"})
        private String aResourcesXitem;
        @Param({"0.25", "0.50", "0.75"})
        private String bDifficulty;
        @Param({"_1", "_2", "_3", "_4", "_5", "_6", "_7", "_8", "_9", "_10"})
        private String cSample;

        BestImprovement rbi;
        BestImprovement gbi;
        BestImprovement tbi;
        Solution iniSol;
        Solution improvedSol;

        private ProblemInstance pIns;
        private final static String PATH = "../instances/";
        private int counter = 0;

        @Setup(Level.Trial)
        public void setup() {
            String fileName = aResourcesXitem + bDifficulty + cSample + ".dat";
            this.pIns = ProblemInstance.readInstance(PATH + fileName);
            counter = 0;
        }

        @Setup(Level.Iteration)
        public void initialzingSolutions(BenchmarkParams params) {
            String method = params.getBenchmark().toLowerCase();
            if (method.contains("random")) {
                iniSol = new RandomSolution(pIns, RANDOM_SEEDS[counter]).getFeasibleSolution();
                System.out.println("RS: " + RANDOM_SEEDS[counter]);
                counter++;
            } else if (method.contains("greedy")) {
                iniSol = new GreedySolution(pIns).getFeasibleSolution();
            } else if (method.contains("toyoda")) {
                iniSol = new ToyodaSolution(pIns).getFeasibleSolution();
            }
        }

        @TearDown(Level.Iteration)
        public void doTearDown() {
            System.out.println("Profit: " + improvedSol.getValue());
        }
    }

    @State(Scope.Benchmark)
    public static class FirstImprovementState {

        @Param({"OR10x100-", "OR10x250-"})
        private String aResourcesXitem;
        @Param({"0.25", "0.50", "0.75"})
        private String bDifficulty;
        @Param({"_1", "_2", "_3", "_4", "_5", "_6", "_7", "_8", "_9", "_10"})
        private String cSample;

        FirstImprovement rfi;
        FirstImprovement gfi;
        FirstImprovement tfi;

        Solution iniSol;
        Solution improvedSol;

        private ProblemInstance pIns;
        private final static String PATH = "../instances/";
        private int counter = 0;

        @Setup(Level.Trial)
        public void setup() {
            String fileName = aResourcesXitem + bDifficulty + cSample + ".dat";
            this.pIns = ProblemInstance.readInstance(PATH + fileName);
            counter = 0;
        }

        @Setup(Level.Iteration)
        public void initialzingSolutions(BenchmarkParams params) {
            String method = params.getBenchmark().toLowerCase();
            if (method.contains("random")) {
                iniSol = new RandomSolution(pIns, RANDOM_SEEDS[counter]).getFeasibleSolution();
                System.out.println("RS: " + RANDOM_SEEDS[counter]);
                counter++;
            } else if (method.contains("greedy")) {
                iniSol = new GreedySolution(pIns).getFeasibleSolution();
            } else if (method.contains("toyoda")) {
                iniSol = new ToyodaSolution(pIns).getFeasibleSolution();
            }
        }

        @TearDown(Level.Iteration)
        public void doTearDown() {
            System.out.println("Profit: " + improvedSol.getValue());
        }
    }

    @State(Scope.Benchmark)
    public static class VNDImprovementState {

        @Param({"OR10x100-", "OR10x250-"})
        private String aResourcesXitem;
        @Param({"0.25", "0.50", "0.75"})
        private String bDifficulty;
        @Param({"_1", "_2", "_3", "_4", "_5", "_6", "_7", "_8", "_9", "_10"})
        private String cSample;

        VNDImprovement rvnd;
        VNDImprovement gvnd;
        VNDImprovement tvnd;

        Solution iniSol;
        Solution improvedSol;

        private ProblemInstance pIns;
        private final static String PATH = "../instances/";
        private int counter = 0;

        @Setup(Level.Trial)
        public void setup() {
            String fileName = aResourcesXitem + bDifficulty + cSample + ".dat";
            this.pIns = ProblemInstance.readInstance(PATH + fileName);
            counter = 0;
        }

        @Setup(Level.Iteration)
        public void initialzingSolutions(BenchmarkParams params) {
            String method = params.getBenchmark().toLowerCase();
            if (method.contains("random")) {
                iniSol = new RandomSolution(pIns, RANDOM_SEEDS[counter]).getFeasibleSolution();
                System.out.println("RS: " + RANDOM_SEEDS[counter]);
                counter++;
            } else if (method.contains("greedy")) {
                iniSol = new GreedySolution(pIns).getFeasibleSolution();
            } else if (method.contains("toyoda")) {
                iniSol = new ToyodaSolution(pIns).getFeasibleSolution();
            }
        }

        @TearDown(Level.Iteration)
        public void doTearDown() {
            System.out.println("Profit: " + improvedSol.getValue());
        }
    }

    @Benchmark
    @Measurement(iterations = 15)
    public void aTestRandomCH(Blackhole bh, ConstructiveHeuristicsState ch) {
        bh.consume(ch.rs.getFeasibleSolution());
    }

    @Benchmark
    @Measurement(iterations = 1)
    public void bTestGreedyCH(Blackhole bh, ConstructiveHeuristicsState ch) {
        bh.consume(ch.gs.getFeasibleSolution());
    }

    @Benchmark
    @Measurement(iterations = 1)
    public void cTestToyodaCH(Blackhole bh, ConstructiveHeuristicsState ch) {
        bh.consume(ch.ts.getFeasibleSolution());
    }

    @Benchmark
    @Measurement(iterations = 15)
    public void dTestRandomBI(Blackhole bh, BestImprovementState bis) {
        bis.rbi = new BestImprovement(bis.iniSol, IMPROVEMENT_SEED);
        bh.consume(bis.improvedSol = bis.rbi.getImprovedSolution());
    }

    @Benchmark
    @Measurement(iterations = 1)
    public void eTestGreedyBI(Blackhole bh, BestImprovementState bis) {
        bis.gbi = new BestImprovement(bis.iniSol, IMPROVEMENT_SEED);
        bh.consume(bis.improvedSol = bis.gbi.getImprovedSolution());
    }

    @Benchmark
    @Measurement(iterations = 1)
    public void fTestToyodaBI(Blackhole bh, BestImprovementState bis) {
        bis.tbi = new BestImprovement(bis.iniSol, IMPROVEMENT_SEED);
        bh.consume(bis.improvedSol = bis.tbi.getImprovedSolution());
    }

    @Benchmark
    @Measurement(iterations = 15)
    public void gTestRandomFI(Blackhole bh, FirstImprovementState fis) {
        fis.rfi = new FirstImprovement(fis.iniSol, IMPROVEMENT_SEED);
        bh.consume(fis.improvedSol = fis.rfi.getImprovedSolution());
    }

    @Benchmark
    @Measurement(iterations = 1)
    public void hTestGreedyFI(Blackhole bh, FirstImprovementState fis) {
        fis.gfi = new FirstImprovement(fis.iniSol, IMPROVEMENT_SEED);
        bh.consume(fis.improvedSol = fis.gfi.getImprovedSolution());
    }

    @Benchmark
    @Measurement(iterations = 1)
    public void iTestToyodaFI(Blackhole bh, FirstImprovementState fis) {
        fis.tfi = new FirstImprovement(fis.iniSol, IMPROVEMENT_SEED);
        bh.consume(fis.improvedSol = fis.tfi.getImprovedSolution());
    }

    @Benchmark
    @Measurement(iterations = 15)
    public void jTestRandomVND(Blackhole bh, VNDImprovementState vs) {
        vs.rvnd = new VNDImprovement(vs.iniSol, IMPROVEMENT_SEED);
        bh.consume(vs.improvedSol = vs.rvnd.getImprovedSolution());
    }

    @Benchmark
    @Measurement(iterations = 1)
    public void kTestGreedyVND(Blackhole bh, VNDImprovementState vs) {
        vs.gvnd = new VNDImprovement(vs.iniSol, IMPROVEMENT_SEED);
        bh.consume(vs.improvedSol = vs.gvnd.getImprovedSolution());
    }

    @Benchmark
    @Measurement(iterations = 1)
    public void lTestToyodaVND(Blackhole bh, VNDImprovementState vs) {
        vs.tvnd = new VNDImprovement(vs.iniSol, IMPROVEMENT_SEED);
        bh.consume(vs.improvedSol = vs.tvnd.getImprovedSolution());
    }

    @State(Scope.Benchmark)
    public static class I10x100State {

        @Param({"OR10x100-"})
        private String aResourcesXitem;
        @Param({"0.25", "0.50", "0.75"})
        private String bDifficulty;
        @Param({"_1", "_2", "_3", "_4", "_5", "_6", "_7", "_8", "_9", "_10"})
        private String cSample;

        GeneticAlgorithm ga;
        SimulatedAnnealing sa;
        Solution iniSol;
        Solution improvedSol;
        Solution finalSolution;

        private ProblemInstance pIns;
        private final static String PATH = "../instances/";

        int counter = 0;
        Double maxTime = 0d;

        @Setup(Level.Trial)
        public void setup() {
            String fileName = aResourcesXitem + bDifficulty + cSample + ".dat";
            this.pIns = ProblemInstance.readInstance(PATH + fileName);
            maxTime = this.pIns.getItems() * this.pIns.getKnapsacks() / 10d;
            counter = 0;
        }

        @Setup(Level.Iteration)
        public void initialzingSolutions(BenchmarkParams params) {
            String method = params.getBenchmark().toLowerCase();
            if (method.contains("ga")) {
                System.out.println("RS: " + RANDOM_SEEDS[counter]);
                counter++;
            } else if (method.contains("sa")) {
                iniSol = new RandomSolution(pIns, RANDOM_SEEDS[counter]).getFeasibleSolution();
                improvedSol = new BestImprovement(iniSol, RANDOM_SEEDS[counter]).getImprovedSolution();
                System.out.println("RS: " + RANDOM_SEEDS[counter]);
                counter++;
            }
        }

        @TearDown(Level.Iteration)
        public void doTearDown(BenchmarkParams params) {
            System.out.println("Profit: " + finalSolution.getValue());
        }
    }

    @State(Scope.Benchmark)
    public static class I10x250State {

        @Param({"OR10x250-"})
        private String aResourcesXitem;
        @Param({"0.25", "0.50", "0.75"})
        private String bDifficulty;
        @Param({"_1", "_2", "_3", "_4", "_5", "_6", "_7", "_8", "_9", "_10"})
        private String cSample;

        GeneticAlgorithm ga;
        SimulatedAnnealing sa;
        Solution iniSol;
        Solution improvedSol;
        Solution finalSolution;

        private ProblemInstance pIns;
        private final static String PATH = "../instances/";

        int counter = 0;
        Double maxTime = 0d;

        @Setup(Level.Trial)
        public void setup() {
            String fileName = aResourcesXitem + bDifficulty + cSample + ".dat";
            this.pIns = ProblemInstance.readInstance(PATH + fileName);
            maxTime = this.pIns.getItems() * this.pIns.getKnapsacks() / 10d;
            counter = 0;
        }

        @Setup(Level.Iteration)
        public void initialzingSolutions(BenchmarkParams params) {
            String method = params.getBenchmark().toLowerCase();
            if (method.contains("ga")) {
                System.out.println("RS: " + RANDOM_SEEDS[counter]);
                counter++;
            } else if (method.contains("sa")) {
                iniSol = new RandomSolution(pIns, RANDOM_SEEDS[counter]).getFeasibleSolution();
                improvedSol = new BestImprovement(iniSol, RANDOM_SEEDS[counter]).getImprovedSolution();
                System.out.println("RS: " + RANDOM_SEEDS[counter]);
                counter++;
            }
        }

        @TearDown(Level.Iteration)
        public void doTearDown(BenchmarkParams params) {
            System.out.println("Profit: " + finalSolution.getValue());
        }
    }

    @State(Scope.Benchmark)
    public static class F5I10x250State {

        @Param({"0.01", "0.1", "1"})
        private Double timeFactor;
        @Param({"OR10x250-"})
        private String aResourcesXitem;
        @Param({"0.25"})
        private String bDifficulty;
        @Param({"_1", "_2", "_3", "_4", "_5"})
        private String cSample;

        GeneticAlgorithm ga;
        SimulatedAnnealing sa;
        Solution iniSol;
        Solution improvedSol;
        Solution finalSolution;

        private ProblemInstance pIns;
        private final static String PATH = "../instances/";

        int counter = 0;
        Double maxTime = 0d;

        @Setup(Level.Trial)
        public void setup() {
            String fileName = aResourcesXitem + bDifficulty + cSample + ".dat";
            this.pIns = ProblemInstance.readInstance(PATH + fileName);
            maxTime = this.pIns.getItems() * this.pIns.getKnapsacks() / 10d;
            maxTime = timeFactor * maxTime;
            counter = 0;
        }

        @Setup(Level.Iteration)
        public void initialzingSolutions(BenchmarkParams params) {
            String method = params.getBenchmark().toLowerCase();
            if (method.contains("ga")) {
                System.out.println("RS: " + RANDOM_SEEDS[counter]);
                counter++;
            } else if (method.contains("sa")) {
                iniSol = new RandomSolution(pIns, RANDOM_SEEDS[counter]).getFeasibleSolution();
                improvedSol = new BestImprovement(iniSol, RANDOM_SEEDS[counter]).getImprovedSolution();
                System.out.println("RS: " + RANDOM_SEEDS[counter]);
                counter++;
            }
        }

        @TearDown(Level.Iteration)
        public void doTearDown(BenchmarkParams params) {
            System.out.println("Profit: " + finalSolution.getValue());
        }
    }

    @Benchmark
    @Measurement(iterations = 5)
    public void ga10x100Instances(Blackhole bh, I10x100State state) {
        int rateOfMutation = 2;
        int populatonSize = 100;
        state.ga = new GeneticAlgorithm(
                rateOfMutation, populatonSize, state.maxTime, state.pIns
        );
        bh.consume(state.finalSolution = state.ga.getSolution());
    }

    @Benchmark
    @Measurement(iterations = 5)
    public void ga10x250Instances(Blackhole bh, I10x250State state) {
        int rateOfMutation = 2;
        int populatonSize = 100;
        state.ga = new GeneticAlgorithm(
                rateOfMutation, populatonSize, state.maxTime, state.pIns
        );
        bh.consume(state.finalSolution = state.ga.getSolution());
    }

    @Benchmark
    @Measurement(iterations = 5)
    public void sa10x100Instances(Blackhole bh, I10x100State state) {
        Float initialTemp = 100f;
        Float annealingFactor = 0.98f;
        Float finalTemp = 0.00001f;
        Integer roundsPerTemp = 150;
        state.sa = new SimulatedAnnealing(
                state.improvedSol, initialTemp, annealingFactor, finalTemp,
                roundsPerTemp, RANDOM_SEEDS[state.counter], state.maxTime
        );

        bh.consume(state.finalSolution = state.sa.getSolution());
    }

    @Benchmark
    @Measurement(iterations = 5)
    public void sa10x250Instances(Blackhole bh, I10x250State state) {
        Float initialTemp = 100f;
        Float annealingFactor = 0.98f;
        Float finalTemp = 0.00001f;
        Integer roundsPerTemp = 150;
        state.sa = new SimulatedAnnealing(
                state.improvedSol, initialTemp, annealingFactor, finalTemp,
                roundsPerTemp, RANDOM_SEEDS[state.counter], state.maxTime
        );

        bh.consume(state.finalSolution = state.sa.getSolution());
    }

    @Benchmark
    @Measurement(iterations = 20)
    public void gaF510x250Instances(Blackhole bh, F5I10x250State state) {
        int rateOfMutation = 2;
        int populatonSize = 100;
        state.ga = new GeneticAlgorithm(
                rateOfMutation, populatonSize, state.maxTime, state.pIns
        );
        bh.consume(state.finalSolution = state.ga.getSolution());
    }

    @Benchmark
    @Measurement(iterations = 20)
    public void saF510x250Instances(Blackhole bh, F5I10x250State state) {
        Float initialTemp = 100f;
        Float annealingFactor = 0.98f;
        Float finalTemp = 0.00001f;
        Integer roundsPerTemp = 150;
        state.sa = new SimulatedAnnealing(
                state.improvedSol, initialTemp, annealingFactor, finalTemp,
                roundsPerTemp, RANDOM_SEEDS[state.counter], state.maxTime
        );

        bh.consume(state.finalSolution = state.sa.getSolution());
    }

    /**
     * This is to run the test from the IDE.
     *
     * @param args
     * @throws IOException
     * @throws RunnerException
     */
    public static void main(String... args) throws IOException, RunnerException {
        Options opts = new OptionsBuilder()
                .include(".*")
                .jvmArgs("-Xms2g", "-Xmx2g")
                .shouldDoGC(true)
                .build();

        new Runner(opts).run();
    }

}
