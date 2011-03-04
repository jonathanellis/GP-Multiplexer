import java.util.ArrayList;


public class IfOp extends Operator {
	private Operator x;
	private Operator y;
	private Operator z;
	
	public IfOp() {
		// default constructor
	}
	
	public IfOp(Operator x, Operator y, Operator z) {
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
	
	public ArrayList<Operator> nonTerminalsToList() {
		ArrayList<Operator> xChildren = x.nonTerminalsToList();
		ArrayList<Operator> yChildren = y.nonTerminalsToList();
		ArrayList<Operator> zChildren = z.nonTerminalsToList();
		
		xChildren.addAll(yChildren);
		xChildren.addAll(zChildren);
		xChildren.add(this);
		return xChildren;
	}
	
	public ArrayList<Operator> terminalsToList() {
		ArrayList<Operator> xTerminals = x.terminalsToList();
		ArrayList<Operator> yTerminals = y.terminalsToList();
		ArrayList<Operator> zTerminals = z.terminalsToList();
		xTerminals.addAll(yTerminals);
		xTerminals.addAll(zTerminals);
		return xTerminals;
	}
	
	public void swapSubtree(Operator o, Operator n) {
		if (x == o) x = n;
		else if (y == o) y = n;
		else if (z == o) z = n;
		else {
			x.swapSubtree(o, n);
			y.swapSubtree(o, n);
			z.swapSubtree(o, n);
		}
	}
	
	
	public Operator clone() {
		IfOp o = new IfOp(x.clone(), y.clone(), z.clone());
		return o;
		
	}
	
	public int treeMaxHeight() {
		int xHeight = x.treeMaxHeight();
		int yHeight = y.treeMaxHeight();
		int zHeight = z.treeMaxHeight();
		if ((xHeight > yHeight) && (xHeight > zHeight)) return xHeight;
		if (yHeight > zHeight) return yHeight;
		return 1+zHeight;
	}
	
	public String toString() {
		return "(" + x + " ? " + y + " : " + z + ")";
	}
	
	public String mathematicaNotation() {
		return "If[" + x.mathematicaNotation() + ", " + y.mathematicaNotation() + ", " + z.mathematicaNotation() + "]";
	}
	
}