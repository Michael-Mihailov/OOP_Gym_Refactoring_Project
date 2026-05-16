import java.util.*;

public class MemberService
{
    IdentifiableRegistry<Member> registry;
    IdGenerator idGenerator;
    
    public MemberService(IdentifiableRegistry<Member> registry, IdGenerator idGenerator)
    {
        this.registry = registry;
        this.idGenerator = idGenerator;
    }
    
    public Member createMember(String name, MembershipType membershipType)
    {
        Member member = new Member(idGenerator.next(), name, membershipType);
        registry.putEntry(member);
        
        return member;
    }
    
    public Collection<Member> getMemberEntries()
    {
        return registry.getEntries();
    }
    
    public int memberCount()
    {
        return registry.size();
    }
    public int activeMemberCount()
    {
        int res = 0;
        for (Member member : registry.getEntries())
        {
            res += member.isActive() ? 1 : 0;
        }
        return res;
    }
    public double totalOutstandingBalance()
    {
        double res = 0;
        for (Member member : registry.getEntries())
        {
            res += member.getTab();
        }
        return res;
    }
    
    public String receiptBasic()
    {
        String res = "\n=== Members ===\n";
        if (registry.size() == 0) res += "No members found.\n";
        
        for (Member entry : registry.getEntries())
        {
            res += String.format("[%d] %s%n", entry.getId(), entry.getName());
        }
        
        return res;
    }
    public String receiptDetailed()
    {
        String res = "\n=== Members ===\n";
        if (registry.size() == 0) res += "No members found.\n";
        
        for (Member entry : registry.getEntries())
        {
            res += entry.toString();
        }
        
        return res;
    }
    
    public Member findMemberById(int memberId)
    {
        return registry.getEntry(memberId);
    }
    public boolean getMemberActiveStatus(int memberId)
    {
        return findMemberById(memberId).isActive();
    }
    
    public DeactivateMemberResult deactivateMember(int memberId)
    {
        Member member = findMemberById(memberId);
        
        if (member == null) return DeactivateMemberResult.NOT_FOUND;
        if (member.isActive() == false) return DeactivateMemberResult.ALREADY_INNACTIVE;
        
        member.Deactivate();
        return DeactivateMemberResult.SUCCESS;
    }
    
    public PaymentResult chargeMember(int memberId, double amount)
    {
        Member member = findMemberById(memberId);
        
        if (member == null) return new PaymentResult(PaymentResult.Status.NOT_FOUND);
        
        member.applyCharge(amount);
        return new PaymentResult(PaymentResult.Status.SUCCESS, amount);
    }
    public PaymentResult chargeMemberWithDiscount(int memberId, double amount)
    {
        Member member = findMemberById(memberId);
        
        if (member == null) return new PaymentResult(PaymentResult.Status.NOT_FOUND);
        
        double discountedAmount = member.getMembershipType().applyFeeMuliplyer(amount);
        
        member.applyCharge(discountedAmount);
        return new PaymentResult(PaymentResult.Status.SUCCESS, amount, discountedAmount);
    }
    
    public PaymentResult applyPaymentFromMember(int memberId, double amount)
    {
        Member member = findMemberById(memberId);
        
        if (member == null) return new PaymentResult(PaymentResult.Status.NOT_FOUND);
        
        member.applyPayment(amount);
        return new PaymentResult(PaymentResult.Status.SUCCESS, amount);
    }
}
