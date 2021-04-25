
  
package solvingMuCalculus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
public class muTerm implements Serializable {
	//Serializable so I can save the class object as byte stream when generating examples
	//and can keep the example used
	
	/*
	 * Declaring the class variables. Since most of the class is solved/created recursively
	 * each subterm will have their own opCounter, varCounter however, the final complete 
	 * term will have the sum of all.
	 */
	
	
	String op,variableName,value;
	//variableCounter will contain bound and free variables
	//where as quantifier size is also counted as this is just bound variables
	int opCounter,variableCounter,quantifierSize,highestRational=0;
	muTerm subEx1,subEx2; //two subterms of a term. can be null.
	String[] operators = new String[]{"mu","v","cap","cup","+",".","var","p","q"};
	List<String> listOfOps = Arrays.asList(operators);
	
	//will contain the list of variables, free and bound
	HashSet<String> variableSet = new HashSet<String>();	
	 
	//will contain varName as key and variable value as the value
	LinkedHashMap<String, String> variableValues = new LinkedHashMap<String,String>();
	
	//will contain the values for the free variables, if any
	ArrayList<String> freeValues = new ArrayList<String>();
	
	
	/*
	 * Need arrays to give to gaussain eliminiation
	 * 
	 * variables: An array of which variable as at which index, maybe with quantifiers as well
	 * interpretation p: An array that contains the values that needs to be given to the free variables
	 * Variable values: So an array which will be 0 for bound variables and a value for free
	 * 
	 * Might have to use hash and then in the end turn it to array? 
	 * key will be var name, value will be the value for that key
	 */
	

	//default constructor
	public muTerm() {
		this.op="";
		this.variableName = "";
		this.value="";
		this.subEx1=null;
		this.subEx2=null;
	}
	
	/*
	 * Recursive initialisation of the muTerm object. Here the definition of operator is
	 * different from the one mentioned in paper. i.e. Variables and quantifiers are called
	 * operators as well. However, it does not contribute to the number of operators
	 *
	 *If a value is given for an operator. So, like a value for a sqCAP, then we just ignore those
	 *and carry on, as it will not do anything. As long as a term has the needed variables.
	 *So a variable name for a type var, two subterms for each of the operators...
	 *
	 *I need to treat free variables as quantifiers, so I can have equation. 
	 */
	public muTerm(String operator, muTerm e1, muTerm e2, String varName, String val) {
		if(listOfOps.contains(operator))this.op=operator;
		else {
			System.err.println("Please enter a valid operator. List of operators: "+Arrays.toString(operators)+"  not "+operator);
		}
		//valid op so can now verify if term is valid or not. 
		
		if(operator.equals("var")) {
			//is variable. So need a variable name.
			if(varName.isEmpty() || varName.length()==0) {
				System.err.println("Variable needs a name");
				System.exit(0);
			}
			this.variableName=varName;
			//variable type only takes a variable name and everything else is null/empty
			this.value="";
			this.subEx1=null;
			this.subEx2=null;
			this.opCounter=0;
			this.variableSet.add(varName);
			this.quantifierSize=0;
		//	this.freeValues=null;
			//this.variableValues.put(varName,"0");
		}
		else if(operator.equals("p")) {
			//is free variable
			//needs a variable name and a value
			if(varName==null ||val==null||varName.isEmpty() || varName.length()==0 || val.length()==0 ||val.isEmpty()) {
				System.err.println("Free variable needs a name and a value");
				System.exit(0);
			}
			this.freeValues.add(varName);
			this.variableName=varName;
			this.value=val;
			this.subEx1=null;
			this.subEx2=null;
			this.opCounter=0;
			this.variableSet.add(varName);
			this.quantifierSize=0;
			
		}
		else if(operator.equals("q")) {
			//this is applying rational number
			//can have no sub terms or have var or p as subterm
			//but needs a value
			if(val==null ||val.length()==0 || val.isEmpty()) {
				System.err.println("Rational number needs a value");
				System.exit(0);
			}
			else {
				if(e1==null || e1.op.length()==0){
					this.subEx1=null;
					this.opCounter=0;
				}
				else {
					if(e1.op.equals("p") || e1.op.equals("var")) {
						this.subEx1=e1;
						this.variableSet.addAll(e1.variableSet);
						this.opCounter=e1.opCounter; //though it will be 0
					}
					else {
						System.err.println("Can only have variables as subterms");
						System.exit(0);
					}

				}
				this.value=val;
				this.subEx2=null;
				this.variableName = "";
				this.quantifierSize=0;
			}
		}
		else if(operator.equals("mu") || operator.equals("v")) {
			//is a quantifier
			if(e1==null) {
				System.err.println("Quantifier needs a subterm.");
				System.exit(0);
			}
			if(varName.isEmpty() || varName.length()==0) {
				System.err.println("Quantifier needs a variable name. The name of the bound variable");
				System.exit(0);
			}
			else {
				this.variableName = varName;
				this.subEx1=e1;
				this.subEx2=null;
				this.value= "";
				this.opCounter = e1.opCounter;
				this.variableSet.addAll(e1.variableSet);
				this.variableSet.add(varName);
				this.quantifierSize=e1.quantifierSize+1;
			}
		}
		
		else if(operator.equals(".") ||operator.equals("cap")||operator.equals("cup")||operator.equals("+")) {
			//is Operator
			//needs two subterms
			if(e1==null || e2==null) {
				System.err.println("Operator "+operator+" needs two subTerms");
				System.exit(0);
			}
			else {
				this.subEx1=e1;
				this.subEx2=e2;
				this.variableName="";
				this.value="";
				this.opCounter = e1.opCounter+e2.opCounter+1;
				//System.out.println("operator "+operator+" counter ="+this.opCounter);
				this.variableSet.addAll(e1.variableSet);
				this.variableSet.addAll(e2.variableSet);
				this.quantifierSize=e1.quantifierSize+e2.quantifierSize;
			}
		}
		
	}
	
