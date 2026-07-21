class Vehicle
{
    void run()
    {
        System.out.println("Vehicle is Running");
    }
}
class Bike extends Vehicle
{
    void run()
    {
        System.out.println("Bike is running safely");
        super.run();
    }
}
class Main
{
    public static void main(String[]args)
    {
        Bike b=new Bike();
        b.run();
    }
}