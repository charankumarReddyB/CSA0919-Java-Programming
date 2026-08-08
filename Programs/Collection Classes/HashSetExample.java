import java.util.*;
class HashSetExample 
{
    public static void main(String[] args) 
    {
        HashSet<Integer> numbers = new HashSet<>();
        numbers.add(10);
        numbers.add(20);
        numbers.add(10);
        numbers.add(30);
        System.out.println(numbers);
    }
}