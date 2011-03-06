
public class SixMultiplexer {

	private Operator correctSolution() {
		// (IF Al (IF A0 D3 D2) (IF A0 D1 D0))
		
		IfOp a = new IfOp(new TerminalOp("a0"), new TerminalOp("d3"), new TerminalOp("d2"));
		IfOp b = new IfOp(new TerminalOp("a0"), new TerminalOp("d1"), new TerminalOp("d0"));
		IfOp c = new IfOp(new TerminalOp("a1"), a, b);
		return c;
	}
	
}
