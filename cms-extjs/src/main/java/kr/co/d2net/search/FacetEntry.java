package kr.co.d2net.search;

public class FacetEntry implements Comparable<FacetEntry>{
	private long count;
	private Object term;
	
	public FacetEntry(){
		
	}
	
	public FacetEntry(long count, Object term) {
		super();
		this.count = count;
		this.term = term;
	}

	public long getCount() {
		return count;
	}
	
	public void setCount(long count) {
		this.count = count;
	}
	
	public Object getTerm() {
		return term;
	}
	
	public void setTerm(Object term) {
		this.term = term;
	}
	
	@Override
	public int compareTo(FacetEntry o) {
		if(count > o.count){
			return 1;
		}
		if(count < o.count){
			return -1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return "CountEntry [count=" + count + ", term=" + term + "]";
	}
}
