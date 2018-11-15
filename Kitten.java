/**
 * Implements a "Kitten" that can receive and send messages about
 * its possessions and give extra items that it doesn't need to others.
 * The Kitten's objective is to collect at least one of each item.
 */

import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;

public class Kitten extends Actor
{
  private static String[] items = {"left glove", "right glove", "hat"};

  private Collection<String> myPossessions;
  private boolean allSetFlag; // = false;

  public Kitten(String name)
  {
    super(name);
  }

  public Kitten(String name, Collection<String> possessions)
  {
    super(name);
    myPossessions = new ArrayList<String>();
    myPossessions.addAll(possessions);
  }

  /**
   * Called by someone else to give an item to this Kitten.
   * Checks whether this Kitten still needs the item.  If so,
   * sends a thank-you message to giver and returns true;
   * otherwise returns false
   */
  public boolean receiveItem(Kitten giver, String item)
  {
    if (!myPossessions.contains(item))
    {
      myPossessions.add(item);
      send(giver, "thx for the " + item);
      return true;
    }
    else
      return false;
  }

  /**
   * 1. Checks possessions for this Kitten and sends a "need <item>"
   *    message to the list for each missing item.
   * 2. Removes and processes messages from the mailbox,
   *    one by one.
   * Processing messages:
   *     i)  Takes action only for messages with the text
   *         "need <item>", "have <item>", or "ship <item>".
   *         where <item> is the name of the item, such as "hat",
   *         "left glove", or "right glove".
   *         Skips all other messages.
   *    ii)  If this is a "need" message and this Kitten has an extra item
   *         requested, it responds with a "have <item>" message.
   *   iii) If this is a "have" message and this Kitten is missing the offered item,
   *        it responds with a "ship <item>" message.
   *    iv)  If this is a "ship" message and this Kitten still has an extra item,
   *         then it calls sender's receiveItem method.  If receiveItem
   *         returns true, removes item from this Kitten's possessions.
   * 3. If allSetFlag is not set and this Kitten is all set, that is has
   *    one of each item, sends "thx, all set" to the list.
   */
  public void readMail()
  {
    for(String item : items){
    	if(!myPossessions.contains(item)){
    		announce("need " + item);
    	}
    }
    while(!mailbox.isEmpty()){
    	Message m = mailbox.remove();
    	if(m.getText().length() < 5){
    		continue;
    	}
    	String type = m.getText().substring(0, 4);
    	String thing = m.getText().substring(5);
    	if(type.equals("need")){
    		int count = 0;
    		for(String item : myPossessions){
    			if(item.equals(thing)){
    				count++;
    				if(count > 1){
    					send(m.getSender(), "have " + thing);
    					break;
    				}
    			}
    		}
    	}
    	else if(type.equals("have") && !myPossessions.contains(thing)){
    		send(m.getSender(), "ship " + thing);
    	}
    	else if(type.equals("ship")){
    		int count = 0;
    		for(String item : myPossessions){
    			if(item.equals(thing)){
    				count++;
    				if(count > 1){
    					if(((Kitten)m.getSender()).receiveItem(this, thing)){
    						myPossessions.remove(thing);
    					}
    					break;
    				}
    			}
    		}
    	}
    	if(allSet()){
    		mailServer.add(new Message(this, null, "thx, all set"));
    	}
    }
  }

  public String toString()
  {
    return super.toString() + " " + myPossessions;
  }

  //*************************************************************

  /**
   * Returns the number of times item occurs in myPossessions
   */
  private int countPossessions(String item)
  {
    int count = 0;

    for (String str : myPossessions)
      if (item.equals(str))
        count++;

    return count;
  }

  /**
   * Checks whether this Kitten has one of each items
   */
  private boolean allSet()
  {
    for (String item : items)
    {
      if (countPossessions(item) != 1)
        return false;
    }
    allSetFlag = true;
    return true;
  }
}

