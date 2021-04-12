package solvingMuCalculus;

import java.util.Arrays;

public class inequalitySolver {

	public inequalitySolver() {};
	
	
	public static boolean leq(double[] array) {
		/*
		 * true = a<= b
		 * false = b>=a
		 */
//		leq(a, b,t) =
//				
//				a  b, if t = 1;
//				a  b, if t = 0;
//		
		if(array.length!=3) {
			System.err.println("LEQ array has to be of size 3");
			System.exit(0);
			return false;
		}
		
		//a is index 0
		//b is index 1
		
		if(array[2]==1) {
		//return true if a<= b
			if(array[0]<=array[1])return true;
			return false;
			
			
		}
		else if(array[2]==0) {
		//return true if a>=b
			if(array[0]>=array[1])return true;
			return false;
			
		}
		else {
			System.err.println("t has to be either 0 or 1 and it is "+Arrays.toString(array));
			System.exit(0);
			return false;
		}		
	}
}
