/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ac.ulb.kubedaar.knapsack.problem.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

/**
 *
 * @author masterulb
 */
public class GeneticAlgorithm {

    Integer numPool; // Number of tournament pool
    Integer rateOfMutation; // Rate of mutation
    Integer initialPopulationSize; // Initial population size
    Integer rSeedArray[]; // Array with seed to generate the intal population
    Double tmax; // Max times
    LinkedList<Solution> population; // Solutions population
    ProblemInstance pIns; // Problem instance
    Random rGenerator;

    public GeneticAlgorithm(ProblemInstance problemInstance) {
        this.numPool = 2;
        this.rateOfMutation = 2;
        this.initialPopulationSize = 100;
        this.tmax = 100d * 1000;// 60 seconds * 1000 ms/sec
        this.population = new LinkedList<>();
        this.pIns = problemInstance;
        this.rSeedArray = new Integer[this.initialPopulationSize];
        this.rGenerator = new Random(1234L);
        for (int i = 0; i < this.initialPopulationSize; i++) {
            this.rSeedArray[i] = rGenerator.nextInt();
        }
    }

    public GeneticAlgorithm(
            Integer rateOfMutation, Integer initialPopulationSize,
            Double tmax, ProblemInstance pIns, Long rSeed
    ) {
        this.numPool = 2;
        this.rateOfMutation = rateOfMutation;
        this.initialPopulationSize = initialPopulationSize;
        this.population = new LinkedList<>();
        this.tmax = tmax * 1000; // seconds * 1000 ms/sec
        this.pIns = pIns;
        this.rSeedArray = new Integer[this.initialPopulationSize];
        this.rGenerator = new Random(rSeed);
        for (int i = 0; i < this.initialPopulationSize; i++) {
            this.rSeedArray[i] = rGenerator.nextInt();
        }
    }

    public Solution getSolution() {
        // set t :D 0;
        // initialise P(t) := {S1, . . . SN},  Si {0, 1}^n
        // evaluate P.t/ : f f .S1/; : : : ; f .SN/gI
        this.initializePopulation();
        // find S⁄ 2 P.t/ s:t: f .S⁄/ ‚ f .S/; 8S 2 P.t/;
        Solution bestSolution = this.getBestSolFromPop();
        //5: while t < tmax do
        long start = System.currentTimeMillis();
        long end = start + this.tmax.longValue(); // seconds * 1000 ms/sec
        while (System.currentTimeMillis() < end) {
            //for (int i = 0; i < this.tmax; i++) {
            Solution c;
            do {
                //6: select fP1; P2g :D 8.P.t//I =⁄ 8 D binary tournament selection ⁄=
                LinkedList<Solution> selection = this.tournament();
                //7: crossover C :D ˜c.P1; P2/I =⁄ ˜c D uniform crossover operator ⁄=
                c = this.crossover(selection);
                //8: mutate C ˆ ˜m.C/I =⁄ ˜m D mutation operator ⁄=
                c = this.mutate(c);
                //9: make C feasible, C ˆ ˜r.C/I =⁄ ˜r D repair operator ⁄=
                c = this.repair(c);
                //10: if C · any S 2 P.t/ then =⁄C is a duplicate of a member of the population ⁄=
            } while (this.isInPopulation(c)); //11: discard C and go to 6; //12: end if           
            //13: evaluate f .C/;
            //14: find S0 2 P.t/ s:t: f .S0/ • f .S/; 8S 2 P.t/and replace S0 ˆ C;
            this.updatePopulation(c);
            //=⁄ steady-state replacement ⁄=
            //15: if f .C/ > f .S⁄/ then
            if (c.getValue() > bestSolution.getValue()) {
                //16: S⁄ ˆ C;
                bestSolution = c.copy();
                System.out.println("new best value:" + bestSolution.getValue());
            }//17: end if =⁄ update best solution S⁄ found ⁄=
            //18: t ˆ t C 1;
        }
        //19: end while
        //20: return S⁄; f .S⁄/:
        return bestSolution;
    }

