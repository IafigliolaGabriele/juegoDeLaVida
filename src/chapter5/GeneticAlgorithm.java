package chapter5;

import java.util.Random;


public class GeneticAlgorithm {

	private int populationSize;
	private double mutationRate;
	private double crossoverRate;
	private int elitismCount;
	protected int tournamentSize;
        private boolean cruce1=true; // cruce 1: escojo aleatoriamente por cada gen individual. cruce2: mitad de uno y mitaddel otro
        private boolean flag=true; // true: torneo, false ruleta

	public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount,
			int tournamentSize) {

		this.populationSize = populationSize;
		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.elitismCount = elitismCount;
		this.tournamentSize = tournamentSize;
	}

	/**
	 * Initialize population
	 * 
	 * @param chromosomeLength
	 *            The length of the individuals chromosome
	 * @return population The initial population generated
	 */
	public Population initPopulation(Timetable timetable) {
		// Initialize population
		Population population = new Population(this.populationSize, timetable);
		return population;
	}

	/**
	 * Check if population has met termination condition
	 * 
	 * @param generationsCount
	 *            Number of generations passed
	 * @param maxGenerations
	 *            Number of generations to terminate after
	 * @return boolean True if termination condition met, otherwise, false
	 */
	public boolean isTerminationConditionMet(int generationsCount, int maxGenerations) {
		return (generationsCount > maxGenerations);
	}

	/**
	 * Check if population has met termination condition
	 *
	 * @param population
	 * @return boolean True if termination condition met, otherwise, false
	 */
	public boolean isTerminationConditionMet(Population population) {
		return population.getFittest(0).getFitness() == 1.0;
	}

	/**
	 * Calculate individual's fitness value
	 * 
	 * @param individual
	 * @param timetable
	 * @return fitness
	 */
	public double calcFitness(Individual individual, Timetable timetable) {

		// Create new timetable object to use -- cloned from an existing timetable
		Timetable threadTimetable = new Timetable(timetable);
		threadTimetable.createClasses(individual);

		// Calculate fitness
		int clashes = threadTimetable.calcClashes();
		double fitness = 1 / (double) (clashes + 1);

		individual.setFitness(fitness);

		return fitness;
	}

	/**
	 * Evaluate population
	 * 
	 * @param population
	 * @param timetable
	 */
	public void evalPopulation(Population population, Timetable timetable) {
		double populationFitness = 0;

		// Loop over population evaluating individuals and summing population
		// fitness
                float aux =0;
		for (Individual individual : population.getIndividuals()) {
                        aux++;
			populationFitness += this.calcFitness(individual, timetable);
		}

		population.setPopulationFitness(populationFitness/aux);
	}

	/**
	 * Selects parent for crossover using tournament selection
	 * 
	 * Tournament selection works by choosing N random individuals, and then
	 * choosing the best of those.
	 * 
	 * @param population
     * @param flag
	 * @return The individual selected as a parent
	 */
	public Individual selectParent(Population population,Boolean flag) {
            
                //If flag is set to true, we use tournament selection 
                //Else we use roulette selection
                
                
		// Create tournament
//		Population tournament = new Population(this.tournamentSize);
                Population tournament;
                Population roulette;
                
                if(flag){
                    //We use tournament
                    // Create tournament
                    tournament = new Population(this.tournamentSize);
                    population.shuffle();
                    for (int i = 0; i < this.tournamentSize; i++) {
                            Individual tournamentIndividual = population.getIndividual(i);
                            tournament.setIndividual(i, tournamentIndividual);
                    }
                    
                    return tournament.getFittest(0);
                }
                else{
                    
                    //We use roulette
                    
                    roulette = new Population(this.populationSize);
                    population.shuffle();
                    
                    Individual[] individuals = population.getIndividuals();
                    double total_fit = population.getPopulationFitness();
                    
                    
                    double[] normalizedProbabilityArray = new double[this.populationSize];
                    double prev_probability = 0;
                    
                    for(int i = 0; i < individuals.length; i++){
                        prev_probability = prev_probability + ( individuals[i].getFitness()/total_fit ) ;
                        normalizedProbabilityArray[i] = prev_probability;
                    }
                    
                    
                    
                    double randomValue = new Random().nextDouble() * 1;
                    
                    int individualPosition=0;
                    
                    for(int i =0 ; i < normalizedProbabilityArray.length ; i++){
                       	
                        if(randomValue < normalizedProbabilityArray[i]){
                            individualPosition = i;
                        }
                    }
                    
                    return individuals[individualPosition];
                }

                
	}


	/**
     * Apply mutation to population
     * 
     * @param population
     * @param timetable
     * @return The mutated population
     */
	public Population mutatePopulation(Population population, Timetable timetable) {
		// Initialize new population
		Population newPopulation = new Population(this.populationSize);

		// Loop over current population by fitness
		for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
			Individual individual = population.getFittest(populationIndex);

			// Create random individual to swap genes with
			Individual randomIndividual = new Individual(timetable);

			// Loop over individual's genes
			for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
				// Skip mutation if this is an elite individual
				if (populationIndex > this.elitismCount) {
					// Does this gene need mutation?
					if (this.mutationRate > Math.random()) {
						// Swap for new gene
						individual.setGene(geneIndex, randomIndividual.getGene(geneIndex));
					}
				}
			}

			// Add individual to population
			newPopulation.setIndividual(populationIndex, individual);
		}

		// Return mutated population
		return newPopulation;
	}

    /**
     * Apply crossover to population
     * 
     * @param population The population to apply crossover to
     * @return The new population
     */
	public Population crossoverPopulation(Population population) {
		// Create new population
		Population newPopulation = new Population(population.size());

		// Loop over current population by fitness
		for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
			Individual parent1 = population.getFittest(populationIndex);

			// Apply crossover to this individual?
			if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
				// Initialize offspring
				Individual offspring = new Individual(parent1.getChromosomeLength());
				
				// Find second parent
                                //If second parameter is true, selection is done via tournament
                                //Else is done via roulette 
				Individual parent2 = selectParent(population,flag);

				// Loop over genome
                                if (cruce1){
                                    for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
                                            // Use half of parent1's genes and half of parent2's genes
                                            if (0.5 > Math.random()) {
                                                    offspring.setGene(geneIndex, parent1.getGene(geneIndex));
                                            } else {
                                                    offspring.setGene(geneIndex, parent2.getGene(geneIndex));
                                            }
                                    }
                                } else{
                                    boolean seleccion= (0.5> Math.random());
                                    for(int geneIndex = 0; geneIndex < offspring.getChromosomeLength(); geneIndex++){
                                        if (seleccion){
                                            if(geneIndex < offspring.getChromosomeLength()/2){
                                                offspring.setGene(geneIndex, parent1.getGene(geneIndex));
                                            }else{
                                                offspring.setGene(geneIndex, parent2.getGene(geneIndex));
                                            }
                                        } else{
                                            if(geneIndex < offspring.getChromosomeLength()/2){
                                                offspring.setGene(geneIndex, parent2.getGene(geneIndex));
                                            }else{
                                                offspring.setGene(geneIndex, parent1.getGene(geneIndex));
                                            }
                                        }
                                    }
                                }

				// Add offspring to new population
				newPopulation.setIndividual(populationIndex, offspring);
			} else {
				// Add individual to new population without applying crossover
				newPopulation.setIndividual(populationIndex, parent1);
			}
		}

		return newPopulation;
	}



}
