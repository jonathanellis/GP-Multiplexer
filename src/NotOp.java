import java.util.ArrayList;


public class NotOp extends Operator {
	private Operator x;
	
	public NotOp() {
		// default constructor
	}
	
	public NotOp(Operator x) {
		this.x = x;
	}
	
	public boolean evaluate(Valuation v) {
		return !x.evaluate(v);
	}
	public void prune(int h) {
	        if(h == 2) this.x = generateRandomTerminal();
	        else x.prune(h-1);
	}
	public void grow(int maxDepth) {
		x = generateRandomChild(maxDepth);
		if (maxDepth > 0) x.grow(maxDepth-1);
	}
	
	public void full(int maxDepth) throws Exception {
		if (maxDepth == 0) {
		x = generateRandomTerminal();
		}
		else if (maxDepth > 0) {
			x = generateRandomFunction();
			x.full(maxDepth-1);
		}
		else if (maxDepth < 0) {
			throw new Exception("maxDepth can't be lower then 0");
		}
	}
	
	public ArrayList<Operator> nonTerminalsToList() {
		ArrayList<Operator> xChildren = x.nonTerminalsToList();
		xChildren.add(this);
		return xChildren;
	}
	
	public ArrayList<Operator> terminalsToList() {
		return x.terminalsToList();
	}
	
	public void swapSubtree(Operator o, Operator n) {
		if (x == o) x = n;
		else x.swapSubtree(o, n);
	}
	public Operator clone() {
		NotOp o = new NotOp(x.clone());
		return o;
		
	}
	
	public int treeMaxHeight() {
		return 1+x.treeMaxHeight();
	}
	
	public String toString() {
		return "!" + x;
	}
}
