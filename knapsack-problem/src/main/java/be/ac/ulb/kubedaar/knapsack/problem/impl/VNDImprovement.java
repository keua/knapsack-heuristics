/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ac.ulb.kubedaar.knapsack.problem.impl;

import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 *
 * @author masterulb
 */
public class VNDImprovement extends Improvement {

    private Solution tmpSol;
    private final LinkedList<LinkedList<Integer>> allPossibleCombinations;

    public VNDImprovement(Solution initialSolution) {
        super(initialSolution);
        super.k = 3;
        allPossibleCombinations = new LinkedList<>();
    }

    @Override
    public Solution getImprovedSolution() {
        for (int ki = 0; ki < this.k; ki++) { // k is here 
            super.improved = true;
            while (super.improved) {
                super.improved = false;
                // shuffle items in s
                super.shuffleInitialSolution();
                // sort list of non-inserted items
                super.sortNonInsertedInSolution();
                // get all possible combinations
                this.getAllPossibleCombinations(
                        super.lkInitialSolution.size(), ki + 1, lkInitialSolution
                );
                for (int i = 0; i < this.allPossibleCombinations.size(); i++) {
                    this.tmpSol = super.initSolution.copy();
                    // remove k=1,2,3 items from shuffled s
                    for (int j = 0; j < this.allPossibleCombinations.get(i).size(); j++) {
                        this.tmpSol.removeItem(
                                this.allPossibleCombinations.get(i).get(j)
                        );
                    }
                    // try to add non-selected items, one by one, from the sorted list
                    super.tmpPqueue = new PriorityQueue<>(super.sortedNonInSolution);
                    while (!tmpPqueue.isEmpty()) {
                        // check feasibility 
                        this.tmpSol.checkBeforeAddItem(tmpPqueue.poll().getKey());
                    }
                    // check improvement
                    if (this.tmpSol.getValue() > super.solution.getValue()) {
                        super.solution = this.tmpSol.copy();
                        super.improved = true;
                    }
                }
                super.initSolution = super.solution.copy();// Apply Move
            }
        }
        return super.solution;
    }

    private void getAllPossibleCombinations(int n, int k, LinkedList<Integer> values) {
        this.allPossibleCombinations.clear();
        int com[] = new int[n];
        for (int i = 0; i < k; i++) {
            com[i] = i;
        }
        while (com[k - 1] < n) {
            LinkedList<Integer> tmp = new LinkedList<>();
            for (int i = 0; i < k; i++) {
                tmp.add(values.get(com[i]));
            }
            this.allPossibleCombinations.add(tmp);
            int t = k - 1;
            while (t != 0 && com[t] == n - k + t) {
                t--;
            }
            com[t]++;
            for (int i = t + 1; i < k; i++) {
                com[i] = com[i - 1] + 1;
            }
        }
    }

}
