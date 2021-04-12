
package solvingMuCalculus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class Evaluation {
	Evaluation(){};

	public HashMap<ArrayList<String>,ArrayList<ArrayList<Double>>>  generateCandidateSolutions(muTerm x){
		ArrayList<ArrayList<Double>> candidateSolutions = new ArrayList<ArrayList<Double>>();
		muTerm singleSequence = x.generateSequenceEquation(x, true, x.variableSet.size());
		
		//so If only 1 quantifier, we add the entire term to the list of eqs
		if(x.seqeunceOfEquations.size()==0) {
			x.seqeunceOfEquations.put(x.singleQuantifier,singleSequence);
		}
		
		HashMap<String, muTerm> sequenceOfEquations = x.seqeunceOfEquations;
		//System.out.println("Sequence length ="+sequenceOfEquations.size());
		
		
		ArrayList<String[]> translatedEquations = new ArrayList<String[]>();
		ArrayList<String> quantifierNames = new ArrayList<String>();
		//adding the translated terms into arraylist
		//one for each sequence
		for(String key:sequenceOfEquations.keySet()) {
			String[] values = new String[x.variableSet.size()+1];
			Arrays.fill(values, "0");
			values = x.translateTerms(sequenceOfEquations.get(key), values,x.tTracker);
			translatedEquations.add(values);
			//System.out.println("trans:: = "+key+"  "+x.toString(sequenceOfEquations.get(key)));
			quantifierNames.add(key);
		}
		ArrayList<String[]>leqList = x.getLEQ();
		
		
		//printing leqlist
//		for(String[] a:leqList) {
//			System.out.println("LEQ :"+Arrays.toString(a));
//		}
		
		//generates all permutation of T for m operators
		int[] emptyArrayforT = new int[x.opCounter];
		variableT tClass = new variableT();
		tClass.generateAllBinaryStrings(x.opCounter,emptyArrayforT,0);
		List<ArrayList<Integer>> allTpermutations =tClass.getPermutations(); 
		
		x.setToArray();
		for(ArrayList<Integer>tList:allTpermutations) {
			//just putting it to an array, can probably use toArray
			int[] currentT = new int[tList.size()];
			for(int i=0;i<currentT.length;i++) {
				currentT[i]=tList.get(i);
			}
			
			double[][] matrix = new double[translatedEquations.size()][translatedEquations.get(0).length];
			
			for(int i=0;i<translatedEquations.size();i++) {
				//so original wont change
				String[] translatedSeq = translatedEquations.get(i).clone();
				//System.out.println("Trnasled seq = "+Arrays.toString(translatedSeq));
				double[] subbedTvalues = x.solveforT(currentT, translatedSeq);
				double[] readyForGauss = x.solveForQuantifiers(subbedTvalues,quantifierNames.get(i));
				matrix[i]=readyForGauss;
			}
			
//			for(int l=0;l<matrix.length;l++) {
//				System.out.println("Matrix == "+Arrays.toString(matrix[l]));
//			}
//			
			double[] solvedForGauss = GaussianElimination.solver(matrix);
			
			//System.out.println("Solved =="+Arrays.toString(solvedForGauss));
			if(GaussianElimination.validSolutions(solvedForGauss)) {
				boolean satisfied=true;
				
				for(int i=0;i<leqList.size();i++) {
					String[] leq = leqList.get(i).clone(); //once again to avoid overwriting
					double[] sub2LEQ = x.subValuesToLEQ(solvedForGauss, currentT, leq);
					satisfied = inequalitySolver.leq(sub2LEQ);
					if(!satisfied) {
						//leq is not satisfied
						break;
					}
				}
				if(satisfied) {
					//is candidate solution
					ArrayList<Double> candidate = new ArrayList<Double>();
					for(double solution:solvedForGauss) {
						candidate.add(solution);
					}
					if(!candidateSolutions.contains(candidate)) {
						candidateSolutions.add(candidate);
						//System.out.println("Adding to solution "+Arrays.toString(solvedForGauss)+" at "+Arrays.toString(currentT));
					}
					else {
					//	System.err.println("Solution already in candidateSolutions");
					}
				}
				else {
					//System.err.println("Inconsistent solution at ="+Arrays.toString(solvedForGauss)+" at "+Arrays.toString(currentT));
					
				}
			}
			else {
				//System.err.println("Invalid solution at "+Arrays.toString(currentT) +" of "+Arrays.toString(solvedForGauss));
			}
			
			
		}
		ArrayList<String> dummyVarList = new ArrayList<String>();
		for(String j:x.variableSet) {
			for(String g:quantifierNames) {
				if(String.valueOf(g.charAt(3)).equals(j)) {
					dummyVarList.add(g);
					//System.out.println(g);
					break;
				}
			}
			
		}
		HashMap<ArrayList<String>,ArrayList<ArrayList<Double>>> returnResult = new HashMap<ArrayList<String>,ArrayList<ArrayList<Double>>>();
		returnResult.put(dummyVarList,candidateSolutions);
		
	//	for some reason order isnt mainted in quantifier names
		for(String b:quantifierNames) {
			//System.out.print(" hi =="+b);
		}
		return returnResult;
	}
	
	public ArrayList<Long> evaluate(muTerm x) {
		/*
		 * Here return the time taken for the algorithm, and for the heursitic, heuristic size, candidate size and candidate solutions time. return an arraylist
		 *
		 *Will first generate candidate solutions, take time for that
		 *Then apply the algorithm for those solutions and take time for that
		 *repeat for heuristic 
		 *check if success
		 *then calculate the reduction from the heuristic>
		 */
		ArrayList<Long> times = new ArrayList<Long>();
		//first take step
		long start = System.nanoTime();
		HashMap<ArrayList<String>,ArrayList<ArrayList<Double>>> quantifierAndSoltuion = generateCandidateSolutions(x);
		//now evaluate?
		long end1 = System.nanoTime();
		
	//	System.out.println("First step = "+(end1-start)/1000);
		
		ArrayList<ArrayList<Double>> candidateSolutions=null;
		ArrayList<String> quantifierNames=null;
		for(ArrayList<String> quantifiers:quantifierAndSoltuion.keySet()) {
			quantifierNames=quantifiers;
			candidateSolutions = quantifierAndSoltuion.get(quantifiers);
		//	System.out.println("QUANTIFIERS ="+Arrays.toString(quantifiers.toArray()));
		}
		if(candidateSolutions.size()==0) {
			//System.out.println("No candidate solutions");
			times.add(0l);
			times.add(0l);
			times.add(0l);
			times.add(0l);
			times.add(0l);
			return times;
		}
		//second time step
		//System.out.println("STARTING algorithm");
		long start2 = System.nanoTime();
 		ArrayList<Double> currentSolution = new ArrayList<Double>();
 		//ArrayList<Double> uniqueSolution = EvaluateAlgorithm.algorithm(candidateSolutions, 0, 0, currentSolution, quantifierNames);
 		ArrayList<ArrayList<Double>> uniqueSolution = EvaluateAlgorithm.algorithm(candidateSolutions, 0, 0, currentSolution, quantifierNames);
 		long end2 = System.nanoTime();
		//System.out.println("Second step = "+(end2-start2)/1000);

 		
 		long timeTaken = (end2-start2)+(end1-start);
 		times.add(timeTaken);
 		
 		//has a solution
// 		if(uniqueSolution.size()!=0) {
// 			times.add(1l);
// 		}
 	//	System.out.println("\nFinal sol ="+ Arrays.toString(uniqueSolution.toArray()));
 		//System.out.println("Time taken = "+timeTaken);

 		
 		//now time taken to evlaute the heuristic
 		long hTimeStart = System.nanoTime();
 		
 		//for only one heuristic 
 		//ArrayList<Double> heuristicSolution = EvaluateAlgorithm.heuristicAlgorithm(candidateSolutions, quantifierNames);
 		
 		//for multiuple heuristic
 		
 		ArrayList<ArrayList<Double>> heuristicSolution = EvaluateAlgorithm.heuristicAlgorithm(candidateSolutions, quantifierNames);
 		long hTimeEnd = System.nanoTime();
 		long timeTakenH =(end1-start)+(hTimeEnd-hTimeStart);
 		times.add(timeTakenH);
 		
 		
 		//adding the heuristic size and the candidate solution size
 		times.add((long) heuristicSolution.size());
 		times.add((long)candidateSolutions.size());

 	//	System.out.println("Heuristic size = "+heuristicSolution.size());
 	//	System.out.println("Cnadidate solution = "+candidateSolutions.size());
 		
 		
 		
 		//System.out.println("Success?? "+times.get(2));
 		//time taken to generate candidate solutions
 		times.add(end1-start);
 		//ArrayList<Double> heuristicSolution = EvaluateAlgorithm.heuristicAlgorithm(candidateSolutions, quantifierNames);
	//	System.out.println("Heuristic sol = "+Arrays.toString(heuristicSolution.toArray()));

// 		for(ArrayList<Double> sol:heuristicSolution) {
// 			System.out.println("Heuristic sol = "+Arrays.toString(sol.toArray()));
// 		}
// 		
 		return times;
 		
	}
//	public static void main(String[] args) {
//		//main example from paper
//		muTerm test1= new muTerm("mu",new muTerm("cup",new muTerm("v",
//				new muTerm(".",new muTerm("var",null,null,"y",""),
//						new muTerm("+",new muTerm("var",null,null,"x",""),
//								new muTerm("q",null,null,"","0.5"),"",""),"","")
//				,null,"y","")
//				,new muTerm("q",null,null,"","0.5"),"","")
//				,null,"x","");
//		
//		
//		muTerm test=new muTerm("mu",new muTerm("cup",
//				new muTerm("var",null,null,"x",""),
//				new muTerm("v",
//						new muTerm("cap",new muTerm("var",null,null,"x","1")
//								,new muTerm("var",null,null,"y",""),"",""),null,"y",""),"",""),null,"x","");
//		//System.out.println(x.toString(x));
//
//		
//		
////		muTerm test = new muTerm("mu",new muTerm("cap",new muTerm("q",null,null,"","0.460243"),
////				new muTerm("cup",new muTerm("var",null,null,"b",""),new muTerm("",null,null,"",""),"",""),"",""),null,"b","");
//////		ArrayList<ArrayList<Double>> candidateSolutions = Evaluation.generateCandidateSolutions(x);
////		
//// 		for(int i=0;i<candidateSolutions.size();i++) {
//// 			System.out.println("Candidate Solution "+i+" = "+Arrays.toString(candidateSolutions.get(i).toArray()));
//// 		}
//		Evaluation e = new Evaluation();
//		//muTerm test = muTermGenerator.generateMuTerms(2,1,3,10000000);
//		System.out.println(test.toString(test));
//
//		ArrayList<Long>times =e.evaluate(test);
// 		
//		System.out.println("Time taken +"+Arrays.toString(times.toArray()));
// 		//now the evalaution part?
// 		
//	}
}
