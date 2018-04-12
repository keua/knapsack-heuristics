/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ac.ulb.kubedaar.knapsack.problem.impl;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.PriorityQueue;
import static java.util.stream.IntStream.rangeClosed;

/**
 * <pre>
 * @author masterulb
 *
 * generic solution for a MKP problem
 *
 * n              : number of objects
 * m              : number of knapsacks
 * value          : objective function value
 * sol            : 0/1 vector (item j discarded/selected)
 *
 * num_selected   : number of items selected
 * selected       : array containing the items selected in the solution
 *                 (corresponding to 1 in sol)
 * num_discarded  : number of items discarded
 * discarded      : array containing the items not selected in the solution
 *                  (corresponding to 0 in sol)
 *
 * resources_used : vector of length m containing the amount of resources used
 *                  by the current solution
 *
 * num_violated   : number of violated constraints
 * violated       : 0/1 vector of length m (1 : j-th constraint violated)
 * </pre>
 */
public abstract class Solution {

    private final ProblemInstance pIns;

    private final int items;
    private final int knapsacks;
    private int value;
    private final int solution[];

    private int numSelected;
    private final int selected[];
    private int numDiscarded;
    private int discarded[];

    private final int resourcesUsed[];
    private int numViolated;
    private final int violated[];

    private static final PrintStream OUT = System.out;

    public Solution(ProblemInstance pIns) { // create an empty solution

        this.pIns = pIns;

        this.items = this.pIns.getItems();
        this.knapsacks = this.pIns.getKnapsacks();
        this.value = 0;
        this.solution = new int[this.items];

        this.numSelected = 0;
        this.selected = new int[this.items];
        this.numDiscarded = this.items;
        this.discarded = rangeClosed(0, this.items - 1).toArray();

        this.resourcesUsed = new int[this.knapsacks];
        this.violated = new int[this.knapsacks];
        this.numViolated = 0;

    }

    public Solution(Solution sol) { // Clone solution

        this.pIns = sol.pIns;

        this.items = sol.items;
        this.knapsacks = sol.knapsacks;
        this.value = sol.value;
        this.solution = sol.solution.clone();

        this.numSelected = sol.numSelected;
        this.selected = sol.selected.clone();
        this.numDiscarded = sol.numDiscarded;
        this.discarded = sol.discarded.clone();

        this.resourcesUsed = sol.resourcesUsed.clone();
        this.violated = sol.violated.clone();
        this.numViolated = sol.numViolated;
    }

    public abstract Solution getFeasibleSolution();

    public abstract PriorityQueue sortNonInsertedList();

    public abstract Solution copy();

    public int computeObjectiveFunction() {
        int c = 0;
        for (int i = 0; i < this.items; i++) {
            c = c + this.solution[i] * this.getpIns().getProfits()[i];
        }
        return (c);
    }

    /**
     * Check how many elements in resources_used exceed the maximum capacity
     * allowed by the constraints.
     *
     */
    public void checkViolatedConstraints() {
        int nv = 0;
        for (int i = 0; i < getpIns().getKnapsacks(); i++) {
            if (this.resourcesUsed[i] > getpIns().getCapacities()[i]) {
                nv++;
                this.violated[i] = 1;
            } else {
                this.violated[i] = 0;
            }
        }
        this.numViolated = nv;
    }

    public boolean isFeasibleSolution() {
        return (this.numViolated == 0);
    }

    public LinkedList<Integer> getSolutionList() {
        LinkedList<Integer> sol = new LinkedList<>();
        for (int i = 0; i < this.items; i++) {
            if (this.solution[i] == 1) {
                sol.add(i);
            }
        }
        return sol;
    }

    public PriorityQueue<Integer> getNonInSolutionList() {
        PriorityQueue<Integer> notsol = new PriorityQueue<>();
        for (int i = 0; i < this.items; i++) {
            if (this.solution[i] == 0) {
                notsol.add(i);
            }
        }
        return notsol;
    }

