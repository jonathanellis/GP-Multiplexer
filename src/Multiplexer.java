import java.util.ArrayList;
import java.util.Random;

public class Multiplexer {
	
	private static int INIT_TREE_DEPTH = 3;
	private static int MAX_TREE_DEPTH = 3;
	private static int POP_SIZE = 500;
	private static int MAX_EPOCHS = 100000;
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
	
	private Operator generateFullTree(int depth) throws Exception {
		Operator root;
		Random rng = new Random();
		
		int r = rng.nextInt(4);
		if (r == 0) root =  new AndOp();
		else if (r == 1) root = new OrOp();
		else if (r == 2) root = new NotOp();
		else root = new IfOp();
		
		root.full(depth-1);
		//System.out.println(root);
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
	
	public Operator bestOperator(ArrayList<Operator> sample) {
		Operator best = null;
		int bestFitness = -1;
		for (Operator o : sample) {
			int f = computeFitness(o);
			if (f > bestFitness) {
				best = o;
				bestFitness = f;
			}
		}
		return best;
	}
	
	public ArrayList<Operator> tournamentSelect(ArrayList<Operator> population) {
		ArrayList<Operator> bestTwo = new ArrayList<Operator>();
		ArrayList<Operator> sample;
		sample = randomSample(population, 50);
		bestTwo.add(bestOperator(sample));
		sample = randomSample(population, 50);
		bestTwo.add(bestOperator(sample));
		return bestTwo;
	}
	
	public ArrayList<Operator> crossover(Operator mother, Operator father) {

//		System.out.println("INmother: " + mother);
//		System.out.println("INfather: " + father);
//		System.out.println("***");

		Operator motherCopy;
		Operator fatherCopy;
		
		do {
			motherCopy = mother.clone();
			fatherCopy = father.clone();
			
//			System.out.println(motherCopy.nonTerminalsToList().size());
//			System.out.println(motherCopy.terminalsToList().size());

			
			ArrayList<Operator> mCandidates;
			if (rng.nextFloat() < 0.9) mCandidates = motherCopy.nonTerminalsToList();
			else mCandidates = motherCopy.terminalsToList();
			
			Operator mOp = randomSelect(mCandidates);
			
			ArrayList<Operator> fCandidates;
			if (rng.nextFloat() < 0.9) fCandidates = fatherCopy.nonTerminalsToList();
			else fCandidates = fatherCopy.terminalsToList();
			Operator fOp = randomSelect(fCandidates);
			
//			System.out.println("MOP: " + mOp);
//			System.out.println("FOP: " + fOp);
//			System.out.println("***");
			
			if (fOp == fatherCopy) fatherCopy = mOp.clone();
			else fatherCopy.swapSubtree(fOp, mOp.clone());
			
			if (mOp == motherCopy) motherCopy = fOp.clone();
			else motherCopy.swapSubtree(mOp, fOp.clone());
		} while (motherCopy.treeMaxHeight() > MAX_TREE_DEPTH && fatherCopy.treeMaxHeight() > MAX_TREE_DEPTH);

		ArrayList<Operator> offspring = new ArrayList<Operator>();
		
//		System.out.println("OUTmother: " + motherCopy);
//		System.out.println("OUTfather: " + fatherCopy);
//		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		offspring.add(motherCopy);
		offspring.add(fatherCopy);
			
	
		//System.exit(0);
		return offspring;
	}
	
	public Operator mutate(Operator o) {
		ArrayList<Operator> candidates = o.nonTerminalsToList();
		candidates.addAll(o.terminalsToList());
		Operator p = randomSelect(candidates);
		int treeHeight = rng.nextInt(4) + 2;
		Operator newTree = generateRandomTree(treeHeight);
		o.swapSubtree(p, newTree);
		
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
	
	private ArrayList<Operator> generatePopulation(int size) throws Exception { 
		// Ramped half-and-half method for a diverse set of population
		ArrayList<Operator> population = new ArrayList<Operator>();
		//System.out.println(size);
		int depthPop = size / (INIT_TREE_DEPTH-1);
		//System.out.println("depthPop: " + depthPop);
		for (int i=2; i<=INIT_TREE_DEPTH; i++) {
			//System.out.println("i: " + i);
			for (int j=0; j<depthPop; j++) {
				//System.out.println("j: " + j);
				if (j >= (depthPop/2)) population.add(generateRandomTree(i));
				else population.add(generateFullTree(i));
			}
		}
		//System.exit(0);
		return population;
	}
	
	public void evolve() throws Exception {
		// generate initial population
		ArrayList<Operator> population = generatePopulation(POP_SIZE);
		int bestFitness = -1;
		
		for (int g=0; g<MAX_EPOCHS; g++) {
			int genBestFitness = -1;
			ArrayList<Operator> newPopulation = new ArrayList<Operator>();
			
			while (newPopulation.size() < population.size()) {
				ArrayList<Operator> bestTwo = tournamentSelect(population);
				ArrayList<Operator> offspring = crossover(bestTwo.get(0), bestTwo.get(1));
				for (Operator o : offspring) {
					float r = rng.nextFloat();
					if (r < 0.1) o = mutate(o);
				}
				newPopulation.addAll(offspring);
			}
			
			for (Operator o : newPopulation) {
				int f = computeFitness(o);
				//System.out.println(o);
				if (f > bestFitness) bestFitness = f;
				if (f > genBestFitness) genBestFitness = f;
			}
		
			
			System.out.println(g + "> " + bestFitness);
		}
	}
	
	public static void main(String[] args) {
		Multiplexer mux = new Multiplexer();
		try {
			mux.evolve();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Operator correctSolution = mux.correctSolution();
		//int f = mux.computeFitness(correctSolution);
		//System.out.println(correctSolution.treeMaxHeight());
	}
	
}
