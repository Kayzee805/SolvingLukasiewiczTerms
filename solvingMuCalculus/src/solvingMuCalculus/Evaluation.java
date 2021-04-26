
package solvingMuCalculus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Hashtable;
import java.util.List;

public class Evaluation {
	Evaluation(){};

	public LinkedHashMap<ArrayList<String>,ArrayList<ArrayList<Double>>>  generateCandidateSolutions(muTerm x){
		ArrayList<ArrayList<Double>> candidateSolutions = new ArrayList<ArrayList<Double>>();
		muTerm singleSequence = x.generateSequenceEquation(x, true, x.variableSet.size());
		
		//so If only 1 quantifier, we add the entire term to the list of eqs
		if(x.seqeunceOfEquations.size()==0) {
			x.seqeunceOfEquations.put(x.singleQuantifier,singleSequence);
		}
		
		LinkedHashMap<String, muTerm> sequenceOfEquations = x.seqeunceOfEquations;
		//System.out.println("Sequence length ="+sequenceOfEquations.size());
		
		
		ArrayList<String[]> translatedEquations = new ArrayList<String[]>();
		ArrayList<String> quantifierNames = new ArrayList<String>();
		//adding the translated terms into arraylist
		//one for each sequence
		System.out.println();

		for(String key:sequenceOfEquations.keySet()) {
			System.out.println("Equation:: "+key+"="+x.toString(sequenceOfEquations.get(key)));
		}
		System.out.println();
		for(String key:sequenceOfEquations.keySet()) {
			String[] values = new String[x.variableSet.size()+1];
			Arrays.fill(values, "0");
			values = x.translateTerms(sequenceOfEquations.get(key), values,x.tTracker);
			translatedEquations.add(values);
			System.out.println("Translated equation:: = "+key+"  ="+x.toString(sequenceOfEquations.get(key))+" -> "+Arrays.toString(values));
			quantifierNames.add(key);
		}
		ArrayList<String[]>leqList = x.getLEQ();
		
		
		//printing leqlist
		int leqCounter=0;
		System.out.println();

		for(String[] a:leqList) {
			System.out.println("LEQ "+leqCounter+"="+Arrays.toString(a));
			leqCounter++;
		}
//		
		//generates all permutation of T for m operators
		int[] emptyArrayforT = new int[x.opCounter];
		variableT tClass = new variableT();
		tClass.generateAllBinaryStrings(x.opCounter,emptyArrayforT,0);
		List<ArrayList<Integer>> allTpermutations =tClass.getPermutations(); 
		
		x.setToArray();
		System.out.println("Generating candidate solutions now:\n");

		for(ArrayList<Integer>tList:allTpermutations) {
			//just putting it to an array, can probably use toArray
			int[] currentT = new int[tList.size()];
			for(int i=0;i<currentT.length;i++) {
				currentT[i]=tList.get(i);
			}
			
			double[][] matrix = new double[translatedEquations.size()][translatedEquations.get(0).length];
			boolean[] equationReduced = new boolean[translatedEquations.size()];
			for(int i=0;i<translatedEquations.size();i++) {
				//so original array wont change
				String[] translatedSeq = translatedEquations.get(i).clone();
				double[] subbedTvalues = x.solveforT(currentT, translatedSeq);
				double[] readyForGauss = x.solveForQuantifiers(subbedTvalues,quantifierNames.get(i));
				
				if(readyForGauss[x.getIndexSet(quantifierNames.get(i).substring(3))]==0) {
					equationReduced[i]=true;
				}
				else {
					equationReduced[i]=false;
				}
				matrix[i]=readyForGauss;
					

			}
			
			for(int l =0;l<matrix.length;l++) {
				if(equationReduced[l]) {
					//been reduced
					//check if there exists 1 in the matrix
					int index = x.getIndexSet(quantifierNames.get(l).substring(3));
					boolean allZero=true;
					for(int z=0;z<matrix.length;z++) {
						if(matrix[z][index]!=0) {
							allZero=false;
							break;
						}
					}
					if(allZero) {
						matrix[l][index]=1;
					}
				}
			}
			
			for(int l=0;l<matrix.length;l++) {
				System.out.println("Matrix == "+Arrays.toString(matrix[l]));
			}
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
						System.err.println("Adding to solution "+Arrays.toString(solvedForGauss)+" at "+Arrays.toString(currentT)+"\n");
					}
					else {
						System.err.println("Solution already in candidateSolutions at "+Arrays.toString(currentT)+"\n");
					}
				}
				else {
					System.err.println("Inconsistent solution at ="+Arrays.toString(solvedForGauss)+" at "+Arrays.toString(currentT) +" with solutions= "+Arrays.toString(solvedForGauss)+"\n");
					
				}
			}
			else {
				System.err.println("Invalid solution at "+Arrays.toString(currentT) +" of "+Arrays.toString(solvedForGauss)+"\n");
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
		LinkedHashMap<ArrayList<String>,ArrayList<ArrayList<Double>>> returnResult = new LinkedHashMap<ArrayList<String>,ArrayList<ArrayList<Double>>>();
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
		long start = System.nanoTime();
		LinkedHashMap<ArrayList<String>,ArrayList<ArrayList<Double>>> quantifierAndSoltuion = generateCandidateSolutions(x);
		//now evaluate?
		long end1 = System.nanoTime();
		
		
		ArrayList<ArrayList<Double>> candidateSolutions=null;
		ArrayList<String> quantifierNames=null;
		for(ArrayList<String> quantifiers:quantifierAndSoltuion.keySet()) {
			quantifierNames=quantifiers;
			candidateSolutions = quantifierAndSoltuion.get(quantifiers);
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
 		
 		
 		times.add((long) heuristicSolution.size());
 		times.add((long)candidateSolutions.size());

 		System.err.println("\nPrinting solutions now:\n");
 		System.out.println("Quantifiers = "+Arrays.toString(quantifierNames.toArray()));

 		System.out.println("Cnadidate solution = "+candidateSolutions.size());
 		
 		for(int c = 0;c<candidateSolutions.size();c++) {
 			System.out.println("Candidate Solution "+c+" ="+Arrays.toString(candidateSolutions.get(c).toArray()));
 		}
 		
 		times.add(end1-start);

 		System.out.println("\nUnique solution of recursive algorithm ="+ Arrays.toString(uniqueSolution.get(0).toArray()));

 		int heuristicCounter=0;
 		for(ArrayList<Double> sol:heuristicSolution) {
 			System.out.println("Heuristic solution "+heuristicCounter+"= "+Arrays.toString(sol.toArray()));
 			heuristicCounter++;
 		}
 		System.err.println("Number of candidate solutions reduced from "+candidateSolutions.size()+" -> "+heuristicSolution.size());
 		
 		return times;
 		
	}
	public static void main(String[] args) {
		//main example from paper
		muTerm test1= new muTerm("mu",new muTerm("cup",new muTerm("v",
				new muTerm(".",new muTerm("var",null,null,"y",""),
						new muTerm("+",new muTerm("var",null,null,"x",""),
								new muTerm("q",null,null,"","0.5"),"",""),"","")
				,null,"y","")
				,new muTerm("q",null,null,"","0.5"),"","")
				,null,"x","");
		
		
		muTerm test2=new muTerm("mu",new muTerm("cup",
				new muTerm("var",null,null,"x",""),
				new muTerm("v",
						new muTerm("cap",new muTerm("var",null,null,"x","1")
								,new muTerm("var",null,null,"y",""),"",""),null,"y",""),"",""),null,"x","");

		muTerm test=new muTerm("mu",new muTerm("cap",
				new muTerm("var",null,null,"x",""),
				new muTerm("v",
						new muTerm("cup",new muTerm("var",null,null,"x","1")
								,new muTerm("var",null,null,"y",""),"",""),null,"y",""),"",""),null,"x","");

		

		Evaluation e = new Evaluation();
	//	muTerm test = muTermGenerator.generateMuTerms(1,1,3,10);
		System.out.println(test.toString(test));

		ArrayList<Long>times =e.evaluate(test);
 		
		System.out.println("Time taken +"+Arrays.toString(times.toArray()));
 		//now the evalaution part?
 		
	}
}
