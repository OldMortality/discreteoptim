#!/usr/bin/python
# -*- coding: utf-8 -*-
import numpy as np
import sys
 

from collections import namedtuple
Item = namedtuple("Item", ['index', 'value', 'weight'])

 
def getBest():
    return(bestValue)

 
def setBest(x,arr):
    global bestValue
    if (x > bestValue):
        #print('set best to ', x)
        bestValue = x
        setBestItems(arr)

 
def initBest():
    global bestValue
    bestValue = 0    

 
def initBestItems(n):
    global bestItems
    bestItems=np.zeros(n, dtype=np.int)

 
def setBestItems(arr):
    global bestItems
    #print('best items' , bestItems)
    bestItems = np.array(arr, dtype=np.int)
        
 
def getBestItems():
    return(bestItems)

 
 

 
def goElement(ix,value,k_used,items,K,v,w):

    if ix < items.size :
        #print(ix)python -m nuitka --standalone --mingw64 hello.py
         # it is feasible to take this element
        if (k_used + w[ix] <= K):
            if value + v[ix:].sum() > getBest():
                # There may be a point in taking this element
                # try taking it
                items[ix] = 1
                goElement(ix+1,value + v[ix],k_used + w[ix],items,K,v,w)  
                # try not taking it
                items[ix] = 0
                goElement(ix+1,value ,k_used,items,K,v,w)  
            else:
                # it is pointless to take this element
                items[ix] = 0
        else:
            # try not taking it
            items[ix] = 0
            goElement(ix+1,value ,k_used,items,K,v,w)
        
    else:
        # end node
        #print(ix,value,k_used,items)
        setBest(value,items)


def solve_it(input_data):
    # Modify this code to run your optimization algorithm

    # parse the input
    lines = input_data.split('\n')

    firstLine = lines[0].split()
    item_count = int(firstLine[0])
    capacity = int(firstLine[1])

    all_items = []

    for i in range(1, item_count+1):
        line = lines[i]
        parts = line.split()
        all_items.append(Item(i-1, int(parts[0]), int(parts[1])))

    it = np.array(all_items)
    v = it[:,1]
    w = it[:,2]
    K = capacity
    
    value_density = v/w
    
    arr1inds = value_density.argsort()[::-1]
    sorted_v = v[arr1inds]
    sorted_w = w[arr1inds]
    v= sorted_v
    w= sorted_w
    
    n = w.size
    
   
    sys.setrecursionlimit(n + 100)
    #items = np.zeros(n)

    #bestValue = 0
    initBest()
    initBestItems(n)
    goElement(0,0,0,np.zeros(n),K,v,w)
    getBest()
    
    value = getBest()
    
    # prepare the solution in the specified output format
    output_data = str(value) + ' ' + str(0) + '\n'
    t = getBestItems()
       # reorder items back again
    ix_list = list(arr1inds)
    a = ''
    for j in range(0,t.size):
        ix = ix_list.index(j)
        a = a + str(t[ix])  
    
    output_data += ' '.join(map(str, a))
   
    #trueOutput = (v2 * t2).sum()
    #print('trueOutput',trueOutput)
    #trueWeight = (w2 * t2).sum()
    #print('trueWeight',trueWeight)
    
    
    return(output_data)


if __name__ == '__main__':
    import sys
    if len(sys.argv) > 0:
        
        file_location =  sys.argv[1].strip()  #'data/ks_30_0' 
        with open(file_location, 'r') as input_data_file:
            input_data = input_data_file.read()
        print(solve_it(input_data))
    #else:
    #    print('This test requires an input file.  Please select one from the data directory. (i.e. python solver.py ./data/ks_4_0)')

