import java.util.ArrayList;
import java.util.Random;

public abstract class Operator implements Cloneable {
	protected Random rng = new Random();
	public abstract boolean evaluate(Valuation v);
	public abstract void grow(int maxDepth);
	public abstract ArrayList<Operator> nonTerminalsToList();
	public abstract ArrayList<Operator> terminalsToList();
	public abstract void swapSubtree(Operator o, Operator n);
	public abstract Operator clone();
	public abstract int treeMaxHeight();
	public abstract String mathematicaNotation();
	
	public Operator generateRandomChild(int maxDepth) {
		if (maxDepth == 0) return generateRandomTerminal();
		
		boolean r = rng.nextBoolean(); // 50% probability of terminal vs function
		if (r) return generateRandomTerminal();
		return generateRandomFunction();
	}
	
	public Operator generateRandomTerminal() {

		int r = rng.nextInt(6);
		switch (r) {
			case 0: return new TerminalOp("a0");
			case 1: return new TerminalOp("a1");
			case 2: return new TerminalOp("d0");
			case 3: return new TerminalOp("d1");
			case 4: return new TerminalOp("d2");
			case 5: return new TerminalOp("d3");
		}
		return null;
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
