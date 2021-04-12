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
sizes=[]
for i in range(9):
    xAxis.append(currentSize)
    array1 = np.loadtxt(str(currentSize)+".txt")
    array2 = np.loadtxt(str(currentSize)+"v2.txt")
    array3 = np.loadtxt(str(currentSize)+"v3.txt")
    array4 = np.loadtxt(str(currentSize)+"v4.txt")
    arrayCombine = (np.concatenate((array1,array2,array3,array4)))
#    TimeTaken = np.log10(1e-6*arrayCombine[:,0])
    TimeTaken = (1e-6*arrayCombine[:,0])
   
    sizeArray = arrayCombine[:,1]
    print(currentSize)

    meanArray.append(np.mean(TimeTaken))
    minArray.append(np.amin(TimeTaken))
    maxArray.append(np.amax(TimeTaken))
    for j in range(len(arrayCombine)):
        times.append(TimeTaken[j])
        sizes.append(currentSize)
    currentSize+=1


plt.scatter(xAxis,meanArray,s=10,color='k')
plt.scatter(xAxis,minArray,s=10,color='k')
plt.scatter(xAxis,maxArray,s=10,color='k')
plt.plot(xAxis,meanArray,label='Mean',color='g')
plt.plot(xAxis,minArray,label='Min',color='b',linestyle='--')
plt.plot(xAxis,maxArray,label='Max',color='r',linestyle='-')
plt.legend()
plt.xlabel("log_10(B)")
#plt.ylabel("log_10(time(s))")
plt.ylabel("Time(s)")

plt.title("Time taken to evaluate terms for varying Magnitude Bound B")
plt.grid(which='both')
#plt.savefig("figures/RationalLOG-processedData.png")
plt.savefig("figures/Rational-processedData.png")

plt.show()


plt.scatter(sizes,times,s=10)
plt.xlabel("log_10(B)")
#plt.ylabel("log_10(time(s))")
plt.ylabel("Time(s)")

plt.title("Raw data:Time taken to evaluate terms for varying Magnitude Bound B")
#plt.savefig("figures/RationalLOG-rawData.png")
plt.savefig("figures/Rational-rawData.png")

plt.show()




