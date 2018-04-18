import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Hashtable;
import java.util.Iterator;

public class Main {

	public static void main(String[] args) throws Exception {
		DBApp a = new DBApp("FirstApp");

		 String strTableName = "Student";
		 
		/* Hashtable htblColNameType = new Hashtable();
		 htblColNameType.put("id", "java.lang.Integer");
		 htblColNameType.put("name", "java.lang.String");
		 htblColNameType.put("gpa", "java.lang.Double");
		 a.createTable(strTableName, "id", htblColNameType);
		
		 Hashtable htblColNameValue = new Hashtable();
		 htblColNameValue.put("id", new Integer(1));
		 htblColNameValue.put("name", new String("Ahmed Noor1"));
		 htblColNameValue.put("gpa", new Double(0.1));
		 a.insertIntoTable(strTableName, htblColNameValue);
		 htblColNameValue.clear();
		 htblColNameValue.put("id", new Integer(2));
		 htblColNameValue.put("name", new String("Ahmed Noor2"));
		 htblColNameValue.put("gpa", new Double(0.8));
		 a.insertIntoTable(strTableName, htblColNameValue);
		 htblColNameValue.clear();
		 htblColNameValue.put("id", new Integer(3));
		 htblColNameValue.put("name", new String("Ahmed Noor3"));
		 htblColNameValue.put("gpa", new Double(0.95));
		 a.insertIntoTable(strTableName, htblColNameValue);
		 htblColNameValue.clear();
		
		 //////////////////////////////
		
		 htblColNameValue.put("id", new Integer(4));
		 htblColNameValue.put("name", new String("Ahmed Noor4"));
		 htblColNameValue.put("gpa", new Double(0.9));
		 a.insertIntoTable(strTableName, htblColNameValue);
		 htblColNameValue.clear();
		 htblColNameValue.put("id", new Integer(5));
		 htblColNameValue.put("name", new String("Ahmed Noor5"));
		 htblColNameValue.put("gpa", new Double(0.5));
		 a.insertIntoTable(strTableName, htblColNameValue);
		 htblColNameValue.clear();
		 htblColNameValue.put("id", new Integer(6));
		 htblColNameValue.put("name", new String("Ahmed Noor6"));
		 htblColNameValue.put("gpa", new Double(0.95));
		 a.insertIntoTable(strTableName, htblColNameValue);
		 htblColNameValue.clear();
		
		 /////////////////////////////
		
		 htblColNameValue.put("id", new Integer(7));
		 htblColNameValue.put("name", new String("Ahmed Noor7"));
		 htblColNameValue.put("gpa", new Double(1.15));
		 a.insertIntoTable(strTableName, htblColNameValue);
		 htblColNameValue.clear();
		 htblColNameValue.put("id", new Integer(8));
		 htblColNameValue.put("name", new String("Ahmed Noor8"));
		 htblColNameValue.put("gpa", new Double(2));
		 a.insertIntoTable(strTableName, htblColNameValue);
		 htblColNameValue.clear();
		 htblColNameValue.put("id", new Integer(9));
		 htblColNameValue.put("name", new String("Ahmed Noor9"));
		 htblColNameValue.put("gpa", new Double(0.95));
		 a.insertIntoTable(strTableName, htblColNameValue);
		 htblColNameValue.clear();
		
		 /////////////////////////////////////////////////////
		
		 htblColNameValue.put("id", new Integer(10));
		 htblColNameValue.put("name", new String("Ahmed Noor10"));
		 htblColNameValue.put("gpa", new Double(0.95));
		 a.insertIntoTable(strTableName, htblColNameValue);
		 htblColNameValue.clear();
		 htblColNameValue.put("id", new Integer(11));
		 htblColNameValue.put("name", new String("Ahmed Noor11"));
		 htblColNameValue.put("gpa", new Double(0.95));
		 a.insertIntoTable(strTableName, htblColNameValue);
		 htblColNameValue.clear();
		 htblColNameValue.put("id", new Integer(12));
		 htblColNameValue.put("name", new String("Ahmed Noor12"));
		 htblColNameValue.put("gpa", new Double(0.95));
		 a.insertIntoTable(strTableName, htblColNameValue);
		 htblColNameValue.clear();
		
		 ///////////////////////////////////////////////////////
		
		 htblColNameValue.put("id", new Integer(13));
		 htblColNameValue.put("name", new String("Ahmed Noor13"));
		 htblColNameValue.put("gpa", new Double(0.95));
		 a.insertIntoTable(strTableName, htblColNameValue);
		 htblColNameValue.clear();
		 htblColNameValue.put("id", new Integer(14));
		 htblColNameValue.put("name", new String("Ahmed Noor14"));
		 htblColNameValue.put("gpa", new Double(0.95));
		 a.insertIntoTable(strTableName, htblColNameValue);
		 htblColNameValue.clear();
		 htblColNameValue.put("id", new Integer(15));
		 htblColNameValue.put("name", new String("Ahmed Noor15"));
		 htblColNameValue.put("gpa", new Double(0.95));
		 a.insertIntoTable(strTableName, htblColNameValue);
		 htblColNameValue.clear();
		 */
		 
		 a.createBRINIndex(strTableName, "gpa");
		 
		 
		 Object[]objarrValues = new Object[2];
		 objarrValues[0] = new Double( 0.75 );
		 objarrValues[1] = new Double( 1.0 );
		 String[] strarrOperators = new String[2];
		 strarrOperators[0] = ">=";
		 strarrOperators[1] = "<";
		 Iterator resultSet = a.selectFromTable(strTableName, "gpa",
				 objarrValues, strarrOperators );
		// System.out.println(resultSet.hasNext());

		// UNCOMMENT AFTER THE INSERTIONS IN THE SECOND RUN

		// String strTableName = "Student";
		// Hashtable htblColNameValue = new Hashtable();
		// htblColNameValue.put("id", new Integer(1111));
		// htblColNameValue.put("name", new String("Hitler"));
		// htblColNameValue.put("gpa", new Double(1.5));
		// a.updateTable(strTableName, 3, htblColNameValue);

		// DELETEION TEST!
		// String strTableName = "Student";
		// Hashtable htblColNameValue = new Hashtable();
		// htblColNameValue.put("id", new Integer(7));
		// htblColNameValue.put("name", new String("Ahmed Noor7"));
		// htblColNameValue.put("gpa", new Double(0.95));
		// a.deleteFromTable(strTableName, htblColNameValue);
		// htblColNameValue.clear();
		// htblColNameValue.put("id", new Integer(13));
		// htblColNameValue.put("name", new String("Ahmed Noor13"));
		// htblColNameValue.put("gpa", new Double(0.95));
		// a.deleteFromTable(strTableName, htblColNameValue);
		// htblColNameValue.clear();
		// htblColNameValue.put("id", new Integer(15));
		// htblColNameValue.put("name", new String("Ahmed Noor15"));
		// htblColNameValue.put("gpa", new Double(0.95));
		// a.deleteFromTable(strTableName, htblColNameValue);

		for (int i = 0; i < 5; i++) {
			System.out.println("============== PAGE " + i + " ==========");
			printPage(i);
		}
		File table = new File("./data/FirstApp/Student/Student.class");
		ObjectInputStream ooo = new ObjectInputStream(new FileInputStream(table));
		Table t = (Table)ooo.readObject();
		System.out.println("TABLE get TYPE: " + t.getPrimaryType() );

	}

	public static void printPage(int pageNum) throws Exception {
		try {
			//File tableFile = new File("./data/FirstApp/Student/Student_" + pageNum + ".class");
			File tableFile = new File("./data/FirstApp/Student/Student" + "_ref_gpa"+ + pageNum  + ".class");
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tableFile));

			pageref page = (pageref) ois.readObject();
			ois.close();

			System.out.println(page);
		} catch (IOException e) {
			System.out.println("ERR Msg: " + e.getMessage());

		}
	}

}
