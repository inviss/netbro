package com.google.code.gson;

import com.google.gson.Gson;

public class GsonExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Gson gson = new Gson();
		 
		try {
	 
			//convert the json string back to object
			DataObject obj = gson.fromJson("{\"data1\":100,\"data2\":\"hello\",\"list\":[\"String 1\",\"String 2\",\"String 3\"]}", DataObject.class);
	 
			System.out.println(obj);
	 
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
