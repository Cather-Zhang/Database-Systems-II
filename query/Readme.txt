Cather Zhang czhang10

Section I:

How to execute the project: 
- unzip the file
- in terminal, go to the directory of the src folder (the path should end with \src)
- run command "javac Main.java" to compile
- run command "java Main" to start
- use query commands for functionality, and "exit" to terminate the program
    - either "SELECT A.Col1, A.Col2, B.Col1, B.Col2 FROM A, B WHERE A.RandomV = B.RandomV"
    - or "SELECT count(*) FROM A, B WHERE A.RandomV > B.RandomV"

Note:
- datasets are already located in src folder under Project1.

Section II:
Program functions properly with correct outputs

Section III:
- The records in hash table were left unsorted. Therefore, I simply looped through the bucket to find the correct A-records. This design is less efficient than if we sort the records, but it works.
- Col1 and Col2 of A were identified as "Fi,Recj" and "Namej". And these two strings were kept in an arraylist. Therefore, the hash table values was an arraylist of arraylist.

