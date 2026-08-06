class Student
{
    int rollno;
    String name;
    static String college="Saveetha University";
    Student(int r,String n)
    {
        rollno=r;
        name=n;
    }
    void display()
    {
        System.out.println(rollno+" "+name+" "+college);
    }
}
public class StaticVariableExample
{
    public static void main(String[]args)
    {
        Student s1=new Student(101,"Arun");
        Student s2=new Student(102,"Bala");
        s1.display();
        s2.display();
    }
}