	/*
	 * toString method to 'visualise' what a mu term looks like
	 * used this mainly when generating examples.
	 * toString method is recursive as the object itself is initialised recursively.
	 */
	public String toString(muTerm x) {
		if(x==null)return "";
		muTerm e1 = x.subEx1;
		muTerm e2= x.subEx2;
		
		if(x.op.equals("var")) {
			return x.variableName;
		}
		if(x.op.equals("q")) {
			//here the sub terms will be either var or p
			if(e1==null) {
				return x.value;
			}
			return x.value+"("+toString(e1)+")";
		}
		if(x.op.equals("p")) {
			return x.variableName+"["+x.value+"]";
		}
		if(x.op.equals("mu") || x.op.equals("v")) {
			return x.op+"."+x.variableName+"("+toString(e1)+")";
		}
		if(x.op.equals(".") ||x.op.equals("+") ||x.op.equals("cup") ||x.op.equals("cap")) {
			return "[("+toString(e1)+")"+x.op+"("+toString(e2)+")]";
		}
		System.err.println("It shoould not arrive here? Method: muTerm::toString");
		System.exit(0);
		return "";
		
	}
	
	/*
	 * Getting indexs for where the variable appears
	 */
	public int getIndexSet(String x) {
		int counter=0;
		for(String s:variableSet) {
			if(s.equals(x)) {
				return counter;
			}
			counter++;
		}
		System.err.println("var not found "+variableSet.size());
		System.exit(0);
		return -1;
	}
	
