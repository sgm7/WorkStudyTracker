/**
 * @author: adavid
 * @date: 4-5-13
 * 
 * Award class
 */

//package src;



/**
 * Start of class Award
 */

public class Award
{
   public String emplID;
   public String itemType;
   public Double total;
   
   /**
    * Award constructor
    * 
    * @param emplID
    * @param itemType
    * @param total
    */
   public Award(String emplID, String itemType,
                  Double total)
   {
      this.emplID = emplID;
      this.itemType = itemType;
      this.total = total;
   }
   
   public String getEmplID() {
     return emplID;
   }
   
   public String getItemType() {
     return itemType;
   }
   
   public Double getTotal() {
     return total;
   }
   
   // toString method
   public String toString()
   {
      return this.emplID.toString() + ", " +
             this.itemType.toString() + ", " +
             this.total.toString();
   }
}//end of class Award