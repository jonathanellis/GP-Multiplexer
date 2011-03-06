import java.util.ArrayList;
import java.util.Random;

public abstract class Operator implements Cloneable {
	protected Random rng = new Random();
	public abstract boolean evaluate(SixValuation v);
	public abstract void grow(int maxDepth);
	public abstract ArrayList<Operator> nonTerminalsToList();
	public abstract ArrayList<Operator> terminalsToList();
	public abstract void swapSubtree(Operator o, Operator n);
	public abstract Operator clone();
	public abstract int treeMaxHeight();
	public abstract String mathematicaNotation();
	
	public Operator generateRandomChild(int maxDepth) {
		if (maxDepth == 0) return new TerminalOp(6);
		
		boolean r = rng.nextBoolean(); // 50% probability of terminal vs function
		if (r) return new TerminalOp(6);
		return generateRandomFunction();
	}
	
	
	public Operator generateRandomFunction() {
		Random rng = new Random();
		int r = rng.nextInt(4);
		switch (r) {
			case 0: return new AndOp();
			case 1: return new OrOp();
			case 2: return new NotOp();
			case 3: return new IfOp();
		}
		return null;
	}
	
}
