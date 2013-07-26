/**
 * @author: adavid
 * @date: 4-5-13
 * 
 * Enrollment class
 */

//package src;


/**
 * Start of class Enrollment
 */

public class Enrollment
{
    private String emplID;
    private double units;
    private String term;
   
   /**
    * Enrollment constructor
    * 
    * @param emplID The HSU ID of the student
    * @param units The number of units the student is enrolled in for the term.
    * @param term The term in question.
    */
   public Enrollment(String emplID,
                  double units,
                  String term)
   {
        this.emplID = emplID;
        this.units = units;
        this.term = term;
   }

    /*
    Empty constructor.
     */
    public Enrollment()
    {
        this.emplID = "";
        this.units = 0.0;
        this.term = "";
    }
   
   public String getEmplID() {
     return emplID;
   }
   
   public double getUnits() {
     return units;
   }

    public String getTerm()
    {
        return this.term;
    }
   
   // toString method
   public String toString()
   {
      return this.emplID + ", " +
             String.valueOf(this.units) + ", "+
             this.term;
   }
}//end of class Enrollment