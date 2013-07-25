/**
 * @author: adavid
 * @date: 4-5-13
 * 
 * Student class
 */

//package src;

/**
 * Start of class Student
 */

public class Student
{
   public String emplID;
   public String name;
   
   /**
    * Student constructor
    * 
    * @param emplID
    * @param name
    */
   public Student(String emplID,
                  String name)
   {
      this.emplID = emplID;
      this.name = name;
   }
   
   public String getEmplID() {
     return emplID;
   }
   
   public String getName() {
     return name;
   }
   
   // toString method
   public String toString()
   {
      return this.emplID + ", " +
             this.name;
   }
}//end of class Student