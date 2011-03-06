
public abstract class Valuation {
	protected boolean values[];
	protected String names[];
	
	public void printValues() {
		for (int i=0; i<values.length; i++) {
			int k = values[i] ? 1 : 0;
			System.out.print(k);
		}
		System.out.println("");
	}
	
	public boolean valueForTerminal(String t) {
		for (int i=0; i<values.length; i++) {
			if (names[i].equals(t)) return values[i];
		}
		return false; // should never happen
	}
}