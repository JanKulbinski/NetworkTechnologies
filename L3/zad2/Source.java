import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Source implements Runnable {

	private int id;
	private int canalLength;
	private volatile int[] canal;
	private int delay;
	private int myDelay;
	private Boolean running = true;
	private Random rand;

	Source(int id, int canalLength,int[] canal,int delay) {
		this.id = id;
		this.canalLength = canalLength;
		this.canal = canal;
		this.delay = delay;
		myDelay = 5 * delay;
		rand = new Random();
	}

	public synchronized Boolean startTransmision() {
		if(canal[id] == 0 && rand.nextBoolean()) {
			canal[id] = id;
			return true;
		}
		return false;
	}

	public synchronized void rightLeftTransmision(int i) {
		if(canal[id + i] == 0) {
			canal[id + i] = id;	
			if((id - i >= 0) && (canal[id - i] == 0)) {
				canal[id - i] = id;
			}			
		}
	}

	public synchronized void informAboutColision(int start) {

		canal[start] = -1;

		for(int i = 1; i < canal.length; i++) {

			if(start + i < canal.length) {
				canal[start + i] = -1;
			}

			if(start - i >= 0) {
				canal[start - i] = -1;
			}

			try {
				TimeUnit.MILLISECONDS.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}

	}
	
	@Override
	public void run() {
		
		int afterColisionDelay = 1000;
		
		while(running) {		
			
			if(startTransmision()) {

				for(int i = 1; i < canalLength; i++) {
					
					if(id + i < canalLength) {  //RIGHT
						if(canal[id + i] == -1)
							break;
						rightLeftTransmision(i);
						if(canal[id + i] != 0 && canal[id + i] != id) {
							informAboutColision(id+i);
							
							CanalCleaner canalCleaner =  new CanalCleaner(id+i, canal, delay);
							canalCleaner.run();
							
							if(rand.nextBoolean())
								 break;
							else {
								afterColisionDelay *= 2;
								if(afterColisionDelay > 1024)
									return;
							}
							
							try {
								TimeUnit.MILLISECONDS.sleep(afterColisionDelay);
								break;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							
						}
					}
					
					if(id - i >= 0) {  //LEFT
						if(canal[id-i] == -1)
							break;
						if(canal[id - i] == 0)
							canal[id - i] = id;
						else if(canal[id - i] != id) {		
							informAboutColision(id-i);

							CanalCleaner canalCleaner =  new CanalCleaner(id-i, canal, delay);
							canalCleaner.run();
							
							if(rand.nextBoolean())
								 break;
							else {
								afterColisionDelay *= 2;
								if(afterColisionDelay > 1024)
									return;
							}
							try {
								TimeUnit.MILLISECONDS.sleep(afterColisionDelay);
								break;
							} catch (InterruptedException e) {
								e.printStackTrace();
							} 
							
						}
					}

					if(canal[0] == id && canal[canalLength - 1] == id) {
						afterColisionDelay = 1;
						System.out.println("Message delivered !");
						CanalCleaner canalCleaner =  new CanalCleaner(id, canal, delay);
						canalCleaner.run();
					}
						
						
					try {
						TimeUnit.MILLISECONDS.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}			
			}
			
				try {
					TimeUnit.MILLISECONDS.sleep(myDelay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

		}
	}
	
	public void stop() {
		running = false;
	}

}
