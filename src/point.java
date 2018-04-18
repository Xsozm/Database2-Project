import java.io.Serializable;
import java.util.Comparator;

//compare between points by obj

public class point implements Comparable<Object> , Serializable  { 
	public Object  d;
	public int pageindex ;
	public int recordnumber;
	
	public point(Object  d ,int pageindex , int recordnumber) {
		this.d=d ;
		this.pageindex=pageindex;
		this.recordnumber=recordnumber;
	}

	@Override
	public int compareTo(Object o) {
		point p = (point) o ;
		//System.out.println(p.d.toString());
		if(p.d instanceof Double) {
			double oo= Double.parseDouble( p.d.toString()) ;
			double v = Double.parseDouble(this.d.toString())  -oo;
			if(v>0)return 1;
			if(v==0)return 0;
			return -1;
		}
		
		if(p.d instanceof Double) {
			Integer oo= Integer.parseInt( o.toString()) ;
			Integer v = Integer.parseInt(this.d.toString())  -oo;
			if(v>0)return -1;
			if(v==0)return 0;
			return 1;
		}
		
		return this.d.toString().compareTo(p.d.toString());
	}

	





	















}
