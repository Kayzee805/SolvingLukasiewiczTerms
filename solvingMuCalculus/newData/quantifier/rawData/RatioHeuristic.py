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
ratioAverage=[]
oneCounters=[]
candidateSizes = np.zeros((20))
successfulCounter=0
for i in range(18):
    quantifiers.append(currentSize)
    array1 = np.loadtxt(str(currentSize)+".txt")
    array2 = np.loadtxt(str(currentSize)+"v2.txt")
    array3 = np.loadtxt(str(currentSize)+"v3.txt")
    array4 = np.loadtxt(str(currentSize)+"v4.txt")
    arrayCombine = (np.concatenate((array1,array2,array3,array4)))
    #timeAlgorithm = np.log10(1e-6*arrayCombine[:,0])
    timeHeuristic = np.log10(1e-6*arrayCombine[:,4])
    candidateSols = arrayCombine[:,6]
    returnSols = arrayCombine[:,5]
    ratio=[]
    counter=0
    for j in range(len(candidateSols)):
        candidateSizes[int(candidateSols[j])]+=1
        if(candidateSols[j]!=1):
            if(returnSols[j]==1 and currentSize!=1):
                successfulCounter+=1
            #more than one candidate sol
            temp = (1-(returnSols[j]/candidateSols[j]))*100        
            ratio.append(temp)
        else:
            counter+=1
   # print(f"Length of candiidate sols={len(candidateSols)}")
    
    oneCounters.append(np.mean(counter)/len(candidateSols)*100)
    ratioAverage.append(np.mean(ratio))

    currentSize+=1

print(f"Number of success = {successfulCounter}")
print(f"\n\nHELLO {np.round(np.array((candidateSizes/np.sum(candidateSizes)*100)),2)}\n\n")
print(f"Number of ones = {oneCounters}")
print(f"Average of ones = {np.mean(oneCounters)}")
print(ratioAverage)
plt.scatter(quantifiers,ratioAverage,color='k',s=10)
plt.plot(quantifiers,ratioAverage,color='g')
plt.title("Reduction of candidate solutions by the heuristic")
plt.xlabel("Number of quantifiers")
plt.ylabel("Average reduction (%)")
plt.grid(which='both')
plt.xticks(np.arange(1,19,1))
plt.savefig("heuristic/reductionInSize.png")
plt.show()


#     plt.scatter(sizes,times,s=10)
#     plt.title("Raw data, size vs log10_time to evaluate terms")
#     plt.xlabel("Term size(bits to represent a term)")
#     plt.ylabel("log_10(time[seconds])")
#     plt.grid()
#     plt.savefig("figures/NewrawData/"+str(currentSize)+".png")
#     plt.show(block=False)

#     #plot processed data
#     x=np.linspace(0,31000,31)
#     fig,ax=plt.subplots()

#     plt.plot(x,meanArray,label='Mean',color='g')
#     plt.plot(x,minArray,label='Min',color='b',linestyle='--')
#     plt.plot(x,maxArray,label='Max',color='r',linestyle='-')
#    # plt.plot(x,median,label='Median',color='m',linestyle='-.')

#     plt.scatter(x,meanArray,s=10,color='k')
#     plt.scatter(x,minArray,s=10,color='k')
#     plt.scatter(x,maxArray,s=10,color='k')
#    # plt.scatter(x,median,s=10,color='k')

#     plt.xlabel("Term size (bits to represent a term)")
#     plt.ylabel("log_10(time[seconds])")
#     plt.title(f"Processed data, size vs log10_time to evaluate terms.")
#     plt.text(15000,-3, f"Data size per point = {currentSize}",fontsize='large')
#     ax.yaxis.set_minor_locator(tck.AutoMinorLocator())
#     ax.xaxis.set_minor_locator(tck.AutoMinorLocator())

#     plt.legend()

#     plt.grid(which='both')
#     plt.savefig("figures/newProcessedData/"+str(currentSize)+".png")

#     plt.show(block=False)
#     np.savetxt("data/NewprocessedData/"+str(currentSize)+".txt",np.transpose((meanArray,maxArray,minArray)),fmt='%.4f')

#     currentSize+=100


#     plt.close('all')




