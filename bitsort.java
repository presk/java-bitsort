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
        /*int[] arr = new int [1000000];
        for(int i = 0; i < arr.length; i++)
        {
            arr[i] = rand.nextInt(10000) - 5000;
        }*/
        float[] arr = new float [1000000];
        for(int i = 0; i < arr.length; i++)
        {
            arr[i] = (rand.nextFloat() - 0.5f) * (10000000000f);
        }
        Bitsort b = new Bitsort(arr);
        long startTime = System.nanoTime();
        float[] sArr = b.sorted(arr);
        long endTime = System.nanoTime();
        long timeElapsed1 = endTime - startTime;
        System.out.println("Time elapsed bitsort: " + timeElapsed1);


        startTime = System.nanoTime();
        Arrays.sort(arr);
        endTime = System.nanoTime();
        long timeElapsed2 = endTime - startTime;
        System.out.println("Time elapsed java sort: " + timeElapsed2);


        //int[] ssarr = new Bitsort(arr).sorted(arr);
        /*for(int i = 0; i < sArr.length; i++)
        {
            System.out.println(sArr[i] + "      " + arr[i]+ " : " + b.bitArray[i]);
        }*/
        if(Arrays.equals(sArr, arr))
        {
            System.out.println("true");
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
    private String numToBit(float f)
    {

        String fString = Integer.toBinaryString(Float.floatToIntBits(f));
        /*if (f < 0.0f)
        {
            System.out.println("Is < 0, bitsize: " + temp.length());
        }*/
        //floatToIntBits seems to return a 31 bit string for positives
        // and 32 bit string for negatives
        if (f >= 0.0f)
        {
            fString = "0" + fString;
        }
        return fString;
    }
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
        if(swapPos == start)
        {
            swapPos = end + 1;
        }
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