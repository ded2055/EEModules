package Module3.Module3_2.Examples;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Ded on 13.06.2016.
 */
public class Atomics {
    private static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        new Atomics().test();
    }

    private int increment() {
        return counter.incrementAndGet();
    }


    public void test() throws InterruptedException {
        List<Atomics.Aggregator> aggregators = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Atomics.Aggregator aggregator = new Atomics.Aggregator();
            aggregators.add(aggregator);
            new Thread(aggregator).start();
        }
        Thread.sleep(100);

        boolean isValid = true;
        Set<Integer> integerSet = new HashSet<>();
        for (Atomics.Aggregator aggregator : aggregators) {
            for (Integer anInt : aggregator.ints) {
                if (!integerSet.add(anInt)) {
                    System.out.println("Error duplicate found " + anInt);
                    isValid = false;
                }
            }

        }
        if (isValid) {
            System.out.println("No duplicates");
        }

    }

    public class Aggregator implements Runnable {

        private List<Integer> ints = new ArrayList<>();

        public void run() {
            for (int i = 0; i < 1000; i++) {
                ints.add(increment());
            }
        }

    }
}
