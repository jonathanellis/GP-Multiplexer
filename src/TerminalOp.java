import java.util.ArrayList;


public class TerminalOp extends Operator {
	private String t;
	
	
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
