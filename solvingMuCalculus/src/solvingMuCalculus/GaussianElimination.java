package solvingMuCalculus;

import java.util.Arrays;
import java.util.LinkedHashMap;

public class GaussianElimination {


	public GaussianElimination() {
		
		
	}
	static double[] bSolve;
	static double[][] aSolve;
	public static  double[] getB() {
		return bSolve;
	}
	public static double[][] getA(){
		return aSolve;
	}
	
    public static double[] lsolve(double[][] A, double[] b) {
        int n = b.length;

        for (int p = 0; p < n; p++) {

            // find pivot row and swap
            int max = p;
            for (int i = p + 1; i < n; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            double[] temp = A[p]; A[p] = A[max]; A[max] = temp;
            double   t    = b[p]; b[p] = b[max]; b[max] = t;

            // pivot within A and b
            for (int i = p + 1; i < n; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < n; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }
        
     //   System.out.println("\n\n\nBefore back sub TEST\nA = "+Arrays.deepToString(A));
      //  System.out.println("b="+Arrays.toString(b));
        // back substitution
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
            if(x[i]==-0)x[i]=0;
        }
        return x;
    }
    
	public static LinkedHashMap<double[][],double[]> ge(double[][] a, double[]b) {
		
		int row = a.length;
		int col = b.length;
		int n=row;

	    		for(int i=0;i<col-1;i++) {
	    			
	    			int max = i;
	    			for(int p =i+1;p<col;p++) {
	    				if(Math.abs(a[p][i])> Math.abs(a[max][i])) {
	    					max=p;
	    				}
	    			}
	    			double[] temp = a[i];a[i]=a[max];a[max]=temp;
	    			double t = b[i];b[i] =b[max];b[max]=t;
	    			
	    			
	    			for(int j=i+1;j<row;j++) {
	    				b[j] =a[i][i]*b[j]-a[j][i]*b[i];	//to solve the system
	    				for(int k=i+1;k<col;k++) {
	    					a[j][k]=a[i][i]*a[j][k]-a[j][i]*a[i][k];
	    				}
	    				a[j][i]=0;
	    			}
	    			if(i>=2){
	    				for(int j=i+1;j<row;j++) {	//they used j here so a bit confused
	    					b[j]=b[j]/a[i-1][i-1];
	    					for(int k=i+1;k<col;k++){
	    						a[j][k]=a[j][k]/a[i-1][i-1];
	    					}
	    				}
	    			}
	    		}

	     
	        
		

		//System.out.println(Arrays.deepToString(a));

	//	System.out.println(Arrays.toString(b));
		LinkedHashMap<double[][],double[]> result = new LinkedHashMap<double[][],double[]>();
		result.put(a,b);
		return result;
		
		/*
		for(int i=0;i<col-1;i++) {
			for(int j=i+1;j<row;j++) {
				int p = gcd(a[i][i],a[j][i]);
				int alpha = a[i][i]/p;
				int gamma = a[j][i]/p;
				b[j] = alpha*b[j]-gamma*b[i];
				for(int k=i+1;k<col;k++) {
					a[j][k] = alpha*a[j][k]-gamma*a[i][k];
				}
				a[j][i]=0;
				
			}
		}*/
	
	}
    
	public static LinkedHashMap<double[][],double[]> geWITHOUTPIVOUT(double[][] a, double[]b) {
		
		int row = a.length;
		int col = b.length;
		
		for(int i=0;i<col-1;i++) {
			for(int j=i+1;j<row;j++) {
				b[j] =a[i][i]*b[j]-a[j][i]*b[i];	//to solve the system
				for(int k=i+1;k<col;k++) {
					a[j][k]=a[i][i]*a[j][k]-a[j][i]*a[i][k];
				}
				a[j][i]=0;
			}
			if(i>=2){
				for(int j=i+1;j<row;j++) {	//they used j here so a bit confused
					b[j]=b[j]/a[i-1][i-1];
					for(int k=i+1;k<col;k++){
						a[j][k]=a[j][k]/a[i-1][i-1];
					}
				}
			}
		}
		//System.out.println(Arrays.deepToString(a));

	//	System.out.println(Arrays.toString(b));
		LinkedHashMap<double[][],double[]> result = new LinkedHashMap<double[][],double[]>();
		result.put(a,b);
		return result;
		
		/*
		for(int i=0;i<col-1;i++) {
			for(int j=i+1;j<row;j++) {
				int p = gcd(a[i][i],a[j][i]);
				int alpha = a[i][i]/p;
				int gamma = a[j][i]/p;
				b[j] = alpha*b[j]-gamma*b[i];
				for(int k=i+1;k<col;k++) {
					a[j][k] = alpha*a[j][k]-gamma*a[i][k];
				}
				a[j][i]=0;
				
			}
		}*/
	
	}
	public static double[] generateB(double[][] x) {
		double[] result= new double[x.length];
		int n = x[0].length;
		for(int i=0;i<x.length;i++) {
			result[i] = x[i][n-1];
		}
		return result;
	}
	public static double[][] generateA(double[][] x){
		double[][] result = new double[x.length][x[0].length-1];
		//System.out.println(x[0].length+" "+result[0].length);
		for(int i=0;i<x.length;i++) {
			for(int j=0;j<result[0].length;j++) {
				result[i][j] = x[i][j];
			}
	//		System.out.println(Arrays.toString(result[i]));

		}
		return result;
	}
	//this subs things back in
	public static double[] solver(double[][] unsolvedMatrix) {
		boolean useNormalGauss = true;
		if(useNormalGauss) {
			return lsolve(generateA(unsolvedMatrix),generateB(unsolvedMatrix));
		}
	//	LinkedHashMap<double[][],double[]> result =geWITHOUTPIVOUT(generateA(unsolvedMatrix),generateB(unsolvedMatrix));
		LinkedHashMap<double[][],double[]> result =ge(generateA(unsolvedMatrix),generateB(unsolvedMatrix));

		//back sub
		double[][]a= new double[unsolvedMatrix.length][unsolvedMatrix[0].length-1];
		double[]b=new double[unsolvedMatrix.length];
		for(double[][] keys:result.keySet()) {
			a= keys;
			b=result.get(keys);
		
		}
		
		//System.out.println("Before Backsub\nA ="+Arrays.deepToString(a)+"\nb="+Arrays.toString(b));
		
		
		double[] x = new double[b.length];
		for(int i=b.length-1;i>=0;i--) {
			double sum=0.0;
			for(int j=i+1;j<b.length;j++) {
				sum+=a[i][j]*x[j];
			}
			//i can make it so i multiply everything here?
			//cos this part gives me fractions
			x[i]=(b[i]-sum)/a[i][i];
			
		}
		return x;
				
	}

	public static boolean validSolutions(double[] x) {
		for(int i=0;i<x.length;i++) {
		//	System.out.println(Double.isNaN(x[i])+" is NANNNN");
			if(Double.isInfinite(x[i]) || Double.isNaN(x[i])) {
				return false;
			}
			if(x[i]>1 || x[i]<0) {
				return false;
			}
		}
		//if(big)
			//System.exit(0);
		return true;
	}
	
	
	public static void main(String[] args) {
		double[][] a =  {{1,0,-1,0,0},
				{0,},
				{-3,-1,3,1,0},
				{2,3,2,-1,-8}};
		double[] b= solver(a);
		System.out.println("Arrays. ="+Arrays.toString(b));
	
		double[][] a2 = {{1,2,-3,-1},
				{0,-3,2,6},
				{-3,-1,3,1},
				{2,3,2,-1}};
		double[] b2 = {0,-8,0,-8};
		System.out.println("Test = "+Arrays.toString(lsolve(a2,b2)));
		

		}
}
