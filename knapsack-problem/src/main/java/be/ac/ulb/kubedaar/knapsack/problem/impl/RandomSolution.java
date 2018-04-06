/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ac.ulb.kubedaar.knapsack.problem.impl;

import static java.lang.Math.abs;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
import static java.util.Comparator.reverseOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import static java.util.stream.IntStream.rangeClosed;

/**
 *
 * @author masterulb
 */
public class RandomSolution extends Solution {

    private final Random generator;

    public RandomSolution(ProblemInstance pIns, long seed) {
        super(pIns);
        this.generator = new Random(seed);
    }

    public RandomSolution(Solution sol) {
        super(sol);
        this.generator = ((RandomSolution) sol).generator;
    }

    private int[] createShuffled() {
        int v[] = rangeClosed(0, super.getItems() - 1).toArray();
        this.shuffleInt(v);
        return (v);
    }

    private void shuffleInt(int[] v) {
        int j, tmp;
        for (int i = super.getpIns().getItems() - 1; i >= 1; i--) {
            int randomNumber = this.generator.nextInt(); // Im not sure this is really working
            j = abs(randomNumber % i);
            tmp = v[i];
            v[i] = v[j];
            v[j] = tmp;
        }
    }

    private void shuffleInt(Integer[] v) {
        int j, tmp;
        for (int i = v.length - 1; i >= 1; i--) {
            int randomNumber = this.generator.nextInt(); // Im not sure this is really working
            j = abs(randomNumber % i);
            tmp = v[i];
            v[i] = v[j];
            v[j] = tmp;
        }
    }

    @Override
    public RandomSolution getFeasibleSolution() {
        int v[] = createShuffled();
        for (int i = 0; i < super.getpIns().getItems(); i++) {
            super.checkBeforeAddItem(v[i]);
        }
        return this;
    }

    @Override
    public PriorityQueue sortNonInsertedList() {
        PriorityQueue<Map.Entry<Integer, Integer>> pqueue
                = new PriorityQueue<>(
                        super.getItems(),
                        Comparator.comparing(Map.Entry::getValue)
                );
        PriorityQueue nonInSol = super.getNonInSolutionList();
        Integer v[] = (Integer[]) nonInSol.toArray(new Integer[nonInSol.size()]);
        this.shuffleInt(v);
        for (int i = 0; i < v.length; i++) {
            HashMap.Entry<Integer, Integer> e
                    = new AbstractMap.SimpleEntry<>(
                            v[i], i
                    );
            pqueue.add(e);
        }
        return pqueue;
    }

    @Override
    public Solution copy() {
        return new RandomSolution(this);
    }
}
