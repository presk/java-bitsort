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
        for(int i = 0; i < arr.length; i++)
        {
            System.out.println(arr[i] + " : " + b.bitArray[i]);
        }
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
}