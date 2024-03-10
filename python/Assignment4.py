# COMP 2800 - Spring 2024 - Assignment 1
# Name: Johnathan Kneice
#Date: Febuary 21, 2024

import numpy as np
import pandas as pd
import re
print("Qestion 2")
df= pd.read_csv("billboard.csv",encoding="latin_1")
display(df)

#Write a single line of code that computes how many null values are in each column.
#Then write code that drops all the columns that are completely null. Use that resulting
#DataFrame for the remainder of the exercises.
print("Qestion 3")
#replace all the empty spaces with NaN
df=df.replace('',np.nan)

#count NaN in df
nan_count = df.isnull().sum()
print(nan_count)
#drop all collumns that are all NaNs
df.dropna(how='all', axis=1, inplace=True)
display(df)


print("Qestion 4")
#Write code to compute which song(s) was/were on the hot 100 list for the most number
#of weeks.
#find the collumns with the least amount of NaNs
topSong=df.isnull().sum(axis=1).min()
#get the collumn of topSong
display(df[df.isnull().sum(axis=1) == topSong])

#Run the following code and observe the result:
#df.query('artist == "Houston, Whitney"')
#Then write another method of getting the same result using Boolean masking.
print("Qestion 5")
display(df.query('`artist.inverted` == "Houston, Whitney"'))

display(df[df['artist.inverted']=="Houston, Whitney"])

#Write code that shows just the artist, track, and datePeaked for any songs that contain
#the word love in their title (case-insensitive)
print("Qestion 6")
#look for "Love" in collumn "track"
df2=df[df['track'].str.contains("Love")]
#get collumns of artist, track and date of peak
df2[['artist.inverted',"track","date.peaked"]]

#Write code that identifies the average time on the hot 100 list of the songs in the
#dataset.
print("Qestion 7")
print("Average time of songs being on hot 100 is:")
#get the collumn of week 1 to 65 and count not NaNs of each row then divide by number of rows
print(df.loc[:,"x1st.week":"x65th.week"].notna().sum().sum()//len(df.axes[0]))