import java.util.ArrayList;
import java.util.Random;

public abstract class Operator implements Cloneable {
	protected Random rng = new Random();
	protected int order;
	public abstract boolean evaluate(Valuation v);
	public abstract void grow(int maxDepth);
	public abstract ArrayList<Operator> nonTerminalsToList();
	public abstract ArrayList<Operator> terminalsToList();
	public abstract void swapSubtree(Operator o, Operator n);
	public abstract Operator clone();
	public abstract int treeMaxHeight();
	public abstract String mathematicaNotation();
	
	
	protected Operator(int order) {
		this.order = order;
	}
	
	protected Operator generateRandomChild(int maxDepth) {
		if (maxDepth == 0) return new TerminalOp(order);
		
		boolean r = rng.nextBoolean(); // 50% probability of terminal vs nonterminal
		if (r) return new TerminalOp(order);
		return generateRandomNonterminal();
	}
	
	private Operator generateRandomNonterminal() {
		Random rng = new Random();
		int r = rng.nextInt(4);
		switch (r) {
			case 0: return new AndOp(order);
			case 1: return new OrOp(order);
			case 2: return new NotOp(order);
			case 3: return new IfOp(order);
		}
		return null;
	}
	
}
