import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Page implements Serializable {
	public static int MAX;
	private Tuple[] a;
	private int n = 0;
	private String path;
	
	public void decrementN(){
		n--;
	}
	public Object[] getObjects() {
		return a;
	}

	public Page(String path) throws IOException {
		this.path = path;
		this.n = 0;
		this.a = new Tuple[MAX];
		write(path);
	}

	public static void main(String[] args) {

	}

	public boolean isEmpty() {
		return this.n == 0;
	}

	public boolean isfull() {
		return this.n == MAX;
	}

	public boolean canadd() {
		return !isfull();
	}

	public void addTuple(Tuple t) throws IOException, DBEngineException {
		if (isfull())
			throw new DBEngineException("page is Full");
		a[n++] = t;
		// don't forget to check can add
		write(this.path);
	}

	public int get_size() {
		return this.n;
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

	public String toString() {
		// TODO: Print nulls according to this.MAX - this.n
		String res = "";
		Tuple t;
		for (int i = 0; i < this.MAX; i++) {
			if (this.n > i) {
				t = this.a[i];
				res += t.toString() + "\n";
			} else
				res += "====================================\n";
		}
		return res;
	}

	public Tuple[] getA() {
		return a;
	}

	public void deleteTuple(int i) {
		// TODO: DELETE PAGE IF EMPTY AND NOTIFY TABLE !
		if (i == MAX - 1) {
			n--;
			a[i] = null;
			return;
		}
		for (; i < MAX - 1; i++) {
			a[i] = a[i + 1];
		}
		a[MAX - 1] = null;
		n--;

	}

	// Returns the first tuple (overflow)
	// Visualization :
	/*
	 * 1 2 3 4 NULL
	 */
	// Will return tuple with value 1 and the page will be :
	/*
	 * 2 3 4 PARAMETER PASSED NULL
	 */
	public Tuple shiftTuplesUpwards(Tuple replacement) {
		Tuple topTuple = a[0];
		int i = 1;
		for (; i < this.n; i++) {
			this.a[i - 1] = this.a[i];
		}
		// For the last page, will pass a null hardcoded
		this.a[n - 1] = replacement;
		if (replacement == null)
			this.n -= 1;
		return topTuple;
	}

	public void remove() {
		
	}

}
