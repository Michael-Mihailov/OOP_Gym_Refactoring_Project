// class: { id (Integer), name (String), difficulty (String), capacity (Integer), enrolled_ids (List<Integer>) }
import java.util.*;

public class Trainer implements Identifiable
{
    private int id;
    private String name;
    
    private String specialty;
    private List<String> schedule;

    public Trainer(int id, String name, String specialty)
    {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        
        this.schedule = new ArrayList();
    } 
    
    public String getSpecialty()
    {
        return specialty;
    }
    
    public void clearSchedule()
    {
        schedule = new ArrayList();
    }
    public void pushToSchedule(String entry) // adds to the end of the schedule
    {
        schedule.add(entry);
    }
    public List<String> getScheduleEntries()
    {
        return Collections.unmodifiableList(schedule);
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
        String scheduleStr = (schedule == null || schedule.isEmpty())
                        ? "No availability"
                        : String.join(", ", schedule);
                        
        return String.format("[%d] %s - %s (Schedule: %s)%n",
                        id, name, specialty, scheduleStr);
    }
}
