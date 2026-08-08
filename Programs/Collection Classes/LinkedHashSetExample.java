import java.util.*;
class LinkedHashSetExample
{
    public static void main(String[] args) 
    {
        LinkedHashSet<String> names = new LinkedHashSet<>();
        names.add("Arun");
        names.add("Bala");
        names.add("Chitra");
        names.add("Arun");
        System.out.println(names);
    }
}