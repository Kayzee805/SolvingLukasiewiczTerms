package solvingMuCalculus;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class evaluationThreads {
	//will call evaluation.evaluate

	
	public static ArrayList<muTerm> readFile(String filePath) throws IOException, ClassNotFoundException{
		ArrayList<muTerm> termList = new ArrayList<muTerm>();
		FileInputStream fis = new FileInputStream(filePath);
		ObjectInputStream ois = new ObjectInputStream(fis);
		muTerm obj = null;
		boolean isExist=true;
		
		while(isExist) {
			if(fis.available()!=0) {
				obj = (muTerm)ois.readObject();
				termList.add(obj);
			}
			else {
				isExist=false;
			}
		}
		return termList;
	}
	public static int CalculateSize(muTerm example) {
		int size =0;
		int highestRational = example.highestRational;
		if(highestRational==0)highestRational+=2;
		if(highestRational==1)highestRational++;
		//lets generate data for quantifiers varying op==8 bound = 10^6
		if(example.opCounter==0) {
			 size = (int) (example.quantifierSize*Math.log10(highestRational));
		}
		else {
			 size = (int) (example.opCounter*Math.pow((example.opCounter+example.quantifierSize),2)*Math.log10(highestRational));
		}		
		return size;
	}
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		System.out.println("Starting now");
		String file ="newData/quantifier/";
		
		int size =1;
		
		for(int i=0;i<18;i++) {
			System.out.println("Evaluation for "+size);
			String filePath = file+size+".bin";
			ArrayList<muTerm> allTerms1 = readFile(filePath);
			ArrayList<Integer> allSize1 = new ArrayList<Integer>();
			
			for(muTerm x:allTerms1) {
				allSize1.add(CalculateSize(x));
			}
			System.err.println("Size=="+allTerms1.size());
			
			String title = file+"rawData/"+size;
	
			//start thread
			List<muTerm> one = allTerms1.subList(0, allTerms1.size()/4);
		//	List<muTerm> three = allTerms1.subList(145,150);

			List<muTerm> two = allTerms1.subList(allTerms1.size()/4,allTerms1.size()/2);
			List<muTerm> three = allTerms1.subList(allTerms1.size()/2,3*allTerms1.size()/4);
			List<muTerm> four = allTerms1.subList(3*allTerms1.size()/4, allTerms1.size());
			List<Integer> s1 = allSize1.subList(0, allSize1.size()/4);
			List<Integer> s2 = allSize1.subList(allSize1.size()/4, allSize1.size()/2);
			List<Integer> s3 = allSize1.subList(allSize1.size()/2,3*allSize1.size()/4);
			List<Integer> s4 = allSize1.subList(3*allSize1.size()/4, allSize1.size());
			threadOne obj1 = new threadOne(one,s1,title);
			threadTwo obj2 = new threadTwo(two,s2,title);
			threadThree obj3 = new threadThree(three,s3,title);
			threadFour obj4 = new threadFour(four,s4,title);

			obj1.start();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		obj2.start();
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			obj3.start();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		 	obj4.start();
	
			size+=1;
		}
		
		System.err.println("Done");
		}


}
class threadOne extends Thread{
	Object[] terms;
	String title;
	Object[] sizes;
	
	threadOne(List<muTerm> one,List<Integer> s1,String name){
		this.terms=one.toArray();
		this.title=name;
		this.sizes=s1.toArray();
	}
	public void run() {
		System.err.println("Starting thread 1 of size "+terms.length);
		try {
			FileWriter myWriter = new FileWriter(this.title+".txt",true);
			for(int i=0;i<terms.length;i++) {
		//		if(i==88)continue;
				muTerm a = (muTerm) terms[i];
				
				//System.out.println(a.toString(a) +" "+a.opCounter);

				Evaluation eval = new Evaluation();
				ArrayList<Long> time = eval.evaluate(a);
				if(time.get(0)==0l)continue;
				

				long algorithm = time.get(0);
				long heuristic = time.get(1);
				long heuristicSize = time.get(2);
				long originalSize = time.get(3);
				long timeForCandidate = time.get(4);
				//time will contain 1 elem, unless heuristic is being applied
				myWriter.write(algorithm/1000+" "+sizes[i]+" "+a.opCounter+" "+a.highestRational+" "+heuristic/1000+" "+heuristicSize+" "+originalSize+" "+timeForCandidate/1000+"\n");
				if(i%50==0) {
					System.out.println("1 == "+i+" "+time.get(0)/1000+"  ");
				//	System.out.println(i+" "+a.toString(a));
				}
			}
			myWriter.close();
		}
		catch(IOException e) {
			System.err.println("Error occured");
			System.exit(0);
		}
		
	}
	
	
}

