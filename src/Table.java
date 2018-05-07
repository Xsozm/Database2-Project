import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

public class Table implements Serializable {
	int curPageIndex = 0;
	String tablename, path;
	String primaryKey;
	Hashtable<String, String> htblColNameType;
	Hashtable<String, Boolean> brin=new Hashtable<>();

	
	public String getPrimaryType(){
		return htblColNameType.get(primaryKey);
	}

	public Table(String path, String tablename, Hashtable<String, String> htblColNameType, String primaryKey)
			throws IOException, ClassNotFoundException, DBEngineException {
		if (!htblColNameType.containsKey(primaryKey)) {
			throw new DBEngineException("primary key must be an attribute");
		}
		this.path = path + tablename + "/";
		this.tablename = tablename;
		this.primaryKey = primaryKey;
		this.htblColNameType = htblColNameType;

		this.curPageIndex = -1;

		createDirectory();
		// createPage(); Why should we create a page for zero records ?
		saveTable();
	}

	// save table in file ()
	public void saveTable() throws IOException {
		File f = new File(path + tablename + ".class");
		if (f.exists())
			f.delete();
		f.createNewFile();
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
		oos.writeObject(this);
		oos.close();
	}
	
	
	
	public Iterator selectFromTable(String strTableName, String strColumnName,Object[] objarrValues,String[] strarrOperators)
			throws DBEngineException, FileNotFoundException, ClassNotFoundException, IOException{
		
		String type = htblColNameType.get(strColumnName);
		Double MAX,MIN;
		MAX=-1111.0;
		MIN=10000.0;
		if(type.equals("java.lang.Integer")) {
			
		 for(int i=0;i<strarrOperators.length;i++) {
			  int I = (Integer.parseInt(objarrValues[i].toString()));
			  String str = strarrOperators[i];
			  if(str.equals(">") || str.equals(">="))
			  {
				  MAX=Math.max(MAX, I);
			  }else
				  MIN=Math.min(MIN, I);
		 }
		 
		
		 	
		}
		
		if(type.equals("java.lang.Double")) {
			
		 for(int i=0;i<strarrOperators.length;i++) {
			  Double I = Double.parseDouble( objarrValues[i].toString());
			  //System.out.println(I);
			  String str = strarrOperators[i];
			  if(str.equals(">") || str.equals(">="))
			  {
				  MAX=Math.max(MAX, I);
			  }else
				  MIN=Math.min(MIN, I);
		 }
		 
		 	
		}
		System.out.println(MAX+" "+MIN);
		//System.out.println(brin.get(strColumnName));
		 if(brin.contains(strColumnName) && brin.get(strColumnName)) {
			 //System.out.println("gggg");
			 return selectspecial( strTableName,  strColumnName,type,MAX, MIN);
		 }else {
			 return normalselect(strTableName,  strColumnName,type,MAX, MIN);
		 }
		 
		
		
		
				
		
	}

	
	
	private Iterator normalselect(String strTableName, String strColumnName, String type, Double mIN , Double mAX ) throws FileNotFoundException, IOException, ClassNotFoundException {
		LinkedList<Tuple> L= new LinkedList<Tuple>();
		int idx =0 ;
		for(String str :htblColNameType.keySet()) {
			if(str.equals(strColumnName))
				break;
			idx++;
		}
		
		for(int y=0;y<=curPageIndex;y++) {
			File f = new File(path + tablename + "_" + y + ".class");
			if (!f.exists())
				continue;
			
			
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			Page curPage = (Page) ois.readObject();
			Tuple[] a = (Tuple[]) curPage.getObjects();
			for (int j = 0; j < a.length; j++) {
					Tuple t = a[j];
					if(t==null)continue;
					double v=(double) t.get(idx);
					if(v>=mIN && v<=mAX) {
						System.out.println("inserted");
						L.add(t);
					}

							
				}
			
		
	}
		if(L==null)return null;
		return L.iterator();
	}

	private Iterator<Tuple> selectspecial(String strTableName, String strColumnName, String type, Double mIN, Double mAX) throws FileNotFoundException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		LinkedList<Tuple> L= new LinkedList<Tuple>();
		int y= 0 ;

