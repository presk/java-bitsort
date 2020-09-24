import java.util.Random;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


class Bitsort
 {
    public String[] bitArray;
    private int[] intArray;
    private float[] floatArray;
    private String type;



    public Bitsort(){}

    public Bitsort(float[] arr)
    {
        this.type = "float";
        this.floatArray = arr.clone();
        this.bitArray = new String [arr.length];
        for(int i =0; i < arr.length ; i++)
        {
            this.bitArray[i] = this.numToBit(arr[i]);
        }
        this.sort();
    }
    public Bitsort(int[] arr)
    {
        this.type = "int";
        this.intArray = arr.clone();
        this.bitArray = new String [arr.length];
        int max = 2;
        for(int i =0; i < arr.length ; i++)
        {
            if(arr[i] != 0)
            {
                //Looks for the minimum amount of bits to represent all integers in the array
                int temp = 32 - Integer.numberOfLeadingZeros(Math.abs(arr[i]));
                if( temp > max)
                {
                    max = temp;
                }
            }
            
        }
        for(int i =0; i < arr.length ; i++)
        {
            this.bitArray[i] = this.numToBit(arr[i], max);
        }
        this.sort();
    }
    public static void main(String[] args) 
    {
        Random rand = new Random();

        int[] intArr = new int [1000000];
        for(int i = 0; i < intArr.length; i++)
        {
            intArr[i] = rand.nextInt(1000000) - 500000;
        }
        
        float[] floatArr = new float [1000000];
        for(int i = 0; i < floatArr.length; i++)
        {
            floatArr[i] = (rand.nextFloat() - 0.5f) * (10000000000f);
        }
        

        long startTime = System.nanoTime();
        int[] sArr = new Bitsort(intArr).sorted(intArr);

        long timeElapsed1 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        System.out.println("Time elapsed bitsort: " + timeElapsed1 + " ms");


        startTime = System.nanoTime();
        Arrays.sort(intArr);

        long timeElapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        System.out.println("Time elapsed java sort: " + timeElapsed2 + " ms");
        System.out.println("Delta bitsort vs sort: " + (timeElapsed1 - timeElapsed2) + " ms");
        System.out.println("Ratio bitsort vs sort: ~" + (timeElapsed1 / timeElapsed2) + "x slower");
        if(Arrays.equals(sArr, intArr))
        {
            System.out.println("true");
        }

        startTime = System.nanoTime();
        float[] sfArr = new Bitsort(floatArr).sorted(floatArr);


        timeElapsed1 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        System.out.println("Time elapsed bitsort: " + timeElapsed1 + " ms");


        startTime = System.nanoTime();
        Arrays.sort(floatArr);


        timeElapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        System.out.println("Time elapsed java sort: " + timeElapsed2 + " ms");
        System.out.println("Delta bitsort vs sort: " + (timeElapsed1 - timeElapsed2) + " ms");
        System.out.println("Ratio bitsort vs sort: ~" + (timeElapsed1 / timeElapsed2) + "x slower");
        if(Arrays.equals(sfArr, floatArr))
        {
            System.out.println("Arrays are equals: true");
        }
    }
    
    public int[] sorted(int [] arr)
    {
        return this.intArray;
    }
    public float[] sorted(float [] arr)
    {
        return this.floatArray;
    }
    /*Returns the bit representation of a float. (IEEE-754)
    */
    private String numToBit(float f)
    {

        String fString = Integer.toBinaryString(Float.floatToIntBits(f));

        /*floatToIntBits returns 31 bits for positives and 32 bits for negatives. 
        * Positives are then padded with a 0 to the left to represent the sign bit.
        */
        if (f >= 0.0f)
        {
            fString = "0" + fString;
        }
        return fString;
    }
    /*Returns the bit representation of the absolute value of an integer with a 
    * sign bit on the left (0->+, 1->-). It also pads integers to have representations
    *  of equal size. 
    */
    private String numToBit(int i, int max)
    {
        if( i >= 0)
        {
            String bitString = Integer.toBinaryString(i);
            String pad = "";
            if(max != bitString.length())
            {
                pad = String.format("%0" + (max - bitString.length()) + "d",0);
            }
            return "0" + pad + bitString;
        }
        else
        {
            String bitString = Integer.toBinaryString(-1*i);
            String pad = "";
            if(max != bitString.length())
            {
                pad = String.format("%0" + (max - bitString.length()) + "d",0);
            }
            return "1" + pad + bitString;
        }
    }
    /*Splits the array into positive and negatives,
    * then recursively sorts the sub-arrays.
    * Sub-arrays of negatives are sorted in descending order,
    * while sub-arrays of positives are sorted in ascending order.
    * Ascending: 1's to the left
    * Descending: 0's to the left
    */
    private void sort()
    {
        int swapPos = 0;
        for(int i = 0; i < bitArray.length; i++)
        {
            if(bitArray[i].charAt(0) == '1')
            {
                swap(i, swapPos);
                swapPos ++;
            }
        }
        innerSort(0, swapPos - 1, 1, '1');
        innerSort(swapPos, this.bitArray.length - 1, 1, '0');
    }
    /*Sorts sub-arrays by splitting 1's and 0's and placing
    * the order type bits to the left (for example: 111000 or 000111). 
    */
    private void innerSort(int start, int end, int bit, char order)
    {
        int swapPos = start;
        for(int i = start; i <= end; i++)
        {
            if(bitArray[i].charAt(bit) == order)
            {
                if(i != swapPos)
                    swap(i, swapPos);
                swapPos ++;
            }
        }
        /*Checks if the sub-array had identical bits different from order
        * Then changes the swap position to the end of the sub-array*/
        if(swapPos == start)
        {
            swapPos = end + 1;
        }
        /*Checks if we've reached the last bit, 
        *then checks if the sub-array's size is bigger than 1*/
        if(bit < bitArray[0].length() - 1)
        {
            if(start < swapPos - 1)
            {
                innerSort(start, swapPos - 1, bit + 1, order);
            }
            if(swapPos < end)
            {
                innerSort(swapPos, end, bit + 1, order);
            }
        }
    }
    /*Swaps numbers in both their bit representation array 
    * and the copy of the original array */
    private void swap(int i, int swapPos)
    {
        if(type.equals("int"))
        {
            int temp = this.intArray[swapPos];
            this.intArray[swapPos] = this.intArray[i];
            this.intArray[i] = temp;
        }
        else if(type.equals("float"))
        {
            float temp = this.floatArray[swapPos];
            this.floatArray[swapPos] = this.floatArray[i];
            this.floatArray[i] = temp;
        }
        String temp = this.bitArray[swapPos];
        this.bitArray[swapPos] = this.bitArray[i];
        this.bitArray[i] = temp;
    }
}