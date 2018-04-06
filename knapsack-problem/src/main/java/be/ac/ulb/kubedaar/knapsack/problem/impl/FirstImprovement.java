/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ac.ulb.kubedaar.knapsack.problem.impl;

import java.util.PriorityQueue;

/**
 *
 * @author masterulb
 */
public class FirstImprovement extends Improvement {

    public FirstImprovement(Solution initialSolution) {
        super(initialSolution);
        super.k = 1;
    }

    @Override
    public Solution getImprovedSolution() {
        while (super.improved) {
            super.improved = false;
            // shuffle items in s
            super.shuffleInitialSolution();
            // sort list of non-inserted items
            super.sortNonInsertedInSolution();
            for (int i = 0; i < super.lkInitialSolution.size(); i++) {
                // remove k items from shuffled s
                for (int j = 0; j < k; j++) {
                    super.initSolution.removeItem(
                            super.lkInitialSolution.get(i + j)
                    );
                }
                // try to add non-selected items, one by one, from the sorted list
                super.tmpPqueue = new PriorityQueue<>(super.sortedNonInSolution);
                while (!tmpPqueue.isEmpty()) {
                    // check feasibility 
                    super.initSolution.checkBeforeAddItem(
                            tmpPqueue.poll().getKey()
                    );
                    // check improvement
                    if (super.initSolution.getValue() > super.solution.getValue()) {
                        super.solution = super.initSolution.copy();
                        super.initSolution = super.solution.copy();
                        super.improved = true;
                        break;
                    }
                }
            }
        }
        return super.solution;
    }

}
