import java.util.*;

public class Menu
{
    private String title;
    private List<MenuOption> optionsList;
    
    public Menu(String title, List<MenuOption> optionsList)
    {
        this.title = title;
        this.optionsList = optionsList;
    }
    
    @Override 
    public String toString()
    {
        String res = "\n=== " + title + " ===";
        
        for (int i = 0; i < optionsList.size(); i++)
        {
            res += "\n";
            res += (i + 1) + ". ";
            res += optionsList.get(i).getLable();
        }
        
        return res;
    }
    
    public int numChoices()
    {
        return optionsList.size();
    }
    
    public boolean exitsMenu(int optionNumber)
    {
        optionNumber -= 1; // List is zero indexed
        return optionsList.get(optionNumber).exitsMenu();
    }
    
    public void runOption(int optionNumber)
    {
        optionNumber -= 1; // List is zero indexed
        MenuOption option = optionsList.get(optionNumber);
        option.run();
    }
}