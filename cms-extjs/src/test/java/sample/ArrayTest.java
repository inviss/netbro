package sample;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ArrayTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Map<Integer, String> map = new HashMap<Integer, String>();
			map.put(1, "1");
			map.put(2, "2");
			map.put(3, "3");
			map.put(4, "4");
			
			Iterator<Integer> it = map.keySet().iterator();
			while(it.hasNext()) {
				System.out.println(it.next());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
