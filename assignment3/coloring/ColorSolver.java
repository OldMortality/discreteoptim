
import java.util.Arrays;

public class ColorSolver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	// returns the number of connections for a given node
	int howManyConnections(int node,int edges,int[] from, int[] to) {
		int result = 0;
		int[] neighbours = getNeighbours(node, edges, from, to);
		if (neighbours != null) {
			result = neighbours.length;
		} 
		return(result);
	}
	
	
	int[] sortByNumberOfConnections(int nodes, int edges, int[] from, int[] to) {
		int[] result = new int[nodes];
		
		Integer[] conns = new Integer[nodes]; 
		for (int node=0;node<nodes;node++) {
			conns[node] = new Integer(howManyConnections(node, edges, from, to));
		}
		ArrayIndexComparator comparator = new ArrayIndexComparator(conns);
		Integer[] indexes = comparator.createIndexArray();
		Arrays.sort(indexes, comparator);
		//System.out.println(Arrays.toString(indexes));
		for (int i=0;i<nodes;i++) {
			result[i] = indexes[i].intValue();
		}
		return(result);
	}
	
	
	
	
	String getPrintable(int topColor, int[] nodeCols) {
		String result = topColor + 1 +  " 0\n";
		String result2 = "";
		for (int i=0;i<nodeCols.length;i++) {
			result2 = result2 + nodeCols[i] + " ";
		}
		return(result + result2);
	}
	
	
	int[] getNeighbours(int node, int edges, int[] from, int[] to) {
		int[] result1 = new int[edges];
		int j = 0;
		for (int i=0;i < edges; i++) {
			if (from[i]==node)  {
				result1[j] = to[i];
				j++;
			} else {
				if (to[i]==node) {
					result1[j] = from[i];
					j++;
				}
			}
		}
		int[] result = null;
		if (j>0) {
			result = new int[j];
			for (int i=0;i<j;i++) {
				result[i] = result1[i];
			}
		}
		return(result);
		
	}
	
	// returns yes if any of the nodes are using the given color
	boolean areUsingColor(int[] nodes, int col, int[] nodeCols) {
		if (nodes == null) {
			return(false);
		}
		boolean result = false;
		for (int i=0;i<nodes.length;i++) {
			if (nodeCols[nodes[i]]==col) {
				result = true;
				break;
			}
		}
		return(result);
	}
	
	public class valueCols {
		int value;
		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public int[] getNodeCols() {
			return nodeCols;
		}

		public void setNodeCols(int[] nodeCols) {
			this.nodeCols = nodeCols;
		}

		int[] nodeCols;
		
		public valueCols(int value, int[] nodeCols) {
			this.value = value;
			this.nodeCols = nodeCols;
		}
	}
	
	
	public valueCols solveR(boolean randomise, int nodes , int edges, int[] from, int[] to) {
		
		// colors used by all the nodes
		int[] nodeCols = new int[nodes];
		for (int i=0;i < nodes;i++) {
			nodeCols[i] = -1;
		}
		
		
		int[] sortedNodes = sortByNumberOfConnections(nodes, edges, from, to);
		// go along all the nodes, in order of highest connectivity
		
		// highest number of colors used
		int topColor = 0;
		nodeCols[sortedNodes[0]] = 0;
				
		for (int ix=1;ix  < nodes;ix++) {
			
			if (randomise && (ix < nodes -1)) {
				
				if ( Math.random() > 0.6 ) {
				 int tmp  = sortedNodes[ix];
				 sortedNodes[ix] = sortedNodes[ix+1];
				 sortedNodes[ix + 1] = tmp;
				}
			}
			

			int node = sortedNodes[ix];
			
			if (nodeCols[node] == -1) {
				// the node does not yet have a color
				
				// Get the nodes connections. This could be null
				int[] neighbours = getNeighbours(node,edges,from,to);
				// try to find a color not used by any connection
				boolean found = false;
				for (int col = 0; col <= topColor ; col++) {
					if (!areUsingColor(neighbours,col,nodeCols)) {
						nodeCols[node] = col;
						found = true;
						break;
					}
				}
				if (!found) {
					nodeCols[node] = topColor + 1;
					topColor = topColor + 1;
				}
			}
		}
		
		
		valueCols result = new valueCols(topColor,nodeCols);
		return(result);
		
		
		
		
		 
	}
	
	public String solve(int nodes , int edges, int[] from, int[] to) {
	
		int lowestValue = nodes + 10;
		int[] bestCols = null;
		boolean randomise = false;
		for (int i=0;i<100;i++) {
			valueCols v = solveR(randomise, nodes , edges, from,  to);
			randomise = true;
			if ( v.getValue() < lowestValue ) {
				lowestValue = v.getValue();
				bestCols = v.getNodeCols();
			}
		
		}
		String result = getPrintable(lowestValue,bestCols);
		return(result);
	}
}

