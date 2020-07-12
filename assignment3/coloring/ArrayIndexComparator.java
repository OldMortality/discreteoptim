 

import java.util.Arrays;
import java.util.Comparator;

public class ArrayIndexComparator implements Comparator<Integer>
{
    private final Integer[] array;

    public ArrayIndexComparator(Integer[] array)
    {
        this.array = array;
    }

    public Integer[] createIndexArray()
    {
        Integer[] indexes = new Integer[array.length];
        for (int i = 0; i < array.length; i++)
        {
            indexes[i] = i; // Autoboxing
        }
        return indexes;
    }

    @Override
    public int compare(Integer index1, Integer index2)
    {
         // Autounbox from Integer to int to use as array indexes
        return ( -1 * (array[index1].compareTo(array[index2]) ));
    }



public static void main(String[] args) {
	Integer[] fs = { new Integer(13), new Integer(12), new Integer(24) };
	ArrayIndexComparator comparator = new ArrayIndexComparator(fs);
	Integer[] indexes = comparator.createIndexArray();
	Arrays.sort(indexes, comparator);
	System.out.println(Arrays.toString(indexes));
	
}


}
