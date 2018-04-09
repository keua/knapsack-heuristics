/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ac.ulb.kubedaar.knapsack.problem.test;

import be.ac.ulb.kubedaar.knapsack.problem.impl.BestImprovement;
import be.ac.ulb.kubedaar.knapsack.problem.impl.FirstImprovement;
import be.ac.ulb.kubedaar.knapsack.problem.impl.GreedySolution;
import be.ac.ulb.kubedaar.knapsack.problem.impl.Improvement;
import be.ac.ulb.kubedaar.knapsack.problem.impl.ProblemInstance;
import be.ac.ulb.kubedaar.knapsack.problem.impl.RandomSolution;
import be.ac.ulb.kubedaar.knapsack.problem.impl.ToyodaSolution;
import be.ac.ulb.kubedaar.knapsack.problem.impl.VNDImprovement;

/**
 *
 * @author masterulb
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ProblemInstance i1 = ProblemInstance.readInstance("debug-instance.dat");
        //ProblemInstance i1 = ProblemInstance.readInstance("../instances/OR10x100-0.25_2.dat");
        i1.printProblemSummary();
        System.out.println("********** Random Soltuion **********");
        RandomSolution rs = new RandomSolution(i1, 12345L);
        rs.getFeasibleSolution();
        rs.printSolution();
        System.out.println("********** Greedy Soltuion **********");
        GreedySolution gs = new GreedySolution(i1);
        gs.getFeasibleSolution();
        gs.printSolution();
        System.out.println("********** Toyoda Soltuion **********");
        ToyodaSolution ts = new ToyodaSolution(i1);
        ts.getFeasibleSolution();
        ts.printSolution();
        System.out.println("********** Greedy Soltuion BI **********");
        Improvement gbi = new BestImprovement(gs);
        gbi.getImprovedSolution().printSolution();
        System.out.println("********** Random Soltuion BI **********");
        Improvement rbi = new BestImprovement(rs);
        rbi.getImprovedSolution().printSolution();
        System.out.println("********** Toyoda Soltuion BI **********");
        Improvement tbi = new BestImprovement(ts);
        tbi.getImprovedSolution().printSolution();
        System.out.println("********** Greedy Soltuion FI **********");
        Improvement gfi = new FirstImprovement(gs);
        gfi.getImprovedSolution().printSolution();
        System.out.println("********** Random Soltuion FI **********");
        Improvement rfi = new FirstImprovement(rs);
        rfi.getImprovedSolution().printSolution();
        System.out.println("********** Toyoda Soltuion FI **********");
        Improvement tfi = new FirstImprovement(ts);
        tfi.getImprovedSolution().printSolution();
        System.out.println("********** Greedy Soltuion VND **********");
        Improvement gvnd = new VNDImprovement(gs);
        gvnd.getImprovedSolution().printSolution();
        System.out.println("********** Random Soltuion VND **********");
        Improvement rvnd = new VNDImprovement(rs);
        rvnd.getImprovedSolution().printSolution();
        System.out.println("********** Toyoda Soltuion VND **********");
        Improvement tvnd = new VNDImprovement(ts);
        tvnd.getImprovedSolution().printSolution();

    }

}
