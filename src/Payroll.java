/**
 * @author: adavid
 * @date: 4-5-13
 * 
 * Student class
 */

//package src;



/**
 * Start of class Payroll
 */

public class Payroll
{
   public String emplID;
   public String lastName;
   public String firstName;
   public String laborCharge;
   public String chargePeriod;
   public double amount;
   
   /**
    * Payroll constructor
    * 
    * @param emplID
    * @param lastName
    * @param firstName
    * @param laborCharge
    * @param chargePeriod
    * @param amount
    */
   public Payroll(String lastName, 
	   		  	  String firstName,
	   		  	  String emplID,
		   		  String laborCharge,
		   		  String chargePeriod, 
		   		  double amount)
   {
	   this.emplID = emplID;
	   this.lastName = lastName;
	   this.firstName = firstName;
	   this.laborCharge = laborCharge;
	   this.chargePeriod = chargePeriod;
	   this.amount = amount;
   };
   
   // toString method
   public String toString()
   {
      return 	this.emplID + ", " +
      			this.lastName + ", " +
      			this.firstName + ", " +
      			this.laborCharge + ", " +
      			this.chargePeriod + ", " +
      			this.amount;
   };
   

}//end of class Payroll
