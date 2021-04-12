'''
import the files, then plot every 1000th?
'''
import matplotlib.pyplot as plt 
import matplotlib
import numpy as np 
from matplotlib.ticker import MultipleLocator
import matplotlib.ticker as tck


'''
index:
0 = time algorithm
1 = size
2 = ops
3 = B
4 = heuristic time
5 = sucess
6 = candidate sols
'''
currentSize = 1
meanHeuristic = []
minHeuristic=[]
maxHeuristic=[]
rawTime=[]
rawQuantifiers=[]

quantifiers=[]
success=[]
sucesssTime=[]
sucesssQuantifier=[]
failTime=[]
failQuantifier=[]
overAllSuccess=[]
overAllFailure=[]
overAllSize=[]
for i in range(18):
    quantifiers.append(currentSize)
    array1 = np.loadtxt(str(currentSize)+".txt")
    array2 = np.loadtxt(str(currentSize)+"v2.txt")
    array3 = np.loadtxt(str(currentSize)+"v3.txt")
    array4 = np.loadtxt(str(currentSize)+"v4.txt")
    arrayCombine = (np.concatenate((array1,array2,array3,array4)))
    #timeAlgorithm = np.log10(1e-6*arrayCombine[:,0])
    # timeHeuristic = np.log10(1e-6*arrayCombine[:,4])
    # timeCandidate = np.log10(1e-6*arrayCombine[:,6])
    timeHeuristic = arrayCombine[:,4]
    timeCandidate = (arrayCombine[:,0])
    timeHeuristic*= 1e-6

    #timeCandidate=timeCandidattimeHeuristic
    # for j in range(len(timeCandidate)):
    #     temp = timeCandidate[j]-timeHeuristic[j]
    #     if(temp==0 or temp<0):
    #         print(f"At: {timeCandidate[j]} and {timeHeuristic[j]} and i={i} j={j}")
    timeHeuristic = np.log10(timeHeuristic)
    minHeuristic.append(np.amin(timeHeuristic))
    meanHeuristic.append(np.mean(timeHeuristic))
    maxHeuristic.append(np.amax(timeHeuristic))
    currentSize+=1
#
print(meanHeuristic)
plt.scatter(quantifiers,meanHeuristic,s=10,color='k')
plt.scatter(quantifiers,minHeuristic,s=10,color='k')
plt.scatter(quantifiers,maxHeuristic,s=10,color='k')

plt.plot(quantifiers,meanHeuristic,label='Mean',color='g')
plt.plot(quantifiers,minHeuristic,label='Min',color='b',linestyle='--')
plt.plot(quantifiers,maxHeuristic,label='Max',color='r',linestyle='-')
plt.legend()
plt.xlabel("Number of quantifiers")
plt.ylabel("log_10(Time (s))")
plt.title("Heuristic:: Time taken to evaluate terms with varying Quantifiers")
plt.xticks(np.arange(1,19,1))
plt.grid(which='both')
#plt.savefig("heuristic/NO-Candidate-Heuristic-Quantifiers-processedData.png")
#plt.savefig("heuristic/with-Candidate-Heuristic-Quantifiers-processedData.png")
plt.savefig("heuristic/heuristicVaryingQuantifiers.png")
plt.show()


