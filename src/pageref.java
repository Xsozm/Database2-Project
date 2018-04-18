import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class pageref implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String path;
	private int n= 0 ;
	
	ArrayList<point> a = new ArrayList<point>();

	public pageref(String path) throws IOException {
		this.path = path;
		write(path);
	}
	
	public String toString() {
		// TODO: Print nulls according to this.MAX - this.n
		String res = "";
		for (int i = 0; i < this.a.size(); i++) {
			res+=a.get(i).d.toString();
		
				res += "====================================\n";
		}
		return res;
	}
	
	public void write(String path) throws IOException {
		// appeand objects
		File f = new File(this.path);
		if (f.exists())
			f.delete();

		f.createNewFile();
		// BufferedWriter outStream= new BufferedWriter(new FileWriter(f,
		// true));
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
		oos.writeObject(this);
		oos.close();
	}
	public void add(point p) throws IOException {
		this.a.add(p);
		setN(getN() + 1);
		this.write(this.path);
	}
	
	public boolean isfull() {
		return getN()==200;
	}

	

	public void setN(int n) {
		this.n = n;
	}

	public int getN() {
		// TODO Auto-generated method stub
		return n;
	}

}
