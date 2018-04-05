/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ac.ulb.kubedaar.knapsack.problem.impl;

import java.util.Collection;
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
    Solution sInitialSolution;
    Solution sFinalSolution;
    LinkedList<Integer> lkInitialSolution;
    PriorityQueue<Integer> nonInSolution;
    PriorityQueue<Map.Entry<Integer, Integer>> sortedNonInSolution;
    PriorityQueue<Map.Entry<Integer, Integer>> sortedNonInSolutionTmp;
    boolean improved;
    int k;

    public Improvement(Solution initialSolution) {
        this.sInitialSolution = initialSolution;
        this.improved = true;
    }

    public void getInitialSolution() {
        this.lkInitialSolution = this.sInitialSolution.getSolutionList();
    }

    public void getNonInSolution() {
        this.nonInSolution = this.sInitialSolution.getNonInSolutionList();
    }

    public void shuffleInitialSolution() {
        Collections.shuffle(this.lkInitialSolution, new Random(1234L));
    }

    public abstract Solution getImprovedSolution();
}
