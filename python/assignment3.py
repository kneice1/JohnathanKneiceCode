# COMP 2800 - Spring 2024 - Assignment 3
# Name: Johnathan Kneice
#Date: Febuary 12, 2024

import numpy as np

import pandas as pd

#1. Given the following dictionary:
student_dict = {"name": ["Joe", "Nat", "Harry"],
"age": [30, 21, 29], "salary": [85.10, 77.80, 91.54]}
col =["Human Resources","Marketing","Development"]
#dictionary to dataframe
df = pd.DataFrame(student_dict,index=col)
df = df[['name', 'salary', 'age']]
print(df)
print("")
#2. Now create (from scratch) a Python list of dictionaries that can be used to produce the
#same DataFrame.
student_list = [{"name": "Joe","age":30,"salary":85.10},
               {"name": "Nat","age":21,"salary":77.80},
               {"name": "Harry","age":29,"salary":91.54}]
#list to dataframe
df = pd.DataFrame(student_list,index=col)
df = df[['name', 'salary', 'age']]
print(df)
print("")
#3. Now write code that will select from the above DataFrame just the rows that have a
#salary above 80.
print(df[df["salary"]>80])
print("")
#4. Write code that will export the DataFrame to a list of lists.
dfList = [df.columns.values.tolist()] + df.values.tolist()
print(dfList)
print("")
#5. Write code that will export the DataFrame to a dictionary
dfDict =df.to_dict()
print(dfDict)

#6. Finally, write code that will sort the DataFrame by name
nameSorted=df.sort_values("name")
print("")
print(df)
print("")
print(nameSorted)

