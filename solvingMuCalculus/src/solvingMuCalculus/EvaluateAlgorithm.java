package solvingMuCalculus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EvaluateAlgorithm {
	
	public EvaluateAlgorithm() {};
	

	
	
	/**
	 * A recursive algorithm to filter the set of candidate solutions.
	 * call recursive method until we're at the final coordinate.
	 * At which point apply the quantifier. Then return the solution.
	 * 
	 * @param candidateSolutions
	 * @param compareIndex
	 * @param variableIndex
	 * @param currentSolution
	 * @param sequences
	 * @return uniqueSolution
	 */
	public static ArrayList<ArrayList<Double>> algorithm(ArrayList<ArrayList<Double>> candidateSolutions, int compareIndex,
			int variableIndex,ArrayList<Double> currentSolution, ArrayList<String> sequences){

		ArrayList<ArrayList<Double>> T = new ArrayList<ArrayList<Double>>();
		
		if(variableIndex==candidateSolutions.get(0).size()-1) {
			//here no need to return anything. 
			for(int i=0;i<candidateSolutions.size();i++) {
				ArrayList<Double> temporarySolutions = (ArrayList<Double>) currentSolution.clone();
				temporarySolutions.add(candidateSolutions.get(i).get(variableIndex));

				if(candidateSolutions.contains(temporarySolutions) && !T.contains(temporarySolutions)) {
					//making sure its valid and exists
					T.add(temporarySolutions);
				}
			}
		}
		else {
			
			//so for (1,1,1,0) it will only create two trees 
			ArrayList<Double> alreadyChecking = new ArrayList<Double>();
			for(int i=0;i<candidateSolutions.size();i++) {
				double valueAtIndex = candidateSolutions.get(i).get(variableIndex);
				
				//checking if recursive branch is already created or not
				if(alreadyChecking.contains(valueAtIndex)) {
					//if created just go next.
					continue;
				}
				else {
					//store the value for which recursive branch has been created
					alreadyChecking.add(valueAtIndex);
				}
				ArrayList<Double> temporarySolutions = (ArrayList<Double>) currentSolution.clone();
				temporarySolutions.add(candidateSolutions.get(i).get(variableIndex));	
				
				
				//call recursive method for the next coordinate
				ArrayList<ArrayList<Double>> newTemp = algorithm(candidateSolutions,compareIndex,variableIndex+1,temporarySolutions,sequences);
				
				//add the solutions returned by the recursive algorithm to a Set T.
				for(ArrayList<Double> x:newTemp) {
					if(candidateSolutions.contains(x) && !T.contains(x)) {
						T.add(x);
					}
				}

				if(variableIndex==0) {
					compareIndex++;
				}

			}
			
		}

		
		if(T.size()==0) {
		//	System.err.println("NOTHING HAS BEEN ADDED TO T");
			return T;
		}
		

		
		if(T.size()==1) {
		   //Just one candidate solution, so return it
			return T;
		}
		else {
			//more than one. this is where I need to check for quantifiers

			String term = sequences.get(variableIndex);

			//apply quantifier and keep the solution that either has min or max at variableIndex.
			ArrayList<ArrayList<Double>> solutions = new ArrayList<ArrayList<Double>>();
			if(term.charAt(0)=='m') {
				//look for min				
				double min = Double.MAX_VALUE;
				ArrayList<Double> result = new ArrayList<Double>();
				  for(ArrayList<Double> x:T) {
					//  System.out.println(x.get(variableIndex)+" < "+min);
				    	if(x.get(variableIndex)<min) {
				    		
				    		//result = x;
				    		min = x.get(variableIndex);
				    	}
				    }
				  for(ArrayList<Double> x:T) {
					  if(x.get(variableIndex)==min) {
						  solutions.add(x);
					  }
				  }

				  return solutions;
			
				
			}
			else if(term.charAt(0)=='n') {
				//look for max

				double max = Double.MIN_VALUE;
				ArrayList<Double> result = new ArrayList<Double>();
				  for(ArrayList<Double> x:T) {

				    	if(x.get(variableIndex)>max) {
				    		//result = x;
				    		max = x.get(variableIndex);
				    	}
				    }
				  for(ArrayList<Double> x:T) {
					  if(x.get(variableIndex)==max) {
						  solutions.add(x);
					  }
				  }

				  return solutions;
			
			}
			else if(term.charAt(0)=='f') {
				//value should be the same throughout?
				
				return T;
				
			}
			System.err.println("HERE   Error because non identified quantifier "+term.charAt(0)+" compare "+compareIndex+" var index = "+variableIndex);

		}
		System.err.println("Error because non identified quantifier ");
		return null;		
		
	}
	


	
	/**
	 * Loops from last coordinate to first. 
	 * For each coordinate, generates a subset of solutions that have similar coords at all but one, and for that one coordinate we will apply the coordinate
	 * 
	 * @param candidateSolutions
	 * @param sequences
	 * @return
	 */
	public static ArrayList<ArrayList<Double>> heuristicAlgorithm(ArrayList<ArrayList<Double>> candidateSolutions, ArrayList<String> sequences){
		
		//returning if solution size is 1
		if(candidateSolutions.size()==1) {
			return candidateSolutions;
		}
		int n= sequences.size();
		//Candidate solutions = Sn+1
		for(int i=n-1;i>=0;i--) {
			candidateSolutions = getNext(candidateSolutions,i,sequences);
			
			if(candidateSolutions.size()==1) {
				return candidateSolutions;
			}
		}
		
		
		return candidateSolutions;
	}
	
	
	/**
	 * Creates a subset of all matching coordinates apart from at index i.
	 * Then runs the applyQuantifier method on each subset and appends the solution of all subsets
	 * and returns it.
	 * @param candidateSolutions
	 * @param i
	 * @param sequences
	 * @return
	 */
	public static ArrayList<ArrayList<Double>> getNext(ArrayList<ArrayList<Double>> candidateSolutions,int i,ArrayList<String> sequences){
		/*
		 * Make a list which contains candidate solutions.
		 * Such that the candidate solutions are teh same but at index i.
		 */
		
		if(sequences.get(i).charAt(0)=='f'){
			return candidateSolutions;
		}
		Map<ArrayList<Double>, ArrayList<ArrayList<Double>>> matchingSubsets = new HashMap<>();
		
		for(ArrayList<Double> x: candidateSolutions) {
			ArrayList<Double> temp = (ArrayList<Double>) x.clone();
			temp.remove(i);
			
			ArrayList<ArrayList<Double>> currentSubset = matchingSubsets.getOrDefault(x, new ArrayList<ArrayList<Double>>());
			currentSubset.add(x);
			matchingSubsets.put(temp,currentSubset);
		}
		
		//now we have a map of subsets of the list.
		ArrayList<ArrayList<Double>> nextSetOfSolutions = new ArrayList<ArrayList<Double>>();
		
		String quantifier = sequences.get(i);
		for(ArrayList<Double>x :matchingSubsets.keySet()) {
			ArrayList<ArrayList<Double>> currentSubset = matchingSubsets.get(x);
			
			ArrayList<Double> nextSolution = applyQuantifierSubset(currentSubset,quantifier,i);
			if(!nextSetOfSolutions.contains(nextSolution)) {
				nextSetOfSolutions.add(nextSolution);
				//should always be true
			}
		}
		
		return nextSetOfSolutions;
	}
	
	
	
	/**
	 * Method to apply quantifier. Returns the solution with the maximum or minimum at coordinate i, for the mu and v quantifiers
	 * respectively
	 * @param currentSubset
	 * @param quantifier
	 * @param i
	 * @return
	 */
	private static ArrayList<Double> applyQuantifierSubset(ArrayList<ArrayList<Double>> currentSubset,
			String quantifier,int i) {

		if(quantifier.charAt(0)=='m') {
			//search for minimum
			double minimum = Double.MAX_VALUE;
			int index = 0;
			
			for(int j=0;j<currentSubset.size();j++){
				if(currentSubset.get(j).get(i)<minimum) {
					minimum = currentSubset.get(j).get(i);
					index = j;
				}
			}
			return currentSubset.get(index);
		}
		else {
			//search for maximum
			double maximum = Double.MIN_VALUE;
			int index = 0;
			
			for(int j=0;j<currentSubset.size();j++){
				if(currentSubset.get(j).get(i)>maximum) {
					maximum = currentSubset.get(j).get(i);
					index = j;
				}
			}
			return currentSubset.get(index);
		}
	}


}
