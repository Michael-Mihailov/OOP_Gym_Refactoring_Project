// member: { id (Integer), name (String), membership_type (String), active (Boolean), balance (Double) }

public class Member implements Identifiable
{
    private int id;
    private String name;
    
    private MembershipType membershipType;
    private boolean active;
    private double tab; // how much the member owes (I'm not sure why it is named balance in the original code)

    public Member(int id, String name, MembershipType membershipType)
    {
        this.id = id;
        this.name = name;
        this.membershipType = membershipType;
        
        this.active = true;
        this.tab = 0.0;
    }
    
    public void applyCharge(double amount)
    {
        tab += amount;
    }
    public void applyPayment(double amount)
    {
        tab -= amount;
    }
    
    public boolean isActive()
    {
        return active;
    }
    public void Activate()
    {
        active = true;
    }
    public void Deactivate()
    {
        active = false;
    }
    
    public MembershipType getMembershipType()
    {
        return membershipType;
    }
    public double getTab()
    {
        return tab;
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
        String type = membershipType.getDisplayName();
        String status = active ? "Active" : "Inactive";
        return String.format("[%d] %s (%s, %s) Balance: $%.2f%n", id, name, type, status, tab);
    }
}