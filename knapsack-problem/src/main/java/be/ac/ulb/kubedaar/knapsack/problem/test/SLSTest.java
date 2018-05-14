/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ac.ulb.kubedaar.knapsack.problem.test;

import be.ac.ulb.kubedaar.knapsack.problem.impl.BestImprovement;
import be.ac.ulb.kubedaar.knapsack.problem.impl.GeneticAlgorithm;
import be.ac.ulb.kubedaar.knapsack.problem.impl.Improvement;
import be.ac.ulb.kubedaar.knapsack.problem.impl.ProblemInstance;
import be.ac.ulb.kubedaar.knapsack.problem.impl.RandomSolution;
import be.ac.ulb.kubedaar.knapsack.problem.impl.SimulatedAnnealing;
import be.ac.ulb.kubedaar.knapsack.problem.impl.Solution;

/**
 *
 * @author masterulb
 */
public class SLSTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //ProblemInstance i1 = ProblemInstance.readInstance("debug-instance.dat");
        ProblemInstance i1 = ProblemInstance.readInstance("../instances/OR10x100-0.25_9.dat");
        i1.printProblemSummary();
        System.out.println("********** Random Soltuion **********");
        RandomSolution rs = new RandomSolution(i1, 12345L);
        rs.getFeasibleSolution();
        //rs.printSolution();
        System.out.println("********** Random Soltuion FI **********");
        Improvement rfi = new BestImprovement(rs);
        Solution nfirsol = rfi.getImprovedSolution();
        //nfirsol.printSolution();
        System.out.println("********** SA Random Soltuion **********");
        SimulatedAnnealing sar = new SimulatedAnnealing(nfirsol, 100f, 0.98f, 0.00001f, 200, 67465L, 100d);
        sar.getSolution().printSolution();
        System.out.println("********** GA Random Soltuion **********");
        GeneticAlgorithm ga = new GeneticAlgorithm(2, 100, 100d, i1, 48658L);
        ga.getSolution().printSolution();
    }

}
