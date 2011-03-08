import java.util.ArrayList;
import java.util.Random;

public class Program {

	private Circuit circuit;
	public Node tree;

	// Cache fitness and the fitnessCases it covers:
	private int fitness = 0;
	private ArrayList<Integer> fitnessCases = null;
	
	// Generate random program:
	public Program(Circuit c) {
		this.circuit = c;
		this.tree = generateRandomTree(circuit.initTreeDepth); // tree depth
	}
	
	// Generate program based on tree:
	public Program(Circuit c, Node tree) {
		this.circuit = c;
		this.tree = tree;
	}
	
	// Computes fitness over all cases:
	public int fitness() {
		//System.out.println("/!\\ WARNING: Full fitness() called!");
		int fitness = 0;
		int fitnessCases = (int) Math.pow(2, circuit.order);
		for (int i=0; i<fitnessCases; i++) {
			Valuation v = new Valuation(i, circuit.order);
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
			Valuation v = new Valuation(c, circuit.order);
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
		// Generate random root...
		Node root;
		if (r == 0) root =  new AndNode(circuit.order);
		else if (r == 1) root = new OrNode(circuit.order);
		else if (r == 2) root = new NotNode(circuit.order);
		else root = new IfNode(circuit.order);
		
		root.grow(depth-1); // ...then grow
		return root;
	}
	
	public ArrayList<Program> crossover(Program spouse) {
		Node treeCopy;
		Node spouseCopy;
		
		do {
			// Copy this tree and spouse (in case we need to retry):
			treeCopy = tree.clone();
			spouseCopy = spouse.tree.clone();
			
			ArrayList<Node> mCandidates; // mother candidate crossover points
			ArrayList<Node> mNonTerminals = treeCopy.nonTerminalsToList();
			if (Circuit.rng.nextFloat() < 0.9 && mNonTerminals.size() > 0) mCandidates = mNonTerminals;
			else mCandidates = treeCopy.terminalsToList();
			
			Node mOp = Circuit.randomSelect(mCandidates); // select a random point
			
			ArrayList<Node> fCandidates; // father candidate crossover points
			ArrayList<Node> fNonTerminals = spouseCopy.nonTerminalsToList();
			if (Circuit.rng.nextFloat() < 0.9 && fNonTerminals.size() > 0) fCandidates = fNonTerminals;
			else fCandidates = spouseCopy.terminalsToList();
			
			Node fOp = Circuit.randomSelect(fCandidates); // select a random point
						
			if (fOp == spouseCopy) spouseCopy = mOp.clone(); // just swap roots
			else spouseCopy.swapSubtree(fOp, mOp.clone()); // need to splice subtree
			
			if (mOp == treeCopy) treeCopy = fOp.clone(); // just swap roots
			else treeCopy.swapSubtree(mOp, fOp.clone()); // need to splice subtree
			
		} while (treeCopy.treeHeight() > circuit.maxTreeDepth || spouseCopy.treeHeight() > circuit.maxTreeDepth); // do check, if fails then do it all again
		
		// array to hold return values:
		ArrayList<Program> offspring = new ArrayList<Program>();
		offspring.add(new Program(circuit, treeCopy));
		offspring.add(new Program(circuit, treeCopy));
		return offspring;
	}
	
	public Program mutate() {
		Node treeCopy;
		do {
			treeCopy = tree.clone(); // copy in case we need to redo
			ArrayList<Node> candidates = treeCopy.nonTerminalsToList();
			candidates.addAll(treeCopy.terminalsToList()); // candidates[]  = nonTerminals + terminals
			Node p = Circuit.randomSelect(candidates); // select random mutation point
			int treeHeight = Circuit.rng.nextInt(circuit.initTreeDepth+1)+1;
			Node newTree = generateRandomTree(treeHeight); // generate tree using grow() to a random height between 1 and initTreeDepth
			treeCopy.swapSubtree(p, newTree); // splice in new subtree
		} while (treeCopy.treeHeight() > circuit.maxTreeDepth); // do check, if fails then redo
		return new Program(circuit, treeCopy); // return this as a new program (flushes cached fitness)
	}
	
}
