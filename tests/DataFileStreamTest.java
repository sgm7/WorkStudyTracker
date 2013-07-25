import org.junit.*;
import java.util.Vector;

/**
 *  Test class for the DisbursementFileStream class.
 */
public class DataFileStreamTest {

    protected Vector<Disbursement> currentDisbursements;
    protected DisbursementStream ds;
    protected Disbursement testDisbursement;

    @Before
    public void setUp()
    {
        // Create the connection to the file and read in the current
        // disbursement records.
        ds = new DataFileStream();
        currentDisbursements = ds.readDisbursements();
        testDisbursement = new Disbursement(
                "010000001",21.95,"D10005","000000130000","2132","201302"
        );
    }

    @After
    public void tearDown()
    {
        // Re-set the disbursements to what they were before we ran the tests.
        ds.writeDisbursements(currentDisbursements);
    }

    @Test
    public void testReadDisbursements()
    {
        ds = new DataFileStream();
        Vector<Disbursement> dv = new Vector<Disbursement>();

        // Clear out the disbursements file.
        ds.writeDisbursements(dv);

        // Write a single disbursement to the file.
        dv.add(testDisbursement);
        ds.writeDisbursements(dv);

        // Does the read method work? We should read our test disbursement only.
        org.junit.Assert.assertEquals(dv, ds.readDisbursements());
        org.junit.Assert.assertEquals(testDisbursement,
                ds.readDisbursements().get(0));
    }

    @Test
    public void testWriteDisbursements()
    {
        int oldSize;
        int newSize;

        Vector<Disbursement> dv = new Vector<Disbursement>();

        oldSize = dv.size();

        dv.add(testDisbursement);

        ds.writeDisbursements(dv);

        dv = ds.readDisbursements();
        newSize = dv.size();

        // Is our new file at least the correct size?
        org.junit.Assert.assertEquals(oldSize + 1, newSize);
    }

}
