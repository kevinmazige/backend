# Quickbase Interview Solution

## Approach Taken
After looking at the problem statement, I wrote a sql query that did all the work of aggregating population data by 
country. 

I then used this query in a method called getPopulationsByCountry() which returns data of the same type as 
the "api" call GetCountryPopulations(). 

The aggregatePopData() function uses a hashtable as a set to meet the requirement that the final 
list not contain any duplicates (and give preference to data from the database if there were).

I solved the 'duplication' and 'preference' problem by first adding all the data from the database into 
the hashtable, and then adding the data from the "api" to the same hashtable - only if that key was NOT already present.

## Runtime
Since insertion of an element into a hashtable is O(1) and the first list has n elements and the second list has m 
elements, the runtime for inserting all elements from both lists is O(n + m). Creating a list from the hashtable entries
also runs in O(n + m) time, so the total runtime of the algorithm is linear. However, this approach uses additional 
space for the hashtable.

## Another Approach Considered
Another approach I could have used, once I had the two lists of population data, was to sort each individual list 
alphabetically and then combine the two. The best sorting algorithms, that would have applied, run in O(n log n) and so
the total runtime would have been (n log n) + (m log m) + n + m, which reduces to O(n log n) and this is worse than 
linear time. Of course, the benefit of this approach is that no space is needed for the hashtable.

## Somethings to Consider
A manual look at the data revealed that the United States was present in both data sources under different names.
In the database it was U.S.A. and in the "api" it was United States of America. I accounted for this difference in my
aggregatePopData() function in Main.java. The only problem is that if this wasn't caught, or if it wasn't clear that the
same country had two names, the final list would contain duplicate data. 

I made the getConnection() method in the Database manager private and had my query method responsible for opening  and
closing the connection. I felt this would make it less likely I'd forget to close a resource (the try with resources 
clause in java automatically closes any open connections). 




