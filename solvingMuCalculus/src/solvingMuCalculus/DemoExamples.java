package solvingMuCalculus;

import java.util.ArrayList;

public class DemoExamples {
	
	public static void main(String[] args) {
		
		int chooseExample =0;
		muTerm exampleTerm;
		//main examples from paper

		if(chooseExample==0) {
			exampleTerm= new muTerm("mu",new muTerm("cup",new muTerm("v",
					new muTerm(".",new muTerm("var",null,null,"y",""),
							new muTerm("+",new muTerm("var",null,null,"x",""),
									new muTerm("q",null,null,"","0.5"),"",""),"","")
					,null,"y","")
					,new muTerm("q",null,null,"","0.5"),"","")
					,null,"x","");
			
		}
		else if(chooseExample==1) {
			exampleTerm=new muTerm("mu",new muTerm("cup",
					new muTerm("var",null,null,"x",""),
					new muTerm("v",
							new muTerm("cap",new muTerm("var",null,null,"x","1")
									,new muTerm("var",null,null,"y",""),"",""),null,"y",""),"",""),null,"x","");
		}
		else if(chooseExample==2){

			exampleTerm=new muTerm("mu",new muTerm("cap",
					new muTerm("var",null,null,"x",""),
					new muTerm("v",
							new muTerm("cup",new muTerm("var",null,null,"x","1")
									,new muTerm("var",null,null,"y",""),"",""),null,"y",""),"",""),null,"x","");

			
		}
		else {
			exampleTerm = muTermGenerator.generateMuTerms(2,2,5,10);

		}

		



		Evaluation e = new Evaluation();
	 	System.out.println("e = "+exampleTerm.toString(exampleTerm));

		ArrayList<Long>times =e.evaluate(exampleTerm);

	//	System.out.println("Time taken ="+Arrays.toString(times.toArray()));

	}

}
