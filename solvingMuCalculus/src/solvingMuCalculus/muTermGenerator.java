package solvingMuCalculus;

import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
public class muTermGenerator {
	
	muTermGenerator(){};
	static String[] varList = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","u","v","w",
			"x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","U","V","W","X","Y","Z"};

	static Random random = new Random();
	static String[] ops = {"cup","cap","+","."};
	
	public static int gcd(int a, int b) {
		//a has to be greater
		   if (b==0) return a;
		   return gcd(b,a%b);
	}
	
	public static muTerm generateMuTerms(int muNumber,int nuNumber, int opLimit, int upperBound) {
	//	System.out.println(muNumber+" "+nuNumber);
		ArrayList<muTerm> subterms = new ArrayList<muTerm>();
		int varNumber = muNumber+nuNumber;
		int randSeed = random.nextInt();
		random.setSeed(1541735597);
	//	random.setSeed(randSeed);

		System.out.println("Random seed = "+randSeed);
		muTerm term = new muTerm("q",null,null,"","0.5");
		ArrayList<String> variables = new ArrayList<String>();
		for(int i=0;i<varNumber;i++) {
			muTerm temp = new muTerm("var",null,null,varList[i],"");
			variables.add(varList[i]);
			subterms.add(temp);
		}
		
		int muCounter=0,nuCounter=0,varCounter=0,iteration=0,opCounter=0;
		int highestRational=2;
		term.highestRational=2;
		//because of the half that we added
		int freeVariableIndex = varNumber;
		Set<String> quantifierNames = new HashSet<String>();
		Set<String> variableNames = new HashSet<String>();
		int freeCounter=0;
		int freeLimit =1;
		//while(term.quantifierSize!=varNumber && (muNumber+nuNumber)!=(muCounter+nuCounter)) {
		while(opCounter<opLimit && term.quantifierSize!=varNumber) {
			//System.out.println(iteration);
			//this way we can add free vars as well
			String op = term.operators[random.nextInt(term.listOfOps.size())];
			//System.out.println("op == "+op);
		//	if(op.equals("p"))continue;
			
			if(iteration%5==0 && opCounter<opLimit && variableNames.size()<varNumber) {
				//Just adding a operator, . + cup or cap
				
				opCounter++;
				op=ops[random.nextInt(ops.length)];
				String varName = variables.get(random.nextInt(variables.size()));
				int position = random.nextInt(2);
				int randomNumerator = random.nextInt(upperBound);
				while(randomNumerator==0) {
					randomNumerator = random.nextInt(upperBound);
				}
				int tempGcd = gcd(upperBound,randomNumerator);
				int dummyUpper = upperBound/tempGcd;
				double randomNumber = (1.0*randomNumerator)/upperBound;
				
				if(dummyUpper>highestRational)highestRational=dummyUpper;
				muTerm s1 = new muTerm("q",new muTerm("var",null,null,varName,""),null,"",String.valueOf(randomNumber));
				variableNames.add(varName);
				if(position==1) {
					term = new muTerm(op,s1,term,"","");
				}
				else {
					term = new muTerm(op,term,s1,"","");
				}
				iteration++;
			}
			else if((op.equals("mu") || iteration%5==0) && muCounter!=muNumber && iteration!=0) {
			//	System.err.println("mu = "+iteration);
				op = "mu";
				term = new muTerm(op,term,null,varList[varCounter],"");
				quantifierNames.add(varList[varCounter]);
				//System.out.println("Mu = "+varList[varCounter]);
				varCounter++;
				muCounter++;
				iteration++;

			}
			else if((op.equals("v") || iteration%7==0) && nuCounter !=nuNumber&& iteration!=0) {
			//	System.err.println("nu = "+iteration);
				op="v";
				term = new muTerm(op,term,null,varList[varCounter],"");
				quantifierNames.add(varList[varCounter]);
			//	System.out.println("nu = "+varList[varCounter]);

				varCounter++;
				nuCounter++;
				iteration++;

			}
			else if(op.equals("q") && (term.op.equals("var"))) {
				//System.err.println(term.op +" is the sub");
				//System.out.println("TEST2 "+randomNumber);
				int randomNumerator = random.nextInt(upperBound);
				while(randomNumerator==0) {
					randomNumerator = random.nextInt(upperBound);
				}
				
				int tempGcd = gcd(upperBound,randomNumerator);
				int dummyUpper = upperBound/tempGcd;
				//System.err.println(tempGcd+ " "+randomNumerator+" "+upperBound+"  newRational= "+highestRational);

				double randomNumber = (1.0*randomNumerator)/upperBound;
				if(dummyUpper>term.highestRational) {
					term.highestRational=dummyUpper;
				}
				//muTermVersion2 temp = new muTermVersion2("q",null,null,"",String.valueOf(randomNumber));
				term = new muTerm("q",term,null,"",String.valueOf(randomNumber));
				iteration++;

				//subterms.add(temp);
			}
			else if(op.equals("q")) {
				
				int randomNumerator = random.nextInt(upperBound);
				while(randomNumerator==0) {
					randomNumerator = random.nextInt(upperBound);
				}
				
				int tempGcd = gcd(upperBound,randomNumerator);
				int dummyUpper = upperBound/tempGcd;
				
				//System.err.println(tempGcd+ " "+randomNumerator+" "+upperBound+"  newRational= "+highestRational);

				double randomNumber = (1.0*randomNumerator)/upperBound;
				muTerm temp = new muTerm("q",null,null,"",String.valueOf(randomNumber));
				temp.highestRational=dummyUpper;
				//temp.highestRational=(dummyUpper);
				subterms.add(temp);
				iteration++;

			}
			else if((op.equals("cup") || op.equals("cap")|| op.equals("+")|| op.equals(".")) && opCounter<opLimit) {
				opCounter++;
				int randomIndex = random.nextInt(subterms.size());
				muTerm randomTerm = subterms.get(randomIndex);
				if(randomTerm.op.equals("q")) {
					if(randomTerm.highestRational>term.highestRational) {
						highestRational = randomTerm.highestRational;
						term.highestRational=(randomTerm.highestRational);
					}

				}
				else if(randomTerm.op.equals("var")) {
					variableNames.add(randomTerm.variableName);
				}
				else if(randomTerm.op.equals("p") && freeCounter<freeLimit) {
					//dont need to add it to variable names as thats for bound only
					if(term.highestRational<randomTerm.highestRational) {
						//??
						highestRational = randomTerm.highestRational;
						term.highestRational=randomTerm.highestRational;
					}
					freeCounter++;
				}
				else {
					continue;
				}
			
			
				if(opCounter+randomTerm.opCounter <opLimit) {
				//subterms.remove(randomIndex);
					opCounter+= randomTerm.opCounter;
				
				int position = random.nextInt(2);
				if(position==1) {
					term = new muTerm(op,randomTerm,term,"","");
				}
				else {

					term = new muTerm(op,term,randomTerm,"","");
				}
				//System.out.println("First subterm = "+randomTerm.toString(randomTerm));

				iteration++;

				}

				//subterms.add(term);
			}
			else if(op.equals("p") && iteration%7==0 && freeCounter<freeLimit) {
				//is free var
				int randomNumerator = random.nextInt(upperBound);
				while(randomNumerator==0) {
					randomNumerator = random.nextInt(upperBound);
				}
				
				int tempGcd = gcd(upperBound,randomNumerator);
				int dummyUpper = upperBound/tempGcd;
				
				//System.err.println(tempGcd+ " "+randomNumerator+" "+upperBound+"  newRational= "+highestRational);

				double randomNumber = (1.0*randomNumerator)/upperBound;
				muTerm tempTerm = new muTerm("p",null,null,varList[freeVariableIndex],String.valueOf(randomNumber));
				freeVariableIndex++;
				tempTerm.highestRational=dummyUpper;
				subterms.add(tempTerm);
				iteration++;
				//freeCounter++;

			}
			
		}
		String[] quantifiers = {"mu","v"};
		//System.out.println("final ="+term.varList.size()+" "+varNumber);
	//	System.err.println("Iteration = "+iteration);

		//System.out.println("Mu need and got = "+muNumber+" "+muCounter+"\nNu needed and got ="+nuNumber+" "+nuCounter);
		while(muCounter!=muNumber || nuCounter!=nuNumber) {
			String op="";
			if(muNumber==muCounter){
				//do nu 
				op = "v";
				nuCounter++;
			}
			else if(nuNumber==nuCounter) {
				//do mu
				op = "mu";
				muCounter++;
			}
			else {
				//both are not full
				op =quantifiers[random.nextInt(quantifiers.length)];
				if(op=="mu") {
					if(muNumber==muCounter)
						continue;
					else {
						muCounter++;
					}
					
				}
				else {
					if(nuNumber==nuCounter)
						continue;
					else {
						nuCounter++;
					}
					
				}
				
			}
			
		
		//	System.out.println("Adding "+varList[varCounter]);
			//System.out.println("op = "+op);
			term = new muTerm(op,term,null,varList[varCounter],"");
			varCounter++;

			if(varCounter==varNumber) {
			//	System.err.println("NEVER");
				break;
			}
		}
		term.highestRational=highestRational;
		return term;
		
	}
	public static void main(String[] args) {
		muTerm term = generateMuTerms(2,1,10,100);	
		System.out.println(term.toString(term));
		//write(term);
	}
	
}
