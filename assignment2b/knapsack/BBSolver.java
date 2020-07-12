
import java.util.Arrays;
import java.util.List;

/*
 * 
 * Branch and Bound Solver
 * 
 * 
 */
public class BBSolver {

	public static void printArray(int[] a) {
		System.out.print("[ ");
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + " ");
		}
		//System.out.println("]");
	}

	private int bestValue = 0;

	private int[] bestItems;

	private void setBestValue(int bestValue) {
		//System.out.println("Best value is "+ bestValue);
		this.bestValue = bestValue;
	}

	private void setBestItems(int[] bestIs) {
		// System.out.println("best items" + bestItems[1]+ " " + bestItems[2]);
		this.bestItems = bestIs.clone();
	}

	private int getBestValue() {
		return (this.bestValue);
	}

	private int[] getBestItems() {
		return (this.bestItems);
	}

	private void setBest(int value, int[] items) {
		// printArray(items);
		// System.out.println(value);
		if (value > this.getBestValue()) {
			setBestValue(value);
			setBestItems(items);

		}
	}

	private void initBest(int n) {
		setBestValue(0);
		int[] bestIt = new int[n];
		setBestItems(bestIt);
	}

	static int nodeCounter = 0;

	/*
	 * @param value : The total value until the current node
	 * 
	 * @param valuesToGo: all values of all nodes below this one
	 * 
	 * @param weightsToGo: Weights of all nodes below this one
	 * 
	 * @parm capacity: total size of the knapsack
	 * 
	 * Get an optimistic upper bound on the value we can have in the knapsack. This
	 * one simply assumes everything will fit in.
	 */
	int getOptimistic(int value, int capacityLeft, int[] valuesToGo, int[] weigthsToGo) {

		int sum = 0;
		for (int i : valuesToGo)
			sum += i;
		return (value + sum);

	}

	/*
	 * @param value : The total value until the current node
	 * 
	 * @param valuesToGo: all values of all nodes below this one
	 * 
	 * @param weightsToGo: Weights of all nodes below this one
	 * 
	 * @parm capacity: total size of the knapsack
	 * 
	 * @returns float: uppber bound on total value
	 * 
	 * Get an optimistic upper bound on the value we can have in the knapsack
	 * 
	 * As many of the next items as will fit as a whole, plus the fraction of the
	 * next 'Belgian chocolate model'. Includes the value already in the knapsack
	 * before you got to this item.
	 * 
	 */
	float getOptimisticLinear(int value, int capacityLeft, int[] valuesToGo, int[] weightsToGo) {

		// the total value if we add what we can into the knapsack
		float sum = (float) value;
		// weight we are adding now
		int weightAdded = 0;
		int i = 0;
		for (i = 0; i < weightsToGo.length; i++) {
			if (weightAdded + weightsToGo[i] <= capacityLeft) {
				weightAdded += weightsToGo[i];
				sum += valuesToGo[i];
			} else {
				if (i < weightsToGo.length) {
					// take a piece of the item, if there are any
					float prop = (float) (capacityLeft - weightAdded) / weightsToGo[i];
					sum += prop * valuesToGo[i];
					break;
				}
			}
		}
		return (sum);
	}

	/*
	public void goElement(int ix, int value, int k_used, int[] items, int K, int[] v, int[] w) {

		
		System.out.println("element count" + nodeCounter++);
		if (ix < items.length) {

			if (k_used + w[ix] <= K) {

				// if (getOptimistic(value, K, Arrays.copyOfRange(v, ix, v.length),
				// Arrays.copyOfRange(w, ix, w.length)) > getBestValue()) {
				float bestPossible = getOptimisticLinear(value, K - k_used, Arrays.copyOfRange(v, ix, v.length),
						Arrays.copyOfRange(w, ix, w.length));
				if (bestPossible > // it is possible to take it
						getBestValue()) {

					// There may be a point in taking this element
					// try taking it
					items[ix] = 1;
					goElement(ix + 1, value + v[ix], k_used + w[ix], items, K, v, w);
					// try not taking it
					items[ix] = 0;
					goElement(ix + 1, value, k_used, items, K, v, w);
				} else {
					// it is pointless to take this element
					items[ix] = 0;
				}
			} else {
				// try not taking it
				items[ix] = 0;
				goElement(ix + 1, value, k_used, items, K, v, w);
			}
		} else {
			// reached the end
			setBest(value, items);
		}

	}
*/
	
	
	public void goNode(int ix, int value, int k_used, int[] _items, int K, int[] v, int[] w, boolean leftNode) {
		//System.out.println(nodeCounter++ + " " + ix + leftNode);
		int[] items = _items.clone();
		if (ix < items.length) {
			if (leftNode) {
				if (k_used + w[ix] <= K) {
					// it is possible to take it
					float bestPossible = getOptimisticLinear(value, K - k_used, Arrays.copyOfRange(v, ix, v.length),
							Arrays.copyOfRange(w, ix, w.length));
					if (bestPossible > getBestValue()) {
						// There may be a point in continuing
						items[ix] = 1;
						goNode(ix + 1, value + v[ix], k_used + w[ix], items, K, v, w, true);
						goNode(ix + 1, value + v[ix], k_used + w[ix], items, K, v, w, false);
					}
				}

			} else {
				// a right node
				float bestPossible = getOptimisticLinear(value, K - k_used, Arrays.copyOfRange(v, ix+1, v.length),
						Arrays.copyOfRange(w, ix+1, w.length));
				if (bestPossible > getBestValue()) {
					// There may be a point in continuing
					items[ix] = 0;
					goNode(ix + 1, value, k_used, items, K, v, w, true);
					goNode(ix + 1, value, k_used, items, K, v, w, false);
				}
			}
		}
		// leaf node
		if (ix == items.length - 1) {
			setBest(value, items);
		}

	}

	public String createOutput(ArraySorter sorter) {

		// we need the sorter, so we can retrieve the
		// order of the elements, as it was before sorting by
		// value density.
		Integer[] ix = sorter.getIndexes();
		// this is the array of items to take (0,1) in original order
		Integer[] bestItemsOriginal = new Integer[ix.length];
		for (int i = 0; i < bestItems.length; i++) {
			bestItemsOriginal[ix[i]] = bestItems[i];
		}
		String line1 = getBestValue() + " 0\n";
		int[] bestIts = getBestItems();
		String line2 = "";
		for (int i = 0; i < bestIts.length; i++) {
			line2 = line2 + bestItemsOriginal[i] + " ";
		}
		return (line1 + line2);
	}

	/*
	 * public Float[] getValueDensity(int[] values,int[] weights) { Float[] result =
	 * new Float[values.length]; for (int i=0; i < values.length;i++) { float f = (
	 * (float) values[i] )/ ( (float) weights[i] ); result[i] = Float.valueOf( f );
	 * } return(result); }
	 * 
	 * public void sortMyArrays(Float[] valueDensity,int[] values, int[] weights) {
	 * ArrayIndexComparator comparator = new ArrayIndexComparator(valueDensity);
	 * Integer[] indexes = comparator.createIndexArray(); // indexes is now ordered
	 * in ascending valueDensity Arrays.sort(indexes, comparator); int[]
	 * valuesSorted = new int[values.length]; int[] weightsSorted = new
	 * int[weights.length]; for (int i=0;i<indexes.length;i++) { valuesSorted[i] =
	 * values[indexes[i]]; weightsSorted[i] = weights[indexes[i]]; }
	 * 
	 * }
	 */

	public String solve(int K, int[] values, int[] weights) {

		initBest(values.length);
		ArraySorter sorter = new ArraySorter(values, weights);
		sorter.sortByValueDensity();
		int[] valuesSorted = sorter.getSortedValues();
		int[] weightsSorted = sorter.getSortedWeights();

		int[] itemsTaken = new int[valuesSorted.length];
		for (int i = 0; i < valuesSorted.length; i++) {
			itemsTaken[i] = 0;
		}
		// left half of tree
		goNode(0, 0, 0, itemsTaken, K, valuesSorted, weightsSorted, true);
		// right half of tree
		goNode(0, 0, 0, itemsTaken, K, valuesSorted, weightsSorted, false);

		return (this.createOutput(sorter));

	}

	public static void main(String[] args) {
		BBSolver bb = new BBSolver();
		int[] weights = { 2, 2, 2, 5, 5, 8, 3 };
		int[] values = { 1, 1, 1, 10, 10, 13, 7 };
		int K = 10;
		String output = bb.solve(K, values, weights);
		System.out.println(output);

	}

}

