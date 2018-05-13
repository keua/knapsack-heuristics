/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ac.ulb.kubedaar.knapsack.problem.impl;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

/**
 *
 * @author masterulb
 */
public class SimulatedAnnealing {

    private Solution w;// Initial solution
    private Float t; // Temperature
    private final Float a; // Control paramter
    private final Float e; //Final Temperature
    private final Integer m; // Iterations per temperature
    private final Integer n; // number of items
    private final Long rseed; // Random seed
    private final Random generator; // Random generator
    private final Double tmax; // Maximum time

    public SimulatedAnnealing(Solution initialSolution) {
        this.w = initialSolution;
        this.t = 125f;
        this.a = 0.90f;
        this.e = 0.00001f;
        this.n = this.w.getItems();
        this.m = n;
        this.rseed = 94649L;
        this.generator = new Random(this.rseed);
        this.tmax = 100d * 1000; // 60 seconds * 1000 ms/sec
    }

    public SimulatedAnnealing(
            Solution w, Float t, Float a,
            Float e, Integer m, Long rseed, Double tmax
    ) {
        this.w = w;
        this.t = t;
        this.a = a;
        this.e = e;
        this.m = m;
        this.n = w.getItems();
        this.rseed = rseed;
        this.generator = new Random(this.rseed);
        this.tmax = tmax * 1000; // seconds * 1000 ms/sec
    }

    public Solution getSolution() {
        // Select an initial solution ω = (x1, · · · , xn) ∈ Ω;
        // an initial temperature t = t0 ; control parameter value α;
        // final temperature e ; a repetition schedule, 
        // M that defines the number of iterations executed at each temperature; 
        //  Incumbent solution ← f(ω) ;
        Solution IncSolution = this.w.copy();
        // Repeat
        long start = System.currentTimeMillis();
        long end = start + this.tmax.longValue(); // seconds * 1000 ms/sec
        float originalT = this.t;
        while (System.currentTimeMillis() < end) {
            this.w = IncSolution.copy();
            this.t = originalT;
            //System.out.println("start again");
            //System.out.println("Initial value: " + this.w.getValue());
            while (!(this.t < this.e)) {
                //  Set repetition counter m = 0;
                // Repeat
                for (int mi = 0; mi < this.m; mi++) {
                    // Select an integer i from the set {1,2,...n} randomly;
                    Integer i = this.generator.nextInt(this.n);
                    // If xi = 0, pick up item i, i.e. set xi = 1, obtain new solution ω', then
                    Solution w1 = this.w.copy();
                    if (w1.getSolution()[i] == 0) {
                        w1.addItem(i);
                        w1.checkViolatedConstraints();
                        // While solution ω' is infeasible, do
                        while (!w1.isFeasibleSolution()) {
                            // Drop another item ω' from randomly; denote the new solution as ω';
                            int itemToDrop;
                            do {
                                int rnd = this.generator.nextInt(w1.getNumSelected());
                                itemToDrop = w1.getSelected()[rnd];
                            } while (itemToDrop == i);
                            w1.removeItem(itemToDrop);
                            w1.checkViolatedConstraints();
                            // Let ∆ = f(ω') − f(ω);
                            Integer d = w1.getValue() - this.w.getValue();
                            // While ∆ ≥ 0 or Random (0, 1) < e^(∆/t), do ω ← ω';
                            if (d >= 0 || Math.random() < Math.exp((d / t))) {
                                if (w1.isFeasibleSolution()) {
                                    this.w = w1.copy();
                                }
                            }
                        }
                        // Else
                    } else {
                        // Drop item i, and pick up another item randomly, get new solution ω';
                        w1.removeItem(i);
                        /*int itemToAdd;
                    do {
                        int rnd = this.generator.nextInt(w1.getNumDiscarded());
                        itemToAdd = w1.getDiscarded()[rnd];
                    } while (itemToAdd == i);*/
                        PriorityQueue<Map.Entry<Integer, Object>> tmpPqueue
                                = new PriorityQueue<>(w1.sortNonInsertedList());
                        while (!tmpPqueue.isEmpty()) {
                            // check feasibility 
                            int itemToAdd = tmpPqueue.poll().getKey();
                            if (i != itemToAdd) {
                                w1.checkBeforeAddItem(itemToAdd);
                            }
                        }
                        //w1.checkBeforeAddItem(itemToAdd);
                        // Let ∆ = f(ω') − f(ω);
                        Integer d = w1.getValue() - this.w.getValue();
                        // While ∆ ≥ 0 or Random (0, 1) < e^(∆/t), do ω ← ω';
                        if (d >= 0 || Math.random() < Math.exp((d / this.t))) {
                            this.w = w1.copy();
                        }
                    }
                    // End If
                    // If incumbent solution < f(ω), Incumbent solution ← f(ω);
                    if (IncSolution.getValue() < this.w.getValue()) {
                        IncSolution = this.w;
                    }
                    // m = m + 1
                }
                // Until m = M
                // Set t = a ∗ t;
                this.t = this.a * this.t;
            }
            // Until t < e.
        }
        return IncSolution;
    }

}
