# COMP 2800 - Spring 2024 - Assignment 1
# Name: Johnathan Kneice
#Date: January 19, 2024
# Complete all of the following functions. Currently they all just
# 'pass' rather than explicitly return a value, which means that they
# implicitly return None. They all include doctests, which you can
# test by running this file as a script: python3 assign1.py

# Feel free to add your own doctests.


def sumFive(lst):
    
    """
    Sum the first 5 elements of the list given. If the length
    of the list is less than 5, just sum as many elements as
    it has. If the length of the list is 0, the sum should be 0.

    >>> sumFive([2,4,6,8,10,12,14,16,18,20])
    30
    >>> sumFive([5,12])
    17
    >>> sumFive([])
    0

    """
    pass
    sum = 0
    # if the length of the list is less than 5
    if len(lst) < 5 and list != 0:
        # loop though the list and add up the sum
        for i in lst:
            sum += i
    else:
        #add up the first 5 integers
        for i in range(5):
            sum += lst[i]
    return sum

def middle(data, num):
   


    """
    Returns a list of length num comprised of the 'middle'
    elements of data. If num is greater or equal to the
    length of data, all of data should be returned.

    >>> middle([1,2,3,4,5,6], 2)
    [3, 4]
    >>> middle([1,2,3,4,5,6], 0)
    []
    >>> middle([1,2,3], 4)
    [1, 2, 3]
    >>> middle([1,2,3,4,5,6], 3)
    [3, 4, 5]
   
    """
    pass
    middle =[]
    # if num is 0 return an empty list
    if num == 0:
        return middle
    #if the length data is less than num add all elements to middle
    elif len(data) <= num:
        for i in data:
            middle.append(i)
    else:
        # find the midPoint and the starting location
        midPoint = len(data)//2
        start = midPoint-(num//2)
        #loop from start to start plus one
        for i in range(start, start+num):
            #add elements to middle
            middle.append(data[i])
    return middle


def invertDict(d):
    

    """
    Returns a new dictionary in which the keys and values
    are inverted. If d contains any values that appear for
    more than one key, then only the first occurance (in the
    normal iteration order) is added to the new dictionary

    >>> invertDict({})
    {}
    >>> invertDict({'a': 1, 'b': 2, 'c': 3})
    {1: 'a', 2: 'b', 3: 'c'}
    >>> invertDict({'a': 'y', 'b': 'z', 'c': 'y'})
    {'y': 'a', 'z': 'b'}
    """
    pass
    dictionary = {}
    #loop through d
    for i in d:
        # take the value of the given dictionary cell and look if it is a key
        if d[i] not in dictionary:
            #add the value as a key and the key as a value
            dictionary.update({d[i]:i})
    return dictionary


def secondLargest(values):
    
    """
    Given a list of integers, return the number that is the
    second largest in the list

    >>> secondLargest([24, 12, 7, 41, 23, 8, 34, 18])
    34
    >>> secondLargest([])

    
    """
    pass
# if value has a length larger than or equal to two
    if len(values)>=2:
        # take the first 2 numers and figure out which is the largest
        largest = values[0]
        if largest> values[1]:
            second = values[1]
        else:
            second = largest
            largest = values[1]
        # loop through finding the largest
        for i in values:
            #figure out if i is larger than largest
            if i > largest:
                # set second largest to largest
                second = largest
                #set largest to i
                largest = i
            #see if i is the second largest
            elif i< largest and i> second:
                second= i
        return second

#print(middle([1,2,3,4,5,6], 3))
if __name__ == '__main__':
    import doctest
    doctest.testmod()