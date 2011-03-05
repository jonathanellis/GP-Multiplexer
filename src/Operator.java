import java.util.ArrayList;
import java.util.Random;

public abstract class Operator implements Cloneable {
	protected Random rng = new Random();
	private String cacheString = "";
	private double fitness = -1;
	public abstract boolean evaluate(Valuation v);
	public abstract void grow(int maxDepth);
	public abstract void full(int maxDepth) throws Exception;
	public abstract ArrayList<Operator> nonTerminalsToList();
	public abstract ArrayList<Operator> terminalsToList();
	public abstract void swapSubtree(Operator o, Operator n);
	public abstract Operator clone();
	public abstract int treeMaxHeight();
	public abstract void prune(int height);
	
	public boolean compare(Operator op) {
	        return getString().equals(op.getString());
	}
	public String getString() {
	        if(cacheString.equals("")) cacheString = toString();
	        return cacheString;
	}
	
	public double getFitness(Multiplexer mux) {
	        if(fitness == -1) fitness = mux.computeFitness(this);
	        return fitness;
	}
	public void normaliseFitness(Multiplexer mux, double fSum) {
	        fitness /= fSum;
	}
	public Operator generateRandomChild(int maxDepth) {
		if (maxDepth == 0) return generateRandomTerminal();
		
		boolean r = rng.nextBoolean(); // 50% probability of terminal vs function
		if (r) return generateRandomTerminal();
		return generateRandomFunction();
	}
	
	public Operator generateRandomTerminal() {
		return new TerminalOp(rng.nextInt(Multiplexer.TOTAL_LINES));
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
