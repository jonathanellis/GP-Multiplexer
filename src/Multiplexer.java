import java.util.ArrayList;
import java.util.Random;

public class Multiplexer {
	
	private static int INIT_TREE_DEPTH = 3;
	private static int MAX_TREE_DEPTH = 8;
	private static int POP_SIZE = 500;
	private static int MAX_EPOCHS = 100000;
	private static double MUTATION_PROB = 0.1; // Probability of mutation
	private static double CROSSOVER_PROB = 0.8; // Probability of crossover
	private static int TOURNAMENT_SIZE = 100;
	
	Random rng = new Random();
	
	private Operator correctSolution() {
		// (IF Al (IF A0 D3 D2) (IF A0 D1 D0))
		
		IfOp a = new IfOp(new TerminalOp(TerminalOp.terminal.a0), new TerminalOp(TerminalOp.terminal.d3), new TerminalOp(TerminalOp.terminal.d2));
		IfOp b = new IfOp(new TerminalOp(TerminalOp.terminal.a0), new TerminalOp(TerminalOp.terminal.d1), new TerminalOp(TerminalOp.terminal.d0));
		IfOp c = new IfOp(new TerminalOp(TerminalOp.terminal.a1), a, b);
		return c;
	}
	
	private Operator generateRandomTree(int depth) {
		Operator root;
		Random rng = new Random();
		
		int r = rng.nextInt(4);
		if (r == 0) root =  new AndOp();
		else if (r == 1) root = new OrOp();
		else if (r == 2) root = new NotOp();
		else root = new IfOp();
		
		root.grow(depth-1);
			
		return root;
	}
	
	public Operator randomSelect(ArrayList<Operator> population) {
		int r = rng.nextInt(population.size());
		return population.get(r);
	}
	
	// random sample without replacement (unique):
	public ArrayList<Operator> randomSample(ArrayList<Operator> population, int sampleSize) {
		ArrayList<Operator> sample = new ArrayList<Operator>();
		ArrayList<Operator> populationCopy = new ArrayList<Operator>();
		populationCopy.addAll(population);
		
		for (int i=0; i<sampleSize; i++) {
			int r = rng.nextInt(populationCopy.size());
			Operator o = populationCopy.get(r);
			populationCopy.remove(r);
			sample.add(o);
		}
		return sample;
	}
	
	public Operator tournamentSelect(ArrayList<Operator> population) {
		Operator best = null;
		int bestFitness = -1;

		ArrayList<Operator> sample = randomSample(population, TOURNAMENT_SIZE);
		for (Operator o : sample) {
			int f = computeFitness(o);
			if (f > bestFitness) {
				best = o;
				bestFitness = f;
			}
		}
		return best;
	}
	
	public ArrayList<Operator> crossover(Operator mother, Operator father) {
		Operator motherCopy;
		Operator fatherCopy;
		
		do {
			motherCopy = mother.clone();
			fatherCopy = father.clone();
			
			ArrayList<Operator> mCandidates;
			if (rng.nextFloat() < 0.9) mCandidates = motherCopy.nonTerminalsToList();
			else mCandidates = motherCopy.terminalsToList();
			
			Operator mOp = randomSelect(mCandidates);
			
			ArrayList<Operator> fCandidates;
			if (rng.nextFloat() < 0.9) fCandidates = fatherCopy.nonTerminalsToList();
			else fCandidates = fatherCopy.terminalsToList();
			Operator fOp = randomSelect(fCandidates);
			
			if (fOp == fatherCopy) fatherCopy = mOp.clone();
			else fatherCopy.swapSubtree(fOp, mOp.clone());
			
			if (mOp == motherCopy) motherCopy = fOp.clone();
			else motherCopy.swapSubtree(mOp, fOp.clone());
		} while (motherCopy.treeMaxHeight() > MAX_TREE_DEPTH || fatherCopy.treeMaxHeight() > MAX_TREE_DEPTH);

		ArrayList<Operator> offspring = new ArrayList<Operator>();
		offspring.add(motherCopy);
		offspring.add(fatherCopy);
		return offspring;
	}
	
	public Operator mutate(Operator o) {
		ArrayList<Operator> candidates = o.nonTerminalsToList();
		candidates.addAll(o.terminalsToList());
		do {
			Operator p = randomSelect(candidates);
			int treeHeight = rng.nextInt(4)+1;
			Operator newTree = generateRandomTree(treeHeight);
			o.swapSubtree(p, newTree);
		} while (o.treeMaxHeight() > MAX_TREE_DEPTH);
		return o;
	}
	
	public int computeFitness(Operator tree) {
		int fitness = 0;
		for (int a0=0; a0<=1; a0++) {
			for (int a1=0; a1<=1; a1++) {
				for (int d0=0; d0<=1; d0++) {
					for (int d1=0; d1<=1; d1++) {
						for (int d2=0; d2<=1; d2++) {
							for (int d3=0; d3<=1; d3++) {
								
								boolean a0b = a0 > 0;
								boolean a1b = a1 > 0;
								boolean d0b = d0 > 0;
								boolean d1b = d1 > 0;
								boolean d2b = d2 > 0;
								boolean d3b = d3 > 0;
								
								boolean actualOutput = tree.evaluate(new Valuation(a0, a1, d0, d1, d2, d3));
								
								// use existing program:
								
								boolean correctOutput;
								
								if (a0b && a1b) {
									correctOutput = d3b;
								} else {
									if (a0b) {
										correctOutput = d1b;
									} else {
										if (a1b) correctOutput = d2b;
										else correctOutput = d0b;
									}
								}
								
								if (actualOutput == correctOutput) fitness++;
								
							}
						}
					}
				}
			}
		}
		return fitness;
	}
	
	private ArrayList<Operator> generatePopulation(int size) {
		ArrayList<Operator> population = new ArrayList<Operator>();
		for (int i=0; i<size; i++) population.add(generateRandomTree(INIT_TREE_DEPTH));
		return population;
	}
	
	public void evolve() {
		// generate initial population
		ArrayList<Operator> population = generatePopulation(POP_SIZE);
		int bestFitness = -1;
		
		for (int g=0; g<MAX_EPOCHS; g++) {
			int genBestFitness = -1;
			ArrayList<Operator> newPopulation = new ArrayList<Operator>();
			
			while (newPopulation.size() < population.size()) {

				
				if (rng.nextFloat() < CROSSOVER_PROB) { // crossover
					ArrayList<Operator> offspring = crossover(tournamentSelect(population), tournamentSelect(population));
					for (Operator o : offspring) {
						if (rng.nextFloat() < MUTATION_PROB) o = mutate(o);
					}
					newPopulation.addAll(offspring);
				} else { // simple copy
					newPopulation.add(tournamentSelect(population));
				}
			}
			
			for (Operator o : newPopulation) {
				int f = computeFitness(o);
				if (f == 64) {
					System.out.println(o.mathematicaNotation());
					System.out.println(o.treeMaxHeight());
				}
				if (f > bestFitness) bestFitness = f;
				if (f > genBestFitness) genBestFitness = f;
			}
		
			population  = newPopulation;
			System.out.println(g + "> " + bestFitness);
		}
		
		
	}
	
	public static void main(String[] args) {
		Multiplexer mux = new Multiplexer();
		mux.evolve();
	}
	
}
