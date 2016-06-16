package Module3.Module3_2.Examples;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.stream.IntStream;

/**
 * Created by Ded on 13.06.2016.
 */
public class Synchronizers {

    public static void main(String[] args) {
        Synchronizers synchronizer = new Synchronizers();
        synchronizer.testExchanger();
    }

    public void testCyclicBarier() throws InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> System.out.println("Barrier exceeded"));
        while (true) {
            new Thread(() -> {
                try {
                    String threadName = Thread.currentThread().getName();
                    System.out.println(threadName + " starts waiting on barrier");
                    cyclicBarrier.await();
                    System.out.println(threadName + " ends waiting");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
            Thread.sleep(1000);
        }
    }

    public void testExchanger() {
        Exchanger<String> exchanger = new Exchanger<>();
        IntStream.range(0, 2).forEach(i -> new Thread(() -> {
                    try {
                        System.out.println();
                        Thread.sleep(new Random().nextInt(1000));
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + "ready to exchange");
                        System.out.println(threadName + " < - > " + exchanger.exchange(threadName));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start()
        );
    }
}