    public void printSolution() {
        if (this.numSelected == 0) {
            OUT.println("No items selected in the solution!");
        } else {
            OUT.print("Items in solution:\n[ ");
            for (int i = 0; i < this.items; i++) {
                if (this.solution[i] == 1) {
                    OUT.print(i + " ");
                }
            }
            OUT.print("]\nSolution value:" + this.value + "\n");
        }
        if (this.numViolated == 0) {
            OUT.println("This is a feasible solution.");
        } else {
            OUT.println("Violated constraints:");
            for (int i = 0; i < this.knapsacks; i++) {
                if (this.violated[i] == 1) {
                    OUT.print(i + " ");
                }
            }
            OUT.println("\n");
        }
        OUT.print("[ ");
        for (int i = 0; i < this.numSelected; i++) {
            OUT.print(this.selected[i] + " ");
        }
        OUT.print("] :: [ ");
        for (int i = 0; i < this.numDiscarded; i++) {
            OUT.print(this.discarded[i] + " ");
        }
        OUT.println("]\n\n");
    }

    /**
     * ******************** NEIGHBOURHOOD *******************
     */
    /**
     * If item was already in the solution, do nothing.
     *
     * @param item
     * @return
     */
    public boolean addItem(int item) {
        if (this.solution[item] == 0) {
            this.solution[item] = 1;
            this.selected[this.numSelected] = item;
            this.numSelected++;
            this.value += this.getpIns().getProfits()[item]; // delta evaluation

            // remove the item from the list of items not in the solution
            // probably not the most efficient way...
            int i = 0;
            while (i < this.numDiscarded) {
                if (this.discarded[i] == item) {
                    break;
                }
                i++;
            }
            while (i < this.numDiscarded - 1) {
                this.discarded[i] = this.discarded[i + 1];
                i++;
            }
            this.discarded[i] = 0;
            this.numDiscarded = this.numDiscarded - 1;

            // update list of resources used by the current solution
            for (i = 0; i < this.knapsacks; i++) {
                this.resourcesUsed[i] += getpIns().getConstraints()[i][item];
            }
            return true;
        }
        return false;
    }

    /**
     * If item was already in the solution, do nothing.
     *
     * @param item
     * @return
     */
    public boolean removeItem(int item) {
        if (this.solution[item] != 0) {
            this.solution[item] = 0;
            this.discarded[this.numDiscarded] = item;
            this.numDiscarded++;
            this.value -= this.getpIns().getProfits()[item]; // delta evaluation

            // remove the item from the list of items not in the solution
            // probably not the most efficient way...
            int i = 0;
            while (i < this.numSelected) {
                if (this.selected[i] == item) {
                    break;
                }
                i++;
            }
            while (i < this.numSelected - 1) {
                this.selected[i] = this.selected[i + 1];
                i++;
            }
            this.selected[i] = 0;
            this.numSelected = this.numSelected - 1;

            // update list of resources used by the current solution
            for (i = 0; i < this.knapsacks; i++) {
                this.resourcesUsed[i] -= getpIns().getConstraints()[i][item];
            }
            return true;
        }
        return false;
    }

    /**
     * Check whether adding the item would lead to violate some constraints and
     * count how many.
     *
     * @param item
     * @return
     */
    public boolean checkBeforeAddItem(int item) {
        int v = 0;
        for (int i = 0; i < this.knapsacks; i++) {
            if (this.resourcesUsed[i] + getpIns().getConstraints()[i][item] > getpIns().getCapacities()[i]) {
                v++;
            }
        }
        // if no constraint is violated, add the item to the solution
        if (v == 0) {
            this.addItem(item);
        }
        return (v != 0);
    }

    /**
     * ******************** NEIGHBOURHOOD *******************
     */
    /**
     * @return the items
     */
    public int getItems() {
        return items;
    }

    /**
     * @return the knapsacks
     */
    public int getKnapsacks() {
        return knapsacks;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @return the solution
     */
    public int[] getSolution() {
        return solution;
    }

    /**
     * @return the numSelected
     */
    public int getNumSelected() {
        return numSelected;
    }

    /**
     * @return the selected
     */
    public int[] getSelected() {
        return selected;
    }

    /**
     * @return the numDiscarded
     */
    public int getNumDiscarded() {
        return numDiscarded;
    }

    /**
     * @return the discarded
     */
    public int[] getDiscarded() {
        return discarded;
    }

    /**
     * @return the resourcesUsed
     */
    public int[] getResourcesUsed() {
        return resourcesUsed;
    }

    /**
     * @return the numViolated
     */
    public int getNumViolated() {
        return numViolated;
    }

    /**
     * @return the violated
     */
    public int[] getViolated() {
        return violated;
    }

    /**
     * @return the pIns
     */
    public ProblemInstance getpIns() {
        return pIns;
    }

}
