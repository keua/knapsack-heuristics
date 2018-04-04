/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ac.ulb.kubedaar.knapsack.problem.impl;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 *
 * @author masterulb
 */
public class ProblemInstance {

    private final int items; // n
    private final int knapsacks; // m
    private final int profits[]; // n profits
    private final int constraints[][]; // m constraints and n binary variables
    private final int capacities[]; // m capacities

    public ProblemInstance(int items, int knapsacks) {
        this.items = items;
        this.knapsacks = knapsacks;
        this.profits = new int[items];
        this.constraints = new int[knapsacks][items];
        this.capacities = new int[knapsacks];
    }

    public static ProblemInstance readInstance(String filePath) {
        try (DataInputStream dis
                = new DataInputStream(
                        new BufferedInputStream(
                                new FileInputStream(filePath)
                        )
                )) {
            StringTokenizer problemData
                    = new StringTokenizer(dis.readLine(), " ");
            ProblemInstance inst
                    = new ProblemInstance(
                            Integer.parseInt(problemData.nextToken()),
                            Integer.parseInt(problemData.nextToken())
                    );
            LinkedList<Integer> tmpData = new LinkedList<>();
            while (!(dis.available() <= 0)) {
                StringTokenizer values
                        = new StringTokenizer(dis.readLine(), " ");
                while (values.hasMoreTokens()) {
                    tmpData.add(Integer.parseInt(values.nextToken()));
                }
            }
            inst.setProfits(tmpData);
            inst.setConstraints(tmpData);
            inst.setCapacities(tmpData);
            return inst;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public void printProblemSummary() {
        System.out.println(
                String.format(
                        "This is the Knapsack Problem information: %n"
                        + "Items: %d %n"
                        + "Knapsacks: %d %n"
                        + "Constraints: %d %n"
                        + "Capacities: %d %n"
                        + "Profits: %n %s %n"
                        + "Constraints: %n %s",
                        this.items,
                        this.knapsacks,
                        this.constraints.length,
                        this.capacities.length,
                        Arrays.toString(this.getProfits()),
                        this.generateInequalities()
                )
        );

    }

    private String generateInequalities() {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < this.constraints.length; i++) {
            buf.append("********* Inequality # ");
            buf.append(i);
            buf.append(" ********** \n");
            for (int j = 0; j < this.constraints[i].length; j++) {
                buf.append(this.constraints[i][j]);
                buf.append("x").append(j).append("+");
            }
            buf.append(" <= ").append(this.capacities[i]).append("\n");
        }
        return buf.toString();
    }

    private void setProfits(LinkedList<Integer> tmp) {
        for (int i = 0; i < this.getProfits().length; i++) {
            this.profits[i] = tmp.poll();
        }
    }

    private void setConstraints(LinkedList<Integer> tmp) {
        for (int[] constraint : this.constraints) {
            for (int j = 0; j < constraint.length; j++) {
                constraint[j] = tmp.poll();
            }
        }
    }

    private void setCapacities(LinkedList<Integer> tmp) {
        for (int i = 0; i < this.capacities.length; i++) {
            this.capacities[i] = tmp.poll();
        }
    }

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
     * @return the constraints
     */
    public int[][] getConstraints() {
        return constraints.clone();
    }

    /**
     * @return the capacities
     */
    public int[] getCapacities() {
        return capacities;
    }

    /**
     * @return the profits
     */
    public int[] getProfits() {
        return profits;
    }

}
