#!/usr/bin/python
# -*- coding: utf-8 -*-
import numpy as np

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

def getBestItems():
    return(bestItems)


def goElement(ix,value,k_used,items,K,v,w):

    if ix < items.size :
        if (k_used + w[ix] <= K):
            if value + v[ix:].sum() > getBest():
                # it is feasible to take this element
                # try taking it
                items[ix] = 1
                goElement(ix+1,value + v[ix],k_used + w[ix],items,K,v,w)  
                # try not taking it
                items[ix] = 0
                goElement(ix+1,value ,k_used,items,K,v,w)  
            else:
                # it is not feasible or pointless to take this element
                items[ix] = 0
                # end node
                #print(ix,value,k_used,items)
    
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
    
    
    n = w.size
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
    a = ''
    for i in range(0,t.size):
        a = a + str(t[i])  
    
    output_data += ' '.join(map(str, a))
    return(output_data)


if __name__ == '__main__':
    import sys
    if len(sys.argv) > 1:
        
        file_location = sys.argv[1].strip()
        with open(file_location, 'r') as input_data_file:
            input_data = input_data_file.read()
        print(solve_it(input_data))
    else:
        print('This test requires an input file.  Please select one from the data directory. (i.e. python solver.py ./data/ks_4_0)')

