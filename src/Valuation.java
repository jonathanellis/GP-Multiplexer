

public class Valuation {
	private boolean values[];
	private String names[];
	private int order;
	
	public Valuation(int seed, int order) {
		values = new boolean[order];
		this.order = order;
		
		if (order == 6) {
			String n[] = {"a0", "a1", "d0", "d1", "d2", "d3"};
			names = n;
		} else if (order == 11) {
			String n[] = {"a0", "a1", "a2", "d0", "d1", "d2", "d3", "d4", "d5", "d6", "d7"};
			names = n;
		}
		
		int i = order-1;
		while (seed > 0) {
			values[i] = seed % 2 == 1;
			seed /= 2;
			i--;
		}
	}
	
	public boolean correctOutput() {
		if (order == 6) return sixMultiplexerCorrectOutput();
		else if (order == 11) return elevenMultiplexerCorrectOutput();
		return false;
	}
	
	private boolean sixMultiplexerCorrectOutput() {
		if (values[0] && values[1]) return values[5];
		if (values[0]) return values[3];
		if (values[1]) return values[4];
		return values[2];
	}
	
	private boolean elevenMultiplexerCorrectOutput() {
		if (values[0]) {
			if (values[2]) {
				if (values[1]) return values[10];
				return values[8];
			}
			if (values[1]) return values[6];
			return values[4];
		}
		if (values[2]) {
			if (values[1]) return values[9];
			return values[7];
		}
		if (values[1]) return values[5];
		return values[3];
	}

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