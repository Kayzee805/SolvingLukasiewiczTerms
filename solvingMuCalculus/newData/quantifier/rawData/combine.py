'''
import the files, then plot every 1000th?
'''
import matplotlib.pyplot as plt 
import matplotlib
import numpy as np 
from matplotlib.ticker import MultipleLocator
import matplotlib.ticker as tck

currentSize=1
meanArray=[]
maxArray=[]
minArray=[]
xAxis = []
times=[]
sizes=[]#

heuristicMean = []
averageRatios=[]
for i in range(18):
    currentSize=i+1
    xAxis.append(currentSize)
    array1 = np.loadtxt(""+str(currentSize)+".txt")
    array2 = np.loadtxt(""+str(currentSize)+"v2.txt")
    array3 = np.loadtxt(""+str(currentSize)+"v3.txt")
    array4 = np.loadtxt(""+str(currentSize)+"v4.txt")
    arrayCombine = (np.concatenate((array1,array2,array3,array4)))

    ratios = []
    for j in range(len(arrayCombine)):
        candidate = arrayCombine[j,6]
        heuristic = arrayCombine[j,5]
        if(candidate==1):
            #already have unique so avoid?
            continue
            #ratios.append(0)
        else:
            ratios.append(heuristic/candidate)
    averageRatios.append(100-np.mean(ratios)*100)
    print(f"Current trying for {i+1} has {len(ratios)}")

print(averageRatios)
print(f"Average number of candidates with 1={np.mean(averageRatios)}")
print(len(xAxis),len(averageRatios))
plt.scatter(xAxis,averageRatios,s=10,color='k')
plt.plot(xAxis,averageRatios,color='b')
plt.xlabel("Number of quantifiers")
plt.ylabel("Average reduction ratio (%)")
plt.title("Candidate solution reduction by the heuristic:No ones")
#plt.title("Candidate solution reduction by the heuristic:with ones")
plt.xticks(np.arange(1,19,1))
plt.grid(which='both')
#plt.savefig("heuristic/ratiosWithOnes.png")
plt.savefig("heuristic/ratiosNoOnes.png")

plt.show()

