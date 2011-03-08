import java.util.ArrayList;
import java.util.Random;

public abstract class Node implements Cloneable {
	protected Random rng = new Random();
	protected int order;
	public abstract boolean evaluate(Valuation v);
	public abstract void grow(int maxDepth);
	public abstract ArrayList<Node> nonTerminalsToList();
	public abstract ArrayList<Node> terminalsToList();
	public abstract void swapSubtree(Node o, Node n);
	public abstract Node clone();
	public abstract int treeHeight();
	public abstract int treeHeight(Node o);
	public abstract String mathematicaNotation();
	
	
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
