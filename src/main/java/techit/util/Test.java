package techit.util;

import java.util.HashMap;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Long l = 10L;
		Integer i = 10;
		
		HashMap<Object,Object> map = new HashMap<>();
		map.put(10, 10);
		System.out.println(map.get(10).toString().equals(l.toString()));
	}

}
