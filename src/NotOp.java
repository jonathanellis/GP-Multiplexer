import java.util.ArrayList;


public class NotOp extends Operator {
	private Operator x;
	
	public NotOp() {
		// default constructor
	}
	
	public NotOp(Operator x) {
		this.x = x;
	}
	
	public boolean evaluate(SixValuation v) {
		return !x.evaluate(v);
	}
	
	public void grow(int maxDepth) {
		x = generateRandomChild(maxDepth);
		if (maxDepth > 0) x.grow(maxDepth-1);
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
	
	public String mathematicaNotation() {
		return "Not[" + x.mathematicaNotation() + "]";
	}
}