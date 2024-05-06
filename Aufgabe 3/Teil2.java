import java.util.ArrayList;

public class Teil2 {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Verwendung: java Teil1.java <min> <max> <numthreads>");
            return;
        }

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

        for (int i = 0; i < numthreads; i++) {
            int start = min + i * threadBlockSize;
            int end = (i == numthreads - 1) ? max : start + threadBlockSize - 1;
            tasks[i] = new Task(start, end);
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        ArrayList<Integer> primeNumbers = new ArrayList<>();
        for (Task task : tasks) {
            primeNumbers.addAll(task.getResult());
        }
    
        System.out.println("Prime numbers of numbers from " + min + " to " + max + ": " + primeNumbers);
    }
}

class Task implements Runnable {
    private final int start;
    private final int end;
    public ArrayList<Integer> result = new ArrayList<>();

    public Task(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        for (int i = start; i <= end; i++) {
            if (i <= 1)
                continue;
            
                boolean isprime = true;
            for (int j = 2; j*j <= i; j++) {
                if (i % j == 0) {
                    isprime = false;
                    break;
                }
            }
            
            if (isprime)
                result.add(i);
        }
    }

    public ArrayList<Integer> getResult() {
        return result;
    }
}
