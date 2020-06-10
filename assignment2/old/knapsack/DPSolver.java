

import java.util.Arrays;

public class DPSolver {

	private String createSolution(int[][] mat, int[] weights) {
		int k = mat.length-1;
		int N = weights.length;
		int[] takes = new int[N];
		String value = "" + mat[k][N-1];
		for (int i= N-1;i >= 1 ; i--) {
			if (mat[k][i] == mat[k][i-1]) {
				// did not take item i
				takes[i] = 0;
			} else {
				takes[i] = 1;
				k = k - weights[i];
			}
		}
		// the first item i == 0
		if (mat[k][0] == 0) {
			takes[0] = 0;
		} else {
			takes[0] = 1;
		}
		String result = value + " 0" + "\n" +  Arrays.toString(takes).replace('[',' ').replace(']', ' ').replace(',',' ') + "\n";
		//System.out.println(result);
		return(result);
	}
	
	private String createSolution2(int[][] mat,byte[][] mat2, int[] weights) {
		int k = mat.length-1;
		int N = weights.length;
		int[] takes = new int[N];
		String value = "" + mat[k][0];
		
		for (int i= N-1;i >= 1 ; i--) {
			if (mat2[k][i] == 0) {
				// did not take item i
				takes[i] = 0;
			} else {
				// did take item i
				takes[i] = 1;
				k = k - weights[i];
			}
		}
		// the first item i == 0
		if (mat[k][0] == 0) {
			takes[0] = 0;
		} else {
			takes[0] = 1;
		}
		
		String result = value + " 0" + "\n" +  Arrays.toString(takes).replace('[',' ').replace(']', ' ').replace(',',' ') + "\n";
		
		//System.out.println(result);
		return(result);
	}
	
	
	/**
	 * Solve knapsack problem by Dynamic Programming
	 * @param K
	 * @param values
	 * @param weights
	 */
	public String solve(int K, int[] values, int[] weights ) {
		int[][] mat = new int[K+1][weights.length];
		for (int i = 0; i < values.length;i++) {
			//System.out.println();
			for (int k=0; k <= K; k++) {
				
				if (i==0) {
					// first item
					if (k < weights[i] ) {
						mat[k][i] = 0;
					} else {
						mat[k][i] = values[i];
					}
				} else {
					// not the first item
					if (k < weights[i] ) {
						mat[k][i] = mat[k][i-1];
					} else {
						mat[k][i] = Math.max(values[i]+mat[k-weights[i]][i-1],mat[k][i-1]);
					}
				}
				//System.out.print(mat[k][i] + " ");
			}
				
		}
		String result = createSolution(mat,weights);
		return(result);
		
	}
	
	public String solve2(int K, int[] values, int[] weights ) {
		byte[][] mat2 = new byte[K+1][weights.length];
		int[][] mat = new int[K+1][2];
		for (int i = 0; i < values.length;i++) {
			//System.out.println();
			for (int k=0; k <= K; k++) {
				
				if (i==0) {
					// first item
					if (k < weights[i] ) {
						mat[k][i] = 0;
						// we did not take item
						mat2[k][i] = 0;
					} else {
						mat[k][i] = values[i];
						// we did take item
						mat2[k][i] = 1;
					}
				} else {
					// not the first item
					if (k < weights[i] ) {
						mat[k][1] = mat[k][0];
						// we did not take item
						mat2[k][i] = 0;
					} else {
						mat[k][1] = Math.max(values[i]+mat[k-weights[i]][0],mat[k][0]);
						// we did take item
						mat2[k][i] = 1;
					}
				}
				
			}
			// shift mat to the left
			for (int k=0; k <= K; k++) {
				if (i>0) {
					mat[k][0] = mat[k][1];
					mat[k][1] = 0;
				}
			}
		}
		String result = createSolution2(mat,mat2,weights);
		return(result);
		
	}
	
	
	public static void main(String[] args) {
		DPSolver dp = new DPSolver();
		int[] weights = {2,3,4,5};
		int[] values = {16,19,23,28};
		 
		System.out.println(dp.solve2(7, values, weights));
		
	}
	
}
