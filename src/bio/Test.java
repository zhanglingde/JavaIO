package bio;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author zhangling  2021/5/12 20:00
 */
public class Test {
    public static void main(String[] args) {
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(3);
        queue.add(3);
        queue.add(1);
        queue.add(2);
        System.out.println("queue.size() = " + queue.size());
        queue.add(4);
        queue.add(5);

        System.out.println("queue.size() = " + queue.size());
    }
}
