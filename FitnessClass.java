// class: { id (Integer), name (String), difficulty (String), capacity (Integer), enrolled_ids (List<Integer>) }
import java.util.*;

public class FitnessClass implements Identifiable
{
    private int id;
    private String name;
    
    private DifficultyType difficultyType;
    private int capacity;
    private IdentifiableRegistry<Member> enrollmentRegistry;

    public FitnessClass(int id, String name, DifficultyType difficultyType, int capacity, IdentifiableRegistry<Member> enrollmentRegistry)
    {
        this.id = id;
        this.name = name;
        this.difficultyType = difficultyType;
        this.capacity = capacity;
        
        this.enrollmentRegistry = enrollmentRegistry;
    }
    
    public AddMemberResult addMember(Member member)
    {
        int memberId = member.getId();
        
        if (isFullCapacity()) return AddMemberResult.CLASS_FULL;
        if (containsMemberId(memberId)) return AddMemberResult.ALREADY_ENROLLED;
        
        enrollmentRegistry.putEntry(member);
        return AddMemberResult.SUCCESS;
    }
    private boolean containsMemberId(int memberId)
    {
        return enrollmentRegistry.getEntry(memberId) != null;
    }
    private boolean isFullCapacity()
    {
        return enrollmentRegistry.size() >= capacity;
    }
    
    public DifficultyType getDifficultyType() 
    {
        return difficultyType;
    }
    public int getCapacity()
    {
        return capacity;
    }
    public Collection<Member> getEnrollmentEntries()
    {
        return enrollmentRegistry.getEntries();
    }
    
    @Override
    public int getId()
    {
        return id;
    }
    @Override
    public String getName()
    {
        return name;
    }
    
    @Override
    public boolean matches(int id)
    {
        return this.id == id;
    }
    @Override
    public boolean matches(String name)
    {
        return this.name.equals(name);
    }
    
    @Override
    public String toString()
    {
        String difficulty = difficultyType.getDisplayName();
        int numEnrolled = enrollmentRegistry.size();
        return String.format("[%d] %s (%s, capacity %d, enrolled %d)%n", id, name, difficulty, capacity, numEnrolled);
    }
}