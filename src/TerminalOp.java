import java.util.ArrayList;


public class TerminalOp extends Operator {
        public int midTerm = -1;
	public enum terminal {a0, a1, d0, d1, d2, d3};
	private terminal t;
	
	public TerminalOp (int t) {
	        midTerm = t;
	}
	
	public TerminalOp (terminal t) {
		this.t = t;
	}
	
	public void prune(int h) {}
	// evaluates the given terminal under a specific valuation
	public boolean evaluate(Valuation v) {
	        if(midTerm > -1) return ((MidTreeValuation)v).testCase[midTerm] == 1;
	        
		if (t == terminal.a0) return v.a0;
		if (t == terminal.a1) return v.a1;
		if (t == terminal.d0) return v.d0;
		if (t == terminal.d1) return v.d1;
		if (t == terminal.d2) return v.d2;
		if (t == terminal.d3) return v.d3;
		return false; // this should never happen
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
	
	public void full(int maxDepth) throws Exception {
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
		return (t + "");
	}
}
