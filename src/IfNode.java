import java.util.ArrayList;


public class IfNode extends Node {
	private Node x;
	private Node y;
	private Node z;
	
	public IfNode(int multiplexerOrder) {
		super(multiplexerOrder);
	}
	
	public IfNode(int multiplexerOrder, Node x, Node y, Node z) {
		super(multiplexerOrder);
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public boolean evaluate(Valuation v) {
		if (x.evaluate(v)) return y.evaluate(v);
		return z.evaluate(v);
	}
	
	public void grow(int maxDepth) {
		x = generateRandomChild(maxDepth);
		y = generateRandomChild(maxDepth);	
		z = generateRandomChild(maxDepth);	
		if (maxDepth > 0) {
			x.grow(maxDepth-1);
			y.grow(maxDepth-1);
			z.grow(maxDepth-1);
		}
	}
	
	public ArrayList<Node> nonTerminalsToList() {
		ArrayList<Node> xChildren = x.nonTerminalsToList();
		ArrayList<Node> yChildren = y.nonTerminalsToList();
		ArrayList<Node> zChildren = z.nonTerminalsToList();
		
		xChildren.addAll(yChildren);
		xChildren.addAll(zChildren);
		xChildren.add(this);
		return xChildren;
	}
	
	public ArrayList<Node> terminalsToList() {
		ArrayList<Node> xTerminals = x.terminalsToList();
		ArrayList<Node> yTerminals = y.terminalsToList();
		ArrayList<Node> zTerminals = z.terminalsToList();
		xTerminals.addAll(yTerminals);
		xTerminals.addAll(zTerminals);
		return xTerminals;
	}
	
	public void swapSubtree(Node o, Node n) {
		if (x == o) x = n;
		else if (y == o) y = n;
		else if (z == o) z = n;
		else {
			x.swapSubtree(o, n);
			y.swapSubtree(o, n);
			z.swapSubtree(o, n);
		}
	}
	
	
	public Node clone() {
		IfNode o = new IfNode(order, x.clone(), y.clone(), z.clone());
		return o;
		
	}
	
	public int treeHeight() {
		int xHeight = x.treeHeight();
		int yHeight = y.treeHeight();
		int zHeight = z.treeHeight();
		if ((xHeight > yHeight) && (xHeight > zHeight)) return 1+xHeight;
		if (yHeight > zHeight) return 1+yHeight;
		return 1+zHeight;
	}
	
	public int treeHeight(Node o) {
		if (this == o) return 1;
		int xHeight = x.treeHeight(o);
		if (xHeight > 0) return 1+xHeight;
		int yHeight = y.treeHeight(o);
		if (yHeight > 0) return 1+yHeight;
		int zHeight = z.treeHeight(o);
		if (zHeight > 0) return 1+zHeight;
		return 0;
	}
	
	
	public String toString() {
		return "(" + x + " ? " + y + " : " + z + ")";
	}
	
	public String mathematicaNotation() {
		return "If[" + x.mathematicaNotation() + ", " + y.mathematicaNotation() + ", " + z.mathematicaNotation() + "]";
	}
	
}