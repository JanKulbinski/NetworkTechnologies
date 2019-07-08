import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CanalCleaner {


	private volatile int[] canal;
	private int delay;
	private int start;
	
	CanalCleaner(int start,int[] canal,int delay) {
		this.canal = canal;
		this.delay = delay;
		this.start = start;
	}

	public synchronized void  run() {

		canal[start] = 0;
		
		for(int i = 1; i < canal.length; i++) {

			if(start + i < canal.length) {
				canal[start + i] = 0;
			}

			if(start - i >= 0) {
				canal[start - i] = 0;
			}
			
			try {
				TimeUnit.MILLISECONDS.sleep(delay);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}

	}
}