Cather Zhang czhang10

Section I:

How to execute the project: 
- unzip the file
- in terminal, go to the directory of the src folder (the path should end with \src)
- run command "javac Main.java" to compile
- run command "java Main [buffer number]" to start
- use GET/SET/PIN/UNPIN for functionality, and EXIT to terminate the program

Note:
- all the files are already located in src folder under Project1.


Section II:
All tests given passed

Section III:
- There is a lastEvicted (integer) in the BufferPool class to keep track of the most recent evicted frame index
- There is a oldID (integer) in the BufferPool class to save the block ID of a frame (only when the block is about to be evicted)
- I have a File IO helper function in BufferPool class to handle writing to the disk when the dirty flag is true
- The readBlock function in BufferPool is the most important helper function that I have developed outside the guideline, which helps identify case 2 (there is empty buffers) and case 3 (when all buffer frames are full)
Within case 3, it also decides which block to be evicted using the last-evicted policy, as well as write by to disk if the dirty flag is true