	/*
	 * Generates a set of equations, without quantifiers, recursively
	 * has two extra arguments, start and var length
	 * Varlength is used to see if a term has varialbes or not
	 * start is used to check if a quantifier has been found, if so start new 
	 * equation and return variable name
	 * 
	 * else we return the subterm of the quantifier
	 */
	String singleQuantifier="";
	LinkedHashMap<String,muTerm> seqeunceOfEquations= new LinkedHashMap<String,muTerm>();
	public muTerm generateSequenceEquation(muTerm x, boolean start, int varLength) {
	//	System.err.println("Im here "+x.op);

		if(x==null)return null;
		if(varLength==0) {
			//no variables (Free and bound)
			//so we just create a new variable as mentioned in the paper and solve for it
			//it will just be a fixed point with the value being the solution of the term.
			
			//choice of quantifier does not matter
			muTerm dummyTerm = new muTerm("mu",x,null,"x","0");
			x.variableSet.add(dummyTerm.variableName);
			return generateSequenceEquation(dummyTerm,start,1);
		}
		else if(varLength==1 && (x.op.equals("mu") || x.op.equals("v"))) {
			//so here we check if theres only 1 quantifier and it appears in the middle
			//as this quantifier is applied to the entire term, not just the later half
			//we return the subTerm however, we make sure that this quantifier is saved
			singleQuantifier=x.op+"."+x.variableName;
			muTerm e1 =generateSequenceEquation(x.subEx1,start,1);
			return e1;
			
		}
		else if(x.op.equals("mu") || x.op.equals("v")) {
			String type = "mu";
			if(x.op.equals("v")) {
				type="nu";
			}
			if(start) {
				//So the first time a quantifier is appearing
				seqeunceOfEquations.put(type+"."+x.variableName, null);
				muTerm e1 = generateSequenceEquation(x.subEx1,false,varLength);
				seqeunceOfEquations.put(type+"."+x.variableName, e1);
				return x;
			}
			else {
				//quantifier appeared before so we return the variable name
				//whilst generating the sequence in the subterm, if any
				seqeunceOfEquations.put(type+"."+x.variableName, null);
				muTerm e1 = generateSequenceEquation(x.subEx1,false,varLength);
				seqeunceOfEquations.put(type+"."+x.variableName,e1);
				return new muTerm("var",null,null,x.variableName,"");
			}
			
		}
		else if(x.op.equals("var")) {
			return x;
		}
		else if(x.op.equals("q")) {
			//Here i need to consider for the free variables
			if(x.subEx1==null) {
				//just a rational number
				return x;
			}
			//now if it has a subterm which will be either p or var 
			if(x.subEx1.op.equals("var")) {
				return x;
			}
			if(x.subEx1.op.equals("p")) {
				//add to seqeunce
				seqeunceOfEquations.put("fe."+x.subEx1.variableName,x);
				return x;
			}
		}
		else if(x.op.equals("p")) {
			//need to make sure that its not already added
			//so if theres a q infront or not
			for(String key:seqeunceOfEquations.keySet()) {
				if(key.charAt(0)=='f') {
					//is a free variable
					//need to make it so its [3:]
					if(key.substring(3).equals(x.variableName)) {
						//so same char?
						return x;
					}
				}
			}
			seqeunceOfEquations.put("fe."+x.variableName,x);
			return x;
		}
		else if(x.op.equals("+") || x.op.equals(".") || x.op.equals("cap") || x.op.equals("cup")){
			muTerm e1 = generateSequenceEquation(x.subEx1,start,varLength);
			muTerm e2 = generateSequenceEquation(x.subEx2,start,varLength);
			return new muTerm(x.op,e1,e2,"","");
		}
		else {
			System.err.println(x.op+" operator is not valid");
			System.exit(0);
			return null;
		}
		return null;
	
		}
	
	public LinkedHashMap<String, muTerm> getseqeunceOfEquations(){
		return seqeunceOfEquations;
	}
	
	/*
	 * Just a method to add the two string arrays for the final evaluation.
	 * It does not add the strings it self,
	 * it just adds the, "+" symbol between the two string arrays.
	 * e.g. a[i]+"+"+b[i]
	 */
	
	public String[] addTwoStringArray(String[] a, String[]b) {
		String[] result = new String[a.length];
		
		for(int i=0;i<a.length;i++) {
			result[i] = "(("+a[i]+")+("+b[i]+"))";
		}
		return result;
	}
	
	
	
	/*
	 * Translating a mu terms to the translation introduced by KK
	 * For a term with 5 variables, the data will be represented as 
	 * [a1,a2,a3,a4,a5,b] where b is the rational that is not associated with any 
	 * variables
	 */
	
