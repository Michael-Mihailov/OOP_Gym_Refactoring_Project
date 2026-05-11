public interface Identifiable
{
    public int getId();
    public String getName();
    
    public boolean matches(int id);
    public boolean matches(String name);
}