 private static String[] tableSchema =  { "CREATE TABLE \"Award\" ( \"student_id\" INTEGER REFERENCES \"Student\"(\"id\"), \"type\" INTEGER, \"year\" INTEGER, \"total\" REAL, \"award_id\" INTEGER PRIMARY KEY NOT NULL UNIQUE );",
  "CREATE TABLE \"Disbursement\" ( \"student_id\" INTEGER REFERENCES \"Student\"(\"id\"), \"amount\" REAL, \"dept\" TEXT, \"type\" INTEGER, \"term\" INTEGER, \"period\" INTEGER, \"disbursement_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL );",
  "CREATE TABLE \"Enrollment\" ( \"id\" INTEGER PRIMARY KEY NOT NULL REFERENCES \"Student\"(\"id\"), \"term\",
 \"units\" );",
  "CREATE TABLE \"Payroll\" ( \"id\" INTEGER PRIMARY KEY, \"last_name\" TEXT, \"first_name\" TEXT, \"labor_charge\" INTEGER, \"charge_period\" TEXT, \"amount\" REAL );",
  "CREATE TABLE \"Student\" ( \"id\" INTEGER NOT NULL PRIMARY KEY UNIQUE, \"lastname\" TEXT, \"firstname\" TEXT, \"email\" TEXT );" };  
 
 
 private static String[] tableIndexes =  { "CREATE UNIQUE INDEX \"award_index\" ON \"Award\" (\"award_id\");",
 "CREATE UNIQUE INDEX \"student_index\" ON \"Student\" (\"id\");",
 "CREATE UNIQUE INDEX \"payroll_index\" ON \"Payroll\" (\"id\");",
 "CREATE UNIQUE INDEX \"disbursement_index\" ON \"Disbursement\" (\"disbursement_id\");",
 "CREATE UNIQUE INDEX \"enrollment_index\" ON \"Enrollment\" (\"id\");" }; 