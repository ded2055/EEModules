package Module3.Module3_2.Examples;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ExecutorsExamples {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorsExamples executorsExamples = new ExecutorsExamples();
        executorsExamples.test();
    }

    public void test() {
        Exchanger<String> exchanger = new Exchanger<>();
        Random random = new Random();
        IntStream.range(0, 2).forEach((i) -> new Thread(() -> {
            try {
                Thread.sleep(random.nextInt(1000));
                String name = Thread.currentThread().getName();
                System.out.println(name + " ready to exchange");
                System.out.println(name + " < - > " + exchanger.exchange(name));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }

    public void testExecute() {
        Executor executor = Executors.newSingleThreadExecutor();
        System.out.println(Thread.currentThread().getName() + ": submits task");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ": Async task started");
            }
        });
    }

    public void testSubmit() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> f = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1000);
                return "Task executed";
            }
        });
        System.out.println("Waiting for result");
        System.out.println("result: " + f.get());

        executorService.shutdown();
    }

    public void testException() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> f = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                throw new RuntimeException("Exception thrown");
            }
        });
        System.out.println("Waiting for result");
        Thread.sleep(1000);
        System.out.println("result: " + f.get());

        executorService.shutdown();
    }

    public void testInvokeAny() throws ExecutionException, InterruptedException {
        List<Callable<String>> callables = new ArrayList<>();
        Random random = new Random();
        IntStream.range(0, 3).forEach(i -> callables.add(() -> {
            Thread.sleep(random.nextInt(1000));
            return String.valueOf(i);
        }));
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        String result = executorService.invokeAny(callables);
        System.out.println(result);
        executorService.shutdown();
    }

    public void testInvokeAll() throws ExecutionException, InterruptedException {
        List<Callable<String>> callables = new ArrayList<>();
        Random random = new Random();
        IntStream.range(0, 3).forEach(i -> callables.add(() -> {
            Thread.sleep(random.nextInt(1000));
            return String.valueOf(i);
        }));
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<String>> result = executorService.invokeAll(callables);

        for (Future f :
                result) {
            System.out.println(f.get());
        }
        executorService.shutdown();
    }

    public void testScheduled() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        System.out.println("Task scheduled " + new Date());
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("Task executed " + new Date());
            }
        }, 2, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();
    }

    public void testScheduledAtFixedRate() throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        System.out.println("Task scheduled " + new Date());
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Task executed " + new Date());
            }
        }, 2, 1, TimeUnit.SECONDS);
        Thread.sleep(10000);
        scheduledExecutorService.shutdown();
    }
}
