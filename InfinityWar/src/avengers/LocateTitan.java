package avengers;
/**
 * 
 * Using the Adjacency Matrix of n vertices and starting from Earth (vertex 0), 
 * modify the edge weights using the functionality values of the vertices that each edge 
 * connects, and then determine the minimum cost to reach Titan (vertex n-1) from Earth (vertex 0).
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * LocateTitanInputFile name is passed through the command line as args[0]
 * Read from LocateTitanInputFile with the format:
 *    1. g (int): number of generators (vertices in the graph)
 *    2. g lines, each with 2 values, (int) generator number, (double) funcionality value
 *    3. g lines, each with g (int) edge values, referring to the energy cost to travel from 
 *       one generator to another 
 * Create an adjacency matrix for g generators.
 * 
 * Populate the adjacency matrix with edge values (the energy cost to travel from one 
 * generator to another).
 * 
 * Step 2:
 * Update the adjacency matrix to change EVERY edge weight (energy cost) by DIVIDING it 
 * by the functionality of BOTH vertices (generators) that the edge points to. Then, 
 * typecast this number to an integer (this is done to avoid precision errors). The result 
 * is an adjacency matrix representing the TOTAL COSTS to travel from one generator to another.
 * 
 * Step 3:
 * LocateTitanOutputFile name is passed through the command line as args[1]
 * Use Dijkstra’s Algorithm to find the path of minimum cost between Earth and Titan. 
 * Output this number into your output file!
 * 
 * Note: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut (here, minCost represents the minimum cost to 
 *   travel from Earth to Titan):
 *     StdOut.setFile(outputfilename);
 *     StdOut.print(minCost);
 *  
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/LocateTitan locatetitan.in locatetitan.out
 * 
 * @author Yashas Ravi
 * 
 */

public class LocateTitan {
	
    public static void main (String [] args) {
    	
        if ( args.length < 2 ) {
            StdOut.println("Execute: java LocateTitan <INput file> <OUTput file>");
            return;
        }

        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

    	int generators = StdIn.readInt();
        double[][] funcArray = new double[generators][2];
        for(int i = 0; i < generators; i++){
            // for(int j = 0; j < 2; j++){
                funcArray[i][0] = StdIn.readInt();
                funcArray[i][1] = StdIn.readDouble();
            }
        // }
        
        // for(int i =0;i<funcArray.length;i++){
        //     for(int j=0;j<funcArray[i].length;j++){
        //         System.out.println(funcArray[i][j]);
        //     }
        // }

        int[][] adjMatrix = new int[generators][generators];
        for(int i = 0; i < generators; i++){
            for(int j = 0; j < generators; j++){
                adjMatrix[i][j] = StdIn.readInt();
            }
        }
        // updating adj matrix
        for(int i = 0; i < generators; i++){
            for(int j = 0; j < generators; j++){
                int weight = adjMatrix[i][j];
                // if(adjMatrix[i][j] != 0){
                    double firstVal = funcArray[i][1];
                    double secVal = funcArray[j][1];
                    // System.out.println("firstVal is" + firstVal + "secondVal is" + secVal);
                    double newVal = (firstVal * secVal);
                    // System.out.println("new value is" + "" + newVal);
                    if(newVal!=0){
                    adjMatrix[i][j] = (int)(weight / newVal);
                    // System.out.println(adjMatrix[i][j]);
                    // }
                }
            }
        }

        int[] minCost = dijikstra(adjMatrix);
        StdOut.print(minCost[generators - 1]);
    }
        //dijistrka's alogrithm
        private static int[] dijikstra(int[][] array){
            int[] minCost = new int[array.length];
            boolean[] dijikstraSet = new boolean[array.length];
            for(int i = 1; i < minCost.length; i++){
                minCost[0] = 0;
                minCost[i] = Integer.MAX_VALUE;
            }
            for(int i = 0; i < minCost.length; i++){
                int currentSource = getMinCostNode(minCost, dijikstraSet);
                dijikstraSet[currentSource] = true;
                //update he distance from 0 to each currensource neighbors if it can be lwoered
                for(int j = 0; j < array.length; j++){
                    if(array[currentSource][j] != 0 && !dijikstraSet[j] && minCost[currentSource] + array[currentSource][j] < minCost[j]){
                        minCost[j] = minCost[currentSource] + array[currentSource][j];
                    }
                }
            } 
            return minCost;
        }

        private static int getMinCostNode(int[] mCost,boolean[] dijSet ){
            int min = Integer.MAX_VALUE;
            int minNode = -1;
            for(int i = 0; i < mCost.length; i++){
                if(!dijSet[i] && mCost[i] < min){
                    min = mCost[i]; //update
                    minNode = i;
                }
            }
            return minNode;
        } 
}
