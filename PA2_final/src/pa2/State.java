package pa2;
import java.util.*;

public class State {
	boolean [][]board;
	int[] height;
	int parent;
	
	public State(int parent)
	{
		board = new boolean[7][6];
		height = new int[7];
		for (int i = 0; i < 7; i++) height[i] = 0;
		this.parent = parent;
	}
	
	// updates the board with move 'action' (0-index) from player 'player
	public State nextState(int action, boolean player)
	{
		if (!isValid(action)) {System.out.println("action not valid"); return null;}
		State next = new State(action);
		// copying the board from the parent
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < height[i]; j++)
			{
				next.board[i][j] = board[i][j];
			}
			next.height[i] = height[i];
		}
		// adding the most recent move
		if (player) next.board[action][next.height[action]++] = true;
		else next.board[action][next.height[action]++] = false;
		return next;
	}
	
	// determines if 'action' is a valid move
	public boolean isValid(int action)
	{
		return height[action] < 6; 
	}
	
	// returns 0 if non-terminal, 1 if player wins, 2 if opponent wins, and 3 if draw
	public int terminal() {
		int i = parent;
		if (i < 0) return 0;
		int j = height[parent] - 1;
		boolean player = board[i][j];
		// horizontal
		if (count(i, j,  1, 0, player) + count(i, j, -1,  0, player) - 1 >= 4) {
			if (player) return 1;
			else return 2;
		}
		// diagonal upwards left to right
		if (count(i, j,  1, 1, player) + count(i, j, -1, -1, player) - 1 >= 4) {
			if (player) return 1;
			else return 2;
		}
		// diagonal downwards left to right
		if (count(i, j, -1, 1, player) + count(i, j,  1, -1, player) - 1 >= 4) {
			if (player) return 1;
			else return 2;
		}
		// vertical
		if (count(i, j,  0, -1, player) >= 4) {
			if (player) return 1;
			else return 2;
		}
		
		if (height[0] == 6 && height[1] == 6 && height[2] == 6 && height[3] == 6 && height[4] == 6 && height[5] == 6 && height[6] == 6) 
			return 3;
		
		return 0;
	}
	
	// rates this row/column/diagonal
	int rate(int a, int b, int c, int d)
	{
		int count = 0;
		int[] ratings = {0,1,15,40,1000};
		if (a * b * c * d != 0)
		{
			// potentially a win row
			if (a == 1) count++;
			if (b == 1) count++;
			if (c == 1) count++;
			if (d == 1) count++;
			return ratings[count];
		}
		else
		{
			// potentially a lose row
			if (a == 1 || b == 1 || c == 1 || d == 1) return 0;
			if (a == 0) count++;
			if (b == 0) count++; 
			if (c == 0) count++; 
			if (d == 0) count++; 
			return -ratings[count];
		}
	}
	// finds the total rating of the board
	int rating()
	{
		int term = terminal();
		if (term == 1) return 1000;
		if (term == 2) return 0;
		if (term == 3) return 500;
		int sum = 0;
		// vertical
		for (int i = 0; i < 7; i++)
		{
			for (int j = 3; j < 6; j++) sum += rate(space(i,j), space(i,j-1), space(i,j-2), space(i,j-3));
		}
		// horizontal
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 6; j++) sum += rate(space(i,j), space(i+1,j), space(i+2,j), space(i+3,j));
		}
		// vertical - down
		for (int i = 0; i < 4; i++)
		{
			for (int j = 3; j < 6; j++) sum += rate(space(i,j), space(i+1,j-1), space(i+2,j-2), space(i+3,j-3));
		}
		// vertical - up
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++) sum += rate(space(i,j),space(i+1,j+1),space(i+2,j+2),space(i+3,j+3));
		}
		return sum + 500;
	}
	// checks how many discs are in a line
	int count(int i, int j, int x, int y, boolean player) {
		if (player) {
			if (space(i, j) == 1) return 1 + count(i + x, j + y, x, y, player);
			else return 0;
		} else {
			if (space(i, j) == 0) return 1 + count(i + x, j + y, x, y, player);
			else return 0;
		}
	}
	
	// returns -1 if the space is empty, 0 if it has the opponent's disc and 1 if it has the player's disc
	int space(int i, int j) 
	{
		if (i < 0 || i > 6 || j < 0 || j > 5) return -1;
		if (j >= height[i]) return -1;
		if (board[i][j]) return 1;
		else return 0;
	}
	
	// prints the board
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
		System.out.print("  ");
		for(int i = 0; i < 7; i++) System.out.print(height[i] + " ");
		System.out.println();
	}

	// the original evaluation function
	int oldRating()
	{
		int result = terminal();
		if (result == 1) return 1000;
		else if (result == 2) return 0;
		else if (result == 3) return 500;
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
		return 500 + type1 - type2;
	}
}