import java.util.ArrayList;


public class NotNode extends Node {
	private Node x;
	
	public NotNode(int multiplexerOrder) {
		super(multiplexerOrder);
	}
	
	public NotNode(int multiplexerOrder, Node x) {
		super(multiplexerOrder);
		this.x = x;
	}
	
	public boolean evaluate(Valuation v) {
		return !x.evaluate(v);
	}
	
	public void grow(int maxDepth) {
		x = generateRandomChild(maxDepth);
		if (maxDepth > 0) x.grow(maxDepth-1);
	}
	
	
	public ArrayList<Node> nonTerminalsToList() {
		ArrayList<Node> xChildren = x.nonTerminalsToList();
		xChildren.add(this);
		return xChildren;
	}
	
	public ArrayList<Node> terminalsToList() {
		return x.terminalsToList();
	}
	
	public void swapSubtree(Node o, Node n) {
		if (x == o) x = n;
		else x.swapSubtree(o, n);
	}
	public Node clone() {
		NotNode o = new NotNode(order, x.clone());
		return o;
		
	}
	
	public int treeHeight() {
		return 1+x.treeHeight();
	}
	
	public int treeHeight(Node o) {
		if (this == o) return 1;
		int xHeight = x.treeHeight(o);
		if (xHeight > 0) return 1+xHeight;
		return 0;
	}
	
	public String toString() {
		return "!" + x;
	}
	
	public String mathematicaNotation() {
		return "Not[" + x.mathematicaNotation() + "]";
	}
}