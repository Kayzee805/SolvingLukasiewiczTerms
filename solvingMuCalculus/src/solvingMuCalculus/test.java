package solvingMuCalculus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Hashtable;
import java.util.List;

public class test {
	
	public static void main(String[] args) {
		
		LinkedHashMap<String,Integer> dummy = new LinkedHashMap();
		
		
		dummy.put("x",null);
		dummy.put("y", 1);
		
		for(String k:dummy.keySet()) {
			System.out.println(k+"="+dummy.get(k));
		}
		
		dummy.put("x",5);
		System.out.println("break ehre");
		for(String k:dummy.keySet()) {
			System.out.println(k+"="+dummy.get(k));
		}
		
	}

}