    private void initializePopulation() {
        for (int i = 0; i < this.initialPopulationSize; i++) {
            this.population.add(
                    new FirstImprovement(
                            new RandomSolution(this.pIns, this.rSeedArray[i]).getFeasibleSolution(),
                            this.rSeedArray[i].longValue()
                    ).getImprovedSolution()
            );
        }
    }

    private Solution getBestSolFromPop() {
        return getMaxValueSolution(this.population);
    }

    private Solution getMaxValueSolution(List<Solution> solutionList) {
        Solution maxSol
                = Collections.max(
                        solutionList,
                        (Solution first, Solution second) -> {
                            if (first.getValue() > second.getValue()) {
                                return 1;
                            } else if (first.getValue() < second.getValue()) {
                                return -1;
                            }
                            return 0;
                        }
                );
        return maxSol;
    }

    private Integer getMinValueSolution(List<Solution> solutionList) {
        Solution maxSol
                = Collections.min(
                        solutionList,
                        (Solution first, Solution second) -> {
                            if (first.getValue() > second.getValue()) {
                                return 1;
                            } else if (first.getValue() < second.getValue()) {
                                return -1;
                            }
                            return 0;
                        }
                );
        return solutionList.indexOf(maxSol);
    }

    private LinkedList<Solution> tournament() {
        LinkedList<Solution> shufflePop = new LinkedList<>(this.population);
        LinkedList<Solution> selection = new LinkedList<>();
        Collections.shuffle(shufflePop, this.rGenerator);
        int ini = 0;
        int fin;
        for (int i = 0; i < this.numPool; i++) {
            fin = (ini + this.initialPopulationSize / this.numPool) - 1;
            selection.add(this.getMaxValueSolution(shufflePop.subList(ini, fin)));
            ini = fin + 1;
        }
        return selection;
    }

    private Solution crossover(LinkedList<Solution> selection) {
        Solution nsol = selection.element().copy();
        for (int i = 0; i < this.pIns.getItems(); i++) {
            int selectedValue
                    = (this.rGenerator.nextBoolean())
                    ? selection.get(0).getSolution()[i]
                    : selection.get(1).getSolution()[i];
            if (selectedValue == 1) {
                nsol.addItem(i);
            } else {
                if (nsol.getSolution()[i] == 1) {
                    nsol.removeItem(i);
                }
            }
        }
        return nsol;
    }

    private Solution mutate(Solution c) {
        for (int i = 0; i < this.rateOfMutation; i++) {
            int flip = this.rGenerator.nextInt(this.pIns.getItems());
            if (c.getSolution()[flip] == 1) {
                c.removeItem(flip);
            } else {
                c.addItem(flip);
            }
        }
        return c;
    }

    private Solution repair(Solution c) {
        LinkedList<Integer> inSolutionList = c.getSolutionList();
        Collections.shuffle(inSolutionList, this.rGenerator);
        c.checkViolatedConstraints();
        LinkedList<Integer> removed = new LinkedList();
        while (!c.isFeasibleSolution()) {
            int itemToRemove = inSolutionList.poll();
            c.removeItem(itemToRemove);
            removed.add(itemToRemove);
            c.checkViolatedConstraints();
        }
        PriorityQueue<Map.Entry<Integer, Object>> tmpPqueue
                = new PriorityQueue<>(c.sortNonInsertedList());
        while (!tmpPqueue.isEmpty()) {
            // check feasibility 
            int itemToAdd = tmpPqueue.poll().getKey();
            if (!removed.stream().anyMatch(x -> x == itemToAdd)) {
                c.checkBeforeAddItem(itemToAdd);
            }
        }
        return c;
    }

    private boolean isInPopulation(Solution c) {
        return this.population
                .stream()
                .anyMatch((indivdual) -> (Arrays.equals(c.getSolution(), indivdual.getSolution())));
    }

    private void updatePopulation(Solution c) {
        Integer minIndex = this.getMinValueSolution(this.population);
        this.population.set(minIndex, c.copy());
    }

}
