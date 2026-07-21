class Charan
{
    void sum(int a[])
    {
        int s=0;
        for(int i=0;i<a.length;i++)
        {
            s=s+a[i];
        }
        System.out.println("sum= "+s);
    }
}
class Kumar extends Charan
{
    void largest(int a[])
    {
        int max=a[0];
        for(int i=0;i<a.length;i++)
        {
            if(a[i]>max)
            max=a[i];
        }
        System.out.println("Largest= "+max);
    }
}
class Reddy extends Kumar
{
    void smallest(int a[])
    {
        int min=a[0];
        for(int i=1;i<a.length;i++)
        {
            if(a[i]<min)
            min=a[i];
        }
        System.out.println("Smallest= "+min);
    }
}
public class Main
{
    public static void main (String[] args)
    {
        Reddy r=new Reddy();
        int arr[]={10,20,30,40,50};
        r.sum(arr);
        r.largest(arr);
        r.smallest(arr);
    }
}