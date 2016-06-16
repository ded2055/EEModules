package Module3.Module3_1.Examples;


public class LatchBootstrap {
    private CountDownLatch countDownLatch;

    public static void main(String[] args) {
        new LatchBootstrap().test();
    }

    public void test() {
        countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(new Worker()).start();
        }
        while (countDownLatch.getCounter() > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    countDownLatch.countDown();
                }
            }).start();
        }
    }

    public class Worker implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println("Thread " + Thread.currentThread().getName() + " start waiting");
                countDownLatch.await();
                System.out.println("Thread " + Thread.currentThread().getName() + " stop waiting");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}