
public class Valuation {
	public boolean a0;
	public boolean a1;
	public boolean d0;
	public boolean d1;
	public boolean d2;
	public boolean d3;
	
	public Valuation(int a0, int a1, int d0, int d1, int d2, int d3) {
		this.a0 = a0 > 0;
		this.a1 = a1 > 0;
		this.d0 = d0 > 0;
		this.d1 = d1 > 0;
		this.d2 = d2 > 0;
		this.d3 = d3 > 0;
	}
	
}