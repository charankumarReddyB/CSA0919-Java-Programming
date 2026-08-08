import java.util.*;
class HashMapExample
{
    public static void main(String[] args) 
    {
        HashMap<Integer, String> students = new HashMap<>();
        students.put(101, "Arun");
        students.put(102, "Bala");
        students.put(103, "Chitra");
        System.out.println(students);
        System.out.println(students.get(102));
    }
}