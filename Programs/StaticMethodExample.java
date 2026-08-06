class Calculation
{
    static int cube(int x)
    {
        return x*x*x;
    }
}
public class StaticMethodExample
{
    public static void main(String[]args)
    {
        int result=Calculation.cube(5);
        System.out.println("Cube="+result);
    }
}