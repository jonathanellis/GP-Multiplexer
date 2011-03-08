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
		} else if (order == 16) {
			String n[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
			names = n;
		}
		
		int i = order-1;
		while (seed > 0) {
			values[i] = seed % 2 == 1; // Check for a 1 at the right-most bit
			seed /= 2; // Equivalent to a binary shift, shifting off the right-most bit
			i--;
		}
	}
	
	// Used to determine the correct output for the given program (model solutions):
	public boolean correctOutput() {
		if (order == 6) return sixMultiplexerCorrectOutput();
		else if (order == 11) return elevenMultiplexerCorrectOutput();
		else if (order == 16) return sixteenMiddleThreeCorrectOutput();
		return false;
	}
	
	// Correct solution for 6-multiplexer:
	private boolean sixMultiplexerCorrectOutput() {
		if (values[0] && values[1]) return values[5];
		if (values[0]) return values[3];
		if (values[1]) return values[4];
		return values[2];
	}
	
	// Correct solution for 11-multiplexer:
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
	
	// Correct output for 16-middle-3:
	private boolean sixteenMiddleThreeCorrectOutput() {
		int sum = 0;
		for (int i=0; i<values.length; i++) {
			sum += values[i] ? 1 : 0;
		}
		if (sum >= 7 && sum <= 9) return true;
		return false;
	}

	// Prints out the valuation in human-readable format:
	public void printValues() {
		for (int i=0; i<values.length; i++) {
			int k = values[i] ? 1 : 0;
			System.out.print(k);
		}
		System.out.println("");
	}
	
	// Returns value or a given terminal name:
	public boolean valueForTerminal(String t) {
		for (int i=0; i<values.length; i++) {
			if (names[i].equals(t)) return values[i];
		}
		return false; // should never happen
	}
}