class Charan
{
	void factorial(int n)
	{
		int fact=1;
		for(int i=1; i<=n; i++)
		{
			fact=fact*i;
		}
		System.out.println("Factorial="+fact);
	}
}
class Reddy extends Charan
{
	void perfect(int n)
    {
        int sum=0;
        for(int i=1;i<n;i++)
        {
            if(n%i==0){
                sum=sum+i;
            }
        }
        if(sum==n)
        System.out.println(n+" is a perfect number");
        else
        System.out.println(n+" is not a perfetc nnumber");
    }
}
public class Main
{
	public static void main(String[] args)
	{
		Reddy r=new Reddy();
		r.factorial(5);
		r.perfect(6);
	}
}