class threadTwo extends Thread{
	Object[] terms;
	String title;
	Object[] sizes;
	
	threadTwo(List<muTerm> one,List<Integer> s1,String name){
		this.terms=one.toArray();
		this.title=name;
		this.sizes=s1.toArray();
	}
	public void run() {
		System.err.println("Starting thread 2 of size "+terms.length);
		try {
			FileWriter myWriter = new FileWriter(this.title+"v2.txt",true);
			for(int i=0;i<terms.length;i++) {
				//if(i==13 || i==32 ||i==214 ||i==242)continue;

				muTerm a = (muTerm) terms[i];
				Evaluation eval = new Evaluation();
				ArrayList<Long> time = eval.evaluate(a);
				if(time.get(0)==0l)continue;
				//System.out.println("has solution");

				//System.out.println("tme sisze ="+time.size());
				//time will contain 1 elem, unless heuristic is being applied
				

				long algorithm = time.get(0);
				long heuristic = time.get(1);
				long heuristicSize = time.get(2);
				long originalSize = time.get(3);
				long timeForCandidate = time.get(4);
				//time will contain 1 elem, unless heuristic is being applied
				myWriter.write(algorithm/1000+" "+sizes[i]+" "+a.opCounter+" "+a.highestRational+" "+heuristic/1000+" "+heuristicSize+" "+originalSize+" "+timeForCandidate/1000+"\n");
				if(i%50==0) {
					System.out.println("2 == "+i+" "+time.get(0)/1000);
				}
			}
			myWriter.close();
		}
		catch(IOException e) {
			System.err.println("Error occured");
			System.exit(0);
		}
		
	}
	
	
}
class threadThree extends Thread{
	Object[] terms;
	String title;
	Object[] sizes;
	
	threadThree(List<muTerm> one,List<Integer> s1,String name){
		this.terms=one.toArray();
		this.title=name;
		this.sizes=s1.toArray();
	}
	public void run() {
		System.err.println("Starting thread 3 of size "+terms.length);
		try {
			FileWriter myWriter = new FileWriter(this.title+"v3.txt",true);
			for(int i=0;i<terms.length;i++) {
				//if(i!=0)continue;
			//	System.out.println("Starting for real now");
				muTerm a = (muTerm) terms[i];
				Evaluation eval = new Evaluation();
				ArrayList<Long> time = eval.evaluate(a);
				if(time.get(0)==0l)continue;
				//time will contain 1 elem, unless heuristic is being applied

				long algorithm = time.get(0);
				long heuristic = time.get(1);
				long heuristicSize = time.get(2);
				long originalSize = time.get(3);
				long timeForCandidate = time.get(4);
				//time will contain 1 elem, unless heuristic is being applied
				myWriter.write(algorithm/1000+" "+sizes[i]+" "+a.opCounter+" "+a.highestRational+" "+heuristic/1000+" "+heuristicSize+" "+originalSize+" "+timeForCandidate/1000+"\n");
				if(i%50==0) {
					System.out.println("3 == "+i+" "+time.get(0)/1000);
				}
			}
			myWriter.close();
		}
		catch(IOException e) {
			System.err.println("Error occured");
			System.exit(0);
		}
		
	}
	
	
}
class threadFour extends Thread{
	Object[] terms;
	String title;
	Object[] sizes;
	
	threadFour(List<muTerm> one,List<Integer> s1,String name){
		this.terms=one.toArray();
		this.title=name;
		this.sizes=s1.toArray();
	}
	public void run() {
		System.err.println("Starting thread 4 of size "+terms.length);
		try {
			FileWriter myWriter = new FileWriter(this.title+"v4.txt",true);
			for(int i=0;i<terms.length;i++) {
				muTerm a = (muTerm) terms[i];
				Evaluation eval = new Evaluation();
				ArrayList<Long> time = eval.evaluate(a);
				if(time.get(0)==0l)continue;
				
				long algorithm = time.get(0);
				long heuristic = time.get(1);
				long heuristicSize = time.get(2);
				long originalSize = time.get(3);
				long timeForCandidate = time.get(4);
				//time will contain 1 elem, unless heuristic is being applied
				myWriter.write(algorithm/1000+" "+sizes[i]+" "+a.opCounter+" "+a.highestRational+" "+heuristic/1000+" "+heuristicSize+" "+originalSize+" "+timeForCandidate/1000+"\n");
				if(i%50==0) {
					System.out.println("4 == "+i+" "+time.get(0)/1000);
				}
			}
			myWriter.close();
		}
		catch(IOException e) {
			System.err.println("Error occured");
			System.exit(0);
		}
		
	}
	
	
}