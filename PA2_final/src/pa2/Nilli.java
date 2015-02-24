package pa2;
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
		
	public void init(String role, int playclock)
	{
		this.role = role;
		this.playclock = playclock;
		myTurn = !role.equals("WHITE");
		System.out.println(role + " " + playclock);

		currentState = new State(-1);
	}
	
	
	// lastDrop is 0 for the first call of nextAction (no action has been executed),
	// otherwise it is a number n with 0<n<8 indicating the column that the last piece was dropped in by the player whose turn it was
	public String nextAction(int lastDrop) { 
		// TODO: 1. update your internal world model according to the action that was just executed
		int move = 0;
		if (lastDrop > 0)
		{
			// updating the board
			if (myTurn) currentState = currentState.nextState(lastDrop - 1, true);
			else currentState = currentState.nextState(lastDrop - 1, false);
		}
		// printing and updating the turn
		currentState.print(myTurn);
		System.out.println("RATING: " + currentState.rating());
		myTurn = !myTurn;
		
		if (myTurn) 
		{
			// first move is hardcoded, since we know it is the best
			if (lastDrop == 0) move = 4;
			else move = find_move(System.currentTimeMillis() + playclock * 1000) + 1;
			return "(Drop " + move + ")";
		} 
		else return "NOOP";

	}
		
	static public int AlphaBetaMaxValue(State state, boolean thisRole, int depth, int alpha, int beta, long timeLimit) throws TimeOutException
	{
		nodes++;
		if (timeLimit - System.currentTimeMillis() < 100) throw new TimeOutException("Out of time");
		
		int term = state.terminal();
		if (term == 1) return 1000;
		if (term == 2) return 0;
		if (term == 3) return 500;
		
		if (depth >= maxDepth) return state.rating();
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
		return v;
	}
	static public int AlphaBetaMinValue(State state, boolean thisRole, int depth, int alpha, int beta, long timeLimit) throws TimeOutException
	{
		nodes++;
		
		int term = state.terminal();
		if (term == 1) return 1000;
		if (term == 2) return 0;
		if (term == 3) return 500;
		
		// depth limit
		if (depth >= maxDepth) return state.rating();
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
		if (bestScore == 0) System.out.println("I have lost");
		if (bestScore == 1000) 
		{
			System.out.println("I have won");
			if (state.nextState(bestAction, true).terminal() != 1)
			{
				for (int i = 0; i < 7; i++)
				{
					if (state.nextState(i, true).terminal() == 1) return i;
				}
			}
		}
		return bestAction;
	}

	public int find_move(long timeLimit)
	{
		int bestMove = -1;
		maxDepth = 2;
		while(true)
		{
			// try to run search
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
		Nilli nilli = new Nilli();
		nilli.init("WHITE", 5);
		int lastAction = 0;
		Scanner in = new Scanner(System.in);
		while(true)
		{
			System.out.println(nilli.nextAction(lastAction));
			lastAction = in.nextInt();
		}
	}
}
