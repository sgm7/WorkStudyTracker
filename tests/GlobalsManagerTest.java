/**
 * User: lrobin
 * Date: 3/27/13
 * Time: 11:09 AM
 * tests for settings manager
 */

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class GlobalsManagerTest 
{
	  @BeforeClass
	  public static void testSetup() 
	  {
	 	  GlobalsManager.getInstance().reset();
	      GlobalsManager.getInstance().loadSettings(System.getProperty("user.dir") + "/config.properties");
 	  }

	  @Test(expected = RuntimeException.class)
	  public void loadBadSettings() 
	  {
	       GlobalsManager.getInstance().loadSettings("piss");
	  }

	  @Test
	  public void readSettings() 
	  {
	       GlobalsManager.getInstance().loadSettings("Test.properties");
	       assertEquals("number should be 4",4,Integer.parseInt(GlobalsManager.getInstance().getProperty("number")));
	       assertEquals("scott should be boring","scott please stop rambling about hanoi",GlobalsManager.getInstance().getProperty("algorithms"));
	       assertEquals("null should be null",null,GlobalsManager.getInstance().getProperty("ffff"));
	  }
	  
}
