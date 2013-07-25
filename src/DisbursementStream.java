import java.util.Vector;

/**
 * User: skymckinley
 * Date: 4/15/13
 * Time: 8:22 PM
 */
public interface DisbursementStream {
    public Vector<Disbursement> readDisbursements();
    public void writeDisbursements(Vector<Disbursement> v);
    public Vector<Student> readStudents();
    public Vector<Award> readAwards();
    public Vector<Enrollment> readEnrollments();
}
