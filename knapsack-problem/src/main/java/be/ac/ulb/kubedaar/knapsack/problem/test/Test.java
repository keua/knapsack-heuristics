/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ac.ulb.kubedaar.knapsack.problem.test;

import be.ac.ulb.kubedaar.knapsack.problem.impl.BestImprovement;
import be.ac.ulb.kubedaar.knapsack.problem.impl.GreedySolution;
import be.ac.ulb.kubedaar.knapsack.problem.impl.Improvement;
import be.ac.ulb.kubedaar.knapsack.problem.impl.ProblemInstance;
import be.ac.ulb.kubedaar.knapsack.problem.impl.RandomSolution;
import be.ac.ulb.kubedaar.knapsack.problem.impl.ToyodaSolution;

/**
 *
 * @author masterulb
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ProblemInstance i1 = ProblemInstance.readInstance("C:\\Users\\masterulb\\OneDrive - Université Libre de Bruxelles\\Master Computer Science and Engineering ULB\\Second term\\Heuristic optimization\\Implementation 1\\mkp_ho2018\\small1.dat");
        i1.printProblemSummary();
        RandomSolution rs = new RandomSolution(i1, 1234L);
        rs.getFeasibleSolution();
        GreedySolution gs = new GreedySolution(i1);
        gs.getFeasibleSolution();
        ToyodaSolution ts = new ToyodaSolution(i1);
        ts.getFeasibleSolution();
        Improvement gbi = new BestImprovement(gs);
        gbi.getImprovedSolution();
        Improvement rbi = new BestImprovement(rs);
        rbi.getImprovedSolution();
        Improvement tbi = new BestImprovement(ts);
        tbi.getImprovedSolution();

    }

}
