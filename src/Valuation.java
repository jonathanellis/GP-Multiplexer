
public class Valuation {
	public boolean values[] = new boolean[6];
	
	public Valuation(int seed) {
		int i = 5;
		while (seed > 0) {
			values[i] = seed % 2 == 1;
			seed /= 2;
			i--;
		}
	}
	
	public boolean correctOutput() {
		if (values[0] && values[1]) return values[5];
		if (values[0]) return values[3];
		if (values[1]) return values[4];
		return values[2];
	}
	
	public void printValues() {
		for (int i=0; i<values.length; i++) {
			int k = values[i] ? 1 : 0;
			System.out.print(k);
		}
		System.out.println("");
	}
	
	public boolean valueForTerminal(String t) {
		if (t.equals("a0")) return values[0];
		if (t.equals("a1")) return values[1];
		if (t.equals("d0")) return values[2];
		if (t.equals("d1")) return values[3];
		if (t.equals("d2")) return values[4];
		if (t.equals("d3")) return values[5];
		return false; // should never happen
	}
	
	public Valuation(int a0, int a1, int d0, int d1, int d2, int d3) {
		values[0] = a0 > 0;
		values[1] = a1 > 0;
		values[2] = d0 > 0;
		values[3] = d1 > 0;
		values[4] = d2 > 0;
		values[5] = d3 > 0;
	}
	
}