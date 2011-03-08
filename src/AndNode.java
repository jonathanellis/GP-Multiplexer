import java.util.ArrayList;

public class AndNode extends Node {
	private Node x;
	private Node y;
	
	public AndNode(int multiplexerOrder) {
		super(multiplexerOrder);
	}
	
	public AndNode(int multiplexerOrder, Node x, Node y) {
		super(multiplexerOrder);
		this.x = x;
		this.y = y;
	}
	
	public boolean evaluate(Valuation v) {
		return x.evaluate(v) && y.evaluate(v);
	}

	public void grow(int maxDepth) {
		x = generateRandomChild(maxDepth);
		y = generateRandomChild(maxDepth);
		if (maxDepth > 0) {
			x.grow(maxDepth-1);
			y.grow(maxDepth-1);
		}
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
		AndNode o = new AndNode(order, x.clone(), y.clone());
		return o;
		
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
	
	public String toString() {
		return "(" + x + " && " + y + ")";
	}
	
	
	public String mathematicaNotation() {
		return "And[" + x.mathematicaNotation() + " , " + y.mathematicaNotation() + "]";
	}


}