	//this will keep track of what the t value is at what sub term
	int tTracker=0;
	public String[] translateTerms(muTerm x, String[] values, int tCounter) {
		//t-counter keeps track of which t-value its on.
		//for a term with m operators there will be t1...tm
		//System.err.println("Im here "+x.op);

		if(x==null)return values;
		
		/*
		 * I need to take care of free variables here, so this is where I will use
		 * the "interpretation" p, and sub in the value for the free variable.
		 */
		if(x.op.equals(".")) {
			tCounter++;
			//System.out.println("t= "+tCounter+" op = "+x.op);
			String t = "t"+String.valueOf(tCounter);
			String[] value1 = new String[values.length];
			Arrays.fill(value1, "0");
			String[] value2 = new String[values.length];
			Arrays.fill(value2, "0");
			
			String[] s1 = translateTerms(x.subEx1,value1,tCounter);
			String[] s2= translateTerms(x.subEx2,value2,tCounter);
//			System.out.println("HELLO 1 "+s1[0]+"   "+x.subEx1.op);
//			System.out.println("HELLO 2 "+s2[0]);

			String[] newString = addTwoStringArray(s1,s2);
			//newString[LENGTH OF THE HashSet +1] += -t
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
			
			String[] s1 = translateTerms(x.subEx1,value1,tCounter);
			String[] s2= translateTerms(x.subEx2,value2,tCounter);
			String[] newString = addTwoStringArray(s1,s2);
			
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

			String[] s1 = translateTerms(x.subEx1,value1,tCounter);

			String[] s2= translateTerms(x.subEx2,value2,tCounter);

			//(1-t)pi(s1)
			for(int i=0;i<s1.length;i++) {
				String temp = s1[i];
				s1[i]= "("+temp+"*(1-"+t+"))";	
				String dummy = s2[i];
				s2[i] ="("+dummy+"*"+t+")";
			}
			String[] newString = addTwoStringArray(s1,s2);
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

			String[] s1 = translateTerms(x.subEx1,value1,tCounter);
			String[] s2= translateTerms(x.subEx2,value2,tCounter);
		//	System.out.println("HELLO  "+s1[2]+"   and "+s2[2]);

			//(1-t)pi(s1)
			for(int i=0;i<s1.length;i++) {
				String temp = s2[i];
				s2[i]= "("+temp+"*(1-"+t+"))";	
				String dummy = s1[i];
				s1[i] ="("+dummy+"*"+t+")";

			}
			String[] newString = addTwoStringArray(s1,s2);
			tTracker=tCounter;
			return newString;
		}
		else if(x.op.equals("var")) {
			int index = getIndexSet(x.variableName);
			//System.err.println("Trans::var before ="+Arrays.toString(values));
			String[] copy = values;
			String temp = copy[index];
			copy[index] = ""+temp+"+1";
			//System.err.println("Trans::var After ="+Arrays.toString(copy));

			return copy;
		}
		else if(x.op.equals("q")) {
			if(x.subEx1!=null) {
				//so either var or p
				//so I have to multiply the variable by the number
				//it will mutliply everything by x.val
				//as s1, will either be 0, or have a value if it needs to be multiplied
				String[] s1 = translateTerms(x.subEx1,values,tCounter);
				for(int i=0;i<s1.length;i++) {
					s1[i]+= "*"+x.value;
				}
				return s1;
			
			}
			else {
				//just a rational number
				//so I just put the number in
				//System.err.println("Trans::q   before =="+Arrays.toString(values));
				String temp = values[values.length-1];
				if(temp.equals("0")) {
					values[values.length-1] = "1*"+x.value;
				}
				else {
					values[values.length-1] = temp+"*"+x.value;

				}
				//System.err.println("Trans::q   After =="+Arrays.toString(values));
				return values;
			}
		
		}
		else if(x.op.equals("p")){
			//free variable
			//so i Add 1 to the index? or add the var itself?
			//this way, I can sub in the variable with the t later
			//probably sub in the value of p.
			int index = getIndexSet(x.variableName);
			//gives index of free variable
			String[] copy = values;
			String temp = copy[index];
			//System.out.println("TEMP =="+temp);
			copy[index] = x.value;
			return copy;
			
		}
		System.err.println("Error:muTerm::translateTerms");
	
		return null;
	}
	
	
	
	
	
	ArrayList<String[]> leqList = new ArrayList<String[]>();
	//will generate leq for the term and put it into leqList
	//this method will be called for each of the equation without quantifiers.
	int tCounter =0;

	public String findLEQ(muTerm x) {
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
		else if(x.op.equals("var")) {
			return x.variableName;
		}
		else if(x.op.equals("q")) {
			//if no subex1, just return x.val, else x.val*findLeq(e1)
			if(x.subEx1!=null) {
				return x.value+"*("+findLEQ(x.subEx1)+")";
			}
			//is null
			return x.value;
		}
		else if(x.op.equals("p")) {
			//do I return the subbed value or the var|?
			//System.err.println("Not sure if I should subsitute value here");
			return x.value;
			
		}
		System.err.println("Should never arrive here. Invalid op");
		System.exit(0);
		return null;
	}
	
	//this method just calls all the equations and finds leq
	public void generateLeq() {
		for(String key:seqeunceOfEquations.keySet()) {
			String temporary = findLEQ(seqeunceOfEquations.get(key));
		}
	}
	
