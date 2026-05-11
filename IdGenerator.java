public class IdGenerator
{
    int nextId = 1;

    public int next()
    {
        return nextId++;
    }
}