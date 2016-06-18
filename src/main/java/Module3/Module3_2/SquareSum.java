package Module3.Module3_2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

interface SquareSum {

    long getSquareSum(int[] values, int numberOfThreads);

}

class SquareSumNThreads implements SquareSum {

    public static void main(String[] args) {
        int[] array = new int[100];
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(1000);
        }
        SquareSum squareSum = new SquareSumNThreads();
        System.out.println(squareSum.getSquareSum(array, 7));
    }

    @Override
    public long getSquareSum(int[] values, int numberOfThreads) {

        if (values.length < numberOfThreads) throw new
                IllegalArgumentException("Number of threads is: " + numberOfThreads + " should be less than length of [] values: " + values.length);

        List<Callable<Long>> callables = new ArrayList<>();
        Phaser phaser = new Phaser();
        IntStream.range(0, numberOfThreads).forEach(i -> callables.add(() -> {
            long result = 0;
            phaser.register();
            System.out.println(Thread.currentThread().getName() + " Executing phase " + phaser.getPhase());
            for (int j = i * (values.length / numberOfThreads); j < ((i == (numberOfThreads - 1)) ? values.length : (values.length / numberOfThreads) * (i + 1)); j++) {
                result += values[j] * values[j];
                System.out.println(" value[" + j + "]" + " = " + values[j]);
            }
            System.out.println(Thread.currentThread().getName() + " finish summing squares of arrays elements from: " + i * (values.length / numberOfThreads) +
                    " to: " + ((i == (numberOfThreads - 1)) ? values.length - 1 : (values.length / numberOfThreads) * (i + 1)) + " result = " + result);
            Thread.sleep(1000);
            phaser.arriveAndAwaitAdvance();
            phaser.arriveAndDeregister();
            return result;
        }));

        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<Long>> result = null;
        try {
            result = executorService.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long arraySquareSum = 0;
        for (Future f :
                result) {
            try {
                arraySquareSum += new Long((Long) f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
        return arraySquareSum;
    }
}