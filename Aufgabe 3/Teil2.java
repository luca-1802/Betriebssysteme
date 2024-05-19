import java.util.TreeSet;
import java.util.concurrent.Semaphore;

public class Teil2 {
    // Global TreeSet
    public static final TreeSet<Integer> primeNumbers = new TreeSet<>();
    // Semaphore for synchronizing access
    public static final Semaphore semaphore = new Semaphore(1);

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        // Check if there are exactly 3 inputs
        if (args.length != 3) {
            System.out.println("Verwendung: java Teil1.java <min> <max> <numthreads>");
            return;
        }

        // Parse input arguments into integers
        int min, max, numthreads;
        try {
            min = Integer.parseInt(args[0]);
            max = Integer.parseInt(args[1]);
            numthreads = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid integer input!");
            return;
        }

        int threadBlockSize = (max - min) / numthreads;

        Task[] tasks = new Task[numthreads];
        Thread[] threads = new Thread[numthreads];

        // Create and start each thread with given min and max
        for (int i = 0; i < numthreads; i++) {
            int start = min + i * threadBlockSize;
            int end = (i == numthreads - 1) ? max : start + threadBlockSize - 1;
            tasks[i] = new Task(start, end);
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.nanoTime();
        long time = endTime - startTime;
        double convertedTime = (double) time / 1000000000.0;
        // Display the final result
        System.out.println("Time: " + convertedTime + " seconds");
        System.out.println("Prime numbers from " + min + " to " + max + ": " + primeNumbers);
    }
}

class Task implements Runnable {
    private final int start;
    private final int end;

    /**
     * Constructs a new Task with the specified range.
     *
     * @param start The starting number.
     * @param end   The ending number.
     */
    public Task(int start, int end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Executes the task by checking if the numbers within the assigned range are
     * prime numbers.
     */
    @Override
    public void run() {
        for (int i = start; i <= end; i++) {
            if (i <= 1)
                continue;

            boolean isPrime = true;
            for (int j = 2; j * j <= i; j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }

            if (isPrime) {
                try {
                    Teil2.semaphore.acquire();
                    Teil2.primeNumbers.add(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Teil2.semaphore.release();
                }
            }
        }
    }
}