	//returns the leqList, should be called after generateLEQ
	public ArrayList<String[]> getLEQ(){
		generateLeq();
		return leqList;
	}
	
	
	
	/*
	 * Method for subsituing t into the translated terms
	 * And solve it to get array which will be used for the matrix
	 * It is more of simiplying it before putting it into the gaussian elimination
	 */
	
	public double[] solveforT(int[] tValues, String[] equations) {
		//equations == translated terms
		//tValues. will have t1...tm , m=number of operators and value either - or 1
		double[] result = new double[equations.length];
		
		String[] equationCopy = equations;
	//	System.err.println("\nBefore "+Arrays.toString(equationCopy)+"  "+Arrays.toString(tValues));

	//	System.out.println("t length = "+tValues.length);
		for(int i=0;i<tValues.length;i++) {
			String t= "t"+String.valueOf(i+1);
			//System.out.println("Now subbing "+t);
			for(int j=0;j<equations.length;j++) {
				equations[j] = equations[j].replaceAll(t,String.valueOf(tValues[i]));

			}
		}
		
		//subbed in values for all t 
		//now solve the equation, which will return a solved array
		//double solved=0.0;
		//System.err.println("ERROR "+Arrays.toString(equationCopy)+"  "+Arrays.toString(tValues));
		for(int i=0;i<equationCopy.length;i++) {
		//	System.err.println("Trying for "+equationCopy[i]);
			result[i] = Double.parseDouble(EquationSolver.solve(equationCopy[i]));
		}
		//System.out.println("Subsituted = "+Arrays.toString(result));

		return result;
		
		}
	
	
	
	
	/*
	 * Just returns an index on which quantifier it is
	 */
	public int whichQuantifier(String quantifierType) {
		if(quantifierType.isEmpty()) {
			System.err.println("Need a quantifier "+quantifierType);
			System.exit(0);
			return -10;
		}
		if(quantifierType.charAt(0)=='m') {
			//mu
			return 0;
		}
		else if(quantifierType.charAt(0)=='n') {
			//its nu
			return 1;
		}
		else if(quantifierType.charAt(0)=='f') {
			//free variable
			return 2;
		}
		
		System.err.println(quantifierType+" is Not a valid quantifier type");
		System.exit(0);
		return -10;
	}
	
