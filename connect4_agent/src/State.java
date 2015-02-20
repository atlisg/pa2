import java.util.*;

public class State {
	boolean [][]board;
	int[] height;
	
	public State()
	{
		board = new boolean[7][6];
		height = new int[7];
		for (int i = 0; i < 7; i++) height[i] = 0;
	}
	// updates the board with move 'action' (0-index) from player 'player
	public State nextState(int action, boolean player)
	{
		if (!isValid(action)) return null;
		State next = new State();
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < height[i]; j++)
			{
				next.board[i][j] = board[i][j];
			}
			next.height[i] = height[i];
		}
		if (player) next.board[action][height[action]++] = true;
		else next.board[action][height[action]++] = false;
		return next;
	}
	public boolean isValid(int action)
	{
		return height[action] < 6; 
	}
	
	int evaluate(String role)
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
	
	void print(boolean myTurn)
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
	int heuristics(String role)
	{
		int type1 = 0, type2 = 0;
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < height[i]; j++)
			{
				if (board[i][j])
				{
					// count positive
					if (space(i+1,j) == 1 || space(i+1,j+1) == 1 || space(i+1,j-1) == 1 || space(i,j+1) == 1 ||
							space(i,j-1) == 1 || space(i-1,j-1) == 1 || space(i-1,j) == 1 ||space(i-1,j+1) == 1) type1++;
				}
				else
				{
					// count negative
					if (space(i+1,j) == 0 || space(i+1,j+1) == 0 || space(i+1,j-1) == 0 || space(i,j+1) == 0 ||
							space(i,j-1) == 0 || space(i-1,j-1) == 0 || space(i-1,j) == 0 ||space(i-1,j+1) == 0) type2++;
				}
			}
		}
		if (role == "white") return type1 - type2;
		else return type2 - type1;
	}
}
