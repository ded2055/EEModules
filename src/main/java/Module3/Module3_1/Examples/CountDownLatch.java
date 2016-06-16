package Module3.Module3_1.Examples;

public class CountDownLatch {
    private int counter = 0;
    private final Object lock = new Object();

    public CountDownLatch(int counter) {
        this.counter = counter;
    }

    public void await() throws InterruptedException {
        synchronized (lock) {
            if (counter > 0) {
                lock.wait();
            } else {
                lock.notify();
            }
        }
    }

    public void countDown() {
        synchronized (lock) {
            if (counter > 0) {
                counter--;
                System.out.println("counter = " + counter);
            }
            if (counter == 0) {
                lock.notifyAll();
            }
        }

    }

    public int getCounter() {
        synchronized (lock) {
            return counter;
        }
    }
}