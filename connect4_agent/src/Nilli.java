import java.util.Random;

import aima.core.util.datastructure.Pair;

public class Nilli implements Agent
{
	private Random random = new Random();
	
	private String role;
	private int playclock;
	private boolean myTurn;
	
	//boolean[][] board;
	//int[] height;
	
	State currentState;
	
	public void init(String role, int playclock)
	{
		this.role = role;
		this.playclock = playclock;
		myTurn = !role.equals("WHITE");
		
		// TODO: add your own initialization code here
		//board = new boolean [7][6];
		//height = new int[7];
		//for (int i = 0; i < 7; i++)	height[i] = 0;
		currentState = new State(-1);
	}
	
	
	// lastDrop is 0 for the first call of nextAction (no action has been executed),
	// otherwise it is a number n with 0<n<8 indicating the column that the last piece was dropped in by the player whose turn it was
	public String nextAction(int lastDrop) { 
		// TODO: 1. update your internal world model according to the action that was just executed
		int move = 0;
		if (lastDrop > 0)
		{
			if ((myTurn && role.equals("white") || (!myTurn && !role.equals("white")))) 
				currentState = currentState.nextState(lastDrop - 1, true);
			else currentState = currentState.nextState(lastDrop - 1, false);
			currentState.print(myTurn);
		}
		myTurn = !myTurn;
		// TODO: 2. run alpha-beta search to determine the best move
		
		if (myTurn) {
			if (lastDrop == 0) move = 4;
			else move = alphabeta(currentState, 5, 0, 0, myTurn).getSecond();
			return "(Drop " + move + ")";
		} else {
			return "NOOP";
		}
	}
	
	Pair<Integer, Integer> alphabeta(State state, int depth, int alpha, int beta, boolean myTurn) {
		if (depth == 0 || currentState.isTerminal()) {
			return new Pair<Integer, Integer>(currentState.heuristics(role), currentState.parent);
		}
		if (myTurn) {
			for (int i = 0; i < 7; i++)
			{
				alpha = Math.max(alpha, alphabeta(currentState.nextState(i, myTurn), depth - 1, alpha, beta, !myTurn).getFirst());
				if (beta <= alpha) break;
			}
			return new Pair<Integer, Integer>(alpha, currentState.parent);
		} else {
			for (int i = 0; i < 7; i++)
			{
				beta = Math.min(beta, alphabeta(currentState.nextState(i, myTurn), depth - 1, alpha, beta, !myTurn).getFirst());
				if (beta <= alpha) break;
			}
			return new Pair<Integer, Integer>(beta, currentState.parent);
		}
	}
		
	static public int maxValue(State state, boolean thisRole, int depth)
	{
		System.out.println("\t\t\t" + depth);
		if (state.isTerminal() > 0) {System.out.println("terminal");return state.heuristics();}
		int v = Integer.MIN_VALUE;
		for (int i = 0; i < 7; i++)
		{
			if (state.isValid(i))
			{
				v = Math.max(v, minValue(state.nextState(i, thisRole), !thisRole, depth + 1));
			}
		}
		return v;
	}
	static public int minValue(State state, boolean thisRole, int depth)
	{
		System.out.println("\t\t\t" + depth);
		if (state.isTerminal() > 0) {System.out.println("terminal"); return state.heuristics();}
		int v = Integer.MAX_VALUE;
		for (int i = 0; i < 7; i++)
		{
			if (state.isValid(i))
			{
				v = Math.min(v, maxValue(state.nextState(i, thisRole), !thisRole, depth + 1));
			}
		}
		return v;
	}
	
	static public int minimaxDecision(State state)
	{
		int depth = 0;
		int bestAction = -1;
		int bestScore = Integer.MIN_VALUE;
		for (int i = 0; i < 7; i++)
		{
			if (state.isValid(i))
			{
				State next = state.nextState(i, true);
				int score = minValue(next, true, depth + 1);
				System.out.println("action " + i + " score " + score);
				if (score > bestScore)
				{
					bestAction = i;
					bestScore = score;
				}
			}
		}
		System.out.println("best score " + bestScore);
		return bestAction;
	}
	
	static public void main(String[] args)
	{
		State state = new State();
		boolean[] pos = {true, true, false, false, false, true, true,
						true, true, false, false, true, true, true,
						true, true, false, false, true, true, true, 
						false, false, true, false, true, false, false,
						false, false, true, false, true, false, false,
						false, false, true, true, false, false, false};
		int[] heights = {5,6,6,0,4,6,6};
		int current = 0;
		for(int i = 5; i >= 0; i--) for (int j = 0; j < 7; j++) state.board[j][i] = pos[current++];
		current = 0;
		for (int i = 0; i < 7; i++) state.height[i] = heights[current++];
		
		state.print(true);
		
		System.out.println(minimaxDecision(state));
		state.print(true);
	}
}




