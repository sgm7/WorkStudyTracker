import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.NumberFormat;
import java.util.Locale;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

/**
 * @author David Hersh
 * @author Sky McKinley
 * @version 2013.07.25
 */
public class WorkStudyTrackingUI extends javax.swing.JFrame {

    /**
     * Creates new form WorkStudyTrackingUI
     */
    public WorkStudyTrackingUI() {
        /**
         * Initialize everything that isn't a displayed component.
         * This is done in a separate initialization than the 
         * displayed components for readability.
         */
        ds = new DataFileStream();
        students = ds.readStudents();
        awards = ds.readAwards();
        enrollments = ds.readEnrollments();

        // Sort the disbursements by pay period. This is necessary
        // for proper functionality later, when constructing the
        // editedDisbursements vector.
        disbursements = ds.readDisbursements();
        Collections.sort(disbursements);

        editedDisbursements = new Vector<Disbursement>();
        editedDisbursementsLocation = new Vector<Integer>();

        nextInsertIndex = 0;
        visCount = 0;
        invisCount = 0;
        editClicked = false;
        disbTableColumns = new Vector<String>();
        disbTableColumns.add("Empl ID");
        disbTableColumns.add("Amount");
        disbTableColumns.add("Department");
        disbTableColumns.add("Item Type");
        disbTableColumns.add("Term");
        disbTableColumns.add("Pay Period");
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        JTabbedPane jTabbedPane1 = new JTabbedPane();
        dataEntryTab = new javax.swing.JPanel();
        JLabel hsuIDLabel = new JLabel();
        hsuID = new javax.swing.JComboBox();
        JLabel departmentLabel = new JLabel();
        JLabel itemTypeLabel = new JLabel();
        JLabel amountLabel = new JLabel();
        JLabel periodLabel = new JLabel();
        JLabel termLabel = new JLabel();
        department = new javax.swing.JFormattedTextField();
        amount = new javax.swing.JFormattedTextField();
        period = new javax.swing.JFormattedTextField();
        term = new javax.swing.JFormattedTextField();
        JLabel enrollmentLabel = new JLabel();
        enrollment = new javax.swing.JTextField();
        awardAmount = new javax.swing.JTextField();
        JLabel awardAmountLabel = new JLabel();
        studentName = new javax.swing.JTextField();
        JLabel studentNameLabel = new JLabel();
        disbursementListPane = new javax.swing.JScrollPane();
        editButton = new EnterButton("editButton");
        EnterButton deleteButton = new EnterButton("deleteButton");
        EnterButton exitButton = new EnterButton("exitButton");
        EnterButton submitButton = new EnterButton("submitButton");
        clearButton = new EnterButton("clearButton");
        itemType = new javax.swing.JFormattedTextField();
        JPanel summaryTab = new JPanel();
        summaryScrollPane = new javax.swing.JScrollPane();

        // set up window
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Work Study Tracking");
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setMinimumSize(new java.awt.Dimension(790, 540));

        // set up labels for textfields
        hsuIDLabel.setText("HSU ID");
        departmentLabel.setText("Department");
        amountLabel.setText("Amount");
        periodLabel.setText("Pay Period");
        termLabel.setText("Term");
        studentNameLabel.setText("Student Name");
        itemTypeLabel.setText("Item Type");
        awardAmountLabel.setText("Award Amount");
        enrollmentLabel.setText("Enrollment");

        // Set up the vector to contain the IDs for the JComboBox.
        // The IDs are fetched from the students file. This is used
        // even though we want to end up with an array (as seen below)
        // because we need to add to it dynamically, which is difficult
        // with an array.
        Vector<String> idsForComboBox = new Vector<String>();
        for(Student s : students) {
            idsForComboBox.add(s.getEmplID());
        }
        Collections.sort(idsForComboBox);

        // convert the ids to an array of objects, so it can be 
        // appropriately added to the JComboBox
        Object[] theIDs = idsForComboBox.toArray();
        hsuID.addPopupMenuListener(new MyPopupMenuListener());
        hsuID.addActionListener(new IDBoxAction());
        // Add autocomplete support to the JComboBox using external library
        // GlazedLists.
        AutoCompleteSupport support = AutoCompleteSupport.install(hsuID,
            GlazedLists.eventListOf(theIDs));
        // "setStrict" means that the user can only select from items 
        // in the JComboBox, they can't add new ones.
        support.setStrict(true);

        // set up input validation for editable fields
        try {
            department.setFormatterFactory(
                new javax.swing.text.DefaultFormatterFactory(
                    new javax.swing.text.MaskFormatter("D#####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        amount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory
            (new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.##"))));

        try {
            period.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory
                (new javax.swing.text.MaskFormatter("######")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            term.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory
                (new javax.swing.text.MaskFormatter("####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            itemType.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory
                (new javax.swing.text.MaskFormatter("############")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        // set up uneditable fields
        enrollment.setEditable(false);
        enrollment.setText("");

        awardAmount.setEditable(false);
        awardAmount.setText("");

        studentName.setEditable(false);
        studentName.setText("");

        // set up table
        //disbursementList.setListData(renderDisbursements(disbursements));
        disbursementTable = new JTable(new Vector<Vector>(), disbTableColumns);
        disbursementListPane.setViewportView(disbursementTable);

        // set up button text and actions
        submitButton.setText("Submit");
        submitButton.addActionListener(new SubmitAction());
        clearButton.setText("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                clearInputFields();
            }
        });
        editButton.setText("Edit");
        editButton.addActionListener(new EditAction());
        deleteButton.setText("Delete");
        deleteButton.addActionListener(new DeleteAction());
        exitButton.setText("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        });

        // lay out components
        GroupLayout dataEntryTabLayout =
            new GroupLayout(dataEntryTab);
        dataEntryTab.setLayout(dataEntryTabLayout);
        dataEntryTabLayout.setHorizontalGroup(
            dataEntryTabLayout.createParallelGroup(
                GroupLayout.Alignment.LEADING)
                .addGroup(dataEntryTabLayout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addGroup(dataEntryTabLayout.createParallelGroup(
                        GroupLayout.Alignment.LEADING)
                        .addGroup(dataEntryTabLayout.createSequentialGroup()
                            .addGroup(dataEntryTabLayout.createParallelGroup(
                                GroupLayout.Alignment.LEADING)
                                .addComponent(
                                    editButton,
                                    GroupLayout.PREFERRED_SIZE,
                                    65,
                                    GroupLayout.PREFERRED_SIZE)
                                .addComponent(
                                    deleteButton,
                                    GroupLayout.PREFERRED_SIZE,
                                    65,
                                    GroupLayout.PREFERRED_SIZE)
                                .addComponent(exitButton,
                                    GroupLayout.PREFERRED_SIZE,
                                    65,
                                    GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(
                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(disbursementListPane))
                        .addGroup(dataEntryTabLayout.createSequentialGroup()
                            .addGroup(dataEntryTabLayout.createParallelGroup(
                                GroupLayout.Alignment.TRAILING)
                                .addComponent(periodLabel)
                                .addComponent(termLabel)
                                .addComponent(amountLabel)
                                .addComponent(departmentLabel)
                                .addComponent(hsuIDLabel))
                            .addPreferredGap(
                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(dataEntryTabLayout.createParallelGroup(
                                GroupLayout.Alignment.LEADING)
                                .addComponent(term)
                                .addComponent(amount)
                                .addComponent(period)
                                .addComponent(department)
                                .addComponent(hsuID,
                                    GroupLayout.DEFAULT_SIZE,
                                    161,
                                    Short.MAX_VALUE))
                            .addGap(8, 8, 8)
                            .addGroup(dataEntryTabLayout.createParallelGroup(
                                GroupLayout.Alignment.LEADING)
                                .addGroup(
                                    dataEntryTabLayout.createSequentialGroup()
                                    .addGap(0, 0, Short.MAX_VALUE)
                                    .addComponent(submitButton)
                                    .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(clearButton))
                                .addGroup(
                                    dataEntryTabLayout.createSequentialGroup()
                                    .addGap(34, 34, 34)
                                    .addGroup(dataEntryTabLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(itemTypeLabel, GroupLayout.Alignment.TRAILING)
                                        .addComponent(enrollmentLabel, GroupLayout.Alignment.TRAILING)
                                        .addComponent(awardAmountLabel, GroupLayout.Alignment.TRAILING)
                                        .addComponent(studentNameLabel, GroupLayout.Alignment.TRAILING))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(dataEntryTabLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(awardAmount, GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                                        .addComponent(itemType, GroupLayout.Alignment.TRAILING)
                                        .addComponent(enrollment)
                                        .addComponent(studentName, GroupLayout.Alignment.TRAILING))))))
                    .addGap(25, 25, 25))
        );
        dataEntryTabLayout.setVerticalGroup(
            dataEntryTabLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(dataEntryTabLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(dataEntryTabLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(hsuIDLabel)
                        .addComponent(hsuID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(studentNameLabel)
                        .addComponent(studentName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(dataEntryTabLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(departmentLabel)
                        .addComponent(department, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(itemTypeLabel)
                        .addComponent(itemType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(5, 5, 5)
                    .addGroup(dataEntryTabLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(amountLabel)
                        .addComponent(amount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(awardAmountLabel)
                        .addComponent(awardAmount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(dataEntryTabLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(periodLabel)
                        .addComponent(period, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(enrollmentLabel)
                        .addComponent(enrollment, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(dataEntryTabLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(termLabel)
                        .addComponent(term, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(submitButton)
                        .addComponent(clearButton))
                    .addGap(18, 18, 18)
                    .addGroup(dataEntryTabLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(dataEntryTabLayout.createSequentialGroup()
                            .addComponent(editButton)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(deleteButton)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 25, 10000)
                            .addComponent(exitButton))
                        .addComponent(disbursementListPane, GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))
                    .addGap(25, 25, 25))
        );

        jTabbedPane1.addTab("Data Entry", dataEntryTab);

        // Set up the summary table.
        populateSummaryTable();
        summaryTab.addComponentListener(new SummaryTabSelected());

        GroupLayout summaryTabLayout = new GroupLayout(summaryTab);
        summaryTab.setLayout(summaryTabLayout);
        summaryTabLayout.setHorizontalGroup(
            summaryTabLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(summaryTabLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(summaryScrollPane, GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                    .addContainerGap())
        );
        summaryTabLayout.setVerticalGroup(
            summaryTabLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(summaryTabLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(summaryScrollPane, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                    .addContainerGap())
        );

        jTabbedPane1.addTab("Summary", summaryTab);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1, GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1, GroupLayout.Alignment.TRAILING)
        );

        pack();

        // set up focus traversal policy (tab order)
        Vector<Component> order = new Vector<Component>(11);
        order.add(hsuID);
        order.add(department);
        order.add(itemType);
        order.add(amount);
        order.add(period);
        order.add(term);
        order.add(submitButton);
        order.add(clearButton);
        order.add(editButton);
        order.add(deleteButton);
        order.add(exitButton);
        newPolicy = new MyFocusPolicy(order);
        this.setFocusTraversalPolicy(newPolicy);
    }

    private class IDBoxAction implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            JComboBox cb = (JComboBox) evt.getSource();
            newSelectedItem = cb.getSelectedItem();
            /**
             * Due to some quirks of the library we're using (GlazedLists) to enable
             * autocomplete in the JComboBox, this if statement is part of the
             * checks & balances needed to ensure that the action of the
             * JComboBox is only run when the user acutally selects an
             * ID. The main idea is to run the action when the popup menu closes,
             * because this is the best indicator of a chosen ID. The normal method of
             * checking this (checking if the PopUpListener function
             * popupMenuWillBecomeInvisible is run) isn't sufficient due to
             * the quirks of GlazedLists.
             *
             * First, it checks if the popupMenuWillBecomeVisible action has been
             * run as many times as the popupMenuWillBecomeInvisible action.
             * (This is accomplished by incrementing a counter for each when they are called.)
             * This is to deal with the fact that these are fired at times that are unintuitive
             * on the user end, and not in equal amounts. Then, it checks
             * if the currently selected item is the same as the last selected item.
             * This is to deal with how JComboBox fires actions, and prevents the action
             * from firing every time the user changes their selection.
             *
             * The combination of these checks make the action run only when it's supposed to,
             * in all potential situations (that we could think of, at least)
             */
            if(visCount.equals(invisCount) && oldSelectedItem == newSelectedItem) {
                // populate the table with the disbursements of the selected ID
                populateTable(newSelectedItem.toString());
                // clear the uneditable fields' values
                department.setValue(null);
                amount.setValue(null);
                period.setValue(null);
                term.setValue(null);
                // if the ID has only one disbursement associated with it, autopopulate
                // the fields with its values by running the edit action.
                if (disbursementTable.getRowCount() == 1) {
                    for (ActionListener a : editButton.getActionListeners()) {
                        a.actionPerformed(new ActionEvent(editButton, ActionEvent.ACTION_PERFORMED, null) {
                        });
                    }
                }
                // If the ID selected has no disbursements, or more than 1 disbursements
                // populate the uneditable fields (and itemType) with the data associated with the ID.
                else {
                    setUneditableFields(newSelectedItem.toString(), -1);
                }
            }
            // part of the measures necessary to make the action run at the correct
            // time
            oldSelectedItem = newSelectedItem;
        }
    }

    private class MyPopupMenuListener implements PopupMenuListener {
        public void popupMenuWillBecomeVisible(PopupMenuEvent evt) {
            // keep track of how many times the function is called
            visCount++;
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent evt) {
            JComboBox cb = (JComboBox) evt.getSource();
            newSelectedItem = cb.getSelectedItem();
            // keep track of how many times the function is called
            invisCount++;
            /**
             * Due to a quirk in the library we're using for the autocomplete JComboBox,
             * popupMenuWillBecomeVisible can be called more than
             * popupMenuWillBecomeInvisible. This checks for that issue,
             * and resolves it so the JComboBox action is called only at the
             * correct time.
             */
            if (invisCount < visCount) {
                invisCount = 0;
                visCount = invisCount;
            }
            /**
             * for loop adapted from:
             * http://stackoverflow.com/questions/3079524/how-do-i-manually-invoke-an-action-in-swing
             *
             * Used to run the action of the JComboBox. Messy, but necessary for proper
             * functionality, due to previously mentioned quirks involving the library
             * used for autocompletion functionality in the JComboBox.
             */
            for (ActionListener a : cb.getActionListeners()) {
                a.actionPerformed(new ActionEvent(cb, ActionEvent.ACTION_PERFORMED, null) {
                });
            }
            oldSelectedItem = newSelectedItem;
        }

        public void popupMenuCanceled(PopupMenuEvent evt) {
            // do nothing
        }
    }

    private class SummaryTabSelected implements ComponentListener {
        @Override
        public void componentResized(ComponentEvent componentEvent) {
            // do nothing
        }

        @Override
        public void componentMoved(ComponentEvent componentEvent) {
            // do nothing
        }

        @Override
        public void componentShown(ComponentEvent componentEvent) {
            // called when the summary tab is shown so the summary table
            // will include data added or modified in the current session
            populateSummaryTable();
        }

        @Override
        public void componentHidden(ComponentEvent componentEvent) {
            // do nothing
        }
    }

    private class SubmitAction implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            // if all the fields don't have data in them, throw an error
            // message and exit the function
            if (!validateInput()) {
                JOptionPane.showMessageDialog
                    (dataEntryTab, "You must fill all of the fields before proceeding.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // create a new disbursement with the data in the fields
            Disbursement d = new Disbursement
                (hsuID.getSelectedItem().toString(),
                    Double.parseDouble(amount.getText()),
                    department.getText(), itemType.getText(),
                    term.getText(), period.getText()
                );

            // If the entry is an edited version of a previous entry,
            // remove the original from the vector to allow for the
            // edited version to replace it.
            if (editClicked) {
                disbursements.remove(nextInsertIndex);
            }
            // Add the new/edited disbursement to the master disbursements
            // vector, write the vector to the disbursements.csv file, then
            // repopulate the table with the updated disbursements.
            disbursements.add(nextInsertIndex, d);
            ds.writeDisbursements(disbursements);
            populateTable(hsuID.getSelectedItem().toString());

            // reset fields and variables
            editClicked = false;
            clearInputFields();
        }
    }

    /**
     * Action performed when the Edit button is clicked.
     */
    private class EditAction implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            // Check if a row is selected. If a row isn't selected,
            // throw an error message and exit the function.
            int index;
            if (disbursementTable.getRowCount() != 1) {
                index = rowSelected();
                if (index == -1) {
                    hsuID.requestFocusInWindow();
                    return;
                }
            } else {
                index = 0;
            }
            /**
             * Set the nextInsertIndex (used in placing the disbursement
             * in the correct location in the disbursements vector)
             * using the editedDisbursementsLocation vector, which contains
             * the currently selected ID's disbursement locations in the disbursements vector.
             * The index variable has the value of the current selected row from the table,
             * which corresponds to an index value in the editedDisbursementsLocation
             * vector. This references the location of the selected
             * disbursement in the disbursements vector.
             */
            nextInsertIndex = editedDisbursementsLocation.get(index);
            /**
             * Fetch the correct disbursement, and display its data
             * in the appropriate fields.
             *
             * NOTE: the index variable passed to the setUneditableFields function
             * is used to display the itemType from the selected disbursement,
             * by getting the itemType in the table.
             * This is different from the action when a disbursement ISN'T selected,
             * where the item type is fetched from the awards.csv file.
             */

            Disbursement d = editedDisbursements.get(index);
            setUneditableFields(d.getEmplID(), index);

            department.setValue(d.getDepartment());
            amount.setValue(d.getAmount());
            period.setValue(d.getPayPeriod());
            term.setValue(d.getTerm());
            editClicked = true;

            clearButton.setText("Cancel");
            hsuID.requestFocusInWindow();
        }
    }

    private class DeleteAction implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            // Check if a row is selected. If one isn't selected, throw
            // an error and exit the function.
            int row = rowSelected();
            if (row == -1) {
                hsuID.requestFocusInWindow();
                return;
            }
            // Confirm that delete command is intentional
            int n = JOptionPane.showConfirmDialog
                (dataEntryTab, "Are you sure you want to delete the selected disbursement?",
                    "Delete Confirmation", JOptionPane.YES_NO_OPTION);

            // If they answer yes, delete the disbursement from vector, write the
            // vector to the file (deleting the disbursement in the file),
            // then repopulate the table.
            if (n == 0) {
                row = editedDisbursementsLocation.get(row);
                disbursements.removeElementAt(row);
                ds.writeDisbursements(disbursements);
                populateTable(hsuID.getSelectedItem().toString());
            }
            hsuID.requestFocusInWindow();
        }
    }

    /**
     * Checks if a row is selected. If a row isn't selected,
     * throws an error in a dialog box.
     */
    private Integer rowSelected() {
        int row = disbursementTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog
                (dataEntryTab, "You must select a row to perform this action.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return row;
    }

    /**
     * Makes sure each editable field has data in it. (NOTE: We don't need to validate
     * the data itself, the fields do that automatically.)
     */
    private Boolean validateInput() {
        return department.getValue() != null && itemType.getValue() != null &&
            amount.getValue() != null && period.getValue() != null &&
            term.getValue() != null;
    }

    /**
     * Returns all editable fields to their original state and
     * resets critical variables.
     */
    private void clearInputFields() {
        if (editClicked) {
            int n = JOptionPane.showConfirmDialog
                (dataEntryTab, "Are you sure you want to cancel editing?",
                    "Edit Confirmation", JOptionPane.YES_NO_OPTION);
            if (n == 1) {
                return;
            }
        }
        department.setValue(null);
        amount.setValue(null);
        period.setValue(null);
        term.setValue(null);
        itemType.setValue(null);

        disbursementTable.clearSelection();
        clearButton.setText("Clear");
        editClicked = false;
        nextInsertIndex = 0;
        hsuID.requestFocusInWindow();
    }

    /**
     * Sets up uneditable fields and item type. The index variable
     * is used differently, depending on the call. If it has a value
     * of 0 or greater, it pulls the item type from the table corresponding
     * to the currently selected disbursement. If it has a value of -1,
     * it pulls the item type from the awards.csv vector corresponding
     * to the currently selected ID.
     */
    private void setUneditableFields(String id, int index) {
        Student s;
        Award a;
        Enrollment e;

        /**
         * In each of the for loops, go through the vector of the appropriate
         * construct until the entry with the same ID is found.
         * Then, populate the appropriate fields, and exit the for loop. If an
         * entry with the same ID isn't found, clear the field(s) associated with
         * that construct.
         */
        for(Student student : students) {
            s = student;
            if (s.getEmplID().equals(id)) {
                studentName.setText(s.getName());
                break;
            }
            studentName.setText("");
        }
        for (Award award : awards) {
            a = award;
            if (a.getEmplID().equals(id)) {
                awardAmount.setText(a.getTotal().toString());
                // if called by non-editing action (i.e. ID selected action)
                if (index == -1) {
                    itemType.setValue(a.getItemType());
                }
                break;
            }
            awardAmount.setText("");
            itemType.setValue(null);
        }
        // if called by editing action (i.e. disbursement selected action)
        if (index > -1) {
            itemType.setValue(disbursementTable.getValueAt(index, 3).toString());
        }
        for(Enrollment enrollment1 : enrollments) {
            e = enrollment1;
            if (e.getEmplID().equals(id)) {
                enrollment.setText(String.valueOf(e.getUnits()));
                break;
            }
            enrollment.setText("");
        }
    }

    /**
     * Sets up the data for the Summary table.
     */
    private void populateSummaryTable() {
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
        summaryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        summaryScrollPane.setViewportView(summaryTable);
    }


    /**
     * Sets up the table
     */
    private void populateTable(String id) {
        // Create the table data source vector.
        Vector<Vector<String>> disbTableRows = new Vector<Vector<String>>();

        /**
         * We're going to iterate over all the disbursements looking for ones
         * which match the selected ID. We're using ListIterator to have the ability to
         * get the indexes of the disbursements to be put into the
         * editedDisbursementsLocation vector.
         */
        ListIterator<Disbursement> disbursementIterator = disbursements.listIterator();
        Disbursement d;

        // empty the editedDisbursements and editedDisbursementsLocation vectors,
        // leaving them ready for new data.

        editedDisbursements.clear();
        editedDisbursementsLocation.clear();

        /**
         * Cycle through all disbursements looking for ones matching the selected
         * ID.  When one is found, add it to the list for display later. Store its
         * location in the disbursements vector (which is the same as its location
         * in the disbursements.csv file) for ease of reference later.
         */
        while (disbursementIterator.hasNext()) {
            d = disbursementIterator.next();
            if (id.equals(d.getEmplID())) {
                editedDisbursements.add(d);
                editedDisbursementsLocation.add(disbursementIterator.previousIndex());
            }
        }

        // Add the disbursements to the data vector for display in the
        // table.
        disbursementIterator = editedDisbursements.listIterator();
        while (disbursementIterator.hasNext()) {
            disbTableRows.add(disbursementIterator.next().toVector());
        }

        // Create and display disbursement table. Set the cells as uneditable
        // and let only one row be selected at a time.
        disbursementTable = new JTable(disbTableRows, disbTableColumns) {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        disbursementTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        disbursementListPane.setViewportView(disbursementTable);
    }

    /*
    private Vector<String> renderDisbursements(Vector<Disbursement> dv) {
        Vector<String> renderedDisbursements = new Vector<String>();
        Iterator<Disbursement> disbursementIterator = dv.iterator();
        Disbursement d;

        while (disbursementIterator.hasNext()) {
            d = disbursementIterator.next();
            renderedDisbursements.add(
                String.format("%1$-10s", d.getEmplID()) +
                    String.format("$%1$-7s", d.getAmount()) +
                    String.format("%1$-7s", d.getDepartment()) +
                    String.format("%1$-13s", d.getItemType()) +
                    String.format("%1$-5s", d.getTerm()) +
                    String.format("%1$-7s", d.getPayPeriod())
            );
        }

        return renderedDisbursements;
    }
    */

    /**
     * Adapted from
     * http://docs.oracle.com/javase/tutorial/uiswing/examples/misc/FocusTraversalDemoProject/src/misc/FocusTraversalDemo.java
     * <p/>
     * Set the tab order so the user can navigate the program
     * easily without the mouse.
     */
    public static class MyFocusPolicy extends FocusTraversalPolicy {
        Vector<Component> order;

        public MyFocusPolicy(Vector<Component> order) {
            this.order = new Vector<Component>(order.size());
            this.order.addAll(order);
        }

        public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
            int i = (order.indexOf(aComponent) + 1) % order.size();
            return order.get(i);
        }

        public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
            int i = order.indexOf(aComponent) - 1;
            if (i < 0) {
                i = order.size() - 1;
            }
            return order.get(i);
        }

        public Component getDefaultComponent(Container focusCycleRoot) {
            return order.get(0);
        }

        public Component getLastComponent(Container focusCycleRoot) {
            return order.lastElement();
        }

        public Component getFirstComponent(Container focusCycleRoot) {
            return order.get(0);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WorkStudyTrackingUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WorkStudyTrackingUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WorkStudyTrackingUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WorkStudyTrackingUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WorkStudyTrackingUI().setVisible(true);
            }
        });
    }

    // Variables declaration
    private javax.swing.JFormattedTextField amount;
    private javax.swing.JTextField awardAmount;
    private EnterButton clearButton;
    private javax.swing.JPanel dataEntryTab;
    private javax.swing.JFormattedTextField department;
    private javax.swing.JTable disbursementTable;
    private javax.swing.JScrollPane disbursementListPane;
    private EnterButton editButton;
    private javax.swing.JTextField enrollment;
    private javax.swing.JComboBox hsuID;
    private javax.swing.JFormattedTextField itemType;
    private javax.swing.JFormattedTextField period;
    private javax.swing.JTextField studentName;
    private javax.swing.JFormattedTextField term;
    private Vector<Disbursement> disbursements;
    private Vector<Student> students;
    private Vector<Award> awards;
    private Vector<Enrollment> enrollments;
    private DisbursementStream ds;
    private int nextInsertIndex;
    private Boolean editClicked;
    static MyFocusPolicy newPolicy;
    private javax.swing.JScrollPane summaryScrollPane;
    private Integer visCount;
    private Integer invisCount;
    private Object oldSelectedItem;
    private Object newSelectedItem;
    private Vector<Disbursement> editedDisbursements;
    private Vector<String> disbTableColumns;
    private Vector<Integer> editedDisbursementsLocation;
}