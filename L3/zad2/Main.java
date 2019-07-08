import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Main {

	private static volatile int[] canal;
	private final static int canalLength = 20;
	private final static int numberOfSources = 3;
	private final static int delay = 300; // in miliseconds

	public static void main(String[] args) throws FileNotFoundException {

		if(canalLength >= numberOfSources) {

			canal = new int[canalLength];
			for(int i = 0; i < canalLength; i++) {
				canal[i] = 0;
			}

			int j = 0;
			for(int i = 1; j < numberOfSources; j++, i+= canalLength/numberOfSources) {

				Thread thread = new Thread(new Source(i,canalLength,canal,delay));
				thread.start();
			}

			Thread thread = new Thread( new Runnable () { 
				@Override
				public void run() {
					
					int[] oldCanal = new int[canal.length];
					oldCanal =	Arrays.copyOf(canal, canalLength);
					
					for( int i : canal)
						System.out.printf("%2d ", i);
					System.out.printf("\n");
					
					while(true) {
						if(!Arrays.equals(oldCanal,canal)) {
							oldCanal =	Arrays.copyOf(canal, canalLength);
							for( int i : canal)
								System.out.printf("%2d ", i);
							try {
								TimeUnit.MILLISECONDS.sleep(delay);
								System.out.printf("\n");
							}
							catch (InterruptedException e) {
								e.printStackTrace();
							} 
						}}}
				});
			thread.start();
		}
	}
}
