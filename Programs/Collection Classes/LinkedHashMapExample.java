import java.util.*;
class LinkedHashMapExample
{
    public static void main(String[] args)
    {
        LinkedHashMap<Integer, String> students = new LinkedHashMap<>();
        students.put(101, "Arun");
        students.put(102, "Bala");
        students.put(103, "Chitra");
        System.out.println(students);
    }
}