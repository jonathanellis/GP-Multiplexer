
public class ElevenValuation extends Valuation {

	public ElevenValuation(int seed) {
		values = new boolean[11];
		//names = 
		int i = 10;
		while (seed > 0) {
			values[i] = seed % 2 == 1;
			seed /= 2;
			i--;
		}
	}
	
	public boolean correctOutput() {
		// need correct output here
		return false;
	}
	
	
}