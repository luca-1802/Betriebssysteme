public class Teil1 {
    public static void main(String[] args) {
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

        // Create and start each threat with given min and max
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

        // Combine the results
        int result = 0;
        for (Task task : tasks) {
            result += task.getResult();
        }

        // Display the final result
        System.out.println("Sum of numbers from " + min + " to " + max + ": " + result);
    }
}

class Task implements Runnable {
    private final int start;
    private final int end;
    private int result = 0;

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
     * Executes the task by adding the numbers to the result within the assigned range.
     */
    @Override
    public void run() {
        for (int i = start; i <= end; i++) {
            result += i;
        }
    }

    /**
     * Retrieves the result of this task.
     *
     * @return The sum of numbers within the assigned range.
     */
    public int getResult() {
        return result;
    }
}
