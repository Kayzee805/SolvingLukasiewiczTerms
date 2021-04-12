# Overview of the system

A mu-term object is built using the muTerm class and this muTerm object can be evaluated using the Evaluation class. The Evaluation class contains 3 parts of the algorithm: generating a set of candidate solutions and two methods of filtering the solutions (the recursive algorithm and the heuristic).

The muTerm object follows a simple tree structure such that each muTerm can have up to 2 subTerms, although the number of subTerms will be dependent on the operator type. 

The evaluateAlgorithm class contains the two filtering method: recursive and the heuristic. Both of which can only be ran after a set of candidate solutions has been generated. 

The Evaluation class returns a List of type long, where the indexes of the list are as follows:

	0. Time taken to evaluate using the recursive algorithm
 	1. Size of the term
 	2. number of operators
 	3. Magnitude bound of the term
 	4. Time taken to evaluate using the heuristic
 	5. Number of candidate solutions returned by the heuristic
 	6. Number of candidate solutions of the term.
 	7. Time taken to generate candidate solutions.



As shown in the Evaluation class, a muTerm *e=* μx.(x ⊓ ν y.(x ⊔ y)) can be constructed with the following code,

```java
muTerm term=new muTerm("mu",
                       new muTerm("cap",
				new muTerm("var",null,null,"x",""),
				new muTerm("v",
					new muTerm("cup",
                                    		new muTerm("var",null,null,"x","")
						,new muTerm("var",null,null,"y","")
                              		,"","")
                          	,null,"y","")
			,"","")
           	,null,"x","");
```

 

