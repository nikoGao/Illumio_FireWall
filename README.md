Illumio-Coding-Challenge
Illumio Coding Assignment 2018, PCE teams

It took me 90 minutes to finish this challege. The process of dealling with such a real world problem was interesting.

I create two classes: FireWall and Rule.

To avoid reading file multiple times while comparing accept packet, I store all inputs in csv as Rule class in a List.

When first initialize the FireWall class, it will take each line of csv and store it as a Rule.

When calling accept_packet function, my program will compare all input parameters with Rules store in list.

Test:

Due to time limitation, I only test on the example csv and input. It took so long to really build a csv with thousands of rules.

Performance:
The first big time consumption is storing each input, the actual time used is up to O(N*n) where N is the number of line in the file, and the n is the time used by String.split() function.

The second time consumption is the accept_packet method, because this method will go through each Rule in list and compare it with our input parameters. In the worst case, the time complexity will be around O(N*n) if the last Rule is what we need.

Improvement:
After finishing the basic implementation, I came up a solution to improve the performance of accept_packet. When reading csv file, I can improve my Rule class. For now, I simply store all data inforamtion in a new Object then in a list. If I have more time, I may create a HashKey for each rule, and store them basing on their key in a HashMap. Then calling accept_packet will be O(1) time as the program only need to check whether the key exist in the HashMap. 


Team Ranks:
1. Platform Team
2. Policy Team
3. Data Team
But I think all these teams are cool.

