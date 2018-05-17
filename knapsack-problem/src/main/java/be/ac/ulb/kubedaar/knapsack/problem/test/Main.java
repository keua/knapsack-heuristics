/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ac.ulb.kubedaar.knapsack.problem.test;

import be.ac.ulb.kubedaar.knapsack.problem.impl.BestImprovement;
import be.ac.ulb.kubedaar.knapsack.problem.impl.FirstImprovement;
import be.ac.ulb.kubedaar.knapsack.problem.impl.GeneticAlgorithm;
import be.ac.ulb.kubedaar.knapsack.problem.impl.GreedySolution;
import be.ac.ulb.kubedaar.knapsack.problem.impl.Improvement;
import be.ac.ulb.kubedaar.knapsack.problem.impl.ProblemInstance;
import be.ac.ulb.kubedaar.knapsack.problem.impl.RandomSolution;
import be.ac.ulb.kubedaar.knapsack.problem.impl.SimulatedAnnealing;
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
        String sls = "";
        SimulatedAnnealing sa;
        GeneticAlgorithm ga;
        Double maxTime = 100d;
        Long slsSeed = 3546L;
        Integer mutationRate = 2;
        Integer populationSize = 50;
        Float initialTemp = 100f;
        Float finalTemp = 0.00001f;
        Float decreasingFactor = 0.845f;
        Integer roundPerTemp = 200;
        Solution slsSol;

        // java -jar mkp.jar -f="instances/xxxxx.dat" 
        //                   -ch=random|greedy|toyoda 
        //                   -s=1234 
        //                   -imp=bi|fi|vnd 
        //                   -is=1236
        //                   -sls=ga|sa
        //                   -maxtime=100.00
        //                   -slsseed=1234
        //                   -gamr=2
        //                   -gaps=50
        //                   -sait=100.00
        //                   -saft=0.00001
        //                   -sadf=0.845
        //                   -sarpt=250
        for (String arg : args) {
            if (arg.contains("-f=")) {
                filePath = arg.split("=")[1];
                System.out.println(filePath);
            } else if (arg.contains("-ch")) {
                ch = arg.split("=")[1];
                System.out.println(ch);
            } else if (arg.contains("-s=")) {
                randomSeed = Long.valueOf(arg.split("=")[1]);
                System.out.println(randomSeed);
            } else if (arg.contains("-imp=")) {
                improvement = arg.split("=")[1];
                System.out.println(improvement);
            } else if (arg.contains("-is=")) {
                improvementRandomSeed = Long.valueOf(arg.split("=")[1]);
                System.out.println(improvementRandomSeed);
            } else if (arg.contains("-sls=")) {
                sls = arg.split("=")[1];
                System.out.println(sls);
            } else if (arg.contains("-maxtime=")) {
                maxTime = Double.valueOf(arg.split("=")[1]);
                System.out.println(maxTime);
            } else if (arg.contains("-slsseed=")) {
                slsSeed = Long.valueOf(arg.split("=")[1]);
                System.out.println(slsSeed);
            } else if (arg.contains("-gamr=")) {
                mutationRate = Integer.valueOf(arg.split("=")[1]);
                System.out.println(mutationRate);
            } else if (arg.contains("-gaps=")) {
                populationSize = Integer.valueOf(arg.split("=")[1]);
                System.out.println(populationSize);
            } else if (arg.contains("-sait=")) {
                initialTemp = Float.valueOf(arg.split("=")[1]);
                System.out.println(initialTemp);
            } else if (arg.contains("-saft=")) {
                finalTemp = Float.valueOf(arg.split("=")[1]);
                System.out.println(finalTemp);
            } else if (arg.contains("-sadf=")) {
                decreasingFactor = Float.valueOf(arg.split("=")[1]);
                System.out.println(decreasingFactor);
            } else if (arg.contains("-sarpt=")) {
                roundPerTemp = Integer.valueOf(arg.split("=")[1]);
                System.out.println(roundPerTemp);
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
        } else {
            impSol = fsol;
        }

        if (!"".equals(sls)) {
            switch (sls) {
                case "sa":
                    sa = new SimulatedAnnealing(
                            impSol, initialTemp, decreasingFactor,
                            finalTemp, roundPerTemp, slsSeed, maxTime
                    );
                    slsSol = sa.getSolution();
                    break;
                case "ga":
                    ga = new GeneticAlgorithm(
                            mutationRate, populationSize, maxTime, pins, slsSeed
                    );
                    slsSol = ga.getSolution();
                    break;
                default:
                    ga = new GeneticAlgorithm(
                            mutationRate, populationSize, maxTime, pins, slsSeed
                    );
                    slsSol = ga.getSolution();
                    break;
            }
            slsSol.printSolution();
        }

    }

}
