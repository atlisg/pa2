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
		
	public int maxValue(State state)
	{
		if (state.isTerminal()) return state.heuristics(role);
		int v = Integer.MIN_VALUE;
		for (int i = 0; i < 7; i++)
		{
			if (state.isValid(i))
			{
				v = Math.max(v, minValue(state.nextState(i, role == "white")));
			}
		}
		return v;
	}
	public int minValue(State state)
	{
		if (state.isTerminal()) return state.heuristics(role);
		int v = Integer.MAX_VALUE;
		for (int i = 0; i < 7; i++)
		{
			if (state.isValid(i))
			{
				v = Math.min(v, maxValue(state.nextState(i, role != "white")));
			}
		}
		return v;
	}
	
	public int minimaxDecision(State state)
	{
		int bestAction = -1;
		int bestScore = Integer.MIN_VALUE;
		for (int i = 0; i < 7; i++)
		{
			if (state.isValid(i))
			{
				State next = state.nextState(i, role == "white");
				int score = minValue(next);
				if (score > bestScore)
				{
					bestAction = i;
					bestScore = score;
				}
			}
		}
		return bestAction;
	}
}




