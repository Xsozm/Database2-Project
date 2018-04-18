import java.io.Serializable;

public class Tuple implements Serializable {
	int sz = 0;
	Object primary;
	private Object[] A;

	Tuple(int sz, Object primary) {
		this.primary = primary;
		this.sz = sz;
		A = new Object[sz];
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Object o : A)
			sb.append(o.toString() + " ");
		return sb.toString();
	}

	public void insert(int idx, Object ob) {
		A[idx] = ob;
	}

	public Object get(int idx) {
		return A[idx];
	}

}
