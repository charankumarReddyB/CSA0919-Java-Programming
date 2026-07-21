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
public class Main
{
    public static void main (String[] args)
    {
        Kumar k=new Kumar();
        int arr[]={10,20,30,40,50};
        k.sum(arr);
        k.largest(arr);
    }
}