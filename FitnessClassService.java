import java.util.*;

public class FitnessClassService
{
    public static final double BASE_FITNESS_CLASS_FEE = 10.0;
    
    IdentifiableRegistry<FitnessClass> registry;
    IdGenerator idGenerator;
    
    public FitnessClassService(IdentifiableRegistry<FitnessClass> registry, IdGenerator idGenerator)
    {
        this.registry = registry;
        this.idGenerator = idGenerator;
    }
    
    public FitnessClass createClass(String name, DifficultyType difficultyType, int capacity)
    {
        FitnessClass fitnessClass = new FitnessClass(
            idGenerator.next(), name, difficultyType, capacity, new IdentifiableRegistry<Member>());
        
        registry.putEntry(fitnessClass);
        
        return fitnessClass;
    }
    
    public Collection<FitnessClass> getFitnessClassEntries()
    {
        return registry.getEntries();
    }
    
    public String receiptBasic()
    {
        String res = "\n=== Fitness Classes ===\n";
        if (registry.size() == 0) res += "No classes found.\n";
        
        for (FitnessClass entry : registry.getEntries())
        {
            res += String.format("[%d] %s%n", entry.getId(), entry.getName());
        }
        
        return res;
    }
    public String receiptDetailed()
    {
        String res = "\n=== Fitness Classes ===\n";
        if (registry.size() == 0) res += "No classes found.\n";
        
        for (FitnessClass entry : registry.getEntries())
        {
            res += entry.toString();
        }
        
        return res;
    }
    
    public FitnessClass findClassById(int classId)
    {
        return registry.getEntry(classId);
    }
    
    public EnrollMemberResult enrollMemberInClass(MemberService memberService, int memberId, int fitnessClassId)
    {
        Member member = memberService.findMemberById(memberId);
        if (member == null) return new EnrollMemberResult(EnrollMemberResult.Status.MEMBER_NOT_FOUND);
        if (memberService.getMemberActiveStatus(memberId) == false) return new EnrollMemberResult(EnrollMemberResult.Status.INNACTIVE_MEMBER);
        
        FitnessClass fitnessClass = findClassById(fitnessClassId);
        if (fitnessClass == null) return new EnrollMemberResult(EnrollMemberResult.Status.CLASS_NOT_FOUND);
        
        AddMemberResult addMemberResult = fitnessClass.addMember(member);
        if (addMemberResult == AddMemberResult.CLASS_FULL) return new EnrollMemberResult(EnrollMemberResult.Status.CLASS_FULL);
        if (addMemberResult == AddMemberResult.ALREADY_ENROLLED) return new EnrollMemberResult(EnrollMemberResult.Status.ALREADY_ENROLLED);
        
        // SUCCESS case
        PaymentResult paymentResult = memberService.chargeMemberWithDiscount(memberId, BASE_FITNESS_CLASS_FEE);
        
        return new EnrollMemberResult(EnrollMemberResult.Status.SUCCESS, paymentResult);
    }
    
    public String listClassRoster(int classId)
    {  
        String res = "";
        
        FitnessClass fitnessClass = findClassById(classId);
        if (fitnessClass == null)
        {
            res += "\nClass not found.";
            return res;
        }
        
        res += "\nRoster for " + fitnessClass.getName() + ":";
        
        Collection<Member> enrolledMembers = fitnessClass.getEnrollmentEntries();
        if (enrolledMembers.size() == 0)
        {
            res += "\nNo members enrolled.";
            return res;
        }
        
        for (Member member : enrolledMembers)
        {
            if (member == null) continue;
            res += "\n- " + member.getName() + " (" + member.getMembershipType().name() + ")";
        }
        
        return res;
    }
}