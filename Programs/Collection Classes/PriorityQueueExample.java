import java.util.*;
class PriorityQueueExample
{
    public static void main(String[] args) 
    {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.add(30);
        queue.add(10);
        queue.add(20);
        System.out.println(queue.poll());
        System.out.println(queue.poll());
    }
}