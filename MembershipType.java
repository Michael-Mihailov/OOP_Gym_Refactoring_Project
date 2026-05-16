
public enum MembershipType implements NameableEnum
{
    STUDENT("student", 0.5),
    FACULTY("faculty", 0.75),
    COMMUNITY("community", 1.0);
    
    private String name;
    private double feeMultiplyer;
    
    MembershipType(String name, double feeMultiplyer)
    {
        this.name = name;
        this.feeMultiplyer = feeMultiplyer;
    }
    
    @Override
    public String getDisplayName()
    {
        return name;
    }
    
    public double applyFeeMuliplyer(double fee)
    {
        return fee * feeMultiplyer;
    }
}
