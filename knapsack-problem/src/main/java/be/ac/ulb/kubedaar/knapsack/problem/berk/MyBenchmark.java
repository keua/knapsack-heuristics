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
import be.ac.ulb.kubedaar.knapsack.problem.impl.GreedySolution;
import be.ac.ulb.kubedaar.knapsack.problem.impl.ProblemInstance;
import be.ac.ulb.kubedaar.knapsack.problem.impl.RandomSolution;
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
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2)
@Fork(1)
public class MyBenchmark {

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

        @Setup(Level.Trial)
        public void setup() {
            String fileName = aResourcesXitem + bDifficulty + cSample + ".dat";
            this.pIns = ProblemInstance.readInstance(PATH + fileName);
            rs = new RandomSolution(pIns, 1324L);
            gs = new GreedySolution(pIns);
            ts = new ToyodaSolution(pIns);
        }

        @TearDown(Level.Trial)
        public void doTearDown() {
            //rs.printSolution();
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

        private ProblemInstance pIns;
        private final static String PATH = "../instances/";

        @Setup(Level.Trial)
        public void setup() {
            String fileName = aResourcesXitem + bDifficulty + cSample + ".dat";
            this.pIns = ProblemInstance.readInstance(PATH + fileName);
            System.out.println("problem");
        }

        @Setup(Level.Iteration)
        public void initialzingSolutions(BenchmarkParams params) {
            String method = params.getBenchmark().toLowerCase();
            if (method.contains("random")) {
                iniSol = new RandomSolution(pIns, 1234L).getFeasibleSolution();
            } else if (method.contains("greedy")) {
                iniSol = new GreedySolution(pIns).getFeasibleSolution();
            } else if (method.contains("toyoda")) {
                iniSol = new ToyodaSolution(pIns).getFeasibleSolution();
            }
        }

        @TearDown(Level.Trial)
        public void doTearDown() {
            //rbi.getImprovedSolution().printSolution();
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

        private ProblemInstance pIns;
        private final static String PATH = "../instances/";

        @Setup(Level.Trial)
        public void setup() {
            String fileName = aResourcesXitem + bDifficulty + cSample + ".dat";
            this.pIns = ProblemInstance.readInstance(PATH + fileName);
            System.out.println("problem");
        }

        @Setup(Level.Iteration)
        public void initialzingSolutions(BenchmarkParams params) {
            String method = params.getBenchmark().toLowerCase();
            if (method.contains("random")) {
                iniSol = new RandomSolution(pIns, 1234L).getFeasibleSolution();
            } else if (method.contains("greedy")) {
                iniSol = new GreedySolution(pIns).getFeasibleSolution();
            } else if (method.contains("toyoda")) {
                iniSol = new ToyodaSolution(pIns).getFeasibleSolution();
            }
        }

        @TearDown(Level.Trial)
        public void doTearDown() {
            //rs.printSolution();
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

        private ProblemInstance pIns;
        private final static String PATH = "../instances/";

        @Setup(Level.Trial)
        public void setup() {
            String fileName = aResourcesXitem + bDifficulty + cSample + ".dat";
            this.pIns = ProblemInstance.readInstance(PATH + fileName);
            System.out.println("problem");
        }

        @Setup(Level.Iteration)
        public void initialzingSolutions(BenchmarkParams params) {
            String method = params.getBenchmark().toLowerCase();
            if (method.contains("random")) {
                iniSol = new RandomSolution(pIns, 1234L).getFeasibleSolution();
            } else if (method.contains("greedy")) {
                iniSol = new GreedySolution(pIns).getFeasibleSolution();
            } else if (method.contains("toyoda")) {
                iniSol = new ToyodaSolution(pIns).getFeasibleSolution();
            }
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
        bis.rbi = new BestImprovement(bis.iniSol);
        bh.consume(bis.rbi.getImprovedSolution());
    }

    @Benchmark
    @Measurement(iterations = 1)
    public void eTestGreedyBI(Blackhole bh, BestImprovementState bis) {
        bis.gbi = new BestImprovement(bis.iniSol);
        bh.consume(bis.gbi.getImprovedSolution());
    }

    @Benchmark
    @Measurement(iterations = 1)
    public void fTestToyodaBI(Blackhole bh, BestImprovementState bis) {
        bis.tbi = new BestImprovement(bis.iniSol);
        bh.consume(bis.tbi.getImprovedSolution());
    }

    @Benchmark
    @Measurement(iterations = 15)
    public void gTestRandomFI(Blackhole bh, FirstImprovementState fis) {
        fis.rfi = new FirstImprovement(fis.iniSol);
        bh.consume(fis.rfi.getImprovedSolution());
    }

    @Benchmark
    @Measurement(iterations = 1)
    public void hTestGreedyFI(Blackhole bh, FirstImprovementState fis) {
        fis.gfi = new FirstImprovement(fis.iniSol);
        bh.consume(fis.gfi.getImprovedSolution());
    }

    @Benchmark
    @Measurement(iterations = 1)
    public void iTestToyodaFI(Blackhole bh, FirstImprovementState fis) {
        fis.tfi = new FirstImprovement(fis.iniSol);
        bh.consume(fis.tfi.getImprovedSolution());
    }

    @Benchmark
    @Measurement(iterations = 15)
    public void jTestRandomVND(Blackhole bh, VNDImprovementState vs) {
        vs.rvnd = new VNDImprovement(vs.iniSol);
        bh.consume(vs.rvnd.getImprovedSolution());
    }

    @Benchmark
    @Measurement(iterations = 1)
    public void kTestGreedyVND(Blackhole bh, VNDImprovementState vs) {
        vs.gvnd = new VNDImprovement(vs.iniSol);
        bh.consume(vs.gvnd.getImprovedSolution());
    }

    @Benchmark
    @Measurement(iterations = 1)
    public void lTestToyodaVND(Blackhole bh, VNDImprovementState vs) {
        vs.tvnd = new VNDImprovement(vs.iniSol);
        bh.consume(vs.tvnd.getImprovedSolution());
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
