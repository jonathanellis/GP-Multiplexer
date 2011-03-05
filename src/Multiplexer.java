import java.util.ArrayList;
import java.util.Random;

public class Multiplexer {
	
	private static int INIT_TREE_DEPTH = 15;
	private static int MAX_TREE_DEPTH = 15;
	private static int POP_SIZE = 500;
	private static int MAX_EPOCHS = 100000;
	public static int TOTAL_LINES = 16;
	
	// MID_LOWER < sum < MIDUPPER
	private static int MID_LOWER = 6;
	private static int MID_UPPER = 10;
	private static int[][] testCases;
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
	
        public ArrayList<Operator> tournamentSelect(ArrayList<Operator> population) {
		ArrayList<Operator> bestTwo = new ArrayList<Operator>();
		
		Operator a = population.get(rng.nextInt(population.size()));
		Operator b = population.get(rng.nextInt(population.size()));
		Operator c = population.get(rng.nextInt(population.size()));
		Operator d = population.get(rng.nextInt(population.size()));
		
		if(a.getFitness(this) > b.getFitness(this)) bestTwo.add(a);
		else bestTwo.add(b);
		
		if(a.getFitness(this) > b.getFitness(this)) bestTwo.add(c);
		else bestTwo.add(d);
		
		return bestTwo;
	}
	
	public ArrayList<Operator> crossover(Operator mother, Operator father) {

//		System.out.println("INmother: " + mother);
//		System.out.println("INfather: " + father);
//		System.out.println("***");
                
		Operator motherCopy;
		Operator fatherCopy;
		
		//do {
			motherCopy = mother.clone();
			fatherCopy = father.clone();
			
//			System.out.println(motherCopy.nonTerminalsToList().size());
//			System.out.println(motherCopy.terminalsToList().size());

			
			ArrayList<Operator> mCandidates;
			if (rng.nextFloat() < 0.9) mCandidates = motherCopy.nonTerminalsToList();
			else mCandidates = motherCopy.terminalsToList();
			if(mCandidates.size() == 0) mCandidates = motherCopy.terminalsToList();
			
			Operator mOp = randomSelect(mCandidates);
			
			ArrayList<Operator> fCandidates;
			if (rng.nextFloat() < 0.9) fCandidates = fatherCopy.nonTerminalsToList();
			else fCandidates = fatherCopy.terminalsToList();
			if(fCandidates.size() == 0) fCandidates = fatherCopy.terminalsToList();
			
			Operator fOp = randomSelect(fCandidates);
			
//			System.out.println("MOP: " + mOp);
//			System.out.println("FOP: " + fOp);
//			System.out.println("***");
			
			if (fOp == fatherCopy) fatherCopy = mOp.clone();
			else fatherCopy.swapSubtree(fOp, mOp.clone());
			
			if (mOp == motherCopy) motherCopy = fOp.clone();
			else motherCopy.swapSubtree(mOp, fOp.clone());
			
			fatherCopy.prune(MAX_TREE_DEPTH);
			motherCopy.prune(MAX_TREE_DEPTH);
			
			//generate fitness values, cache them in the trees
			fatherCopy.getFitness(this);
			motherCopy.getFitness(this);
		//} while (motherCopy.treeMaxHeight() > MAX_TREE_DEPTH && fatherCopy.treeMaxHeight() > MAX_TREE_DEPTH);

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
	public void generateTestInputs() {
	        int testSpace = (int)Math.pow(2,TOTAL_LINES);
	        testCases = new int[testSpace][TOTAL_LINES];
	
	        //counts in binary and adds to array
	        for(int i = 0; i < testSpace; i++)
                        for(int j = 0; j < TOTAL_LINES; j++) 
                                testCases[i][j] = (i & (int)Math.pow(2,(TOTAL_LINES - j - 1))) > 0 ? 1 : 0;
	        
	        //uncomment this to see binary
	        /*for(int i = 0; i < testSpace; i++) {
                       for(int j = 0; j < TOTAL_LINES; j++) System.out.print(testCases[i][j]+" ");
                       System.out.println("");
                }*/
	}
	
	public double computeFitness(Operator tree) {
		int testSpace = (int)Math.pow(2,TOTAL_LINES);
		int fitness = 0;
		
		//iterate accross test space
		for(int i=0; i<testSpace; i++) {
		        int sum = 0;
		        for(int j=0; j<TOTAL_LINES; j++) sum += testCases[i][j];
		        
		        boolean testResult = sum > MID_LOWER && sum < MID_UPPER;
		        boolean actualResult = tree.evaluate(new MidTreeValuation(testCases[i]));
		        
		        if(testResult == actualResult) fitness++;
	        }					
		return 1. * fitness / testSpace;
	}
	
	private ArrayList<Operator> generatePopulation(int size) throws Exception { 
		// Ramped half-and-half method for a diverse set of population
		ArrayList<Operator> population = new ArrayList<Operator>();
		//System.out.println(size);
		int depthPop = size / (INIT_TREE_DEPTH-1);
		
		System.out.print("generating initial population");
		//System.out.println("depthPop: " + depthPop);
		for (int i=2; i<=INIT_TREE_DEPTH; i++) {
			//System.out.println("i: " + i);
			for (int j=0; j<depthPop; j++) {
			        Operator candidate;
				
				//System.out.println("j: " + j);
				if (j >= (depthPop/2)) candidate = generateRandomTree(i);
				else candidate = generateFullTree(i);
				
				boolean duplicate = false;
				for(int k = 0; k < population.size() && !duplicate; k++)
				        duplicate = population.get(k).compare(candidate);
				
				if(!duplicate) {
				        population.add(candidate);
				        System.out.print(".");
				}
				else j--;
			}
		}
		System.out.println("done");
		//System.exit(0);
		return population;
	}
	
	public void evolve() throws Exception {
        	double bestFitness = -1;
	
		// generate initial population
		ArrayList<Operator> population = generatePopulation(POP_SIZE);
		
		generateTestInputs();
		
		for (int g=0; g<MAX_EPOCHS && bestFitness < 1.0; g++) {
			double genBestFitness = -1;
			double fSum = 0;
			ArrayList<Operator> newPopulation = new ArrayList<Operator>();
			
			while (newPopulation.size() < population.size()) {
				ArrayList<Operator> bestTwo = tournamentSelect(population);
				ArrayList<Operator> offspring = crossover(bestTwo.get(0), bestTwo.get(1));
				for (Operator o : offspring) {
					float r = rng.nextFloat();
					if (r < 0.1) o = mutate(o);
				}
				newPopulation.addAll(offspring);
				System.out.print(".");
			}
			
			
			for (Operator o : newPopulation) {
				double f = o.getFitness(this);
				fSum += f;
				
				//System.out.println(o);
				if (f > bestFitness) bestFitness = f;
				if (f > genBestFitness) genBestFitness = f;
			}
			
			System.out.println(g + "> " + genBestFitness + " : " + bestFitness);
			
			population = newPopulation;
			
			//normalise fitnesses
			for(Operator o : population) o.normaliseFitness(this,fSum);
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
