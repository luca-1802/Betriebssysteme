import java.util.TreeSet;
import java.util.concurrent.Semaphore;

public class Teil2 {	
	
    public static final TreeSet<Integer> primeNumbers = new TreeSet<>();
    public static final Semaphore semaphore = new Semaphore(1);
    
	public static void main(String[] args) {
		
		if (args.length != 3) {
			System.out.println("Kommandozeilenargumente nicht vollständig");
			return;
		}

		int start = Integer.parseInt(args[0]);
		int end = Integer.parseInt(args[1]);
		int numthreads = Integer.parseInt(args[2]);
		
		// Bestimmung Zahlenbereich:
		int[] ranges = new int[numthreads + 1];
        int thread_block_size = (end - start + 1) / numthreads;
        for (int i = 0; i < numthreads; i++) {
            ranges[i] = start + i * thread_block_size;
        }
        ranges[numthreads] = end + 1;
        
        // Threads starten:
		Thread[] thread = new Thread[numthreads];
		Counter2[] counter = new Counter2[numthreads];
		for (int i = 0; i < numthreads; i++) {
			counter[i] = new Counter2(ranges[i], ranges[i + 1]);
			thread[i] = new Thread(counter[i]);
			thread[i].start();
		}
		
		// auf Threads warten:
		for (int i = 0; i < numthreads; i++) {
			try {
				thread[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("gefundene Primzahlen: " + primeNumbers);
	}
}

class Counter2 implements Runnable{
	
	protected int start, end;
	
	public Counter2(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	@Override
	public void run() {
		for (int i = start; i <= end; i++) {
			if(isPrime(i)) {
				try {
                    Teil2.semaphore.acquire();
                    Teil2.primeNumbers.add(i);
                    Teil2.semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
			}			
		}
	}
	
	protected boolean isPrime(int number) {
        if (number <= 1) return false;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }
}