		for( y=0;y<=curPageIndex;y++) {
		File f = new File(path + tablename + "_ref_" +strColumnName+ y + ".class");
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
		pageref curPage = (pageref) ois.readObject();
		Double minpage = Double.parseDouble(curPage.a.get(0).d.toString());
		Double maxpage = Double.parseDouble(curPage.a.get(curPage.getN()-1).d.toString());
		//System.out.println(minpage+" "+maxpage+" "+mAX);
		if(mIN>=minpage && mIN<=maxpage)
			break;
		}
		for(int i=y;i<=curPageIndex;i++) {
				File f = new File(path + tablename + "_ref_" +strColumnName+ i + ".class");
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
				pageref curPage = (pageref) ois.readObject();
				Double minpage = Double.parseDouble(curPage.a.get(0).d.toString());
				Double maxpage = Double.parseDouble(curPage.a.get(curPage.getN()-1).d.toString());
				for(int j=0;j<curPage.a.size();j++) {
					point p = curPage.a.get(j);
					int pageidx = p.pageindex;
					int re= p.recordnumber;
					File ff = new File(path + tablename + "_" + pageidx + ".class");
					ObjectInputStream u = new ObjectInputStream(new FileInputStream(ff));
					//System.out.println(pageidx);
					//System.out.println(Double.parseDouble(p.d.toString()));
					if(Double.parseDouble(p.d.toString())>=mIN &&  Double.parseDouble(p.d.toString())<=mAX) {
					L.add(( (Page) u.readObject()).getA()[re]);
					System.out.println("inserted one tuple");
					}
					
					
				
			}
		}
		if(L==null)return null;
		return L.listIterator();
		
	}
		
	

	public void createBRINIndex(String strTableName,String strColName) throws DBEngineException, FileNotFoundException, IOException, ClassNotFoundException{
		int idx =0 ;
		for(String str :htblColNameType.keySet()) {
			if(str.equals(strColName))
				break;
			idx++;
		}
		
		
		//File f = new File(path + tablename + "_" + curPageIndex + ".class");
		ArrayList<point> a= new ArrayList<point>();
		
		for (int i = 0; i <= this.curPageIndex; i++) {
			File f = new File(path + tablename + "_" + i + ".class");
			if (!f.exists())
				continue;
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			Page curPage = (Page) ois.readObject();
			Tuple[] aa = (Tuple[]) curPage.getObjects();
			for(int j=0;j<aa.length;j++) {
				Tuple t = aa[j];
				if(t==null)continue;
				//System.out.println(t.get(idx).toString());
				a.add(new point((t.get(idx)),curPageIndex,j));
			}

		
		
		}
		
		Collections.sort(a);
		int y= 0 ;
		pageref p =null;
		for(int i=0;i<a.size();i++) {
			if(i==0)  p = new pageref(path + tablename + "_ref_" +strColName+ y + ".class");
			p.add(a.get(i));;

			if((i+1)%4==0) {
				y++;
				p=new pageref(path + tablename + "_ref_"+ strColName+ y + ".class");
			}

			
		}
		this.brin.put(strColName, true);
	
		saveTable();

		
	}
	
	
	
	

	public boolean check_record(Hashtable<String, Object> htblColNameValue)
			throws DBEngineException, FileNotFoundException, IOException, ClassNotFoundException {
		Object obj = null;
		for (String str : htblColNameValue.keySet())
			if (str.equals(primaryKey))
				obj = htblColNameValue.get(str);
		if (obj == null) {
			throw new DBEngineException("REcord doesn't contain primary key");

		}
		boolean found = false;
		for (int i = 0; i <= this.curPageIndex; i++) {
			File f = new File(path + tablename + "_" + i + ".class");
			if (!f.exists())
				continue;
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			Page curPage = (Page) ois.readObject();
			Tuple[] a = (Tuple[]) curPage.getObjects();
			for (Tuple t : a) {
				if (t != null)
					if (t.primary.equals(obj)) {
						found = true;
					}
			}
		}
		if (found) {
			return false;
		}
		return true;

	}

	public void add(Hashtable<String, Object> htblColNameValue) throws ClassNotFoundException, IOException, Exception {
		if (!check_record(htblColNameValue))
			throw new DBEngineException("THis REcord EXists !");

		Object obj = null;
		for (String str : htblColNameValue.keySet()) {
			if (str.equals(primaryKey))
				obj = htblColNameValue.get(str);
		}

		if (obj == null) {
			throw new DBEngineException("primary key doesn't exists in input tuple");
		}

		if (this.curPageIndex == -1) {
			this.createPage(); // Includes table saving
		}

		Tuple t = new Tuple(htblColNameValue.size() + 1, obj);
		int i = 0;
		for (String str : htblColNameType.keySet()) {
			
			t.insert(i++, (htblColNameValue.get(str)));
		}

		if (!check(htblColNameValue))
			throw new DBEngineException("Invalid Input");
		else {
			java.util.Date date = Calendar.getInstance().getTime();
			t.insert(i, date);
			this.addRecord(t);
		}

	}

	private boolean check(Hashtable<String, Object> htblColNameValue) {

		for (String str : htblColNameValue.keySet()) {
			Object obj = htblColNameValue.get(str);
			String type = this.htblColNameType.get(str);
			if (!valid(obj, type))
				return false;
		}
		return true;

	}

	// add record in the current page and return the current page
	private Page addRecord(Tuple T) throws IOException, ClassNotFoundException, DBEngineException {

		File f = new File(path + tablename + "_" + curPageIndex + ".class");
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
		Page curPage = (Page) ois.readObject();
		// System.out.println(curPage.get_size());
		if (curPage.isfull())
			curPage = createPage();
		curPage.addTuple((T));
		ois.close();
		return curPage;
	}

	private Page createPage() throws IOException {
		curPageIndex++;
		Page p = new Page(path + tablename + "_" + curPageIndex + ".class");
		saveTable();
		return p;

	}

	private boolean valid(Object value, String type) {

		if (type.equals("java.lang.Integer"))
			if ((value instanceof Integer))
				return true;
		if (type.equals("java.lang.String"))
			if ((value instanceof String))
				return true;
		if (type.equals("java.lang.Double"))
			if ((value instanceof Double))
				return true;
		if (type.equals("java.lang.Boolean"))
			if ((value instanceof Boolean))
				return true;
		if (type.equals("java.util.Date"))
			if ((value instanceof Date))
				return true;
		return false;
	}

	private void createDirectory() {
		File tableDir = new File(path);
		tableDir.mkdirs();
	}

	// Given a key object, it will search for the tuple holding that key in the
	// current table.
	// Returns the index of the page as the first item in the array, and the
	// index of the tuple in the page as the 2nd element.
	// result[0] = pageIndex; result[1] = tupleIndex
	// Returns {-1,-1} if the record was not found.
	private int[] findRecordByKey(Object keyTarget) throws Exception {

		int[] result = { -1, -1 };

		for (int i = 0; i <= this.curPageIndex; i++) {
			File f = new File(path + tablename + "_" + i + ".class");
			if (!f.exists())
				continue;
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			Page curPage = (Page) ois.readObject();
			Tuple[] a = (Tuple[]) curPage.getObjects();
			for (int j = 0; j < a.length; j++) {
				Tuple t = a[j];
				if (t != null)
					if (t.primary.equals(keyTarget)) {
						result[0] = i;
						result[1] = j;
						break;
					}
			}
		}
		return result;
	}

	public void updateTuple(Object objKey, Hashtable<String, Object> htblColNameValue) throws Exception {
		if (!check_record(htblColNameValue))
			throw new DBEngineException("Record Exist!");

		Object obj = null;
		for (String str : htblColNameValue.keySet()) {
			if (str.equals(primaryKey))
				obj = htblColNameValue.get(str);
		}
		if (obj == null)
			throw new DBEngineException("primary key doesn't exists in input tuple");

		Tuple t = new Tuple(htblColNameValue.size() + 1, obj);
		int i = 0;
		for (String str : htblColNameValue.keySet()) {
			t.insert(i++, (htblColNameValue.get(str)));
		}

		if (!check(htblColNameValue))
			throw new DBEngineException("Invalid Input");

		else {
			java.util.Date date = Calendar.getInstance().getTime();
			t.insert(i, date);
			//////////////////////////////////////////////////////////////
			int[] indecies = findRecordByKey(objKey);
			String pagePath = path + tablename + "_" + indecies[0] + ".class";
			File f = new File(pagePath);
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			Page curPage = (Page) ois.readObject();
			curPage.getA()[indecies[1]] = t;
			ois.close();
			curPage.write(pagePath);
		}

	}

	public void deleteTuple(Hashtable<String, Object> htblColNameValue) throws Exception {
		Object primaryObj = null;
		for (String str : htblColNameValue.keySet()) {
			if (str.equals(primaryKey))
				primaryObj = htblColNameValue.get(str);
		}
		if (primaryObj == null)
			throw new DBEngineException("Primary key is not entered");
		int[] indecies = findRecordByKey(primaryObj);
		if (indecies[0] == -1)
			throw new DBEngineException("The tuple you're trying to delete doesn't exist");

		int targetPageIndex = indecies[0];
		int tupleIndex = indecies[1];
		// UGLY CODE INCOMING!

		// Tuple exists and needs to be deleted
		// Start from the bottom page (curPageIndex) and shift upwards till the
		// index
		int tempPageIndex = this.curPageIndex;
		Tuple overflowTuple = null;

		while (tempPageIndex != targetPageIndex) {
			String pagePath = path + tablename + "_" + tempPageIndex + ".class";
			File f = new File(pagePath);
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			Page curPage = (Page) ois.readObject();
			overflowTuple = curPage.shiftTuplesUpwards(overflowTuple);
			ois.close();
			curPage.write(pagePath);
			tempPageIndex--;
		}

		String pagePath = path + tablename + "_" + tempPageIndex + ".class";
		File f = new File(pagePath);
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
		Page curPage = (Page) ois.readObject();

		if (tupleIndex == curPage.get_size() - 1)
			curPage.getA()[curPage.get_size() - 1] = overflowTuple;
		else {

			int i = tupleIndex + 1;
			while (true) {
				if (i == curPage.get_size()) {
					curPage.getA()[i - 1] = overflowTuple;
					break;
				}
				curPage.getA()[i - 1] = curPage.getA()[i];
				i++;
			}

		}
		// In case it was the last page in the table, we would need to
		// Decrement it here using the method.
		// Since in other cases (where deletions wasn't from the last page)
		// We were decrementing the N in the method shiftTuplesUpwards
		// However in case of it being the last page, we never execute said
		// method.
		if (overflowTuple == null)
			curPage.decrementN();

		ois.close();
		curPage.write(pagePath);

		// Trying to check the last page and removing it on being empty.
		// Only the last page can be removed since we are deleting one record at
		// a time.
		f = new File(path + tablename + "_" + this.curPageIndex + ".class");
		ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream(f));
		Page tmp = (Page) ois2.readObject();
		if (tmp.isEmpty()) {
			f.delete();
			this.curPageIndex--;
			this.saveTable();
		}
	}

}
