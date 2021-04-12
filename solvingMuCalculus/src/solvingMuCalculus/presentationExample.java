
package solvingMuCalculus;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class presentationExample {
	
	public static void evaluate(muTermVersion2 book1) {
		System.out.println("op counter= "+book1.opCounter);
		System.out.println("Var counter = "+book1.getVarLength());
		System.out.println(book1.toString(book1));
		System.out.println("Length == "+book1.getVarLength());
		muTermVersion2 singleSequence = book1.generateSequences(book1, true,book1.getVarLength());
		System.out.println(singleSequence.singleQuantifier);
		
		
		//this is in case of no quantifiers
		if(book1.sequences.size()==0) {
			book1.addSequence(book1.singleQuantifier, singleSequence);

		}
		
		//this prints the sequnce of equations to solve
		Hashtable<String,muTermVersion2> sequences = book1.sequences;
		System.out.println("Sequences length = "+sequences.size()+" ");

		System.out.println("variables are = "+book1.varList);
		
		
		
		
//		String[] values = new String[book1.varList.size()+1];
//		Arrays.fill(values, "0");
//		values = book1.translatedTerms(new muTermVersion2(), values,0);
//		
		ArrayList<String[]> equations = new ArrayList<String[]>();
		
		for(String key:sequences.keySet()) {
			System.out.println(key);
			String[] values = new String[book1.varList.size()+1];
			Arrays.fill(values, "0");
			values = book1.translatedTerms2(sequences.get(key), values, book1.tTracker);
			equations.add(values);
			System.out.println("TESTINGGG"+Arrays.toString(values));
		}
		
		ArrayList<String> keys = new ArrayList<String>();
		for(String k:sequences.keySet()) {
			System.out.println("quantifeirs =="+k+" =="+ book1.toString(sequences.get(k)));
			keys.add(k); 
		}
		
	//	System.out.println("Equations = "+Arrays.toString(equations.get(0)));
	//	System.out.println("Equations = "+Arrays.toString(equations.get(1)));

		book1.setVarArray();

		book1.generateLeq();
		ArrayList<String[]> leqList = book1.getLEQ();
		for(String[] a:leqList) {
			System.out.println("LEQ:  "+Arrays.toString(a));
		}
		
		
		
		int[] arr = new int[book1.opCounter];
		variableT varT = new variableT();
		varT.generateAllBinaryStrings(book1.opCounter, arr, 0);
		List<ArrayList<Integer>> tListAll = varT.getPermutations();
	
		ArrayList<ArrayList<Double>> candidateSolutions = new ArrayList<ArrayList<Double>>();
		
 		for(ArrayList<Integer> tList:tListAll) {
			int[] ts = new int[tList.size()];
			for(int i=0;i<ts.length;i++) {
				ts[i]=tList.get(i);
			}
			//generated ts
			double[][] matrix = new double[equations.size()][equations.get(0).length];
			
			for(int i=0;i<equations.size();i++) {
				String[] values = equations.get(i).clone();
			//	System.out.println(Arrays.toString(values));

				double[] subbed = book1.solveForT(ts,values);
				double[] ready2solve = book1.solveLHS(subbed,keys.get(i));
				matrix[i] = ready2solve;
				System.err.println("ready2Solve = "+Arrays.toString(ready2solve)+"   "+Arrays.toString(values));

			}
		
			//printing matrix and the t
			
			
			//this solves it, so rhs of the matrix
			double[] solved = GaussianElimination.solver(matrix);
			//System.out.println(Arrays.toString(solved)+"  ts= "+Arrays.toString(ts)+"\n");
			//System.out.println(Arrays.toString(solved) +"   "+Arrays.toString(matrix[0])+" "+Arrays.toString(matrix[1])+" "+Arrays.toString(ts));
			
			//System.out.println(GaussianElimination.validSolutions(solved) +" is NAAN?");
			if(GaussianElimination.validSolutions(solved)) {
				boolean satisfied= true;
				
				for(int i=0;i<leqList.size();i++) {
					String[] a = leqList.get(i).clone();
					double[] subbed = book1.subToLeq(solved, ts,a);
					satisfied = inequalitySolver.leq(subbed);
					if(!satisfied) {
						break;
					}
				}
				if(satisfied) {
					System.out.println("Candidate Solution = "+Arrays.toString(solved)+" and ts = "+Arrays.toString(ts));
					ArrayList<Double> candidate = new ArrayList<Double>();
					for(double x:solved) {
						candidate.add(x);
					}
					if(!candidateSolutions.contains(candidate))
						candidateSolutions.add(candidate);
					else
						System.err.println("Solutions already contains "+ Arrays.toString(candidate.toArray()) +" ts="+Arrays.toString(ts));
				}
				else {
					System.err.println("Inconsistent at solution = "+Arrays.toString(solved)+" and ts = "+Arrays.toString(ts));
				}
			}
			else {
				System.err.println("Invalid Solution for "+Arrays.toString(ts)+"   "+Arrays.toString(solved));
			}	
		}
 		
 		for(ArrayList<Double> x:candidateSolutions) {
 			System.out.println("Soltuions = "+Arrays.toString(x.toArray()));
 		}
 		
	}
	public static void main(String[] args) {
//		
//		muTermVersion2 example = new muTermVersion2("mu",new muTermVersion2("cap",new muTermVersion2("var",null,null,"x",""),
//				new muTermVersion2("v",new muTermVersion2("cup",new muTermVersion2("var",null,null,"x",""),
//						new muTermVersion2("var",null,null,"y",""),"",""),null,"y",""),"","")
//				,null,"x","");
//		
//		
//		evaluate(example);
//		System.out.println("\n\n");
//		 
//		muTermVersion2 example2 = new muTermVersion2("mu",new muTermVersion2("cup",new muTermVersion2("var",null,null,"x",""),
//				new muTermVersion2("v",new muTermVersion2("cap",new muTermVersion2("var",null,null,"x",""),
//						new muTermVersion2("var",null,null,"y",""),"",""),null,"y",""),"","")
//				,null,"x","");
//		evaluate(example2);
//	
//		muTermVersion2 example3 = new muTermVersion2("mu",new muTermVersion2("+",
//				new muTermVersion2("v",new muTermVersion2("cup",new muTermVersion2("var",null,null,"y",""),
//						new muTermVersion2("cap",new muTermVersion2("var",null,null,"x",""),
//								new muTermVersion2("q",null,null,"","0.5"),"",""),"",""),null,"y","")
//				,new muTermVersion2("q",null,null,"","0.5"),"",""),null,"x","");
		
		//muTermVersion2 example3 = new muTermVersion2("mu",new muTermVersion2("+",new muTermVersion2("var",null,
			//	null,"x",""),new muTermVersion2("q",null,null,"","0.5"),"",""),null,"x","");
	
//		muTermVersion2 book1 = new muTermVersion2("mu",new muTermVersion2("cup",new muTermVersion2("v",new muTermVersion2(".",
//				new muTermVersion2("var",null,null,"y",""),new muTermVersion2("+", new muTermVersion2("var",null,null,"x",""),
//						new muTermVersion2("q",null,null,"","0.5"),"",""),"",""),null,"y",""),
//				new muTermVersion2("q",null,null,"","0.5"),"x",""),null,"x","");
		muTermVersion2 xx=new muTermVersion2("mu",new muTermVersion2("cup",
				new muTermVersion2("var",null,null,"x",""),
				new muTermVersion2("v",
						new muTermVersion2("cap",new muTermVersion2("var",null,null,"x","")
								,new muTermVersion2("var",null,null,"y",""),"",""),null,"y",""),"",""),null,"x","");
		
		
		muTermVersion2 x = new muTermVersion2("mu",
				new muTermVersion2("cup",
						new muTermVersion2("v",
								new muTermVersion2(".",new muTermVersion2("var",null,null,"y","")
										,new muTermVersion2("+",new muTermVersion2("var",null,null,"x","")
												,new muTermVersion2("q",null,null,"","0.5"),"",""),"","")
								,null,"y","")
						,new muTermVersion2("q",null,null,"","0.5"),"","")
			,null,"x","");
		
		evaluate(x);
	
	}

}

