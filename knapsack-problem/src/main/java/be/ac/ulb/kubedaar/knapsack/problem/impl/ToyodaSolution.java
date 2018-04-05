/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ac.ulb.kubedaar.knapsack.problem.impl;

import java.io.PrintStream;
import static java.lang.Math.sqrt;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import static java.util.Comparator.reverseOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 * @author masterulb
 */
public class ToyodaSolution extends Solution {

    private final double normConstraints[][]; // Normalized constraints A = W/L
    private final double normCapacities[]; // Normalized capacities L/L
    private double u[]; // resources used so far
    private double euclideanNorm; // ||U||
    private final double v[]; // v
    private final PriorityQueue<Map.Entry<Integer, Double>> pseudoUtility;

    private static final PrintStream OUT = System.out;

    public ToyodaSolution(ProblemInstance pIns) {
        super(pIns);
        this.normConstraints = new double[super.getKnapsacks()][super.getItems()];
        this.normCapacities = new double[super.getKnapsacks()];
        this.u = new double[super.getKnapsacks()];
        this.euclideanNorm = 0.0;
        this.v = new double[super.getItems()];
        this.pseudoUtility
                = new PriorityQueue<>(
                        super.getItems(),
                        Comparator.comparing(Map.Entry::getValue, reverseOrder())
                );
    }

    public ToyodaSolution(Solution sol) {
        super(sol);
        ToyodaSolution tmp = (ToyodaSolution) sol;
        this.normConstraints = tmp.normConstraints.clone();
        this.normCapacities = tmp.normCapacities.clone();
        this.u = tmp.u.clone();
        this.euclideanNorm = tmp.euclideanNorm;
        this.v = tmp.v.clone();
        this.pseudoUtility = new PriorityQueue<>(tmp.pseudoUtility);
    }

    @Override
    public ToyodaSolution getFeasibleSolution() {
        OUT.println("********** Toyoda Soltuion **********");
        this.normalizeConstraints();
        boolean again = true;
        for (int i = 0; again; i++) {
            // step 4 calculate "u"
            this.computeResourcesUsed(i);
            // step 5 calculate "v"
            // step 6 if first iteration  v = a
            this.computeV(i);
            // step 7 sort non-selected items accorging to the pseudoutility
            this.sortAndComputePseudoUtility();
            // step 8 scan the list until you can add one item
            // step 9 when you add one item, stop the cycle, 
            // update and repeat teh process until you cannot add any new item
            again = this.addItem();
        }
        super.printSolution();
        return this;
    }

    private void normalizeConstraints() {
        int constraints[][] = super.getpIns().getConstraints();
        int capacities[] = super.getpIns().getCapacities();

        for (int i = 0; i < constraints.length; i++) {
            for (int j = 0; j < constraints[i].length; j++) {
                this.normConstraints[i][j]
                        = (double) constraints[i][j] / (double) capacities[i];
            }
            this.normCapacities[i]
                    = (double) capacities[i] / (double) capacities[i];
        }
        this.printNormalizedConstraints();
    }

    private void printNormalizedConstraints() {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < this.normConstraints.length; i++) {
            buf.append("********* Inequality # ");
            buf.append(i);
            buf.append(" ********** \n");
            for (int j = 0; j < this.normConstraints[i].length; j++) {
                buf.append(this.normConstraints[i][j]);
                buf.append("x").append(j).append("+");
            }
            buf.append(" <= ").append(this.normCapacities[i]).append("\n");
        }
        OUT.println("Normalized Constraints \n" + buf.toString());
    }

    private void computeResourcesUsed(int iteration) {
        OUT.println("********** Iteration " + iteration + " **********");

        if (iteration > 0) {
            for (double resource : this.u) {
                this.euclideanNorm += resource * resource;
            }
            this.euclideanNorm = sqrt(this.euclideanNorm);
        }
        OUT.println("eucliedan norm");
        OUT.println(this.euclideanNorm);
    }

    private void computeV(int iteration) {
        for (int i = 0; i < super.getItems(); i++) {
            double sum = 0.0;
            for (int j = 0; j < super.getKnapsacks(); j++) {

                sum += (iteration > 0)
                        ? this.normConstraints[j][i] * u[j] / this.euclideanNorm
                        : this.normConstraints[j][i];
            }
            this.v[i] = sum;
        }
        OUT.println("v vector");
        OUT.println(Arrays.toString(v));
    }

    private void sortAndComputePseudoUtility() {
        int profits[] = super.getpIns().getProfits();

        for (int i = 0; i < this.v.length; i++) {

            if (!(super.getSolution()[i] == 1)) {

                HashMap.Entry<Integer, Double> e
                        = new AbstractMap.SimpleEntry<>(
                                i, (double) profits[i] / v[i]
                        );
                pseudoUtility.add(e);

            }
        }
        OUT.println("pseudo utility");
        OUT.println(this.pseudoUtility.toString());
    }

    private boolean addItem() {
        while (!this.pseudoUtility.isEmpty()) {

            int item = this.pseudoUtility.poll().getKey();
            double utemp[] = Arrays.copyOf(this.u, this.u.length);

            for (int i = 0; i < this.normConstraints.length; i++) {
                utemp[i] += this.normConstraints[i][item];
            }
            double greatest
                    = Arrays
                            .stream(utemp)
                            .max()
                            .orElseThrow(IllegalStateException::new);

            if (greatest < this.normCapacities[0]) {
                super.addItem(item);
                this.u = utemp;
                this.pseudoUtility.clear();// clean pseudoutility
                this.euclideanNorm = 0.0; // clean eucliedean norm
                OUT.println("u vector");
                OUT.println(Arrays.toString(this.u));
                return true;
            }
        }
        return false;
    }

    @Override
    public PriorityQueue sortNonInsertedList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Solution cloneSolution() {
        return new ToyodaSolution(this);
    }

}
