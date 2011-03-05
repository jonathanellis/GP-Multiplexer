import java.util.ArrayList;

public class AndOp extends Operator {
	private Operator x;
	private Operator y;
	
	public AndOp() {
		// default constructor
	}
	
	public AndOp(Operator x, Operator y) {
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
		AndOp o = new AndOp(x.clone(), y.clone());
		return o;
		
	}
	
	public int treeMaxHeight() {
		int xHeight = x.treeMaxHeight();
		int yHeight = y.treeMaxHeight();
		if (xHeight > yHeight) return 1+xHeight;
		return 1+yHeight;
	}
	
	public String toString() {
		return "(" + x + " && " + y + ")";
	}
	
	
	public String mathematicaNotation() {
		return "And[" + x.mathematicaNotation() + " , " + y.mathematicaNotation() + "]";
	}

}