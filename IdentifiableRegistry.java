import java.util.*;

public class IdentifiableRegistry <T extends Identifiable>
{
    Map<Integer, T> entries; // a map has a better lookup time than a list
    
    public IdentifiableRegistry()
    {
        entries = new TreeMap<Integer, T>();
    }
    
    public void putEntry(T entry)
    {
        entries.put(entry.getId(), entry);
    }
    
    public T getEntry(int id)
    {
        return entries.get(id);
    }
    
    public int size()
    {
        return entries.size();
    }
    
    public Collection<T> getEntries()
    {
        return Collections.unmodifiableCollection(entries.values()); // look don't touch
    }
}