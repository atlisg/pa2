
public class Nilli implements Agent {
	private String role;
	private int playclock;
	private boolean myTurn;
	
	public void init(String role, int playclock) {
		this.role = role;
		this.playclock = playclock;
		myTurn = !role.equals("WHITE");
		// TODO: add your own initialization code here
    }
	
	public String nextAction(int lastDrop) { 
		// TODO: 1. update your internal world model according to the action that was just executed
		
		myTurn = !myTurn;
		// TODO: 2. run alpha-beta search to determine the best move

		if (myTurn) {
			return "(DROP " + 5 + ")";
		} else {
			return "NOOP";
		}
	}
}
