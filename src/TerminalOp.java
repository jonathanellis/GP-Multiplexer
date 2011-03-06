import java.util.ArrayList;


public class TerminalOp extends Operator {
	private String t;
	
	// Generates a random terminal, based on the specified range:
	public TerminalOp(int range) {
		int r = rng.nextInt(range);
		if (range == 6) {
			String opts[] = {"a0", "a1", "d0", "d1", "d2", "d3"};
			t = opts[r];
		}
		else if (range == 11) {
			String opts[] = {"a0", "a1", "a2", "d0", "d1", "d2", "d3", "d4", "d5", "d6", "d7"};
			t = opts[r];
		}
		else {
			System.out.println("UNSUPPORTED RANGE!");
		}
	}
	
	public TerminalOp(String t) {
		this.t = t;
	}
	
	// evaluates the given terminal under a specific valuation
	public boolean evaluate(Valuation v) {
		return v.valueForTerminal(t);
	}
	
	public ArrayList<Operator> nonTerminalsToList() {
		return new ArrayList<Operator>();
	}
	
	public ArrayList<Operator> terminalsToList() {
		ArrayList<Operator> list = new ArrayList<Operator>();
		list.add(this);
		return list;
	}
	
	public void grow(int maxDepth) {
		// do nothing
	}
	
	public void swapSubtree(Operator o, Operator n) {
		// do nothing
	}
	// never used in practice:
	public Operator clone() {
		return new TerminalOp(this.t);
	}
	
	public int treeMaxHeight() {
		return 1;
	}
	
	public String toString() {
		return t + "";
	}
	
	public String mathematicaNotation() {
		return t + "";
	}
}
