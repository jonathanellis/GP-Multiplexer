import java.util.ArrayList;
import java.util.Random;

public class Program {
	private static int INIT_TREE_DEPTH = 3;
	private static int MAX_TREE_DEPTH = 4;
	
	public Node tree;
	private int order;
	
	// Cache fitness and the fitnessCases it covers:
	private int fitness = 0;
	private ArrayList<Integer> fitnessCases = null;
	
	// Generate random program:
	public Program(int order) {
		this.order = order;
		this.tree = generateRandomTree(INIT_TREE_DEPTH); // tree depth
	}
	
	// Generate program based on tree:
	public Program(Node tree, int order) {
		this.tree = tree;
		this.order = order;
	}
	
	// Computes fitness over all cases:
	public int fitness() {
		System.out.println("/!\\ WARNING: Full fitness() called!");
		int fitness = 0;
		int fitnessCases = (int) Math.pow(2, order);
		for (int i=0; i<fitnessCases; i++) {
			Valuation v = new Valuation(i, order);
			boolean actualOutput = tree.evaluate(v);
			boolean correctOutput = v.correctOutput();
			
			if (actualOutput == correctOutput) fitness++;
		}
		
		this.fitness = fitness;
		this.fitnessCases = null;
		
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
	
	private Node generateRandomTree(int depth) {
		Random rng = new Random();
		int r = rng.nextInt(4);
		Node root;
		if (r == 0) root =  new AndNode(order);
		else if (r == 1) root = new OrNode(order);
		else if (r == 2) root = new NotNode(order);
		else root = new IfNode(order);
		
		root.grow(depth-1);
		return root;
	}
	
	public ArrayList<Program> crossover(Program spouse) {
		Node treeCopy;
		Node spouseCopy;
		
		do {
			// Copy this tree and spouse:
			treeCopy = tree.clone();
			spouseCopy = spouse.tree.clone();
			
			ArrayList<Node> mCandidates;
			ArrayList<Node> mNonTerminals = treeCopy.nonTerminalsToList();
			if (Circuit.rng.nextFloat() < 0.9 && mNonTerminals.size() > 0) mCandidates = mNonTerminals;
			else mCandidates = treeCopy.terminalsToList();
			
			Node mOp = Circuit.randomSelect(mCandidates);
			
			ArrayList<Node> fCandidates;
			ArrayList<Node> fNonTerminals = spouseCopy.nonTerminalsToList();
			if (Circuit.rng.nextFloat() < 0.9 && fNonTerminals.size() > 0) fCandidates = fNonTerminals;
			else fCandidates = spouseCopy.terminalsToList();
			
			Node fOp = Circuit.randomSelect(fCandidates);
						
			if (fOp == spouseCopy) spouseCopy = mOp.clone();
			else spouseCopy.swapSubtree(fOp, mOp.clone());
			
			if (mOp == treeCopy) treeCopy = fOp.clone();
			else treeCopy.swapSubtree(mOp, fOp.clone());
			
		} while (treeCopy.treeHeight() > MAX_TREE_DEPTH || spouseCopy.treeHeight() > MAX_TREE_DEPTH);
		
		ArrayList<Program> offspring = new ArrayList<Program>();
		offspring.add(new Program(treeCopy, order));
		offspring.add(new Program(spouseCopy, order));
		return offspring;
	}
	
	public Program mutate() {
		Node treeCopy;
		do {
			treeCopy = tree.clone();
			ArrayList<Node> candidates = treeCopy.nonTerminalsToList();
			candidates.addAll(treeCopy.terminalsToList());
			Node p = Circuit.randomSelect(candidates);
			int treeHeight = Circuit.rng.nextInt(INIT_TREE_DEPTH+1)+1;
			Node newTree = generateRandomTree(treeHeight);
			treeCopy.swapSubtree(p, newTree);
		} while (treeCopy.treeHeight() > MAX_TREE_DEPTH);
		return new Program(treeCopy, order);
	}
	
}
