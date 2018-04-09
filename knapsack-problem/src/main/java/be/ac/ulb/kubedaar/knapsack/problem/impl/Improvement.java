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

    public Improvement(Solution initialSolution) {
        this.initSolution = initialSolution;
        this.solution = this.initSolution.copy();
        this.lkInitialSolution = new LinkedList<>();
        this.improved = true;
    }

    public void shuffleInitialSolution() {
        this.lkInitialSolution = this.initSolution.getSolutionList();
        Collections.shuffle(this.lkInitialSolution, new Random(1234L));
    }

    void sortNonInsertedInSolution() {
        this.sortedNonInSolution = this.initSolution.sortNonInsertedList();
    }

    public abstract Solution getImprovedSolution();

}
