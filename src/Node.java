import java.util.ArrayList;
import java.util.Random;

public abstract class Node implements Cloneable {
	protected Random rng = new Random();
	protected int order; // Number of terminals, needed to generate new random terminals where required
	public abstract boolean evaluate(Valuation v); // Evaluates the tree under some valuation v
	public abstract void grow(int maxDepth); // Uses the grow method to generate initial trees to a supplied max depth [Koza]
	public abstract ArrayList<Node> nonTerminalsToList(); // Returns the tree's non-terminals as a list
	public abstract ArrayList<Node> terminalsToList(); // Returns the tree's terminals as a list
	public abstract void swapSubtree(Node o, Node n); // Swaps the first subtree with the second tree (or nothing if o doesn't exist).
	public abstract Node clone(); // Deep-copies the tre
	public abstract int treeHeight(); // Returns the height of the longest branch of the tree
	public abstract int treeHeight(Node o); // Returns the height at which the specified subtree begins
	public abstract String mathematicaNotation(); // Returns the tree in notation that can be used by Mathematica to draw the tre
	
	
	protected Node(int order) {
		this.order = order;
	}
	
	protected Node generateRandomChild(int maxDepth) {
		if (maxDepth == 0) return new TerminalNode(order);
		
		boolean r = rng.nextBoolean(); // 50% probability of terminal vs nonterminal
		if (r) return new TerminalNode(order);
		return generateRandomNonterminal();
	}
	
	private Node generateRandomNonterminal() {
		Random rng = new Random();
		int r = rng.nextInt(4);
		switch (r) {
			case 0: return new AndNode(order);
			case 1: return new OrNode(order);
			case 2: return new NotNode(order);
			case 3: return new IfNode(order);
		}
		return null;
	}
	
}
