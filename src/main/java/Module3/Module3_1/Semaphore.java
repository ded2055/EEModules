package Module3.Module3_1;

public interface Semaphore {
    // Запрашивает разрешение. Если есть свободное захватывает его. Если нет - приостанавливает поток до тех пор пока не появится свободное разрешение.

    public void acquire();

    // Запрашивает переданое количество разрешений. Если есть переданое количество свободных разрешений захватывает их.

    // Если нет - приостанавливает поток до тех пор пока не появится переданое колтчество свободных разрешений.

    public void acquire(int permits);

    // Отпускает разрешение возвращая его семафору.

    public void release();

    // Отпускает переданое количество разрешений возварщая их семафору.

    public void release(int permits);

    // Возвращает количество свободных разрешений на данный момент.

    public int getAvailablePermits();
}

class SimpleSemaphore implements Semaphore {
    private final int maxPermits = 10;
    private int permits;

    public SimpleSemaphore(int permits) {
        if (permits <= 0) {
            throw new IllegalArgumentException(
                    String.format(
                            "Permits should be greater than zero. You passed: %d.",
                            permits));
        }
        if (permits > maxPermits) throw new IllegalArgumentException(
                String.format(
                        "Permits should be less %d than zero. You passed: %d.",
                        maxPermits, permits));
        this.permits = permits;
    }

    @Override
    public synchronized void acquire() {
        synchronized (this) {
            if (permits > 0) {
                permits--;
                System.out.println("Thread " + Thread.currentThread().getName() + " acquire 1 permit");
            } else try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void acquire(int permits) {
        if (permits <= 0) {
            throw new IllegalArgumentException(
                    String.format(
                            "Permits should be greater zero. You passed: %d",
                            permits));
        }
        synchronized (this) {
            if (this.permits - permits > 0) {
                this.permits -= permits;
            } else {
                if (this.permits > 0) {
                    notify();
                }
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void release() {
        synchronized (this) {
            if (permits < maxPermits) {
                permits++;
                System.out.println("Thread " + Thread.currentThread().getName() + " release 1 permit");
            } else {
                System.out.println("Max permits count reached");
            }
            notify();
        }

    }

    @Override
    public void release(int permits) {
        if (permits <= 0) {
            throw new IllegalArgumentException(
                    String.format(
                            "Permits should be greater %d than zero. You passed: %d.",
                            permits));
        }
        synchronized (this) {
            if (getAvailablePermits() + permits > maxPermits) {
                this.permits = maxPermits;
                System.out.println("Max permits count reached");
            } else {
                this.permits += permits;
                System.out.println("Thread " + Thread.currentThread().getName() + " release " + permits + " permits");
            }
            notify();
        }
    }

    @Override
    public int getAvailablePermits() {
        return this.permits;
    }

    public static void main(String[] args) {
        final Semaphore semaphore = new SimpleSemaphore(2);
        new Thread(new Worker(1, semaphore)).start();
        new Thread(new Worker(2, semaphore)).start();
        new Thread(new Worker(3, semaphore)).start();
        new Thread(new Worker(5, semaphore)).start();
        new Thread(new Worker(-5, semaphore)).start();
    }
}

class Worker implements Runnable {
    int permits;
    Semaphore semaphore;

    public Worker(int permits, Semaphore semaphore) {
        this.permits = permits;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        if (permits == 1) {
            for (; ; ) {
                semaphore.acquire();
                try {
                    System.out.println("Acquired semaphore.. sleeping!");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                semaphore.release();
            }
        } else {
            for (; ; ) {
                semaphore.acquire(permits);
                try {
                    System.out.println("Acquired semaphore.. sleeping!");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                semaphore.release(permits);
            }
        }
    }
}
