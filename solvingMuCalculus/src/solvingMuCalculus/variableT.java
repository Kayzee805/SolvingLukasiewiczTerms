package solvingMuCalculus;

import java.util.ArrayList;
import java.util.List;

public class variableT {
	
	//no initialise needed
	public variableT() {};

	 List<ArrayList<Integer>> test = new ArrayList<ArrayList<Integer>>();
	 void addToArray(int arr[], int n) 
	{ 
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for(int i:arr) {
			temp.add(i);
		}
		test.add(temp);
	} 
	  
	// Function to generate all binary strings 
	 void setTest() {
		test = new ArrayList<ArrayList<Integer>>();
	}
	 void generateAllBinaryStrings(int n,  
	                            int arr[], int i) 
	{ 
	    if (i == n)  
	    { 
	   
	        addToArray(arr, n); 
	        return; 
	    } 
	  
	    // First assign "0" at ith position 
	    // and try for all other permutations 
	    // for remaining positions 
	    arr[i] = 0; 
	    generateAllBinaryStrings(n, arr, i + 1); 
	  
	    // And then assign "1" at ith position 
	    // and try for all other permutations 
	    // for remaining positions 
	    arr[i] = 1; 
	    generateAllBinaryStrings(n, arr, i + 1); 
	} 
	 List<ArrayList<Integer>> getPermutations(){
		return test;
	}
	  
	// Driver Code 
	
	public static void main(String args[]) 
	{ 
	    int n = 6; 
	  
	    int[] arr = new int[n]; 
	  
	    // Print all binary strings 
	    variableT vt = new variableT();
	    vt.generateAllBinaryStrings(n, arr, 0); 
	    List<ArrayList<Integer>> permutations = vt.getPermutations();
	    
	    for(ArrayList<Integer> a:permutations) {
	    	System.out.println(a.toString());
	    }
	} 
	} 
	
	
	

