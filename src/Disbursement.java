import java.util.Vector;

/**
 * Disbursement class.  Represents a single payment for a  single student
 * from a single department.
 */

public class Disbursement implements Comparable
{
    public String emplID;
    public Double amount;
    public String department;
    public String itemType;
    public String term;
    public String payPeriod;
//    public long db_id;

    /**
     *  Getters are defined here but we haven't had cause to use them yet.
     * Feel free to uncomment when necessary.
     */

//    public void setEmplID(String emplID) {
//        this.emplID = emplID;
//    }

//    public void setAmount(Double amount) {
//        this.amount = amount;
//    }

//    public void setDepartment(String department) {
//        this.department = department;
//    }

//    public void setItemType(String itemType) {
//        this.itemType = itemType;
//    }

//    public void setTerm(String term) {
//        this.term = term;
//    }

//    public void setPayPeriod(String payPeriod) {
//        this.payPeriod = payPeriod;
//    }

    public Double getAmount() {
        return amount;
    }

    public String getDepartment() {
        return department;
    }

    public String getItemType() {
        return itemType;
    }

    public String getTerm() {
        return term;
    }

    public String getPayPeriod() {
        return payPeriod;
    }

    public String getEmplID() {
        return emplID;
    }

    /**
     * Constructor for Disbursement class.
     *
     * @param emplID        The emplID for the student.
     * @param amount        The disbursement amount.
     * @param department    The employing department's code.
     * @param itemType      The award item type.
     * @param term          The term during which the earning occurred.
     * @param payPeriod     The pay period during which the earning occurred.
     */
    public Disbursement(String emplID,
                        Double amount,
                        String department,
                        String itemType,
                        String term,
                        String payPeriod)
    {
        this.emplID = emplID;
        this.amount = amount;
        this.department = department;
        this.itemType = itemType;
        this.term = term;
        this.payPeriod = payPeriod;
    }

    /**
     * Empty constructor for Disbursement class.
     */
//    public Disbursement()
//    {
//        this("", 0.0, "", "", "", "");
//    }


    /**
     * A proper equals method for Disbursements.
     *
     * @param o Object to be compared.
     * @return boolean
     */
    @Override
    public boolean equals(Object o)
    {
        boolean equal;

        equal = (o instanceof Disbursement);

        try
        {
            // Cast o to a Disbursement.
            //noinspection ConstantConditions
            Disbursement d = (Disbursement) o;

            // Check if all the fields are equal.
            equal = (this.emplID.equals(d.getEmplID()));
            equal = (this.amount.equals(d.getAmount()));
            equal = (this.term.equals(d.getTerm()));
            equal = (this.payPeriod.equals(d.getPayPeriod()));
            equal = (this.department.equals(d.getDepartment()));
            equal = (this.itemType.equals(d.getItemType()));
        }
        catch(ClassCastException e)
        {
            return equal;
        }

        return equal;
    }


    /**
     * Create a String representation of a disbursement.  Used to write
     * disbursements to the CSV file.
     * @return String
     */
    public String toString()
    {
         return this.emplID + "," +
                this.amount + "," +
                this.department + "," +
                this.itemType + "," +
                this.term + "," +
                this.payPeriod;
    }

    public Vector<String> toVector()
    {
        Vector<String> disbursementVector= new Vector<String>();

        disbursementVector.add(this.emplID);
        disbursementVector.add(String.format("$%1$-6.2f",this.amount));
        disbursementVector.add(this.department);
        disbursementVector.add(this.itemType);
        disbursementVector.add(this.term);
        disbursementVector.add(this.payPeriod);

        return disbursementVector;
    }

  /**
   * A comparison method for disbursements.
   * @param o The disbursement to be compared to.
   * @return int
   */
  @Override
  public int compareTo(Object o) {
    return this.getPayPeriod().compareTo(((Disbursement) o).getPayPeriod());
  }
}