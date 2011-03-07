import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Multiplexer {
	
	private static int INIT_TREE_DEPTH = 4;
	private static int MAX_TREE_DEPTH = 10;
	private static int POP_SIZE = 3000;
	private static int MAX_EPOCHS = 100000;
	private static double MUTATION_PROB = 0.15; // Probability of mutation
	private static double CROSSOVER_PROB = 0.8; // Probability of crossover
	private static int TOURNAMENT_SAMPLE_SIZE = 50;
	private int order = 11;
	
	static Random rng = new Random();
	
	public Multiplexer(int order) {
		this.order = order;
	}
	
	public static <T> T randomSelect(ArrayList<T> population) {
		int r = rng.nextInt(population.size());
		return (T) population.get(r);
	}
	
	// random sample without replacement (unique):
	public static <T> ArrayList<T> randomSample(ArrayList<T> population, int sampleSize) {
		ArrayList<T> sample = new ArrayList<T>();
		ArrayList<T> populationCopy = new ArrayList<T>();
		populationCopy.addAll(population);
		
		for (int i=0; i<sampleSize; i++) {
			int r = rng.nextInt(populationCopy.size());
			Object o = populationCopy.get(r);
			populationCopy.remove(r);
			sample.add((T) o);
		}
		return sample;
	}
	
	public Program tournamentSelect(ArrayList<Program> population, ArrayList<Integer> fitnessCases) {
		Program best = null;
		int bestFitness = -1;

		ArrayList<Program> sample = randomSample(population, TOURNAMENT_SAMPLE_SIZE);
		for (Program p : sample) {
			int f = p.fitness(fitnessCases);
			if (f > bestFitness) {
				best = p;
				bestFitness = f;
			}
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
		ArrayList<Program> population = generatePopulation(POP_SIZE);

		int bestFitness = -1;
		
		for (int g=0; g<MAX_EPOCHS; g++) {
			ArrayList<Integer> fitnessCases = generateFitnessCases(2048, 205);
			int genBestFitness = -1;
			ArrayList<Program> newPopulation = new ArrayList<Program>();
			
			while (newPopulation.size() < POP_SIZE) { // this does sometimes allow population to grow too big by 1

				if (rng.nextFloat() < CROSSOVER_PROB) { // crossover
					Program parent = tournamentSelect(population, fitnessCases);
					ArrayList<Program> offspring = parent.crossover(tournamentSelect(population, fitnessCases));
					
					ArrayList<Program> result = new ArrayList<Program>();
					for (Program p : offspring) {
						if (rng.nextFloat() < MUTATION_PROB) result.add(p.mutate());
						else result.add(p.mutate());
					}
					newPopulation.addAll(result);
				} else { // simple copy
					newPopulation.add(tournamentSelect(population, fitnessCases));
				}
			}
			
			for (Program p : newPopulation) {
				int f = p.fitness(fitnessCases);
				if (f == fitnessCases.size()) {
					System.out.println("CANDIDATE OPTIMUM FOUND: " + p.fitness());
					System.out.println(p.tree.mathematicaNotation());
					System.out.println(p.tree.treeMaxHeight());
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
		Multiplexer mux = new Multiplexer(11);
		mux.evolve();
	//	System.out.println(mux.computeFitness(mux.correctSolution()));
	}
	
}
