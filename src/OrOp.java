import java.util.ArrayList;


public class OrOp extends Operator {
	private Operator x;
	private Operator y;
	
	public OrOp() {
		// default constructor
	}
	
	public OrOp(Operator x, Operator y) {
		this.x = x;
		this.y = y;
	}
	
	// Could change this to lazy or (time-saving):
	public boolean evaluate(SixValuation v) {
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
	
	public int treeMaxHeight() {
		int xHeight = x.treeMaxHeight();
		int yHeight = y.treeMaxHeight();
		if (xHeight > yHeight) return 1+xHeight;
		return 1+yHeight;
	}
	
	public ArrayList<Operator> nonTerminalsToList() {
		ArrayList<Operator> xChildren = x.nonTerminalsToList();
		ArrayList<Operator> yChildren = y.nonTerminalsToList();
		
		xChildren.addAll(yChildren);
		xChildren.add(this);
		return xChildren;
	}
	
	public ArrayList<Operator> terminalsToList() {
		ArrayList<Operator> xTerminals = x.terminalsToList();
		ArrayList<Operator> yTerminals = y.terminalsToList();
		xTerminals.addAll(yTerminals);
		return xTerminals;
	}
	
	
	public void swapSubtree(Operator o, Operator n) {
		if (x == o) x = n;
		else if (y == o) y = n;
		else {
			x.swapSubtree(o, n);
			y.swapSubtree(o, n);
		}
	}
	
	public Operator clone() {
		OrOp o = new OrOp(x.clone(), y.clone());
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