import java.util.ArrayList;


public class OrNode extends Node {
	private Node x;
	private Node y;
	
	public OrNode(int order) {
		super(order);
	}
	
	public OrNode(int order, Node x, Node y) {
		super(order);
		this.x = x;
		this.y = y;
	}
	
	// Could change this to lazy or (time-saving):
	public boolean evaluate(Valuation v) {
		return x.evaluate(v) || y.evaluate(v);
	}
	
	public void grow(int maxDepth) {
		x = generateRandomChild(maxDepth);
		y = generateRandomChild(maxDepth);
		if (maxDepth > 0) {
			x.grow(maxDepth-1);
			y.grow(maxDepth-1);
		}
	}
	
	public int treeHeight() {
		int xHeight = x.treeHeight();
		int yHeight = y.treeHeight();
		if (xHeight > yHeight) return 1+xHeight;
		return 1+yHeight;
	}
	
	public int treeHeight(Node o) {
		if (this == o) return 1;
		int xHeight = x.treeHeight(o);
		if (xHeight > 0) return 1+xHeight;
		int yHeight = y.treeHeight(o);
		if (yHeight > 0) return 1+yHeight;
		return 0;
	}
	
	
	public ArrayList<Node> nonTerminalsToList() {
		ArrayList<Node> xChildren = x.nonTerminalsToList();
		ArrayList<Node> yChildren = y.nonTerminalsToList();
		
		xChildren.addAll(yChildren);
		xChildren.add(this);
		return xChildren;
	}
	
	public ArrayList<Node> terminalsToList() {
		ArrayList<Node> xTerminals = x.terminalsToList();
		ArrayList<Node> yTerminals = y.terminalsToList();
		xTerminals.addAll(yTerminals);
		return xTerminals;
	}
	
	
	public void swapSubtree(Node o, Node n) {
		if (x == o) x = n;
		else if (y == o) y = n;
		else {
			x.swapSubtree(o, n);
			y.swapSubtree(o, n);
		}
	}
	
	public Node clone() {
		OrNode o = new OrNode(order, x.clone(), y.clone());
		return o;
	}
	
	public String toString() {
		return "(" + x + " || " + y + ")";
	}
	
	protected String uniqueName() {
		return "OR (" + this.hashCode() + ")";
	}
	
	public String mathematicaNotation() {
		return "Or[" + x.mathematicaNotation() + " , " + y.mathematicaNotation() + "]";
	}
}