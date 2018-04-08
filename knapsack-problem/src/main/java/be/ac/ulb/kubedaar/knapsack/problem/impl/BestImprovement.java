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
public class BestImprovement extends Improvement {

    private Solution tmpSol;

    public BestImprovement(Solution initialSolution) {
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
                this.tmpSol = super.initSolution.copy();
                // remove k=1 items from shuffled s
                this.tmpSol.removeItem(super.lkInitialSolution.get(i));
                // try to add non-selected items, one by one, from the sorted list
                super.tmpPqueue = new PriorityQueue<>(super.sortedNonInSolution);
                while (!tmpPqueue.isEmpty()) {
                    // check feasibility 
                    this.tmpSol.checkBeforeAddItem(
                            tmpPqueue.poll().getKey()
                    );
                    // check improvement
                    if (this.tmpSol.getValue() > super.solution.getValue()) {
                        super.solution = this.tmpSol.copy();
                        super.improved = true;
                    }
                }
            }
            super.initSolution = super.solution.copy();// Apply Move
        }
        return super.solution;
    }

}
