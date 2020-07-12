//package week3;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * The class <code>Solver</code> is an implementation of a greedy algorithm to solve the knapsack problem.
 *
 */
public class Solver {
    
    /**
     * The main class
     */
    public static void main(String[] args) {
        try {
            solve(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Read the instance, solve it, and print the solution in the standard output
     */
    public static void solve(String[] args) throws IOException {
        String fileName = null;
        
        // get the temp file name
        for(String arg : args){
            if(arg.startsWith("-file=")){
                fileName = arg.substring(6);
            } 
        }
        if(fileName == null)
            return;
        //System.out.println(fileName);
        
        // read the lines out of the file
        List<String> lines = new ArrayList<String>();

        BufferedReader input =  new BufferedReader(new FileReader(fileName));
        try {
            String line = null;
            while (( line = input.readLine()) != null){
                lines.add(line);
            }
        }
        finally {
            input.close();
        }
        
        
        // parse the data in the file
        String[] firstLine = lines.get(0).split("\\s+");

        int nodes = Integer.parseInt(firstLine[0]);
        int edges = Integer.parseInt(firstLine[1]);
        int[] from = new int[edges];
        int[] to = new int[edges];



        for(int i=1; i < edges+1; i++){
          String line = lines.get(i);
          String[] parts = line.split("\\s+");
          Integer val = Integer.parseInt(parts[0]); 
          from[i-1] = val.intValue();
          val = Integer.parseInt(parts[1]); 
          to[i-1] = val.intValue();
        }
        
        
        ColorSolver colorSolver = new ColorSolver();
        String result = colorSolver.solve(nodes, edges, from, to);
        System.out.println(result);

 
    }
}
