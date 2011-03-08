import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Circuit {
	
	// USER DEFINED PARAMETERS:
	private int popSize; // Size of the population (remains constant)
	private int maxEpochs; // Maximum number of generations until the program should terminate
	public int initTreeDepth;
	public int maxTreeDepth;
	private double mutationProb; // Probability of mutation
	private double crossoverProb; // Probability of crossover
	private int tournamentSampleSize; // Size of the sample used in tournament selection
	private int fitnessCasesCount; // Number of fitness cases to apply (for efficiency, this is a supet of 2^order)
	private boolean elitismEnabled;

	public int order; // Order of the multiplexer [computed at runtime]
	private int maxFitnessCasesCount; // 2^order [computed at runtime]

	static Random rng = new Random();
	
	public Circuit(int order) {
		this.order = order;
		this.maxFitnessCasesCount = (int)Math.pow(2,order);
	}
	
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
		for (int i=0; i<size; i++) population.add(new Program(this));
		return population;
	}
	
	public void evolve() {
		// generate initial population
		ArrayList<Program> population = generatePopulation(popSize);

		Program bestProgram = null;
		
		for (int g=0; g<maxEpochs; g++) {
			ArrayList<Integer> fitnessCases = generateFitnessCases(maxFitnessCasesCount, fitnessCasesCount);

			ArrayList<Program> newPopulation = new ArrayList<Program>();
			
			// Elitism:
			if (elitismEnabled) {
				if (fitnessCasesCount != maxFitnessCasesCount) System.out.println("WARNING: Elitism is enabled but fitnessCasesCount != 2^order. This means elitism will have a limited (or no) effect as fitness cannot be compared between generations");
				Program prevBestProgram = null;
				for (Program p : population) {
					if (prevBestProgram == null) prevBestProgram = p;
					if (p.fitness(fitnessCases) > prevBestProgram.fitness(fitnessCases)) prevBestProgram = p;
				}
				newPopulation.add(prevBestProgram);
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
			
			Program genBestProgram = null;
			for (Program p : newPopulation) {
				
				int f = p.fitness(fitnessCases);
				if (f == fitnessCases.size()) {
					int fullFitness = p.fitness();
					if (fullFitness == maxFitnessCasesCount) {
						System.out.println("*********************************************");
						System.out.println(g + "> " + fullFitness + "/" + maxFitnessCasesCount + "\t\t" + p.tree);
						System.out.println("You can also plot this in Mathematica using: " + p.tree.mathematicaNotation() + " // TreeForm");
						System.exit(0);
						
					}
				}
				if (bestProgram == null) bestProgram = p;
				if (genBestProgram == null) genBestProgram = p;
				
				if (f > bestProgram.fitness(fitnessCases)) bestProgram = p;
				if (f > genBestProgram.fitness(fitnessCases)) genBestProgram = p;
			}
		
			population  = newPopulation;
			System.out.println(g + "> " + genBestProgram.fitness(fitnessCases) + "/" + fitnessCasesCount + "/" + maxFitnessCasesCount + "\t\t" + genBestProgram.tree);
		}
		
	}
	
	private Node correctSolution() {
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
			Circuit c = null;
			if (args[0].equals("6multiplexer")) {
				c = new Circuit(6);
				c.popSize = 500;
				c.maxEpochs = 10000;
				c.initTreeDepth = 3;
				c.maxTreeDepth = 8;
				c.mutationProb = 0.2;
				c.crossoverProb = 0.8;
				c.tournamentSampleSize = 50;
				c.fitnessCasesCount = 64;
				c.elitismEnabled = true;
			} else if (args[0].equals("11multiplexer")) {
				c = new Circuit(11);
				c.popSize = 3000;
				c.maxEpochs = 10000;
				c.initTreeDepth = 3;
				c.maxTreeDepth = 10;
				c.mutationProb = 0.15;
				c.crossoverProb = 0.8;
				c.tournamentSampleSize = 150;
				c.fitnessCasesCount = 512;
				c.elitismEnabled = false;
			} else if (args[0].equals("16middle3")) {
				c = new Circuit(16);
				c.popSize = 6000;
				c.maxEpochs = 10000;
				c.initTreeDepth = 5;
				c.maxTreeDepth = 15;
				c.mutationProb = 0.15;
				c.crossoverProb = 0.8;
				c.tournamentSampleSize = 150;
				c.fitnessCasesCount = 300;
				c.elitismEnabled = false;
			} else {
				System.out.println("ERROR: Invalid option. Valid options: 6multiplexer, 11multiplexer, 16middle3.");
			}
			c.evolve();
		//	System.out.println(mux.computeFitness(mux.correctSolution()));

		}
	}
	
}
