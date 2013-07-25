/**
 * User: lrobin
 * Date: 3/27/13
 * Time: 11:07 AM
 * singleton that manages preferences etc
 */

import java.util.Properties;
import java.io.*;


/**
 * Manages preferences and.. that's all, since the db is gone. yay hours of pointless unusable work.
 */

public class GlobalsManager
{
    private static volatile GlobalsManager instance = new GlobalsManager();
    private static Properties configFile = new Properties();
    
    //    private Vector<Disbursement> disbursements;

    private GlobalsManager()
    {
    }
    
    /**
     * Returns the GlobalsManager since it's a singleton. 
     */

    public static GlobalsManager getInstance()
    {
        return instance;
    }

    /**
     * Convenience method that returns a property from the config. Needs to be padded out to throw an error if the config isn't loaded. 
     */

    public String getProperty(String name)
    {
        return configFile.getProperty(name);
    }

    /**
     * Attempts to load the Properties-formatted settings file from the path specified..
     */

    public void loadSettings(String settings_path)
    {
        try
        {
            configFile.load(new FileReader(settings_path));
        }
        catch (IOException ex)
        {
            throw new RuntimeException("Can't load settings!", ex);
        }
    }
    
    
    /**
     *  resets the class
     */

    public void reset()
    {
        synchronized (GlobalsManager .class)
        {
            instance = new GlobalsManager();
        }
    }
}
