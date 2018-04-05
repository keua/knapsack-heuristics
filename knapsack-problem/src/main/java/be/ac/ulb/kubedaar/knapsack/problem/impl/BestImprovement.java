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

    public BestImprovement(GreedySolution initialSolution) {
        super(initialSolution);
        super.solution = new GreedySolution(initialSolution.getpIns());
        super.k = 1;
    }

    public BestImprovement(RandomSolution initialSolution) {
        super(initialSolution);
        super.solution = new RandomSolution(initialSolution.getpIns(), 0L);
        super.k = 1;
    }

    public BestImprovement(ToyodaSolution initialSolution) {
        super(initialSolution);
        super.solution = new ToyodaSolution(initialSolution.getpIns());
        super.k = 1;
    }

    @Override
    public Solution getImprovedSolution() {
        while (super.improved) {
            super.improved = false;
            // shuffle items in s
            super.getInitialSolution();
            super.shuffleInitialSolution();
            // sort list of non-inserted items
            super.getNonInSolution();
            super.sortedNonInSolution
                    = super.sInitialSolution.sortNonInsertedList();
            super.solution = super.sInitialSolution.copy();
            for (int i = 0; i < super.lkInitialSolution.size(); i++) {
                // remove k items from shuffled s
                for (int j = 0; j < k; j++) {
                    super.solution.removeItem(
                            super.lkInitialSolution.get(i + j)
                    );
                }
                // try to add non-selected items, one by one, from the sorted list
                super.sortedNonInSolutionTmp
                        = new PriorityQueue<>(super.sortedNonInSolution);
                // check feasibility 
                while (!this.sortedNonInSolutionTmp.isEmpty()) {
                    super.solution.checkBeforeAddItem(
                            this.sortedNonInSolutionTmp.poll().getKey()
                    );
                    // check improvement
                    if (super.solution.getValue() > super.sInitialSolution.getValue()) {
                        super.sFinalSolution = super.solution.copy();
                        super.sInitialSolution = super.sFinalSolution;
                        super.improved = true;
                    }
                }
            }
        }
        System.out.println("BI");
        super.sInitialSolution.printSolution();
        return super.sInitialSolution;
    }

}