	/*
	 * Now for each of the array, I add the quantifier case
	 * so solving for lfp or gfp
	 * It basically ready the array for the gaussian elimination
	 */
		public double[] solveForQuantifiers(double[] rhs, String quantifierType) {
			//rhs is the array with t values subbed in
			//quantifier type is the type of quantifier for each of the equation
			int quantifier = whichQuantifier(quantifierType);
			int index = getIndexSet(quantifierType.substring(3));
			//index in the array of which is the bound variable or free variable
			int length = rhs.length;
			
			//used to check for fixed point conditions
			boolean sub = false;
			
			//checking if others are non zero;
			for(int i=0;i<length-1;i++) {
				if(i!=index && rhs[i]!=0) {
					sub=true;
					break;
				}
			}
		//	System.out.println("Quantifer = "+quantifierType.charAt(0)+"  index = "+index);
			if(quantifier==0) {
				//is mu
		
			//	System.err.println("BEFORE = "+Arrays.toString(rhs)+"  "+quantifier);

				if(!sub) {
		
					if(rhs[index]==0 &&rhs[length-1]==0) {
						//so other values are all 0
						//so Just need to make sure i can have x=0, for [0,0,0,0]
						//do that by [x,y,z,c] = [1,0,0,0]
						rhs[index]--;
						return rhs;
					}
					else if(rhs[index]==0 && rhs[length-1]!=0) {
						//so for [0,0,0,5], i need to change it to x = -5, instead of 0=-5
						//do taht by [1,0,0,-5]
						rhs[index]=-1;
						rhs[length-1] *=-1;
						return rhs;
					}
					else {
						//System.err.println("WTF IS THIS "+ Arrays.toString(rhs));
					//	rhs[index]--;
						rhs[length-1]*=-1;
					//	System.err.println("AFter "+ Arrays.toString(rhs));

						return rhs;
					}
				}
				else {
					//so everything else is 0, apart from maybe index and c
					if(rhs[index]==0 && rhs[length-1]==0) {
						rhs[index]=-1;
						return rhs;
					}
					else if(rhs[index]==0 && rhs[length-1]!=0) {
						rhs[index]=1;
						return rhs;
					}
					else if(rhs[index]!=0 && rhs[length-1]!=0) {
						rhs[index]--;
						rhs[length-1]*=-1;
						return rhs;
					}
					else if(rhs[index]!=0 && rhs[length-1]==0) {
						return rhs;
					}
					
				}
				System.err.println("HELP GETTING HERE "+Arrays.toString(rhs)+"  sub="+sub);
				// so if var has a value, but others has value as well
				rhs[index]--;
				rhs[length-1] *=-1;
				return rhs;
		
			}
			else if(quantifier==1) {
			//	System.out.println("RHS  "+Arrays.toString(rhs));
				//is nu
				//need to check for all 0, constant has val but rest =0
				//if var = 1 and rest =0
				boolean isOne = (rhs[index]==1)?true:false;
				
				if(sub) {
					//[x=1.y=0,c=0] which means, y=x
					rhs[index]--;
					rhs[length-1]*=-1;
					return rhs;
				}
				else {
					//all 0
					if(isOne && rhs[length-1]==0) {
						//as its gfp. y=y, will give y=1
						rhs[index]=-1;
						rhs[rhs.length-1]=-1;
						return rhs;
					}
					else if(isOne && rhs[length-1]!=0) {
						// so something like nu.y = y+0.5
						//which is not a gfp nor a fixed point
						rhs[index]=0;
						rhs[length-1] *=-1;
						return rhs;
					}
					else if(!isOne && rhs[length-1]!=0) {
						//everything else is 0
						//e.g. nu.y = 0.5
						rhs[length-1]*= 1;
						rhs[index]=1;
						return rhs;
					}
					else if(!isOne && rhs[length-1]==0) {
						//e.g. nu.y = 0
						rhs[index]=1;
						return rhs;
					}
					System.err.println("shouldnt be anything else "+Arrays.toString(rhs));
					System.exit(0);
					return null;
				}
				
			}
			else if(quantifier==2) {
				//is free
				//The index in rhs, is the value of the free var
				//so just change index to 1 and rhs.len-1 to - of whats at index
				//free variables are not dependent on any other vars. 
				//Its given a value by the interpretation p
				double actualValue = rhs[index];
				rhs[length-1] = -1*actualValue;
				rhs[index]=-1;
				//System.err.println("Hello "+Arrays.toString(rhs)+"   "+actualValue+"  "+index+"  "+quantifierType);
//				for(String s:variableSet) {
//					//System.out.print(s+" ");
//				}
			//	System.out.println(Arrays.toString(variableSet.toArray()));
				return rhs;
				
			}
			System.err.println("Quantifier type not valid "+quantifier);
			System.exit(0);
			return null;
		}
	
		
		
		
		
		/*
		 * Now I should have values for the variables and t variables
		 * I subbed it into the leq list then will have another method which checks if
		 * its valud
		 */
	
	String[] variableArray;	
	public void setToArray() {
		variableArray = variableSet.toArray(new String[variableSet.size()]);
	}
	
	public double[] subValuesToLEQ(double[] variableValues, int[] tValues, String[] leq) {
		//note free variable is already subsituted, so should be any more;
		//will return a solved version of the leq 
		
		
		//this should of the size, [operator size][3]
		//sub t first
		//System.out.println(Arrays.toString(ts)+ "  hello "+Arrays.toString(vars));
		
	//	System.out.println("Before here "+Arrays.toString(leq)+"  "+Arrays.toString(tValues));

		for(int i=0;i<leq.length;i++) {

			for(int j=tValues.length-1;j>=0;j--) {
				String t= "t"+String.valueOf(j+1);
				leq[i]= leq[i].replaceAll(t,String.valueOf(tValues[j]));
			}
				for(int k=0;k<variableValues.length;k++) {
					String var = variableArray[k];
					//this should return something like "x" or "y"
					//System.out.println("first = "+String.valueOf(vars[k])+"   second = "+var);
					leq[i] = leq[i].replaceAll(var,String.valueOf(variableValues[k]));

				
			}
		
		}
		double[] result = new double[leq.length];
		double solved=0.0;

		for(int i=0;i<leq.length;i++) {
		//	System.out.println("After here "+Arrays.toString(leq)+"  "+i);

			solved = Double.parseDouble(EquationSolver.solve(leq[i]));
			//System.out.println(solved);
			result[i]=solved;
		}
		return result;
	}
		
	
}
