
  
package solvingMuCalculus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class muTermVersion2 implements Serializable {
	String op;
	String varName;
	String value;
	muTermVersion2 subEx1;
	muTermVersion2 subEx2;
	int opCounter=0;
	int varCounter=0;
	int quantifierSize = 0;
	//Set<String> variableTracker = new HashSet<String>();
	String[] listOfOps = new String[] {".","+","cap","cup","var","p","mu","v","q"};
	List<String> ops = Arrays.asList(listOfOps);
	Set<String> varList = new HashSet<String>();
	String[] varListArray;
	int highestRational=0;
	public muTermVersion2() {
		this.op = "";
		this.varName="";
		this.value = "";
		this.subEx1=null;
		this.subEx2 = null;
	}
	public muTermVersion2(String op,muTermVersion2 subEx1,muTermVersion2 subEx2,String varName,String value) {
	
		if(ops.contains(op))this.op=op;
		else {
			System.err.println("Please enter a valid operator from the following list.\n"+Arrays.deepToString(listOfOps)+"  and not "+op);
			//i want to exit the program here
			System.exit(0);

		}
		if(this.op.equals("var")) {
			//check if var name has a thing
			if(varName==null || varName.isEmpty() || varName.length()==0) {
				System.err.println("Variable needs a name.");
				
				System.exit(0);

			}
			else {
				this.varName=varName;
				this.value = value;
				this.subEx1 = subEx1;
				this.subEx2=null;
				this.opCounter =0;
				this.varList.add(varName);
			}
			
		}
		else if(this.op.equals("p")) {
			if(subEx1==null || !subEx1.op.equals("var")) {
				System.err.println("Interpretation p needs a subexpression, which must be of type variable");
				System.exit(0);

			}
			if(value=="" ||value.length()==0 ||value.isEmpty()) {
				System.err.println("Interpretation p needs a value.");
				System.exit(0);;
				
			}
			//is valid
			this.subEx1=subEx1;
			this.subEx2=null;
			this.value=value;
			this.varName="";
			this.varList.addAll(subEx1.varList); //even tho it will be the same?
			this.opCounter=0;

		
		}
		else if(this.op.equals("mu") || this.op.equals("v")){
			//check if it has a sub expression and a name e.g. mux or muy.
			//subexpression so  vx(1 . 2)muy as this doesnt make sense
			if(subEx1==null) {
				System.err.println("Quantifier needs a subexpression. Cannot end on a quantifier");
				System.exit(0);

			}
			else {
				if(varName==null || varName.isEmpty() || varName.length()==0) {
					System.err.println("Operator needs a variable name. e.g. vx or vy  where v is the quantifier");
					System.exit(0);

				}
				else {
					this.varName=varName;
					this.subEx1=subEx1;
					this.value = null;
					this.subEx2=null;
					this.opCounter =subEx1.opCounter;
					this.varList.add(varName);
					this.varList.addAll(subEx1.varList);
					
				

				}
				
			}
		
		}
		else if(this.op.equals(".")) {
			//this is circleDot
			//takes two sub terms, dont need var name and value
			if(subEx1==null || subEx2==null) {
				System.err.println("Circle dot needs two subterms");
				System.exit(0);

			}
			else {
				this.subEx1=subEx1;
				this.subEx2=subEx2;
				this.varName=null;
				this.value=null;
				this.opCounter = subEx1.opCounter+subEx2.opCounter+1;
				this.varList.addAll(subEx1.varList);
				this.varList.addAll(subEx2.varList);
			}
		}
		else if(this.op.equals("+")) {
			//same as circle dot
			if(subEx1==null || subEx2==null) {
				System.err.println("Circle PLUS needs two subterms");
				System.exit(0);

			}
			else {
				this.subEx1=subEx1;
				this.subEx2=subEx2;
				this.varName=null;
				this.value=null;
				this.opCounter = subEx1.opCounter+subEx2.opCounter+1;
				this.varList.addAll(subEx1.varList);
				this.varList.addAll(subEx2.varList);
			}
		}
		else if(this.op.equals("cap")) {
			//this is n the upsidedown cup
			//this needs two subterms only
			if(subEx1==null || subEx2==null) {
				System.out.println(subEx1.toString(subEx1));
				System.out.println("First subterm = "+subEx1.op);
				System.out.println("Seconds subterm = "+subEx2.op);
				System.err.println("CAP needs two subterms");
				System.exit(0);

			}
			else {
				this.subEx1=subEx1;
				this.subEx2=subEx2;
				this.varName=null;
				this.value=null;
				this.opCounter = subEx1.opCounter+subEx2.opCounter+1;
				this.varList.addAll(subEx1.varList);
				this.varList.addAll(subEx2.varList);
			}
		}
		else if(this.op.equals("cup")) {
			//same as cap
			if(subEx1==null || subEx2==null) {
				System.err.println("CUP needs two subterms");
				System.exit(0);

			}
			else {
				this.subEx1=subEx1;
				this.subEx2=subEx2;
				this.varName=null;
				this.value=null;
				this.opCounter = subEx1.opCounter+subEx2.opCounter+1;
				this.varList.addAll(subEx1.varList);
				this.varList.addAll(subEx2.varList);
			}
		}
		else if(this.op.equals("q")) {		//rational number 0,1
			//so r == rational number?
			//need to check for its value and thats it
			//so for a number like mux(x. 0.5) would be
//muTerm("mu",muTerm(".",muTerm("r",null,null,0.5),muTerm("var",null,null,"x"),null),null,"x")
			
			if(value.length()==0 || value==null || value.isEmpty()) {
				System.out.println("Rational number needs a value");
			}
			else {
				if(value.length()>0) {
					//cos idk how to input stuff like 1/2 or 1/4 yet
				//if(value.equals("1") || value.equals("0")) {
					
					if(subEx1==null) {
						this.subEx1=null;
						this.opCounter =0;

					}
					else {
						this.subEx1 = subEx1;
						this.varList.addAll(subEx1.varList);
						this.opCounter =subEx1.opCounter;

					}
					//System.err.println("Value = "+value+"  "+op);
					this.value = value;
				//	this.subEx1=subEx1;
					this.subEx2=null;
					this.varName = null;

				}
				else {
					System.err.println(value);
					System.err.println(value.equals("0"));
					System.err.println("Rational Number has to be either 0 or 1");
					System.exit(0);
				}
				//I now need to check for possible subterms?
		

			}
		}
			else if(this.op.equals("r")) {
				if(value.length()==0 || value==null || value.isEmpty()) {
					System.out.println("Rational number needs a value");
					System.exit(0);
				}
				else {
					//System.err.println("Value = "+value+"  "+op);

					this.value = value;
					this.subEx1=null;
					this.subEx2=null;
					this.varName=null;
					this.opCounter =0;

					
				}
			}
		
			
		else {
			System.err.println("Operator is not valid"); //this is checked at the start tho
		}
	}
	public void highestRationalSetter(int x) {
		this.highestRational=x;
	}
	public int getHighestRational() {
		return this.highestRational;
	}
	public int getVarLength() {
		return varList.size();
	}
	public void setVarArray() {
		varListArray = varList.toArray(new String[varList.size()]);
	}
	
	Hashtable<String,muTermVersion2> sequences = new Hashtable<String,muTermVersion2>();
	public void addSequence(String key,muTermVersion2 x) {
		sequences.put(key,x);
	}
	public Hashtable<String,muTermVersion2> getSequences(){
		return sequences;
	}
	String singleQuantifier = "";
	boolean addedQuantifer = false;
	public muTermVersion2 generateSequences(muTermVersion2 x,boolean start,int varLength) {
		if(x==null)return null;
		/*
		 * I can assume that if there are no quantifier, it should mean that there are 0 variables.
		 * If no quantifier, just create a new mux or vx and use the entire term as a subexpression of that quantifier
		 * and solve for that quantifier.
		 */
		//System.out.println(x.getVarLength());
	//	System.out.println(varLength);
		if(varLength==0) {
			//this will ensure that if no quantifier, it'll make one with a varname "x"
			//cannot solve 
			//System.out.println(x.getVarLength());
			muTermVersion2 dummyTerm = new muTermVersion2("mu",x,null,"x","0");
			//System.out.println("new term q size = "+dummyTerm.quantifierSize);
			addedQuantifer=true;
			x.varList.add("x");
			//System.out.println("VarList size = "+dummyTerm.varList.size());
			return generateSequences(dummyTerm,start,1);
		}
		else if(varLength==1 &&( (x.op.equals("mu") | x.op.equals("v")))) {
			//here I will have a different way of handling mu and v.
			//I will just return the variable and add the mu part as a string for the key
			//have to consider all previous terms
			
			if(x.op.equals("mu")) {
				//System.out.println("MUUUUUUU");

				//I will add it to the list of quantifier at the end
				singleQuantifier = "mu."+x.varName;
				//System.out.println("Single quantifier = "+singleQuantifier +" "+x.varList.size());
				muTermVersion2 e1 = generateSequences(x.subEx1,start,1);
				System.out.println("ERROR HEREE?"+x.subEx1.op);
				
				if(addedQuantifer) {
					return e1;
				}
				return e1;
				//return new muTermVersion2("var",e1,null,x.varName,"");
			}
			else {
				//op equals v
				singleQuantifier = "nu.."+x.varName;
				muTermVersion2 e1 = generateSequences(x.subEx1,start,1);
				if(addedQuantifer)
					return e1;
				return new muTermVersion2("var",e1,null,x.varName,"");
				
			}
		}
		else if(x.op.equals("mu")) {
			//System.err.println("ITS MU AND "+start);

			if(start) {
				muTermVersion2 e1 = generateSequences(x.subEx1,false,varLength);
				sequences.put("mu."+x.varName,e1);
				return x;
			}
			else {
				muTermVersion2 e1 = generateSequences(x.subEx1,false,varLength);
				sequences.put("mu."+x.varName, e1);
				return new muTermVersion2("var",null,null,x.varName,"");
			}
			
		}
		else if(x.op.equals("v")) {

			if(start) {
				muTermVersion2 e1 = generateSequences(x.subEx1,false,varLength);
				sequences.put("nu."+x.varName,e1);
				return x;
			}
			else {
				muTermVersion2 e1 = generateSequences(x.subEx1,false,varLength);
				sequences.put("nu."+x.varName, e1);
				return new muTermVersion2("var",null,null,x.varName,"");
			}
		}
		
		else if(x.op.equals("var") || x.op.equals("r")) {
			return x;
		}
		else if(x.op.equals("q")) {
			
			if(subEx1==null) {
				return new muTermVersion2("q",null,null,"",x.value);
			}
			muTermVersion2 e1 = generateSequences(x.subEx1,start,varLength);
			return new muTermVersion2("q",e1,null,x.varName,x.value);
		}
		
		else if(x.op.equals("+") || x.op.equals(".") || x.op.equals("cap") || x.op.equals("cup")){
			
			muTermVersion2 e1 = generateSequences(x.subEx1,start,varLength);
			muTermVersion2 e2 = generateSequences(x.subEx2,start,varLength);
			return new muTermVersion2(x.op,e1,e2,null,null);
			
		}

		else {
			System.err.println("Operator type not valid");
			System.exit(0);
			return null;
		}
	}
	
	//create a toString method to display what it looks like. 
	public String toString(muTermVersion2 x) {
		if(x==null)return "";
		muTermVersion2 e1 = x.subEx1;
		muTermVersion2 e2 = x.subEx2;
		if(x.op.equals("var")) {
			return ""+x.varName+""+toString(e1);
		}
		if(x.op.equals("r")) {
			return x.value;
		}
		if(x.op.equals("q")) {
			return "("+x.value+"("+toString(e1)+"))";
			
		}
		if(x.op.equals(".")) {
			return ("{("+toString(e1)+")"+"cDOT"+"("+toString(e2)+")}");
		}
		if(x.op.equals("+")) {
			return ("[("+toString(e1)+")"+"cPlus"+"("+toString(e2)+")]");
		}
		if(x.op.equals("cup")) {
			return ("|("+toString(e1)+")"+"CUP"+"("+toString(e2)+")|");
		}
		if(x.op.equals("cap")) {
			return ("/("+toString(e1)+")"+"CAP"+"("+toString(e2)+")/");

		}
		if(x.op.equals("v")) {
		//	System.out.println("it shouldnt be here if its a sequence");
			return ("v."+x.varName+toString(e1));
		}
		if(x.op.equals("mu")) {
			return ("mu."+x.varName+toString(e1));
		}
		if(x.op.equals("q")) {
			return x.value+"("+toString(x.subEx1)+")";
		}
		return "";
		
	}
	//now an array of all the terms in the sequences
	//q0+q1x1+q2x2+...qnXn
	//have an array of string, so you can put in t values in teh array
	//later I can just use a package to solve it after subbing in t
	public String[] addTwoString(String[] a, String[] b) {
		String[] result = new String[a.length];
		
		for(int i=0;i<a.length;i++) {
			result[i] = "(("+a[i]+")+("+b[i]+"))";
		}
		return result;
	}
	public int getIndexSet(String x) {
		int counter=0;
		for(String s:varList) {
			if(s.equals(x)) {
				return counter;
			}
			counter++;
		}
		System.err.println("var not found "+varList.size());
		System.exit(0);
		return -1;
	}
	
	int tTracker = 0;
	public String[] translatedTerms(muTermVersion2 x, String[] values, int tCounter) {
		if(x==null)return values;
		
		if(x.op.equals(".")) {
			tCounter++;
			String t = "t"+String.valueOf(tCounter);
			String[] s1 = translatedTerms(x.subEx1,values,tCounter);
			String[] s2= translatedTerms(x.subEx2,values,tCounter);
			String[] newString = addTwoString(s1,s2);
			
			//newString[LENGTH OF THE HASHSET +1] += -t
			for(int i=0;i<newString.length;i++) {
				String dummy = newString[i];
				newString[i] = "("+dummy+")"+"*"+t;
			}
			newString[newString.length-1] += "-"+t;
			tTracker=tCounter;
			return newString;
		}
		else if(x.op.equals("+")) {
			tCounter++;
			String t = "t"+String.valueOf(tCounter);
			String[] s1 = translatedTerms(x.subEx1,values,tCounter);
			String[] s2= translatedTerms(x.subEx2,values,tCounter);
			String[] newString = addTwoString(s1,s2);
			
			for(int i=0;i<newString.length;i++) {
				String dummy = newString[i];
				newString[i] = "("+dummy+")"+"*(1-"+t+")";
			}
			newString[newString.length-1]+= "+"+t;
			tTracker=tCounter;
			return newString;
		}
		else if(x.op.equals("cup")) {
			tCounter++;
			String t = "t"+String.valueOf(tCounter);
			String[] s1 = translatedTerms(x.subEx1,values,tCounter);
			String[] s2= translatedTerms(x.subEx2,values,tCounter);
			
			//(1-t)pi(s1)
			for(int i=0;i<s1.length;i++) {
				String temp = s1[i];
				s1[i]= "(("+temp+")*(1-"+t+"))";	
				String dummy = s2[i];
				s2[i] ="(("+dummy+")*"+t+")";
			}
			String[] newString = addTwoString(s1,s2);
			tTracker=tCounter;
			return newString;
			
		}
		else if(x.op.equals("cap")) {
			tCounter++;
			String t = "t"+String.valueOf(tCounter);
			String[] s1 = translatedTerms(x.subEx1,values,tCounter);
			String[] s2= translatedTerms(x.subEx2,values,tCounter);
			
			//(1-t)pi(s1)
			for(int i=0;i<s1.length;i++) {
				String temp = s2[i];
				s2[i]= "(("+temp+")*(1-"+t+"))";	
				String dummy = s1[i];
				s1[i] ="(("+dummy+")*"+t+")";
			}
			String[] newString = addTwoString(s1,s2);
			tTracker=tCounter;
			return newString;
		}
		else if(x.op.equals("r")) {
			String temp = values[values.length-1] ;
			values[values.length-1]= "("+temp+")+"+x.value;
			
		}
		else if (x.op.equals("var")) {
			int index = getIndexSet(x.varName);
			String temp = values[index];
			values[index] = "("+temp+")"+"+1";
		}
		else if(x.op.equals("q")) {
			String[] s1 = translatedTerms(x.subEx1,values,tCounter);
			for(int i=0;i<s1.length;i++) {
				String temp = s1[0];
				s1[i] = "("+temp+")*"+x.value;
			}
			return s1;
			
		}
		
		
		return values;
	}

	
	
	//NEED TO FIX THE MATHS FOR . AND +
	public String[] translatedTerms2(muTermVersion2 x, String[] values, int tCounter) {
		//System.err.println("OP =="+x.op+" tCounter="+tCounter);
		if(x==null)return values;
		
		if(x.op.equals(".")) {
			tCounter++;
			//System.out.println("t= "+tCounter+" op = "+x.op);
			String t = "t"+String.valueOf(tCounter);
			String[] value1 = new String[values.length];
			Arrays.fill(value1, "0");
			String[] value2 = new String[values.length];
			Arrays.fill(value2, "0");
			
			String[] s1 = translatedTerms2(x.subEx1,value1,tCounter);
			String[] s2= translatedTerms2(x.subEx2,value2,tCounter);
//			System.out.println("HELLO 1 "+s1[0]+"   "+x.subEx1.op);
//			System.out.println("HELLO 2 "+s2[0]);

			String[] newString = addTwoString(s1,s2);
			//newString[LENGTH OF THE HASHSET +1] += -t
			for(int i=0;i<newString.length;i++) {
				String dummy = newString[i];
				if(i==newString.length-1) {
					newString[i] = "(("+dummy+")*"+t+")-"+t;
				}
				else {
					newString[i] = "("+dummy+""+"*"+t+")";
				}
				
			}
			tTracker=tCounter;
			//System.out.println("HELLLOOO "+Arrays.toString(newString));

			return newString;
		}
		else if(x.op.equals("+")) {
			tCounter++;
			//System.out.println("t= "+tCounter+" op = "+x.op);

			String t = "t"+String.valueOf(tCounter);
			String[] value1 = new String[values.length];
			Arrays.fill(value1, "0");
			String[] value2 = new String[values.length];
			Arrays.fill(value2, "0");
			
			String[] s1 = translatedTerms2(x.subEx1,value1,tCounter);
			String[] s2= translatedTerms2(x.subEx2,value2,tCounter);
			String[] newString = addTwoString(s1,s2);
			
			for(int i=0;i<newString.length;i++) {
				String dummy = newString[i];
				newString[i] = "("+dummy+""+"*(1-"+t+"))";
			}
			newString[newString.length-1]+= "+"+t;
			tTracker=tCounter;
			return newString;
		}
		else if(x.op.equals("cup")) {
			tCounter++;
			//System.out.println("t= "+tCounter+" op = "+x.op);

			String t = "t"+String.valueOf(tCounter);
			String[] value1 = new String[values.length];
			Arrays.fill(value1, "0");
			String[] value2 = new String[values.length];
			Arrays.fill(value2, "0");

			String[] s1 = translatedTerms2(x.subEx1,value1,tCounter);

			String[] s2= translatedTerms2(x.subEx2,value2,tCounter);

			//(1-t)pi(s1)
			for(int i=0;i<s1.length;i++) {
				String temp = s1[i];
				s1[i]= "("+temp+"*(1-"+t+"))";	
				String dummy = s2[i];
				s2[i] ="("+dummy+"*"+t+")";
			}
			String[] newString = addTwoString(s1,s2);
			tTracker=tCounter;
			return newString;
			
		}
		else if(x.op.equals("cap")) {
			tCounter++;
		//	System.out.println("t= "+tCounter+" op = "+x.op);
			//System.out.println( "o2 ="+x.subEx2.op+"   "+x.subEx2.value);
			String t = "t"+String.valueOf(tCounter);
			String[] value1 = new String[values.length];
			Arrays.fill(value1, "0");
			String[] value2 = new String[values.length];
			Arrays.fill(value2, "0");

			String[] s1 = translatedTerms2(x.subEx1,value1,tCounter);
			String[] s2= translatedTerms2(x.subEx2,value2,tCounter);
		//	System.out.println("HELLO  "+s1[2]+"   and "+s2[2]);

			//(1-t)pi(s1)
			for(int i=0;i<s1.length;i++) {
				String temp = s2[i];
				s2[i]= "("+temp+"*(1-"+t+"))";	
				String dummy = s1[i];
				s1[i] ="("+dummy+"*"+t+")";

			}
			String[] newString = addTwoString(s1,s2);
			tTracker=tCounter;
			return newString;
		}
		else if(x.op.equals("r")) {
			String[] copy = values;

			String temp = copy[copy.length-1] ;
			
			copy[copy.length-1]= ""+temp+"+"+x.value;
			return copy;
			
		}
		else if (x.op.equals("var")) {
			//System.out.println("HEllo");
			int index = getIndexSet(x.varName);
			String[] copy = values;
			String temp = copy[index];
			copy[index] = ""+temp+""+"+1";
			return copy;
		}
		else if(x.op.equals("q")) {
			String[] s1 = translatedTerms2(x.subEx1,values,tCounter);
			boolean isNonZero = true;
			for(int i=0;i<s1.length;i++) {
			//	System.out.println("Before  + "+s1[i]+"   "+i+"  "+x.value);
				if(!s1[i].equals("0")) {
					isNonZero=false;
				}
				if(i==s1.length-1) {
					if(s1[i].equals("0") && isNonZero) {
						s1[i]= "1*"+x.value;
					}
					else {
						s1[i]+= "*"+x.value;
					}
				}
				
				else {
					String temp = s1[i];
					System.err.println(temp+"  error here?");
					s1[i] = temp+"*"+x.value;
				}
				//System.out.println("After  + "+s1[i]+"   "+i+"  "+x.value);

			}


			return s1;	
		}
		return values;
	}
	/*
	 * Just need to sub different combinations of t and check if LEQ consists
	 * Need to find a list of leq as well. probably copy it from the other class.
	 */
	
	/*
	 * need to sub in t values to equations
	 * Would probably prefer to have an array [1,1,1] where index 0 = t1, 1= t2, 2=t3
	 * Array should of the same length as amount of operators
	 */
	
	
	//Subsitute t into equations
	public double[] solveForT(int[] ts,String[] temp) {
		double[] result = new double[temp.length];
		String[] equation = temp;
		//System.out.println(Arrays.toString(equation)+"  "+Arrays.toString(temp));

		//System.out.println("\n\nTs = "+Arrays.toString(ts)+"   for "+Arrays.toString(temp));
		for(int i=0;i<ts.length;i++) {
			String t = "t"+String.valueOf(i+1);
			for(int j=0;j<equation.length;j++) {
				equation[j] = equation[j].replaceAll(t,String.valueOf(ts[i]));
			}
		}
		
		//System.out.println("ts == "+ts.length);
		for(int i=0;i<equation.length;i++) {
			//System.err.println(" test "+equation[i]);
			double solved = Double.parseDouble(EquationSolver.solve(equation[i])); 
//			if(solved>1 || solved<0) {
//				System.err.println("ERROR HERE AT INDEX  "+i+"  with "+solved +" at "+equation[i]);
//			}
			result[i] = solved;
		}
	//	System.out.println("After = "+Arrays.toString(result));
		return result;
	}
	
	//returns which quantifier we are solving for
	public int whichQuantifier(String lhs) {
		//0 is mu, 1 is nu, 2= error
		if(lhs.length()<=3 || lhs.isEmpty()) {
			System.err.println("Needs a quantifier");
			System.exit(0);
			return 2;
		}
		if(lhs.charAt(0)=='m') {
			return 0;
		}
		else if(lhs.charAt(0)=='n')
			return 1;
		else
			return 2;
	}
	
	//generates an array to put into the gaussian elimination
	public double[] solveLHS(double[] rhs, String lhs) {
		//this method will check for the conditions of mu and nu
		//just compare the first index for mu or nu
		//then the last index for which variable
		int quantifier = whichQuantifier(lhs);
		String varName = lhs.substring(3);
		//System.out.println(varName+" testing "+getVarLength()+" "+varName+" = "+lhs);
		int index = getIndexSet(varName);
		boolean sub = false;
		//System.err.println("TESTINGG  BEFORE SUB = "+Arrays.toString(rhs));
		//System.out.println("BEFORE ="+Arrays.toString(rhs) +" "+lhs);
		//System.out.println("bEFORE =="+Arrays.toString(rhs));
		//System.out.println("\n\nBefore ="+Arrays.toString(rhs));

		if(quantifier==0) {
		//	System.out.println("Before ="+Arrays.toString(rhs));

			//its mu
			//check if all apart from the variable index is empty
			//if so, just return [1,0,0] or wherever the variable is
			//else return [x-1,y,c] if x is the variable
			//if sub is true, we do the -1 thing else just [1,0,0]
			boolean isOne = (rhs[index]==1) ? true : false;
			
			//System.err.println("TESTINGG: MU==1");
			//System.out.println("Then array=="+Arrays.toString(rhs));
			for(int i=0;i<rhs.length-1;i++) {
				if(i!=index && rhs[i]!=0) {
					sub=true;
				//	System.err.println("hello??");
					break;
				}
			}
			if(sub) {
				//	System.err.println("It should not be here "+rhs[index]);
					rhs[index] -=1; //so this is the [x-1,y,c] part
					// if x=0.5, then this will reutrn [-1,0,-0.5] which is the same
					rhs[rhs.length-1] *= -1;
				//	System.out.println("After ="+Arrays.toString(rhs));

					return rhs;	
				
			
			}
			else {
				//return [1,0,0] so x=0
				if(rhs[index]==0 && rhs[rhs.length-1]==0) {
					double[] result = new double[rhs.length];
					Arrays.fill(result,0);
					result[index]=1;
					//System.out.println("After ="+Arrays.toString(result));

					return result;
				}
				else if(rhs[index]==0 && rhs[rhs.length-1]!=0) {
				//	System.out.println("Afte 222r ="+Arrays.toString(rhs));

					rhs[index]=-1;
					rhs[rhs.length-1] *=-1;
				//	System.out.println("After ="+Arrays.toString(rhs));

					return rhs;
					
				}
				else {
					//isOne
					//mux = x+0.5
					//not a lfp
					//System.err.println("OOPISE");
					rhs[index]--;
					rhs[rhs.length-1]*=-1;
					return rhs;
				}
			
				//so if [x=1,0,1] then x= 0 regardless
				
				//return rhs;
			}
			
			
		}
		else if(quantifier==1) {
			//its nu
			//check if all apart from the variable is 1, rest empty
			//so if [0,1,0] for y, then return [0,1,-1]
			//else return [x,y-1,c]
			
			//if q value is 1 then its true,else its false
			
			//System.out
			boolean isOne = (rhs[index]==1) ? true : false;
			//we check if rest are empty now
			
			for(int i=0;i<rhs.length-1;i++) {
				if(i!=index && rhs[i] !=0) {
					sub = true;
					break;
				}
			}
			if(sub) {
				//if [x=1,y=0,0] 
				//non empty. so just -1 from y
				rhs[index]-=1;
				rhs[rhs.length-1] *= -1;
				//System.out.println("After ="+Arrays.toString(rhs));

				return rhs;
				
			}
			else {
					
				//here all are empty apart from y 
				//isOne, if true, return [0,1,1]
				//else return [0,1,0]
				//System.out.println("Before ="+Arrays.toString(rhs));
				if(isOne && rhs[rhs.length-1]==0) {
					rhs[index]=-1;
					rhs[rhs.length-1]=-1;
					//so if [0,1,0] then return [0,1,1]
					//because if it is non zero it wont be here
				}
				else if(isOne && rhs[rhs.length-1]!=0) {
					//[0,1,1] then just do [0,0,1]
					rhs[index] =0;
					rhs[rhs.length-1] =-1;
				}
				
				else if(!isOne) {
					//everything else is 0?
					
					rhs[index]=1;
					
				}
			//	System.out.println("After 2 ="+Arrays.toString(rhs));

			//	System.out.println("After ="+Arrays.toString(rhs));

				return rhs;
//				else {
//					
//				}
//				Arrays.fill(rhs,0);
//				rhs[index]=1;
//				if(isOne) 
//					rhs[rhs.length-1]=1;
//				return rhs;
			}
		}
		else {
			System.err.println("Invalid quantifier");
			System.exit(0);
			return null;
		}	
	}
	

	
	ArrayList<String[]> leqList = new ArrayList<String[]>();
	int tCounter =0;
	public String findLEQ(muTermVersion2 x ) {
		//the muterm should have no quantifer
		if(x==null)return "";

		if(x.op.equals(".")) {
			tCounter++;
			String t= "t"+String.valueOf(tCounter);
			String s1 = findLEQ(x.subEx1);
			String s2 = findLEQ(x.subEx2);
			String[] inequality = new String[3];
			inequality[0] = "1";
			inequality[1] = s1+"+"+s2;
			inequality[2] = t;
			leqList.add(inequality);
			//pi translation for oDot
			String returnValue = t+"("+s1+"+"+s2+"-1)";

			return returnValue;
			
		}
		else if(x.op.equals("+")) {
			tCounter++;
			String t= "t"+String.valueOf(tCounter);
			String s1 = findLEQ(x.subEx1);
			String s2 = findLEQ(x.subEx2);
			String[] inequality = new String[3];
			inequality[0] = "1";
			inequality[1] = s1+"+"+s2;
			inequality[2] = t;
			leqList.add(inequality);
			//pi translation for oPlus
			String returnValue=t+"+(1-"+t+")("+s1+"+"+s2+")";
			return returnValue;
			
		}
		else if(x.op.equals("cup")) {
			tCounter++;
			String t= "t"+String.valueOf(tCounter);

			String s1 = findLEQ(x.subEx1);
			String s2 = findLEQ(x.subEx2);
			String[] inequality = new String[3];
			
			inequality[0] =s1;
			inequality[1] =s2;
			inequality[2] = t;
			leqList.add(inequality);
			//pi translation for cup
			String returnValue="(1-"+t+")"+s1+"+"+t+"*"+s2;
			return returnValue;
			
		}
		else if(x.op.equals("cap")) {
			tCounter++;
			String t= "t"+String.valueOf(tCounter);
			String s1 = findLEQ(x.subEx1);
			String s2 = findLEQ(x.subEx2);
			String[] inequality = new String[3];
			inequality[0] =s1;
			inequality[1] =s2;
			inequality[2] = t;
			leqList.add(inequality);
			//pi translation for cup
			String returnValue=t+"*"+s1+"+(1-"+t+")"+s2;
			return returnValue;
			
		}
		else if(x.op.equals("r")) {
			return x.value;
			
		}
		else if(x.op.equals("q")) {
			if(x.subEx1==null) {
				return "("+x.value+")";
			}
			return "("+x.value+"*"+findLEQ(x.subEx1)+")";
			
		}
		else if(x.op.equals("var")) {
			String temp = "";
			if(x.subEx1!=null) {
				temp = findLEQ(x.subEx1);
				return x.varName+"("+temp+")";
			}
			return x.varName;
			
		}
		else {
			System.err.println("Invalid op");
			System.exit(0);
			return "";
		}
	}
	
	//this creates a leq arraylist which contains an array for the 3 leq terms
	//once I get the x,y and t values. I just sub it into each string array. then solve it
	//then check if it satisfies or not.
	//from ["1","1+t+y","1+x"] to [1,1,0] then check if the leq satisfies
	public void generateLeq() {
		//assume generate sequence has been called
		for(String key: sequences.keySet()) {
			String temporary = findLEQ(sequences.get(key));

		}
		
	}
	public ArrayList<String[]> getLEQ(){
		return leqList;
	}
	
	/*
	 * Now just left to turn these arrays into two arrays
	 * so [x1,y1,c1], [x2,y2,c2] 
	 * to: [[x1,y1],[x2,y2]] and [c1,c2]
	 * then use the gaussian solve to solve it.
	 * then this will give values for x and y
	 * then I sub the x,y and ts to the leq strings
	 * and check if they satisfy or not
	 * if they do horray and add x and y to list of solutions
	 * else try for new ts
	 */
		
	
	/*
	 * To-do:
	 * Generate an array of all permutations of t
	 * 			for each t
	 * 			find x and y through gaussian elimination
	 * 			then sub that t,x and y to the leq
	 * 			then solve the leq and turn it into double[]
	 * 			Then check if inequality is satisfied or not
	 * 			for that t.
	 * 		If satisfied, add it to list of solution
	 */
	
	
	//subsitutes x's and y's and t1s,t2s.. to leq.
	//have to call this for each leq.
	//reason: so i can stop if a leq is unsatisfied
	
	/*
	 * Problem is that its replacing t1=0 however, it thinks t10= t1 and 0
	 * so probably need to add some kind of barrier? 
	 * so something like (t1) and (t10) and make sure that I replace all (t1) and (t10)
	 */
	public double[] subToLeq(double[] vars, int[]ts, String[] leq){
		//this should of the size, [operator size][3]
		//sub t first
		//System.out.println(Arrays.toString(ts)+ "  hello "+Arrays.toString(vars));
		
	
		for(int i=0;i<leq.length;i++) {
			if(i==0 || i==1) {
				for(int k=0;k<vars.length;k++) {
					String var = varListArray[k];
					//this should return something like "x" or "y"
					//System.out.println("first = "+String.valueOf(vars[k])+"   second = "+var);
					leq[i] = leq[i].replaceAll(var,String.valueOf(vars[k]));

				}
			}
		
			for(int j=ts.length-1;j>=0;j--) {
				String t= "t"+String.valueOf(j+1);
				leq[i]= leq[i].replaceAll(t,String.valueOf(ts[j]));
			}
		}
		double[] result = new double[leq.length];
		for(int i=0;i<leq.length;i++) {
			//System.out.println(leq[i]);
			
		//	System.out.println(leq[i]);
		//	System.out.println("ERROR AT "+ leq[i]);
			double solved = Double.parseDouble(EquationSolver.solve(leq[i]));
			result[i]=solved;
		}
		return result;
		
		
	}
	
	
	/*
	 * Todo:
	 * 	Start reading chapter 2 and 6 and using the algorithm
	 *  store the list of working x's and y's in a set
	 *  then find the unique solution
	 */

}

