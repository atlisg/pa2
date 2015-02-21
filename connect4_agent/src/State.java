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
		System.out.println("player is " + player);
		if (!isValid(action)) {System.out.println("action not valid"); return null;}
		State next = new State();
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
		if (height[0] == 6 && height[1] == 6 && height[2] == 6 && height[3] == 6 && height[4] == 6 && height[5] == 6 && height[6] == 6) return 3;
		// vertical
		for (int i = 0; i < 7; i++)
		{
			int player;
			if (board[i][3]) player = 1;
			else player = 2;
			if (height[i] >= 4) if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][2] == board[i][3]) {System.out.println("vertical from " + i + " " + 4); return player;}
			if (height[i] >= 5) if (board[i][1] == board[i][2] && board[i][2] == board[i][3] && board[i][3] == board[i][4]) {System.out.println("vertical from " + i + " " + 5); return player;}
			if (height[i] >= 6) if (board[i][2] == board[i][3] && board[i][3] == board[i][4] && board[i][4] == board[i][5]) {System.out.println("vertical from " + i + " " + 6); return player;}
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
				if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[2][i] == board[3][i]) {System.out.println("horizontal from " + 0 + " " + i);return player;}
			}
			// 1
			if (Math.min(height[4], Math.min(height[1], Math.min(height[2], height[3]))) > i)
			{
				if (board[3][i] == board[4][i] && board[1][i] == board[2][i] && board[2][i] == board[3][i]) {System.out.println("horizontal from " + 1 +" "+ i); return player;}
			}
			// 2
			if (Math.min(height[4], Math.min(height[5], Math.min(height[2], height[3]))) > i)
			{
				if (board[3][i] == board[4][i] && board[4][i] == board[5][i] && board[2][i] == board[3][i]) {System.out.println("horizontal from " + 2 +" "+ i);return player;}
			}
			// 3
			if (Math.min(height[4], Math.min(height[5], Math.min(height[6], height[3]))) > i)
			{
				if (board[3][i] == board[4][i] && board[4][i] == board[5][i] && board[5][i] == board[6][i]) {System.out.println("horizontal from " + 3 + " " + i);return player;}
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
						if(height[i + 3] > j - 3 && value == board[i + 3][j - 3]) {System.out.println("diagonal-1 from " + i + " " + j);return player;}
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
						if(height[i - 3] > j - 3 && value == board[i - 3][j - 3]) {System.out.println("diagonal-2 from " + i + " "+ j);return player;}
			}
		}
		return 0;
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
		System.out.print("  ");
		for(int i = 0; i < 7; i++) System.out.print(height[i] + " ");
		System.out.println();
	}
	int heuristics()
	{
		int result = isTerminal();
		if (result == 1) return 100;
		else if (result == 2) return 0;
		else if (result == 3) return 50;
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
		return type1 - type2;
		//if (role == "white") return type1 - type2;
		//else return type2 - type1;
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
		State state = new State();
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
