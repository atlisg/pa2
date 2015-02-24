package pa2;
public interface Agent
{
    public void init(String role, int playclock);
    public String nextAction(int lastDrop);
}
