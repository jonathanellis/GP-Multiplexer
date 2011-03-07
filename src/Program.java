import java.util.ArrayList;
import java.util.Random;

public class Program {
	private static int MAX_TREE_DEPTH = 10;
	
	public Operator tree;
	private int order;
	
	// Cache fitness and the fitnessCases it covers:
	private int fitness = 0;
	private ArrayList<Integer> fitnessCases;
	
	// Generate random program:
	public Program(int order) {
		this.order = order;
		this.tree = generateRandomTree(6); // tree depth
		
	}
	
	// Generate program based on tree:
	public Program(Operator tree, int order) {
		this.tree = tree;
		this.order = order;
	}
	
	// Computes fitness over all cases:
	public int fitness() {
		int fitness = 0;
		int fitnessCases = (int) Math.pow(2, order);
		for (int i=0; i<fitnessCases; i++) {
			Valuation v = new Valuation(i, order);
			boolean actualOutput = tree.evaluate(v);
			boolean correctOutput = v.correctOutput();
			
			if (actualOutput == correctOutput) fitness++;
		}
		return fitness;
	}
	
	// Computes fitness over the specified fitness cases:
	public int fitness(ArrayList<Integer> fitnessCases) {
		if (this.fitnessCases == fitnessCases) return fitness; // cache hit
		
		int fitness = 0;
		for (int c : fitnessCases) {
			Valuation v = new Valuation(c, order);
			boolean actualOutput = tree.evaluate(v);
			boolean correctOutput = v.correctOutput();
			
			if (actualOutput == correctOutput) fitness++;
		}
		// cache these values:
		this.fitness = fitness;
		this.fitnessCases = fitnessCases;
		
		return fitness;
	}
	
	private Operator generateRandomTree(int depth) {
		Operator root;
		Random rng = new Random();
		
		int r = rng.nextInt(4);
		if (r == 0) root =  new AndOp(order);
		else if (r == 1) root = new OrOp(order);
		else if (r == 2) root = new NotOp(order);
		else root = new IfOp(order);
		
		root.grow(depth-1);
			
		return root;
	}
	
	public ArrayList<Program> crossover(Program spouse) {
		Operator treeCopy;
		Operator spouseCopy;
		
		do {
			treeCopy = tree.clone();
			spouseCopy = spouse.tree.clone();
			
			ArrayList<Operator> mCandidates;
			ArrayList<Operator> mNonTerminals = treeCopy.nonTerminalsToList();
			if (Multiplexer.rng.nextFloat() < 0.9 && mNonTerminals.size() > 0) mCandidates = mNonTerminals;
			else mCandidates = treeCopy.terminalsToList();
			
			Operator mOp = Multiplexer.randomSelect(mCandidates);
			
			ArrayList<Operator> fCandidates;
			ArrayList<Operator> fNonTerminals = spouseCopy.nonTerminalsToList();
			if (Multiplexer.rng.nextFloat() < 0.9 && fNonTerminals.size() > 0) fCandidates = fNonTerminals;
			else fCandidates = spouseCopy.terminalsToList();
			
			Operator fOp = Multiplexer.randomSelect(fCandidates);
			
			if (fOp == spouseCopy) spouseCopy = mOp.clone();
			else spouseCopy.swapSubtree(fOp, mOp.clone());
			
			if (mOp == treeCopy) treeCopy = fOp.clone();
			else treeCopy.swapSubtree(mOp, fOp.clone());
		} while (treeCopy.treeMaxHeight() > MAX_TREE_DEPTH || spouseCopy.treeMaxHeight() > MAX_TREE_DEPTH);
		
		ArrayList<Program> offspring = new ArrayList<Program>();
		offspring.add(new Program(treeCopy, order));
		offspring.add(new Program(spouseCopy, order));
		return offspring;
	}
	
	public Program mutate() {
		Operator treeCopy;
		do {
			treeCopy = tree.clone();
			ArrayList<Operator> candidates = treeCopy.nonTerminalsToList();
			candidates.addAll(treeCopy.terminalsToList());
			Operator p = Multiplexer.randomSelect(candidates);
			int treeHeight = Multiplexer.rng.nextInt(4)+1;
			Operator newTree = generateRandomTree(treeHeight);
			treeCopy.swapSubtree(p, newTree);
		} while (treeCopy.treeMaxHeight() > MAX_TREE_DEPTH);
		return new Program(treeCopy, order);
	}
	
}
