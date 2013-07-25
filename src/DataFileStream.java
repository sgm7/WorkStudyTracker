import org.apache.commons.csv.*;
import java.io.*;
import java.util.*;

/**
 * Manages reading/writing data between CSV files and the application.
 */

public class DataFileStream implements DisbursementStream
{
    private File disbursementFile;
    private File awardFile;
    private File enrollmentFile;
    private File studentFile;
    private FileReader disbursementFileReader;
    private FileReader genericFileReader;
    public Boolean hasHeader;

    /**
     * Constructor for DisbursementFileStream.
     */
    public DataFileStream()
    {
        try
        {
            // The file names are in a properties file for easy modification
            // later.
            Properties properties = new Properties();
            properties.load(new FileInputStream("config.properties"));

            // Read in the file names, as well as whether the files have a
            // header row or not.
            this.disbursementFile =
                    new File(properties.getProperty("disbursementsfile"));
            this.awardFile =
                    new File(properties.getProperty("awardsfile"));
            this.enrollmentFile =
                    new File(properties.getProperty("enrollmentsfile"));
            this.studentFile =
                    new File(properties.getProperty("studentsfile"));
            this.hasHeader =
                    "true".equals(properties.getProperty("hasheader"));
        }
        catch(Exception e)
        {
            System.err.println("Could not load properties file.");
            System.exit(1);
        }
    }
    
