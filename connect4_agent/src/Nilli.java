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

		currentState = new State(-1);
	}
	
	
	// lastDrop is 0 for the first call of nextAction (no action has been executed),
	// otherwise it is a number n with 0<n<8 indicating the column that the last piece was dropped in by the player whose turn it was
	public String nextAction(int lastDrop) { 
		int move = 0;
		if (lastDrop > 0)
		{
			// updating the board
			if (myTurn) currentState = currentState.nextState(lastDrop - 1, true);
			else currentState = currentState.nextState(lastDrop - 1, false);
		}
		// updating the turn
		myTurn = !myTurn;
		
		if (myTurn) 
		{
			// first move is hard coded, since we know it is the best
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
				if ((score > bestScore) || (score == bestScore && random.nextBoolean()))
				{
					bestAction = i;
					bestScore = score;
				}
			}
		}
		// To ensure that we pick the winning move when available and to
		// delay loss as long as possible, we re-examine the moves and 
		// choose the one that wins or prevents a loss, if possible
		if (bestScore == 0) 
		{
			for (int i = 0; i < 7; i++)
			{
				if (state.isValid(i))
				{
					boolean losingMove = false;
					State tempState = state.nextState(i, true);
					for(int j = 0; j < 7 && !losingMove; j++)
					{
						if (tempState.isValid(j) && tempState.nextState(j, false).terminal() == 2) losingMove = true;
					}
					if (!losingMove) return i;
				}
			}
		}
		if (bestScore == 1000) 
		{
			if (state.nextState(bestAction, true).terminal() != 1)
			{
				for (int i = 0; i < 7; i++)
				{
					if (state.isValid(i))
					{
						State tempState = state.nextState(i,  true);
						if (tempState.terminal() == 1)
						{
							return i;
						}
					}
				}
			}
		}
		return bestAction;
	}

	public int find_move(long timeLimit)
	{
		// we keep track of the best move we have found, search further, and update it
		// if we find a better one
		int bestMove = -1;
		maxDepth = 2;
		while(true)
		{
			// try to run search
			nodes = 0;
			try 
			{
				bestMove = AlphaBetaSearch(currentState, timeLimit);
			}
			catch (TimeOutException t)
			{
				return bestMove;
			}
			maxDepth += 2;
			// if we have searched to the end, we stop
			if (maxDepth > 42) return bestMove;
		}
	}
}
