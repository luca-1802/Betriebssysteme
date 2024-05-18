public class Teil1 {	
	public static void main(String[] args) {
		
		if (args.length != 3) {
			System.out.println("Kommandozeilenargumente nicht vollständig");
			return;
		}
		
		int min = Integer.parseInt(args[0]);
		int max = Integer.parseInt(args[1]);
		int numthreads = Integer.parseInt(args[2]);
		
		// Bestimmung Zahlenbereich:
		int[] ranges = new int[numthreads + 1];
        int thread_block_size = (max - min + 1) / numthreads;
        for (int i = 0; i < numthreads; i++) {
            ranges[i] = min + i * thread_block_size;
        }
        ranges[numthreads] = max + 1;
        
        // Threads starten:
		Thread[] thread = new Thread[numthreads];
		Counter[] counter = new Counter[numthreads];
		for (int i = 0; i < numthreads; i++) {
			counter[i] = new Counter(ranges[i], ranges[i + 1]);
			thread[i] = new Thread(counter[i]);
			thread[i].start();
		}
		
		// auf Threads warten & Ergebnis berechnen:
		int value = 0;
		for (int i = 0; i < numthreads; i++) {
			try {
				thread[i].join();
				value += counter[i].getValue();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Gesamtwert: " + value);
	}
}

class Counter implements Runnable{
	
	int min, max, value = 0;
	
	public Counter(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public void run() {
		for (int i = min; i < max; i++) {
			value = value + i;
		}
	}
	
	public int getValue() {
		return value;
	}
}