    /**
     * Method for reading all disbursements from the disbursement file.  Parses
     * the CSV file and puts the records into a Vector which is then returned.
     *
     * @return dv Vector of Disbursement records.
     */
    public Vector<Disbursement> readDisbursements()
    {
        Vector<Disbursement> dv = new Vector<Disbursement>();

        // Open the disbursements file for reading.
        try{
            this.disbursementFileReader = new FileReader(this.disbursementFile);
        }
        catch(FileNotFoundException e)
        {
            System.err.println("Could not open file " +
                    this.disbursementFile.getName());
        }
        catch(Exception e)
        {
            System.err.println("An unspecified error occurred when opening" +
                    this.disbursementFile.getName());
        }

        /**
         * Parse the disbursements file.
         */

        // Create an empty parser in case we have problems creating one below.
        Iterable<CSVRecord> parser = new Iterable<CSVRecord>() {
            @Override
            public Iterator<CSVRecord> iterator() {
                return null;
            }
        };

        // Try and parse the disbursements file.
        try
        {
            parser = CSVFormat.EXCEL.parse(this.disbursementFileReader);
            // Skip the header row if we have one.
            if(this.hasHeader)
            {
                parser.iterator().next();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.err.println(e);
        }

        //
        //Iterate through the parser, adding each record. If a record can't be
        //parsed, it's going to be removed from the file.
        //
        for (CSVRecord record : parser) {
            try
            {
                Disbursement d = new Disbursement(
                        record.get(0),
                        Double.parseDouble(record.get(1)),
                        record.get(2),
                        record.get(3),
                        record.get(4),
                        record.get(5)
                );

                dv.add(d);
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                // If we can't read the record, print an error message to err.
                System.err.println("Malformed record read.");
                e.printStackTrace(System.err);
            }
        }
        return dv;
    }

    /**
     * Method for writing disbursement records to the data file.
     *
     * @param v Vector of Disbursements to write to the data file.
     */
    public void writeDisbursements(Vector<Disbursement> v)
    {
        // Create an empty string which is to be written to the disbursements
        // file.
        String disbursementsString = "";

        // Add a header if we are using one.
        if(this.hasHeader)
        {
            disbursementsString =
                "EMPLID,AMOUNT,DEPT,ITEMTYPE,TERM,PERIOD\n";
        }

        // Append each of the disbursements in the vector using their toString
        // method.

        for (Disbursement aV : v) {
            disbursementsString = disbursementsString.concat(aV.toString());
            disbursementsString += "\n";
        }

        // Try to open the disbursements file for writing.
        try
        {
            FileWriter disbursementFileWriter =
                    new FileWriter(this.disbursementFile);
            disbursementFileWriter.append(disbursementsString);
            disbursementFileWriter.flush();
            disbursementFileWriter.close();
        }
        catch(IOException e)
        {
            System.err.println("The file cannot be written to: " +
                    this.disbursementFile.getName());
        }
        catch(Exception e)
        {
            System.err.println("An unspecified error occurred.");
            e.printStackTrace(System.err);
        }
    }
    
    /**
     * Method for reading data from Student CSV file.  Parses
     * the CSV file and puts the records into a Vector which is then returned.
     *
     * @return ds Vector of Student records.
     */
    public Vector<Student> readStudents()
    {
        Vector<Student> sv = new Vector<Student>();

        // Open the students file for reading.
        try{
            this.genericFileReader = new FileReader(this.studentFile);
        }
        catch(FileNotFoundException e)
        {
            System.err.println("Could not open file " +
                    this.studentFile.getName());
        }
        catch(Exception e)
        {
            System.err.println("An unspecified error occurred when opening" +
                    this.studentFile.getName());
        }

        /**
         * Parse the students file.
         */

        // Create an empty parser in case we have problems creating one below.
        Iterable<CSVRecord> parser = new Iterable<CSVRecord>() {
            @Override
            public Iterator<CSVRecord> iterator() {
                return null;
            }
        };

        // Try and parse the students file.
        try
        {
            parser = CSVFormat.EXCEL.parse(this.genericFileReader);
            // Skip the header row if we have one.
            if(this.hasHeader)
            {
                parser.iterator().next();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.err.println(e);
        }

        //
        //Iterate through the parser, adding each record. If a record can't be
        //parsed, it's going to be removed from the file.
        //
        for (CSVRecord record : parser) {
            try
            {
              String name = record.get(2) + " " + record.get(1);
                Student s = new Student(
                        record.get(0),
                        name
                );

                sv.add(s);
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                // If we can't read the record, print an error message to err
                System.err.println("Malformed record read.");
                e.printStackTrace(System.err);
            }
        }
        return sv;
    }
    
    /**
     * Method for reading data from Award CSV file.  Parses
     * the CSV file and puts the records into a Vector which is then returned.
     *
     * @return av Vector of Award records.
     */
    public Vector<Award> readAwards()
    {
        Vector<Award> av = new Vector<Award>();

        // Open the awards file for reading.
        try{
            this.genericFileReader = new FileReader(this.awardFile);
        }
        catch(FileNotFoundException e)
        {
            System.err.println("Could not open file " +
                    this.awardFile.getName());
        }
        catch(Exception e)
        {
            System.err.println("An unspecified error occurred when opening" +
                    this.awardFile.getName());
        }

        /**
         * Parse the awards file.
         */

        // Create an empty parser in case we have problems creating one below.
        Iterable<CSVRecord> parser = new Iterable<CSVRecord>() {
            @Override
            public Iterator<CSVRecord> iterator() {
                return null;
            }
        };

        // Try and parse the awards file.
        try
        {
            parser = CSVFormat.EXCEL.parse(this.genericFileReader);
            // Skip the header row if we have one.
            if(this.hasHeader)
            {
                parser.iterator().next();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.err.println(e);
        }

        //
        //Iterate through the parser, adding each record. If a record can't be
        //parsed, it's going to be removed from the file.
        //
        for (CSVRecord record : parser) {
            try
            {
                Award a = new Award(
                        record.get(0),
                        record.get(1),
                        Double.parseDouble(record.get(3))
                );

                av.add(a);
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                // If we can't read the record, print an error message to err
                System.err.println("Malformed record read.");
                e.printStackTrace(System.err);
            }
        }
        return av;
    }
    
    /**
     * Method for reading data from Enrollment CSV file.  Parses
     * the CSV file and puts the records into a Vector which is then returned.
     *
     * @return ev Vector of Enrollment records.
     */
    public Vector<Enrollment> readEnrollments()
    {
        Vector<Enrollment> ev = new Vector<Enrollment>();

        // Open the enrollments file for reading.
        try{
            this.genericFileReader = new FileReader(this.enrollmentFile);
        }
        catch(FileNotFoundException e)
        {
            System.err.println("Could not open file " +
                    this.enrollmentFile.getName());
        }
        catch(Exception e)
        {
            System.err.println("An unspecified error occurred when opening" +
                    this.enrollmentFile.getName());
        }

        /**
         * Parse the enrollments file.
         */

        // Create an empty parser in case we heve problems creating one below.
        Iterable<CSVRecord> parser = new Iterable<CSVRecord>() {
            @Override
            public Iterator<CSVRecord> iterator() {
                return null;
            }
        };

        // Try and parse the enrollments file.
        try
        {
            parser = CSVFormat.EXCEL.parse(this.genericFileReader);
            // Skip the header row if we heve one.
            if(this.hasHeader)
            {
                parser.iterator().next();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.err.println(e);
        }

        //
        //Iterate through the parser, adding each record. If a record can't be
        //parsed, it's going to be removed from the file.
        //
        for (CSVRecord record : parser) {
            try
            {
                Enrollment e = new Enrollment(
                        record.get(0),
                        Double.valueOf(record.get(2)),
                        "TERM"
                );

                ev.add(e);
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                // If we can't read the record, print an error message to err
                System.err.println("Malformed record read.");
                e.printStackTrace(System.err);
            }
        }
        return ev;
    }
}
