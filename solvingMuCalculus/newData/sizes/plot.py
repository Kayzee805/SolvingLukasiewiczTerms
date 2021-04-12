'''
import the files, then plot every 1000th?
'''
import matplotlib.pyplot as plt 
import matplotlib
import numpy as np 
from matplotlib.ticker import MultipleLocator
import matplotlib.ticker as tck

currentSize=1000
meanArray=[]
maxArray=[]
minArray=[]
xAxis = []
times=[]
sizes=[]
for i in range(15):
    xAxis.append(currentSize)
    array1 = np.loadtxt("rawData/"+str(currentSize)+".txt")
    array2 = np.loadtxt("rawData/"+str(currentSize)+"V2.txt")
    array3 = np.loadtxt("rawData/"+str(currentSize)+"V3.txt")
    array4 = np.loadtxt("rawData/"+str(currentSize)+"V4.txt")
    arrayCombine = (np.concatenate((array1,array2,array3,array4)))
    TimeTaken = np.log10(1e-6*arrayCombine[:,0])
    sizeArray = arrayCombine[:,1]
    print(currentSize)

    meanArray.append(np.mean(TimeTaken))
    minArray.append(np.amin(TimeTaken))
    maxArray.append(np.amax(TimeTaken))
    for j in range(len(arrayCombine)):
        times.append(TimeTaken[j])
        sizes.append(sizeArray[j])
    currentSize+=1000


plt.scatter(xAxis,meanArray,s=10,color='k')
plt.scatter(xAxis,minArray,s=10,color='k')
plt.scatter(xAxis,maxArray,s=10,color='k')
plt.plot(xAxis,meanArray,label='Mean',color='g')
plt.plot(xAxis,minArray,label='Min',color='b',linestyle='--')
plt.plot(xAxis,maxArray,label='Max',color='r',linestyle='-')
plt.legend()
plt.xlabel("Operators")
plt.ylabel("log_10(time(s))")
plt.title("Time taken to evaluate terms with varying quantifiers")
plt.grid(which='both')
plt.savefig("figures/ProcessedData2.png")

plt.show()


plt.scatter(sizes,times,s=10)
plt.title("Raw data of time taken to evaluate terms with varying magnitude bound B")
plt.xlabel("log_10(B)")
plt.ylabel("log_10(Time(s))")
plt.savefig("figures/rawdata2.png")
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




