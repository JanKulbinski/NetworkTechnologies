import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class z2 {

	static BufferedReader br;
	static float graph [][];
	static float maxDelay;
	static float probability;
	static int n;
	static int packageSize;
	static int capacity; 
	final static int numberOfExperiments = 10000;

	public static void main(String[] args) {

		setUp();
		
		float[][] graphCopy = new float [n+1][n+1];
		float[][] graphFlow = new float [n+1][n+1];
		float delaySum = 0;
		float success = 0;
		
		for (int i = 0; i < numberOfExperiments; i++) {
			makeCopy(graph,graphCopy);
			for (int j = 1; j <= n; j++) {
				for (int k = j; k <= n; k++) {
					if(graph[j][k] > 0) { // if edge exists try to delete it
						float r = (float) Math.random();
						if(r >= probability) {
							graphCopy[j][k] = -1;
							graphCopy[k][j] = -1;
						}	
					}
				}
			}	
			
			if(ifConnected(graphCopy)) {
				
				makeCopy(graphCopy,graphFlow);
				generateFlow(graphFlow);
				
				if(capacityTest(graphFlow)) {
					float averageDelay = calculateAverageDelay(graphCopy,graphFlow);
					if(averageDelay <= maxDelay)
						success++;
					delaySum = delaySum + averageDelay;
				}
					
			}
				
		}
		
		System.out.printf("Average delay : %.6f\n",delaySum/numberOfExperiments);
		System.out.printf("Reliability of given network is : %.4f\n",success/numberOfExperiments);
	}
	
	public static float calculateAverageDelay(float[][] graph,float[][] graphFlow) {
		float delay = 0;
		
		float sum = 0;
		for(int i = 1; i <= n; i++)
			for(int j = 0; j <= n; j++)
					if(graph[i][j] > 0)
						sum=+graph[i][j];
		
		for(int i = 1; i <= n; i++)
			for(int j = i; j <= n; j++)
				if(graphFlow[i][j] > 0) {
					float flow = graphFlow[i][j];
					delay = delay + (flow/((capacity/packageSize)- flow));
				}
				
				
		return delay/sum;
	}
	
	public static void setUp() {

		Scanner sc = new Scanner(System.in);
		n = sc.nextInt();
		packageSize = sc.nextInt();
		probability = sc.nextFloat();
		maxDelay = sc.nextFloat();
		capacity = sc.nextInt();

		try {
			StringBuilder path = new StringBuilder("C:/Users/janku/OneDrive/Pulpit/EclipseWorkspace/SieciL2/src/example2.1");
			StringBuilder path2 = new StringBuilder("/home/jan/EclipseWorkspace/SieciL2/src/example2.1");
			br = new BufferedReader(new FileReader(path.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}	

		graph = new float [n+1][n+1];

		for (int j = 1; j <= n; j++) {
			String[] integersInString;
			try {
				integersInString = br.readLine().split(" ");
				for (int i = 1; i <= n; i++) {
					graph[j][i] = Float.parseFloat(integersInString[i-1]);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static boolean capacityTest(float[][] graphFlow) {
		for(int l = 1; l <= n; l++)
			for(int m = 1; m <= n; m++)
				if(graphFlow[l][m] > 0) {
					if(graphFlow[l][m] > capacity/packageSize)
						return false;
				}			
		return true;		
	}
	
	public static void generateFlow(float[][] graphFlow) {
		int[] distance = new int[n+1];
		int[] path = new int[n+1];
		boolean[] visited = new boolean[n+1];
		
		for(int x = 1; x <= n; x++)
			path[x] = 0;
		for(int x = 1; x <= n; x++)
			distance[x] = Integer.MAX_VALUE;
		for(int x = 1; x <= n; x++)
			visited[x] = false;
		
		for(int l = 1; l <= n; l++)
			for(int m = 1; m <= n; m++) {
				
				if(graphFlow[l][m] == -1) {
					distance[l] = 0;
					visited[l] = true; 
					
					findShortestPath(l,m,distance,path,visited,graphFlow);
				
					int i = m;
					float flow = 0;
					
					while(i != l) {		
						flow = flow + graphFlow[i][path[i]];
						i = path[i];			
					}
					
					graphFlow[l][m] = flow;
					graphFlow[m][l] = flow;
					
					for(int x = 1; x <= n; x++)
						path[x] = 0;
					for(int x = 1; x <= n; x++)
						distance[x] = Integer.MAX_VALUE;
					for(int x = 1; x <= n; x++)
						visited[x] = false;
				}
				
			}
	}
	
	public static void findShortestPath(int start, int finish, int[] distance, int[]path, boolean[]visited, float[][] graph ) {
		 Queue<Integer> q = new LinkedList<>(); 
		 q.add(start);
		 while(!q.isEmpty()) {
			 int u = q.poll();
			 for(int i = 1; i<= n; i++) {
				 if(graph[u][i] > 0 && !visited[i]) {
					 visited[i] = true;
					 distance[i]++;
					 path[i] = u;
					 q.add(i);
				 }
			 }
			 
		 }
	}
	
	public static void makeCopy(float[][] graph, float[][] graphCopy) {
		for(int l = 1; l <= n; l++)
			for(int m = 1; m <= n; m++)
				graphCopy[l][m]=graph[l][m];
	}
	
	public static boolean ifConnected(float[][] graphCopy) {
		boolean[] visited =  new boolean[n+1];

		for(boolean v : visited)
			v = false;

		dfs(1,graphCopy,visited);	

		for(int i = 1; i <= n; i++) {
			if (!visited[i])
				return false;
		}
		return true;
	}

	public static void dfs(int start, float[][] graphCopy, boolean[] visited) {
		if(!visited[start]) {
			visited[start] = true;
			for(int i = 1; i<= n; i++) {
				if(graphCopy[start][i] > 0) {
					dfs(i,graphCopy,visited);
				}
			}
		}
	}
}
