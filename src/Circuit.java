import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Circuit {
	
	private int popSize; // Size of the population (remains constant)
	private int maxEpochs; // Maximum number of generations until the program should terminate
	private double mutationProb; // Probability of mutation
	private double crossoverProb; // Probability of crossover
	private int tournamentSampleSize; // Size of the sample used in tournament selection
	private int fitnessCasesCount; // Number of fitness cases to apply (for efficiency, this is a supet of 2^order)
	private int order; // Order of the multiplexer
	private boolean elitismEnabled;
	
	static Random rng = new Random();
	
	
	public static <T> T randomSelect(ArrayList<T> population) {
		int r = rng.nextInt(population.size());
		return population.get(r);
	}
	
	// random sample without replacement (unique):
	public static <T> ArrayList<T> randomSample(ArrayList<T> population, int sampleSize) {
		ArrayList<T> sample = new ArrayList<T>();
		ArrayList<T> populationCopy = new ArrayList<T>();
		populationCopy.addAll(population);
		
		for (int i=0; i<sampleSize; i++) {
			int r = rng.nextInt(populationCopy.size());
			T o = populationCopy.get(r);
			populationCopy.remove(r);
			sample.add(o);
		}
		return sample;
	}
	
	public Program tournamentSelect(ArrayList<Program> population, ArrayList<Integer> fitnessCases) {
		Program best = null;

		ArrayList<Program> sample = randomSample(population, tournamentSampleSize);
		for (Program p : sample) {
			if (best == null) best = p;
			if (p.fitness(fitnessCases) > best.fitness(fitnessCases)) best = p;
		}
		return best;
	}
	

	/* Generates a subset of fitness cases to allow for fast(er) processing for more complex multiplexers.
	 * totalCases is used to specify the total number of cases that this multiplexer supports, and fraction
	 * is used to specify the desired fraction of fitness cases that will be used in order to speed up processing.
	 * e.g. totalCases = 2048, fraction 0.1 = 10% of 2048 = 205
	 */
	
	private ArrayList<Integer> generateFitnessCases(int totalCases, int subsetCases) {
		ArrayList<Integer> allCases = new ArrayList<Integer>();
		for (int i=0; i<totalCases; i++) allCases.add(i);
		
		ArrayList<Integer> subset = new ArrayList<Integer>();
		for (int i=0; i<subsetCases; i++) {
			int j = rng.nextInt(allCases.size());
			subset.add(allCases.get(j));
			allCases.remove(j);
		}
		return subset;
	}
	
	private ArrayList<Program> generatePopulation(int size) {
		ArrayList<Program> population = new ArrayList<Program>();
		for (int i=0; i<size; i++) population.add(new Program(order));
		return population;
	}
	
	public void evolve() {
		// generate initial population
		ArrayList<Program> population = generatePopulation(popSize);

		int bestFitness = -1;
		
		for (int g=0; g<maxEpochs; g++) {
			ArrayList<Integer> fitnessCases = generateFitnessCases((int) Math.pow(2, order), fitnessCasesCount);

			int genBestFitness = -1;
			ArrayList<Program> newPopulation = new ArrayList<Program>();
			
			// Elitism:
			if (elitismEnabled) {
				Program bestProgram = null;
				for (Program p : population) {
					if (bestProgram == null) bestProgram = p;
					if (p.fitness(fitnessCases) > bestProgram.fitness(fitnessCases)) bestProgram = p;
				}
				newPopulation.add(bestProgram);
			}
			
			while (newPopulation.size() < popSize) { // this does sometimes allow population to grow too big by 1

				if (rng.nextFloat() < crossoverProb) { // crossover
					Program parent = tournamentSelect(population, fitnessCases);
					ArrayList<Program> offspring = parent.crossover(tournamentSelect(population, fitnessCases));
					
					ArrayList<Program> mutated = new ArrayList<Program>();
					for (Program p : offspring) {
						if (rng.nextFloat() < mutationProb) mutated.add(p.mutate());
						else mutated.add(p);
					}
					newPopulation.addAll(mutated);
				} else { // simple copy
					newPopulation.add(tournamentSelect(population, fitnessCases));
				}
			}
			
			for (Program p : newPopulation) {
				int f = p.fitness(fitnessCases);
				if (f == fitnessCases.size()) {
					int fullFitness = p.fitness();
					if (fullFitness == Math.pow(2, order)) {
						System.out.println(p.tree.mathematicaNotation());
						System.out.println(p.tree.treeMaxHeight());
						System.exit(0);
					}
				}
				if (f > bestFitness) bestFitness = f;
				if (f > genBestFitness) genBestFitness = f;
			}
		
			population  = newPopulation;
			System.out.println(g + "> " + bestFitness);
		}
		
	}
	
	private Operator correctSolution() {
		// (IF Al (IF A0 D3 D2) (IF A0 D1 D0))
		
	/*	IfOp a = new IfOp(new TerminalOp("a0"), new TerminalOp("d3"), new TerminalOp("d2"));
		IfOp b = new IfOp(new TerminalOp("a0"), new TerminalOp("d1"), new TerminalOp("d0"));
		IfOp c = new IfOp(new TerminalOp("a1"), a, b);
		return c;*/
		return null;
	}
	
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("ERROR: You didn't specify the mode you want to run. Valid options: 6multiplexer, 11multipler, 16middle3.");
		} else {
			Circuit c = new Circuit();
			if (args[0].equals("6multiplexer")) {
				c.order = 6;
				c.popSize = 300;
				c.maxEpochs = 10000;
				c.mutationProb = 0.2;
				c.crossoverProb = 0.9;
				c.tournamentSampleSize = 30;
				c.fitnessCasesCount = 64;
				c.elitismEnabled = false;
			} else if (args[0].equals("11multiplexer")) {
				c.order = 11;
				c.popSize = 3000;
				c.maxEpochs = 10000;
				c.mutationProb = 0.15;
				c.crossoverProb = 0.8;
				c.tournamentSampleSize = 150;
				c.fitnessCasesCount = 205;
				c.elitismEnabled = false;
			} else if (args[0].equals("16middle3")) {
				c.order = 16;
				c.popSize = 3000;
				c.maxEpochs = 10000;
				c.mutationProb = 0.15;
				c.crossoverProb = 0.8;
				c.tournamentSampleSize = 150;
				c.fitnessCasesCount = 655;
				c.elitismEnabled = true;
			} else {
				System.out.println("ERROR: Invalid option. Valid options: 6multiplexer, 11multiplexer, 16middle3.");
			}
			c.evolve();
		//	System.out.println(mux.computeFitness(mux.correctSolution()));

		}
	}
	
}
