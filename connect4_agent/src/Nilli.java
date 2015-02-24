import java.util.Random;

//import aima.core.util.datastructure.Pair;
import java.util.*;

public class Nilli implements Agent
{
	static public Random random = new Random();
	
	private String role;
	private int playclock;
	private boolean myTurn;
	
	static int maxDepth = 12;
	static int nodes = 0;
	
	static State currentState;
	
//	static public class TimeLimitException extends Exception {}
	
	public void init(String role, int playclock)
	{
		this.role = role;
		this.playclock = playclock;
		myTurn = !role.equals("WHITE");
		System.out.println(role + " " + playclock);
		
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
			if (myTurn) currentState = currentState.nextState(lastDrop - 1, true);
			else currentState = currentState.nextState(lastDrop - 1, false);
		}
		currentState.print(myTurn);
		System.out.println("RATING: " + currentState.rating());
		myTurn = !myTurn;
		// TODO: 2. run alpha-beta search to determine the best move
		
		if (myTurn) {
			if (lastDrop == 0) move = 4;
			//else move = currentState.evaluate(role);
			//else move = AlphaBetaSearch(currentState, 20) + 1;
			else move = find_move(System.currentTimeMillis() + playclock * 1000) + 1;
			return "(Drop " + move + ")";
		} else {
			return "NOOP";
		}

	}
		
	static public int maxValue(State state, boolean thisRole, int depth)
	{
		nodes++;
		System.out.println("\t\t\t" + depth);
		System.out.println("max val, player is " + thisRole);
		state.print(true);
		if (state.isTerminal() > 0) {System.out.println("terminal");return state.heuristics();}
		// depth limit
		if (depth >= maxDepth) {System.out.println("deep enough"); return state.heuristics();}
		int v = Integer.MIN_VALUE + 1;
		for (int i = 0; i < 7; i++)
		{
			if (state.isValid(i))
			{
				v = Math.max(v, minValue(state.nextState(i, thisRole), !thisRole, depth + 1));
			}
		}
		System.out.println("value " + v);
		return v;
	}
	static public int minValue(State state, boolean thisRole, int depth)
	{
		nodes++;
		System.out.println("\t\t\t" + depth);
		System.out.println("min val, player is " + thisRole);
		state.print(false);
		if (state.isTerminal() > 0) {System.out.println("terminal"); return state.heuristics();}
		// depth limit
		if (depth >= maxDepth) {System.out.println("deep enough"); return state.heuristics();}
		int v = Integer.MAX_VALUE;
		for (int i = 0; i < 7; i++)
		{
			if (state.isValid(i))
			{
				v = Math.min(v, maxValue(state.nextState(i, thisRole), !thisRole, depth + 1));
			}
		}
		System.out.println("value "+ v);
		return v;
	}
	
	
	static public int minimaxDecision(State state)
	{
		int depth = 0;
		int bestAction = -1;
		int bestScore = Integer.MIN_VALUE + 1;
		for (int i = 0; i < 7; i++)
		{
			if (state.isValid(i))
			{
				State next = state.nextState(i, true);
				int score = minValue(next, false, depth + 1);
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
	
	
	
	static public int AlphaBetaMaxValue(State state, boolean thisRole, int depth, int alpha, int beta, long timeLimit) throws TimeOutException
	{
		nodes++;
		//System.out.println("Nilli2 - maxValue, time: " + System.currentTimeMillis());
		if (timeLimit - System.currentTimeMillis() < 100) throw new TimeOutException("Out of time");
		//System.out.println("\t\t\t" + depth);
		//System.out.println("max val, player is " + thisRole);
		//state.print(true);
		if (state.terminal(thisRole) > 0) {/*System.out.println("terminal");*/return state.evaluate();}
		// depth limit
		if (depth >= maxDepth) {/*System.out.println("deep enough");*/ return state.evaluate();}
		int v = Integer.MIN_VALUE + 1;
		for (int i = 0; i < 7; i++)
		{
			if (state.isValid(i))
			{
				v = Math.max(v, AlphaBetaMinValue(state.nextState(i, thisRole), !thisRole, depth + 1, alpha, beta, timeLimit));
				if (v >= beta) return v;
				alpha = Math.max(alpha,v);
			}
		}
		//System.out.println("value " + v);
		return v;
	}
	static public int AlphaBetaMinValue(State state, boolean thisRole, int depth, int alpha, int beta, long timeLimit) throws TimeOutException
	{
		nodes++;
		//System.out.println("Nilli2 - minValue, time: " + System.currentTimeMillis());
		//System.out.println("\t\t\t" + depth);
		//System.out.println("min val, player is " + thisRole);
		//state.print(false);
		if (state.terminal(thisRole) > 0) {/*System.out.println("terminal");*/ return state.evaluate();}
		// depth limit
		if (depth >= maxDepth) {/*System.out.println("deep enough");*/ return state.evaluate();}
		int v = Integer.MAX_VALUE;
		for (int i = 0; i < 7; i++)
		{
			if (state.isValid(i))
			{
				v = Math.min(v, AlphaBetaMaxValue(state.nextState(i, thisRole), !thisRole, depth + 1, alpha, beta, timeLimit));
				if (v <= alpha) return v;
				beta = Math.min(beta, v);
			}
		}
		//System.out.println("value "+ v);
		return v;
	}
	
	static public int AlphaBetaSearch(State state, long timeLimit) throws TimeOutException
	{
		int depth = 0;
		int bestAction = -1;
		int bestScore = Integer.MIN_VALUE + 1;
		for (int i = 0; i < 7; i++)
		{
			if (state.isValid(i))
			{
				State next = state.nextState(i, true);
				int score = AlphaBetaMinValue(next, false, depth + 1, Integer.MIN_VALUE + 1, Integer.MAX_VALUE, timeLimit);
				//System.out.println("action " + i + " score " + score);
				if ((score > bestScore) || (score == bestScore && random.nextBoolean()))
				{
					bestAction = i;
					bestScore = score;
				}
			}
		}
		//System.out.println("best score " + bestScore);
		return bestAction;
	}

	public int find_move(long timeLimit)
	{
		int bestMove = -1;
		maxDepth = 2;
		while(true)
		{
			// try to run search
			//System.out.println("depth: " + maxDepth + "\t time: " + System.currentTimeMillis() + " \t limit: " + timeLimit);
			try 
			{
				bestMove = AlphaBetaSearch(currentState, timeLimit);
			} 
			catch (TimeOutException t)
			{
				System.out.println("Nilli2: out of time, depth " + maxDepth + "\t time " + System.currentTimeMillis());
				return bestMove;
			}
			maxDepth += 2;
			if (maxDepth > 42) return bestMove;
		}
	}
	
	static public void main(String[] args)
	{
		//State2 state = new State2(-1);
		
		Nilli nilli = new Nilli();
		nilli.init("WHITE", 5);
		int lastAction = 0;
		Scanner in = new Scanner(System.in);
		while(true)
		{
			System.out.println(nilli.nextAction(lastAction));
			lastAction = in.nextInt();
		}
		
		/*boolean[] pos = {true, true, false, false, false, true, true,
						false, true, false, false, true, true, true,
						false, true, false, false, true, true, true, 
						false, false, false, false, true, false, false,
						true, false, true, false, true, false, false,
						false, false, false, true, false, false, false};
		int[] heights = {5,6,5,1,4,6,6};*/
		/*boolean[] pos = {false, false, false, false, false, false, false, 
				false, false, false, false, true, false, false, 
				false, true, true, false, true, true, false, 
				false, false, false, true, false, false, false, 
				true, true, true, false, true, true, true,
				false, false, false, true, false, false, false};
		int[] heights = {4,5,5,4,5,5,4};*/
		/*boolean[] pos = {false, false, false, false, false, false, true, 
				false, false, false, false, false, false, false, 
				false, true, false, true , true, true, false,
				true, false, false, false, true, false, true, 
				true, true, true, false, true, false, true,
				false, true, true, true, false, true, false};*/
		//int[] heights = {4,5,4,5,3,6,6};
		/*boolean[] pos = {false, false, false, false, false, false, true, 
						false, false, false, false, false, false, false, 
						false, true, false, true , true, true, false,
						false, false, false, false, true, false, true, 
						false, true, true, false, true, false, true,
						false, true, true, true, false, true, false};
		//int[] heights = {3,3,3,0,3,3,3};
		int[] heights = {0,0,0,0,0,0,0};
		int current = 0;
		for(int i = 5; i >= 0; i--) for (int j = 0; j < 7; j++) state.board[j][i] = pos[current++];
		current = 0;
		for (int i = 0; i < 7; i++) state.height[i] = heights[current++];
		
		state.print(true);
		
		//System.out.println(minimaxDecision(state));
		//System.out.println(AlphaBetaSearch(state, ));
		state.print(true);
		System.out.println("nodes: " + nodes);*/
	}
	
	/*
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
		
	}*/
}