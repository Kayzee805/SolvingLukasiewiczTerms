package solvingMuCalculus;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
public class newGenerator {
	static Random random = new Random();
	public static ArrayList<muTerm> generateTerms(int totalTests, int opLimit,int mu,int nu,int upperBound,int currentSize){
		ArrayList<muTerm> termList = new ArrayList<muTerm>();
		int[] bBound = new int[] {10,100,1000,10000,100000,1000000,10000000,100000000,100000000};
		int length = bBound.length;
		
		while(termList.size()!=totalTests) {
			int randomNumber = random.nextInt(length);
			int muNumber = random.nextInt(mu);
			int nuNumber = random.nextInt(nu);
			opLimit = random.nextInt(opLimit)+4;
//			int muNumber = mu;
//			int nuNumber = nu;
			
			//add terms that match the conditions to termList
		//	muTerm example = muTermGenerator.generateMuTerms(muNumber, nuNumber, opLimit, bBound[randomNumber]);
			
			//for quantifiers
			muTerm example = muTermGenerator.generateMuTerms(mu, nu, opLimit,bBound[random.nextInt(length)]);
			
			int highestRational = example.highestRational;
			if(highestRational==0)highestRational+=2;
			if(highestRational==1)highestRational++;
			int size=0;
			//lets generate data for quantifiers varying op==8 bound = 10^6
			if(example.opCounter==0) {
				 size = (int) (example.quantifierSize*Math.log10(highestRational));
			}
			else {
				 size = (int) (example.opCounter*Math.pow((example.opCounter+example.variableSet.size()),2)*Math.log10(highestRational));
			}		
		//	if(example.opCounter==6 && highestRational==100000) {
			if(size>=currentSize-1000 && size<currentSize) {
		//	if(example.opCounter==5 && example.quantifierSize==(mu+nu) &&example.highestRational==upperBound) {
				termList.add(example);
//				muTerm dummy = example;
//				Evaluation eval = new Evaluation();
//				HashMap<ArrayList<String>,ArrayList<ArrayList<Double>>> quantifierAndSoltuion = eval.generateCandidateSolutions(example);
//				if(quantifierAndSoltuion.size()!=0) {
//					System.err.println(termList.size()+" "+dummy.opCounter);
//
//					termList.add(example);
//				}
//				else {
//					System.out.println("Not found");
//				}
				if(termList.size()%100==0) {
					System.err.println(termList.size()+" "+currentSize+"  "+size);
				}
			//	termList.add(example);
			}
			else{
				//System.out.println(example.opCounter+" "+example.quantifierSize+" "+example.highestRational+"  === "+currentSize+" "+(mu+nu)+  " "+upperBound);
				//System.out.println("size ="+size);
			}
//		
			}
		
		
		
		return termList;
	}
	public static void writeTerms(int totalTests, int opLimit,int mu,int nu,int upperBound,int size) throws IOException {
		ArrayList<muTerm> termList = generateTerms(totalTests,opLimit,mu,nu,upperBound,size);
		FileOutputStream fos = new FileOutputStream("newData/sizes/"+size+".bin");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		int counter=0;
		for(muTerm x:termList) {
			oos.writeObject(x);
			//System.err.println(x.toString(x));
//			if(counter%1==0) {
//				System.out.println(x.quantifierSize+" "+x.highestRational+" "+x.opCounter);
//			}
			counter++;
		}
		oos.close();
		System.out.println("Finish writing");
		
		
	}
	public static void main(String[] args) throws IOException {
		//Do varying quantifiers
		int totalTests =1000;
		int opLimit = 9;
	
		int upperBound=100000;

		int mu=7;
		int nu=7;
		//int size = mu+nu;
		int size=21000;
		for(int i=0;i<10;i++) {
			System.out.println("Generating "+mu +" "+nu);
			writeTerms(totalTests,opLimit,mu,nu,upperBound,size);
			//size+=1;
//			if(i%2==0)nu++;
	//		else {mu++;}
		//	size=(mu+nu);
			System.out.println("  size = "+size); 
			//upperBound*=10;
			size+=1000;
		}
		System.out.println("Done");

		
	}
	
//	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
//		ArrayList<muTerm> objects = new ArrayList<muTerm>();
//		FileInputStream fis = new FileInputStream("newData/test.bin");
//		ObjectInputStream ois = new ObjectInputStream(fis);
//		muTerm obj=null;
//		boolean isExist = true;
//		
//		while(isExist) {
//			if(fis.available()!=0) {
//				obj = (muTerm)ois.readObject();
//				objects.add(obj);
//			}
//			else {
//				isExist = false;
//			}
//		}
//		for(muTerm x:objects) {
//			System.out.println(x.toString(x));
//		}

//}
	
}
