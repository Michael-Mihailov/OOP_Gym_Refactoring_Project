public class IdGenerator
{
    private int nextId = 1;

    public int next()
    {
        return nextId++;
    }
}