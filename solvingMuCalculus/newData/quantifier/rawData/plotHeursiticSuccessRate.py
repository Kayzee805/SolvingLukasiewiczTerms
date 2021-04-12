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
    timeHeuristic = np.log10(1e-6*arrayCombine[:,4])
    timeCandidate = np.log10(1e-6*arrayCombine[:,6])
    minHeuristic.append(np.amin(timeHeuristic))
    meanHeuristic.append(np.mean(timeHeuristic))
    maxHeuristic.append(np.amax(timeHeuristic))
    failCounter=0
    succCounter=0
    for j in range(len(arrayCombine)):
        if(arrayCombine[j,6]!=1):
            if(arrayCombine[j,5]==1):
                #success
                succCounter+=1
            else:
                failCounter+=1
          

    overAllFailure.append(failCounter)
    overAllSuccess.append(succCounter)
    overAllSize.append(failCounter+succCounter)
    currentSize+=1
#
successInPercentage = []
for i in range(len(overAllSuccess)):
    successInPercentage.append(np.round((overAllSuccess[i]/overAllSize[i]),2)*100)


print(successInPercentage)
print(overAllSize)
plt.scatter(quantifiers,successInPercentage,s=10,marker='x',color='k')
plt.plot(quantifiers,successInPercentage,color='g',label='Success rate')
plt.xlabel("Number of quantifiers")
plt.ylabel("Success rate (%)")
plt.title("Sucess rate of the heuristic over varying quantifiers")
plt.xticks(np.arange(1,19,1))
plt.yticks(np.arange(0,110,10))
plt.grid(which='both')
plt.savefig("heuristic/heuristicSucesssRateMultipleSolutions.png")

plt.show()
