import java.util.*;

public class TrainerService
{
    IdentifiableRegistry<Trainer> registry;
    IdGenerator idGenerator;
    
    public TrainerService(IdentifiableRegistry<Trainer> registry, IdGenerator idGenerator)
    {
        this.registry = registry;
        this.idGenerator = idGenerator;
    }
    
    public Trainer createTrainer(String name, String specialty)
    {
        Trainer trainer = new Trainer(idGenerator.next(), name, specialty);
        registry.putEntry(trainer);
        
        return trainer;
    }
    
    public Collection<Trainer> getTrainerEntries()
    {
        return registry.getEntries();
    }
    
    public String receiptBasic()
    {
        String res = "\n=== Trainers ===\n";
        if (registry.size() == 0) res += "No trainers found.\n";
        
        for (Trainer entry : registry.getEntries())
        {
            res += String.format("[%d] %s%n", entry.getId(), entry.getName());
        }
        
        return res;
    }
    public String receiptDetailed()
    {
        String res = "\n=== Trainers ===\n";
        if (registry.size() == 0) res += "No trainers found.\n";
        
        for (Trainer entry : registry.getEntries())
        {
            res += entry.toString();
        }
        
        return res;
    }
    
    public Trainer findTrainerById(int trainerId)
    {
        return registry.getEntry(trainerId);
    }
    
    public void appendToTrainerSchedule(int trainerId, String slot)
    {
        findTrainerById(trainerId).pushToSchedule(slot);
    }
    public void clearTrainerSchedule(int trainerId)
    {
        findTrainerById(trainerId).clearSchedule();
    }
}