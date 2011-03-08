import java.util.ArrayList;


public class TerminalNode extends Node {
	private String t;
	
	// Generates a random terminal, based on the specified order:
	public TerminalNode(int multiplexerOrder) {
		super(multiplexerOrder);
		
		int r = rng.nextInt(multiplexerOrder);
		if (multiplexerOrder == 6) {
			String opts[] = {"a0", "a1", "d0", "d1", "d2", "d3"};
			t = opts[r];
		}
		else if (multiplexerOrder == 11) {
			String opts[] = {"a0", "a1", "a2", "d0", "d1", "d2", "d3", "d4", "d5", "d6", "d7"};
			t = opts[r];
		}
		else if (multiplexerOrder == 16) {
			String opts[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
			t = opts[r];
		}
		else {
			System.out.println("UNSUPPORTED RANGE!");
		}
	}
	
	public TerminalNode(int multiplexerOrder, String t) {
		super(multiplexerOrder);
		this.t = t;
	}
	
	// evaluates the given terminal under a specific valuation
	public boolean evaluate(Valuation v) {
		return v.valueForTerminal(t);
	}
	
	public ArrayList<Node> nonTerminalsToList() {
		return new ArrayList<Node>();
	}
	
	public ArrayList<Node> terminalsToList() {
		ArrayList<Node> list = new ArrayList<Node>();
		list.add(this);
		return list;
	}
	
	public void grow(int maxDepth) {
		// do nothing
	}
	
	public void swapSubtree(Node o, Node n) {
		// do nothing
	}
	// never used in practice:
	public Node clone() {
		return new TerminalNode(order, this.t);
	}
	
	public int treeHeight() {
		return 1;
	}
	
	public int treeHeight(Node o) {
		if (this == o) return 1;
		return 0;
	}
	
	public String toString() {
		return t + "";
	}
	
	public String mathematicaNotation() {
		return t + "";
	}
}
