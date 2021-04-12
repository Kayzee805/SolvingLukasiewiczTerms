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
            if(currentSize==1):
                print(f"Size = {arrayCombine[j,6]}  and final ={arrayCombine[j,5]}")
            sucesssQuantifier.append(currentSize)
            sucesssTime.append(timeHeuristic[j])
            succCounter+=1
        else:
            failCounter+=1
            failTime.append(timeHeuristic[j])
            failQuantifier.append(currentSize)

    overAllFailure.append(failCounter)
    overAllSuccess.append(succCounter)
    overAllSize.append(failCounter+succCounter)
    currentSize+=1
#
successInPercentage = []
for i in range(len(overAllSuccess)):
    successInPercentage.append(np.round((overAllSuccess[i]/overAllSize[i]),2)*100)

plt.scatter(quantifiers,successInPercentage,s=10,marker='x',color='k')
plt.plot(quantifiers,successInPercentage,color='g',label='Success rate')
plt.xlabel("Number of quantifiers")
plt.ylabel("Success rate (%)")
plt.title("Sucess rate of the heuristic over varying quantifiers")
plt.xticks(np.arange(1,19,1))
plt.yticks(np.arange(40,105,10))
plt.grid(which='both')
plt.savefig("heuristic/sucessRate.png")

plt.show()


width=0.35
quantifiers=np.asarray(quantifiers)
plt.bar(quantifiers,overAllFailure,width,label="Failure")
plt.bar(quantifiers+width,overAllSuccess,width,label="Success")
plt.ylabel("Frequency")
plt.xlabel("Number of Quantifiers")
plt.title("Numbers of successes, same answer as algorithm")
plt.xticks((quantifiers)+width/2, np.arange(1,19,1))
plt.legend(loc='best')
plt.savefig("heuristic/barChart.png")
plt.show()

plt.scatter(quantifiers,meanHeuristic,s=10,color='k')
plt.scatter(quantifiers,minHeuristic,s=10,color='k')
plt.scatter(quantifiers,maxHeuristic,s=10,color='k')

plt.plot(quantifiers,meanHeuristic,label='Mean',color='g')
plt.plot(quantifiers,minHeuristic,label='Min',color='b',linestyle='--')
plt.plot(quantifiers,maxHeuristic,label='Max',color='r',linestyle='-')
plt.legend()
plt.xlabel("Number of quantifiers")
plt.ylabel("log_10(Time (s))")
plt.title("Heuristic:: Time taken to evaluate temrs with varying Quantifiers")
plt.xticks(np.arange(1,19,1))
plt.grid(which='both')
plt.savefig("heuristic/Heuristic-Quantifiers-processedData.png")
plt.show()



plt.scatter(failQuantifier,failTime,s=2,marker='x',color='r',label=f"failed: {len(failTime)}")
plt.scatter(sucesssQuantifier,sucesssTime,s=2,marker='+',color='g',label=f'success: {len(sucesssTime)}')
plt.legend()
plt.xlabel("Number of quantifiers")
plt.ylabel("log_10(Time (s))")
plt.title("Heuristic:: Time taken to evaluate temrs with varying Quantifiers")
plt.xticks(np.arange(1,19,1))

plt.savefig("heuristic/Heuristic-Quantifiers-rawData.png")
plt.show()




#     #plot raw data
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




