import javax.swing.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

/**
 * User: skymckinley
 * Date: 7/26/13
 * Time: 9:39 AM
 */
public class DisbursementSummaryTable extends JTable
{
    public DisbursementSummaryTable(
        Vector<Disbursement> disbursements,
        Vector<Student> students,
        Vector<Award> awards)
    {
        // Set the column names for the disbursement table.
        Vector<String> summaryTableColumns = new Vector<String>();
        summaryTableColumns.add("Type");
        summaryTableColumns.add("ID");
        summaryTableColumns.add("Name");
        summaryTableColumns.add("# Disbursements");
        summaryTableColumns.add("Total");

        // Set row data.
        Vector<Vector<String>> summaryTableRows = new Vector<Vector<String>>();

        Iterator<Student> studentIterator;
        Student s;

        Vector<String> summary;
        ArrayList<String> disbursementTypes = new ArrayList<String>();

        Iterator<Disbursement> disbursementIterator;
        Iterator<String> disbTypeIterator;

        Disbursement d;
        int num_disp;
        double sum;
        String type;

        disbursementIterator = disbursements.iterator();
        while (disbursementIterator.hasNext()) {
            d = disbursementIterator.next();
            if (!disbursementTypes.contains(d.itemType))
                disbursementTypes.add(d.itemType);
        }

        disbTypeIterator = disbursementTypes.iterator();
        while (disbTypeIterator.hasNext()) {
            studentIterator = students.iterator();
            type = disbTypeIterator.next();
            while (studentIterator.hasNext()) {
                s = studentIterator.next();

                num_disp = 0;
                sum = 0;

                summary = new Vector<String>();

                disbursementIterator = disbursements.iterator();
                while (disbursementIterator.hasNext()) {
                    d = disbursementIterator.next();
                    if (s.emplID.equals(d.getEmplID()) && d.itemType.equals(type)) {
                        num_disp++;
                        sum += d.amount;
                    }
                }

                summary.add(type);
                summary.add(String.format("%09d", Integer.parseInt(s.emplID)));
                summary.add(s.name);
                summary.add(Integer.toString(num_disp));
                summary.add(NumberFormat.getCurrencyInstance(Locale.US).format(sum));

                if (num_disp != 0)
                    summaryTableRows.add(summary);
            }
        }

        // Create and display disbursement table. Set the cells as uneditable
        // and let only one row be selected at a time.
        JTable summaryTable = new JTable(summaryTableRows, summaryTableColumns) {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
