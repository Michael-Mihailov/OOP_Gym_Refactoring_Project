public class MenuOption
{
    private String lable;
    private Runnable action;
    private boolean exitsMenu;

    public MenuOption(String lable, Runnable action, boolean exitsMenu)
    {
        this.lable = lable;
        this.action = action;
        this.exitsMenu = exitsMenu;
    }
    public MenuOption(String lable, Runnable action)
    {
        this.lable = lable;
        this.action = action;
        
        this.exitsMenu = false;
    } 
    
    public String getLable()
    {
        return lable;
    }
    
    public boolean exitsMenu()
    {
        return exitsMenu;
    }
    
    public void run()
    {
        action.run();
    }
}