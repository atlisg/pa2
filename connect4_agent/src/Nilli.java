import java.util.Random;

public class Nilli implements Agent
{
	private Random random = new Random();
	
	private String role;
	private int playclock;
	private boolean myTurn;
	
	boolean[][] board;
	int[] height;
	
	public void init(String role, int playclock)
	{
		this.role = role;
		this.playclock = playclock;
		myTurn = !role.equals("WHITE");
		
		// TODO: add your own initialization code here
		board = new boolean [7][6];
		height = new int[7];
		for (int i = 0; i < 7; i++)	height[i] = 0;
	}
	
	
	// lastDrop is 0 for the first call of nextAction (no action has been executed),
	// otherwise it is a number n with 0<n<8 indicating the column that the last piece was dropped in by the player whose turn it was
	public String nextAction(int lastDrop) { 
		// TODO: 1. update your internal world model according to the action that was just executed
		int move = 0;
		if (lastDrop > 0)
		{
			if ((myTurn && role.equals("white") || (!myTurn && !role.equals("white")))) board[lastDrop - 1][height[lastDrop - 1]++] = true;
			else board[lastDrop - 1][height[lastDrop - 1]++] = false;
			print();
		}
		myTurn = !myTurn;
		// TODO: 2. run alpha-beta search to determine the best move
		if (myTurn) {
			if (lastDrop == 0) move = 4;
			else move = evaluate();
			return "(Drop " + move + ")";
		} else {
			return "NOOP";
		}

	}
	
	boolean isValid(int column)
	{
		return height[column] < 6; 
	}
	
	int evaluate()
	{
		int type1, type2, max1 = 0, max2 = 0, i1 = 0, i2 = 0;
		for (int i = 0; i < 7; i++)
		{
			type1 = 0; type2 = 0;
			if (height[i] < 5) {
				int j = height[i];
				// count positive
				if (space(i+1,j) == 1) type1++;
				if (space(i+1,j+1) == 1) type1++;
				if (space(i-1,j+1) == 1) type1++;
				if (space(i-1,j) == 1) type1++;
				if (space(i-1,j-1) == 1) type1++;
				if (space(i,j-1) == 1) type1++;
				if (space(i+1,j-1) == 1) type1++;
				/*if (space(i+1,j) == 1 || space(i+1,j+1) == 1 || space(i+1,j-1) == 1 || space(i,j+1) == 1 ||
						space(i,j-1) == 1 || space(i-1,j-1) == 1 || space(i-1,j) == 1 ||space(i-1,j+1) == 1) type1++;*/
				// count negative
				if (space(i+1,j) == 0) type2++;
				if (space(i+1,j+1) == 0) type2++;
				if (space(i-1,j+1) == 0) type2++;
				if (space(i-1,j) == 0) type2++;
				if (space(i-1,j-1) == 0) type2++;
				if (space(i,j-1) == 0) type2++;
				if (space(i+1,j-1) == 0) type2++;
				/*if (space(i+1,j) == 0 || space(i+1,j+1) == 0 || space(i+1,j-1) == 0 || space(i,j+1) == 0 ||
						space(i,j-1) == 0 || space(i-1,j-1) == 0 || space(i-1,j) == 0 ||space(i-1,j+1) == 0) type2++;*/
			}
			if (max1 < type1) i1 = i;
			if (max2 < type2) i2 = i;
			max1 = Math.max(max1, type1);
			max2 = Math.max(max2, type2);
		}
		System.out.println("max1: " + max1 + " max2: " + max2 + " i1: " + i1 + " i2: " + i2);
		if (role.equals("WHITE")) return i2 + 1;
		else return i1 + 1;
	}
	int space(int i, int j) // returns -1 if empty or not on board, else 0 if false and 1 if true
	{
		if (i < 0 || i > 6 || j < 0 || j > 5) return -1;
		if (j >= height[i]) return -1;
		if (board[i][j]) return 1;
		else return 0;
	}
	void print()
	{
		if (myTurn) System.out.println("Nilli drops:");
		else System.out.println("Opponent drops:");
		System.out.println("-----------------");
		for (int j = 5; j >= 0; j--)
		{
			System.out.print("| ");
			for (int i = 0; i < 7; i++)
			{
				if (j >= height[i]) System.out.print("  ");
				else if (board[i][j]) System.out.print("o ");
				else System.out.print("x ");
			}
			System.out.println("|");
		}
		System.out.println("-----------------");
	}
}