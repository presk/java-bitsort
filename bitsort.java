import java.util.Random;


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
        int[] arr = new int [10];
        for(int i = 0; i < arr.length; i++)
        {
            arr[i] = rand.nextInt(100) - 50;
        }
        Bitsort b = new Bitsort(arr);
        int[] sArr = b.sorted(arr);
        //int[] ssarr = new Bitsort(arr).sorted(arr);
        for(int i = 0; i < arr.length; i++)
        {
            System.out.println(sArr[i] + " : " + b.bitArray[i]);
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
        return Integer.toBinaryString(Float.floatToIntBits(Float.floatToIntBits(f)));
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