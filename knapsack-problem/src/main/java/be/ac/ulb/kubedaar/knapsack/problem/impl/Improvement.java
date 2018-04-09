/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ac.ulb.kubedaar.knapsack.problem.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

/**
 *
 * @author masterulb
 */
public abstract class Improvement {

    Solution solution;
    Solution initSolution;
    LinkedList<Integer> lkInitialSolution;
    PriorityQueue<Map.Entry<Integer, Object>> sortedNonInSolution;
    PriorityQueue<Map.Entry<Integer, Object>> tmpPqueue;
    boolean improved;
    int k;
    private static final Long RANDOM_SEED = 12345L;//15421841L
    private final Random randomGenerator;

    public Improvement(Solution initialSolution) {
        this.initSolution = initialSolution;
        this.solution = this.initSolution.copy();
        this.lkInitialSolution = new LinkedList<>();
        randomGenerator = new Random(RANDOM_SEED);
        this.improved = true;
    }

    public Improvement(Solution initialSolution, Long randomSeed) {
        this.initSolution = initialSolution;
        this.solution = this.initSolution.copy();
        this.lkInitialSolution = new LinkedList<>();
        this.randomGenerator = new Random(randomSeed);
        this.improved = true;
    }

    public void shuffleInitialSolution() {
        this.lkInitialSolution = this.initSolution.getSolutionList();
        Collections.shuffle(this.lkInitialSolution, this.randomGenerator);
    }

    void sortNonInsertedInSolution() {
        this.sortedNonInSolution = this.initSolution.sortNonInsertedList();
    }

    public abstract Solution getImprovedSolution();

}
