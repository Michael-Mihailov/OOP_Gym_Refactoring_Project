
public class MemberService
{
    IdentifiableRegistry<Member> registry;
    IdGenerator idGenerator;
    
    public MemberService(IdentifiableRegistry<Member> registry, IdGenerator idGenerator)
    {
        this.registry = registry;
    }
    
    public void addMember(String name, MembershipType membershipType)
    {
        Member member = new Member(idGenerator.next(), name, membershipType);
        registry.putEntry(member);
    }
    
    public String receiptBasic()
    {
        String res = "\n=== Members ===";
        if (registry.size() == 0) res += "\nNo members found.";
        
        for (Member entry : registry.getEntries())
        {
            res += "\n";
            res += String.format("[%d] %s%n", entry.getId(), entry.getName());
        }
        
        return res;
    }
    public String receiptDetailed()
    {
        String res = "\n=== Members ===";
        if (registry.size() == 0) res += "\nNo members found.";
        
        for (Member entry : registry.getEntries())
        {
            res += "\n";
            res += entry.toString();
        }
        
        return res;
    }
    
    public Member findMemberById(int memberId)
    {
        return registry.getEntry(memberId);
    }
    
    public DeactivateMemberResult deactivateMember(int memberId)
    {
        Member member = registry.getEntry(memberId);
        
        if (member == null) return DeactivateMemberResult.NOT_FOUND;
        if (member.isActive() == false) return DeactivateMemberResult.ALREADY_INNACTIVE;
        
        member.Deactivate();
        return DeactivateMemberResult.SUCCESS;
    }
    
    // addChargeToMember
    
    // applyPaymentFromMember
}
