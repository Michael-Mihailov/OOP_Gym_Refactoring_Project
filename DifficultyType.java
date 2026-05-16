public enum DifficultyType implements NameableEnum
{
    BEGINNER("beginner"),
    INTERMEDIATE("intermediate"),
    ADVANCED("advanced");
    
    private String name;
    
    DifficultyType(String name)
    {
        this.name = name;
    }
    
    @Override
    public String getDisplayName()
    {
        return name;
    }
}