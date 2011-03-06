
public class SixValuation extends Valuation {

	public SixValuation(int seed) {
		values = new boolean[6];
		String n[] = {"a0", "a1", "d0", "d1", "d2", "d3"};
		names = n;
		
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
	
}