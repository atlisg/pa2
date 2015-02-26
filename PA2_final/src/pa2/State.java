package pa2;
import java.util.*;

public class State {
	boolean [][]board;
	int[] height;
	int parent;         // who the fuck just called for this state?
	
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
		//System.out.println("player is " + player);
		if (!isValid(action)) {System.out.println("action not valid"); return null;}
		State next = new State(action);
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < height[i]; j++)
			{
				next.board[i][j] = board[i][j];
			}
			next.height[i] = height[i];
		}
		if (player) next.board[action][next.height[action]++] = true;
		else next.board[action][next.height[action]++] = false;
		return next;
	}
	public boolean isValid(int action)
	{
		return height[action] < 6; 
	}
	
	public int isTerminal()
	{
		// vertical
		for (int i = 0; i < 7; i++)
		{
			int player;
			if (board[i][3]) player = 1;
			else player = 2;
			if (height[i] >= 4) if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][2] == board[i][3]) {/*System.out.println("vertical from " + i + " " + 4);*/ return player;}
			if (height[i] >= 5) if (board[i][1] == board[i][2] && board[i][2] == board[i][3] && board[i][3] == board[i][4]) {/*System.out.println("vertical from " + i + " " + 5);*/ return player;}
			if (height[i] >= 6) if (board[i][2] == board[i][3] && board[i][3] == board[i][4] && board[i][4] == board[i][5]) {/*System.out.println("vertical from " + i + " " + 6);*/ return player;}
		}
		// horizontal
		for (int i = 0; i < 6; i++)
		{
			// 0
			int player;
			if (board[3][i]) player = 1;
			else player = 2;
			if (Math.min(height[0], Math.min(height[1], Math.min(height[2], height[3]))) > i)
			{
				if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[2][i] == board[3][i]) {/*System.out.println("horizontal from " + 0 + " " + i);*/return player;}
			}
			// 1
			if (Math.min(height[4], Math.min(height[1], Math.min(height[2], height[3]))) > i)
			{
				if (board[3][i] == board[4][i] && board[1][i] == board[2][i] && board[2][i] == board[3][i]) {/*System.out.println("horizontal from " + 1 +" "+ i);*/ return player;}
			}
			// 2
			if (Math.min(height[4], Math.min(height[5], Math.min(height[2], height[3]))) > i)
			{
				if (board[3][i] == board[4][i] && board[4][i] == board[5][i] && board[2][i] == board[3][i]) {/*System.out.println("horizontal from " + 2 +" "+ i);*/return player;}
			}
			// 3
			if (Math.min(height[4], Math.min(height[5], Math.min(height[6], height[3]))) > i)
			{
				if (board[3][i] == board[4][i] && board[4][i] == board[5][i] && board[5][i] == board[6][i]) {/*System.out.println("horizontal from " + 3 + " " + i);*/return player;}
			}
		}
		// diagonal
		for (int i = 0; i < 4; i++)
		{
			for(int j = 3; j < height[i]; j++)
			{
				boolean value = board[i][j];
				int player;
				if (value) player = 1;
				else player = 2;
				if (height[i + 1] > j - 1 && value == board[i + 1][j - 1])
					if (height[i + 2] > j - 2 && value == board[i + 2][j - 2])
						if(height[i + 3] > j - 3 && value == board[i + 3][j - 3]) {/*System.out.println("diagonal-1 from " + i + " " + j);*/return player;}
			}
		}
		// other diagonal
		for (int i = 6; i > 2; i--)
		{
			for(int j = 3; j < height[i]; j++)
			{
				boolean value = board[i][j];
				int player;
				if (value) player = 1;
				else player = 2;
				if (height[i - 1] > j - 1 && value == board[i - 1][j - 1])
					if (height[i - 2] > j - 2 && value == board[i - 2][j - 2])
						if(height[i - 3] > j - 3 && value == board[i - 3][j - 3]) {/*System.out.println("diagonal-2 from " + i + " "+ j);*/return player;}
			}
		}
		if (height[0] == 6 && height[1] == 6 && height[2] == 6 && height[3] == 6 && height[4] == 6 && height[5] == 6 && height[6] == 6) return 3;
		return 0;
	}
	
	public int terminal() {
		int i = parent;
		if (i < 0) return 0;
		int j = height[parent] - 1;
		// down
		if(space(i,j) == space(i,j-1) && space(i,j-1) == space(i,j-2) && space(i,j-2) == space(i,j-3))
		{
			if (space(i,j) == 1) return 1; else return 2;
		}
		// diagonal-up-1
		if(space(i,j) == space(i-1,j-1) && space(i-1,j-1) == space(i-2,j-2) && space(i-2,j-2) == space(i-3,j-3))
		{
			if (space(i,j) == 1) return 1; else return 2;
		}
		// diagonal-up-2
		if(space(i+1,j+1) == space(i,j) && space(i,j) == space(i-1,j-1) && space(i-1,j-1) == space(i-2,j-2))
		{
			if (space(i,j) == 1) return 1; else return 2;
		}
		// diagonal-up-1
		if(space(i+2,j+2) == space(i+1,j+1) && space(i+1,j+1) == space(i,j) && space(i,j) == space(i-1,j-1))
		{
			if (space(i,j) == 1) return 1; else return 2;
		}
		// diagonal-up-4
		if(space(i,j) == space(i+1,j+1) && space(i+1,j+1) == space(i+2,j+2) && space(i+2,j+2) == space(i+3,j+3))
		{
			if (space(i,j) == 1) return 1; else return 2;
		}
		
		// diagonal-down-1
		if(space(i,j) == space(i+1,j-1) && space(i+1,j-1) == space(i+2,j-2) && space(i+2,j-2) == space(i+3,j-3))
		{
			if (space(i,j) == 1) return 1; else return 2;
		}
		// diagonal-down-2
		if(space(i-1,j+1) == space(i,j) && space(i,j) == space(i+1,j-1) && space(i+1,j-1) == space(i+2,j-2))
		{
			if (space(i,j) == 1) return 1; else return 2;
		}
		// diagonal-down-3
		if(space(i-2,j+2) == space(i-1,j+1) && space(i-1,j+1) == space(i,j) && space(i,j) == space(i+1,j-1))
		{
			if (space(i,j) == 1) return 1; else return 2;
		}
		// diagonal-down-4
		if(space(i,j) == space(i-1,j-1) && space(i-1,j-1) == space(i-2,j-2) && space(i-2,j-2) == space(i-3,j-3))
		{
			if (space(i,j) == 1) return 1; else return 2;
		}
		// horizontal - 1
		if(space(i,j) == space(i+1,j) && space(i+1,j) == space(i+2,j) && space(i+2,j) == space(i+3,j))
		{
			if (space(i,j) == 1) return 1; else return 2;
		}
		// horizontal - 2
		if(space(i-1,j) == space(i,j) && space(i,j) == space(i+1,j) && space(i+1,j) == space(i+2,j))
		{
			if (space(i,j) == 1) return 1; else return 2;
		}
		// horizontal - 3
		if(space(i-2,j) == space(i-1,j) && space(i-1,j) == space(i,j) && space(i,j) == space(i+1,j))
		{
			if (space(i,j) == 1) return 1; else return 2;
		}
		// horizontal - 4
		if(space(i,j) == space(i-1,j) && space(i-1,j) == space(i-2,j) && space(i-2,j) == space(i-3,j))
		{
			if (space(i,j) == 1) return 1; else return 2;
		}
		
		
		
		/*int i = parent;
		int j = height[parent];
		
		if (count(i, j,  1, 0, player) + count(i, j, -1,  0, player) - 1 == 4) {
			if (player) return 1;
			else return 2;
		}
		if (count(i, j,  1, 1, player) + count(i, j, -1, -1, player) - 1 == 4) {
			if (player) return 1;
			else return 2;
		}
		if (count(i, j, -1, 1, player) + count(i, j,  1, -1, player) - 1 == 4) {
			if (player) return 1;
			else return 2;
		}
		if (count(i, j,  0, 1, player) + count(i, j,  0, -1, player) - 1 == 4) {
			if (player) return 1;
			else return 2;
		}*/
		
		if (height[0] == 6 && height[1] == 6 && height[2] == 6 && height[3] == 6 && height[4] == 6 && height[5] == 6 && height[6] == 6) 
			return 3;
		
		return 0;
	}
	
	int rate(int a, int b, int c, int d)
	{
		// Aðal Nilli - 4003
		int count = 0;
		int[] ratings = {0,1,20,40,1000};
		if (a * b * c * d != 0)
		{
			// potential true
			if (a == 1) count++;
			if (b == 1) count++;
			if (c == 1) count++;
			if (d == 1) count++;
			return ratings[count];
		}
		else
		{
			// potential false;
			if (a == 1 || b == 1 || c == 1 || d == 1) return 0;
			if (a == 0) count++;
			if (b == 0) count++; 
			if (c == 0) count++; 
			if (d == 0) count++; 
			return -ratings[count];
		}
	}
	
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
	
	int evaluate()
	{	
		//return heuristics();
		return rating();
		
		/*int i = parent;					// last action taken
		int j = height[parent] - 1;		// height of the most recently played piece
		
		boolean player = false;
		if (board[i][j]) player = true;
		
		int result = terminal(player);
		if (result == 1) return 100;
		else if (result == 2) return 0;
		else if (result == 3) return 50;

		int horizontal = count(i, j,  1, 0, player) + count(i, j, -1,  0, player) - 1;
		int diagonal1  = count(i, j,  1, 1, player) + count(i, j, -1, -1, player) - 1;
		int diagonal2  = count(i, j, -1, 1, player) + count(i, j,  1, -1, player) - 1;
		int vertical   = count(i, j,  0, 1, player) + count(i, j,  0, -1, player) - 1;
		
		int twos   = 0;
		int threes = 0;
		
		if (horizontal == 2) twos++;
		else if (horizontal == 3) threes++;
		if (diagonal1 == 2) twos++;
		else if (diagonal1 == 3) threes++;
		if (diagonal2 == 2) twos++;
		else if (diagonal2 == 3) threes++;
		if (vertical == 2) twos++;
		else if (vertical == 3) threes++;
		
		twos *= 5;
		threes *= 10;
		
		return twos + threes;*/
	}
	int count(int i, int j, int x, int y, boolean player) {
		if (player) {
			if (space(i, j) == 1) return 1 + count(i + x, j + y, x, y, player);
			else return 0;
		} else {
			if (space(i, j) == 0) return 1 + count(i + x, j + y, x, y, player);
			else return 0;
		}
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
		System.out.print("  ");
		for(int i = 0; i < 7; i++) System.out.print(height[i] + " ");
		System.out.println();
	}
	int heuristics()
	{
		int result = isTerminal();
		//if(result > 0) {System.out.println("is terminal ???"); print(true);}
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
	
	public static void main(String[] args)
	{
		//State state = new State();
		//boolean player = true;
		//int[] moves = {0,1,1,2,2,3,2,3,3,6,3,3};
		//int[] moves = {0,0,0,0,0,1,1,1,1,5,2,2,2,2,};
		/*state.print(true);
		for (int i = 0; i < moves.length; i++)
		{
			state = state.nextState(moves[i], player);
			state.print(player);
			player = !player;
			if (state.isTerminal())
			{
				System.out.println("Terminal state");
				break;
			}
		}*/
		Scanner in = new Scanner(System.in);
		State state = new State(-1);
		Random rand = new Random();
		boolean player = true;
		//state.print(player);
		while(true)
		{
			int move = rand.nextInt(7);
			while(!state.isValid(move)) {System.out.println("invalid move"); move = rand.nextInt(7);}
			state = state.nextState(move, player);
			state.print(player);
			player = !player;
			if (state.isTerminal() > 0) {System.out.println("Game over, result: " + state.isTerminal()); break;}
			in.next();
		}
		in.close();
	}
}