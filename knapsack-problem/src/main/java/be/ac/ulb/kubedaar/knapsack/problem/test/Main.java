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
import be.ac.ulb.kubedaar.knapsack.problem.impl.Solution;
import be.ac.ulb.kubedaar.knapsack.problem.impl.ToyodaSolution;
import be.ac.ulb.kubedaar.knapsack.problem.impl.VNDImprovement;

/**
 *
 * @author masterulb
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Long randomSeed = 12345L;
        Long improvementRandomSeed = 12345L;
        String filePath = "debug-instance.dat";
        String ch = "random";
        String improvement = "";
        Solution sol;
        Solution fsol;
        Solution impSol;
        Improvement impv;
        ProblemInstance pins;

        // java -jar mkp.jar -f="instances/xxxxx.dat" 
        //                   -ch=random|greedy|toyoda 
        //                   -s=1234 
        //                   -imp=bi|fi|vnd 
        //                   -is=1236
        for (String arg : args) {
            if (arg.contains("-f")) {
                filePath = arg.split("=")[1];
                System.out.println(filePath);
            } else if (arg.contains("-ch")) {
                ch = arg.split("=")[1];
                System.out.println(ch);
            } else if (arg.contains("-s")) {
                randomSeed = Long.valueOf(arg.split("=")[1]);
                System.out.println(randomSeed);
            } else if (arg.contains("-imp")) {
                improvement = arg.split("=")[1];
                System.out.println(improvement);
            } else if (arg.contains("-is")) {
                improvementRandomSeed = Long.valueOf(arg.split("=")[1]);
                System.out.println(improvementRandomSeed);
            }
        }

        pins = ProblemInstance.readInstance(filePath);

        switch (ch) {
            case "random":
                sol = new RandomSolution(pins, randomSeed);
                break;
            case "greedy":
                sol = new GreedySolution(pins);
                break;
            case "toyoda":
                sol = new ToyodaSolution(pins);
                break;
            default:
                sol = new RandomSolution(pins, randomSeed);
                break;
        }

        fsol = sol.getFeasibleSolution().copy();
        fsol.printSolution();

        if (!improvement.equals("")) {
            switch (improvement) {
                case "bi":
                    impv = new BestImprovement(fsol, improvementRandomSeed);
                    break;
                case "fi":
                    impv = new FirstImprovement(fsol, improvementRandomSeed);
                    break;
                case "vnd":
                    impv = new VNDImprovement(fsol, improvementRandomSeed);
                    break;
                default:
                    impv = new BestImprovement(fsol, improvementRandomSeed);
                    break;
            }

            impSol = impv.getImprovedSolution();
            impSol.printSolution();
        }
    }

}
