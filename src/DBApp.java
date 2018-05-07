import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

public class DBApp {
	private static String Directory;
	private static String APPname;
	private File metadata;

	public static void main(String[] args) {

	}

	public DBApp(String APPname) throws IOException, DBEngineException {
		try {
			Properties properties = new Properties();
			FileReader fr = new FileReader(new File("./config/DBApp.properties"));
			properties.load(fr);
			Page.MAX = Integer.parseInt(properties.getProperty("MaximumRowsCountinPage"));
		} catch (Exception e) {
			Page.MAX = 200; // Default in case of any problems
		}
		this.APPname = APPname;
		this.Directory = "./data/" + APPname + "/"; // Edited to contain
													// everything in a data
													// folder :)
		new File(Directory).mkdirs();
		new File(Directory + "Meta" + "/").mkdirs();// meta file

	}
	
	public Iterator selectFromTable(String strTableName, String strColumnName,
			Object[] objarrValues,
			String[] strarrOperators) throws FileNotFoundException, ClassNotFoundException, IOException, DBEngineException {
		Table tb = getTable(strTableName);
		if(tb==null)return null;
		return tb.selectFromTable(strTableName, strColumnName, objarrValues, strarrOperators);
	}
	
	public void createBRINIndex(String strTableName,String strColName) throws FileNotFoundException, ClassNotFoundException, IOException, DBEngineException {
		Table tb = getTable(strTableName);
		if(tb==null) return;
		tb.createBRINIndex(strTableName, strColName);
		
	}
	
	
	private Table getTable(String strTableName) throws FileNotFoundException, IOException, ClassNotFoundException {
		File tableFile = new File(Directory + strTableName + "/" + strTableName + ".class");
		if (!tableFile.exists())
			return null;
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tableFile));
		Table table = (Table) ois.readObject();
		ois.close();
		return table;
	}

	private void writedata(String strTableName, Hashtable<String, String> htblColNameType, String primarykey)
			throws IOException {
		this.metadata = new File(this.Directory + "Meta/" + strTableName + "meta.csv");
		metadata.createNewFile();
		PrintWriter out = new PrintWriter(new FileWriter(metadata, true));
		out.println("Table Name, Column Name, Column Type, Key, Indexed");
		for (Entry<String, String> entry : htblColNameType.entrySet()) {
			String colName = entry.getKey();
			String colType = entry.getValue();
			out.append(strTableName + "," + colName + "," + colType + "," + (colName == primarykey) + "," + "false"
					+ "\n");
		}
		out.flush();
		out.close();
	}

	public void createTable(String strTableName, String primaryKey, Hashtable<String, String> H)
			throws ClassNotFoundException, IOException, DBEngineException {
		if (!(getTable(strTableName) == null)) {
			throw new DBEngineException("Table already exist");
		} else {
			Table t = new Table(Directory, strTableName, H, primaryKey);
			writedata(strTableName, H, primaryKey);
		}
	}

	public void insertIntoTable(String strTableName, Hashtable<String, Object> H) throws Exception {

		File file = new File(Directory + strTableName + "/" + strTableName + ".class");
		if (!file.exists())
			throw new DBEngineException("doesn't exist");

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		Table table = (Table) ois.readObject();
		ois.close();
		table.add(H);
		if(table.brin!=null)
		for(String str : table.brin.keySet()) {
			if(table.brin.contains(str) && table.brin.get(str)) {
				table.createBRINIndex(strTableName, str);
			}
		}
		table.saveTable();

	}

	// Changed strKey to Object type, since it makes no sense to bind it to a
	// string
	// Assuming it's the value of the primaryKey for the tuple to be replaced
	public void updateTable(String strTableName, String strKey, Hashtable<String, Object> htblColNameValue)
			throws DBEngineException, Exception {
		File file = new File(Directory + strTableName + "/" + strTableName + ".class");
		if (!file.exists())
			throw new DBEngineException("Table doesn't exist");
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		Table table = (Table) ois.readObject();
		ois.close();
		String primaryType = table.getPrimaryType();
		Object objKey;
		if (primaryType.equals("java.lang.Integer")) {
			objKey = Integer.parseInt(strKey);
		} else if (primaryType.equals("java.lang.Double")) {
			objKey = Double.parseDouble(strKey);
		} else if (primaryType.equals("java.lang.Boolean")) {
			objKey = Boolean.parseBoolean(strKey);
		} else if (primaryType.equals("java.util.Date")) {
			// TODO: Absolutely no IDEA ! we would need to unify a format for
			// dates in general not just updates.
			objKey = Date.parse(strKey);
		} else {
			objKey = strKey;
		}
		table.updateTuple(objKey, htblColNameValue);
		for(String str : table.brin.keySet()) {
			if(table.brin.contains(str) && table.brin.get(str)) {
				table.createBRINIndex(strTableName, str);
			}
		}
		table.saveTable();


	}

	public void deleteFromTable(String strTableName, Hashtable<String, Object> htblColNameValue)
			throws DBEngineException, Exception {
		File file = new File(Directory + strTableName + "/" + strTableName + ".class");
		if (!file.exists())
			throw new DBEngineException("Table doesn't exist");
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		Table table = (Table) ois.readObject();
		ois.close();
		table.deleteTuple(htblColNameValue);
		for(String str : table.brin.keySet()) {
			if(table.brin.contains(str) && table.brin.get(str)) {
				table.createBRINIndex(strTableName, str);
			}
		}
		table.saveTable();
		
	}

}
