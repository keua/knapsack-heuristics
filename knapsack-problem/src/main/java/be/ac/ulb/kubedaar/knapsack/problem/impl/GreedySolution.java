/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ac.ulb.kubedaar.knapsack.problem.impl;

import java.util.AbstractMap;
import java.util.Comparator;
import static java.util.Comparator.reverseOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

/**
 *
 * @author masterulb
 */
public class GreedySolution extends Solution {

    public GreedySolution(ProblemInstance pIns) {
        super(pIns);
    }

    public GreedySolution(Solution sol) {
        super(sol);
    }

    private int[] sortByProfit() {
        int sortedArray[] = new int[super.getItems()];
        PriorityQueue<Map.Entry<Integer, Integer>> pqueue
                = new PriorityQueue<>(
                        super.getItems(),
                        Comparator.comparing(Entry::getValue, reverseOrder())
                );
        for (int i = 0; i < super.getItems(); i++) {
            HashMap.Entry<Integer, Integer> e
                    = new AbstractMap.SimpleEntry<>(
                            i, super.getpIns().getProfits()[i]
                    );
            pqueue.add(e);
        }
        int i = 0;
        while (!pqueue.isEmpty()) {
            // System.out.println(pqueue.peek());
            sortedArray[i++] = pqueue.poll().getKey();
        }
        return sortedArray;
    }

    @Override
    public GreedySolution getFeasibleSolution() {
        //System.out.println(Arrays.toString(this.sortByProfit()));
        int v[] = this.sortByProfit();
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
                        Comparator.comparing(Entry::getValue, reverseOrder())
                );
        for (int i = 0; i < super.getItems(); i++) {
            if (super.getSolution()[i] == 0) {
                HashMap.Entry<Integer, Integer> e
                        = new AbstractMap.SimpleEntry<>(
                                i, super.getpIns().getProfits()[i]
                        );
                pqueue.add(e);
            }
        }
        return pqueue;
    }

    @Override
    public Solution copy() {
        return new GreedySolution(this);
    }

}
