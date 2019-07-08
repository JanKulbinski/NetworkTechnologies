import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class z1 {

	static BufferedReader br;
	static int n;
	static float graph [][];
	final static int numberOfExperiments = 100000;

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		setUp(sc.nextInt());
		
		float[][] graphCopy = new float [n+1][n+1];
		int success = 0;
		for (int i = 0; i < numberOfExperiments; i++) {
			makeCopy(graph,graphCopy);
			for (int j = 1; j <= n; j++) {
				for (int k = j; k <= n; k++) {
					if(graph[j][k] != -1) { // if edge exists try to delete it
						float r = (float) Math.random();
						if(r >= graph[j][k]) {
							graphCopy[j][k] = -1;
							graphCopy[k][j] = -1;
						}	
					}
				}
			}	

			if(ifConnected(graphCopy)) 
				success++;
		}
		float result = ((float)success/(float)numberOfExperiments);		
		System.out.printf("Connectivity result: %.4f", result);
		System.out.println(" for "+numberOfExperiments+" attempts");
	}

	public static void setUp(int example) {
		try {
			StringBuilder path = new StringBuilder("C:/Users/janku/OneDrive/Pulpit/EclipseWorkspace/SieciL2/src/example");
			StringBuilder path2 = new StringBuilder("/home/jan/EclipseWorkspace/SieciL2/src/example");
			path.append(example);
			path2.append(example);
			br = new BufferedReader(new FileReader(path.toString()));
			n = Integer.parseInt(br.readLine());
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
	
	public static void makeCopy(float[][] graph, float[][] graphCopy) {
		for(int l = 1; l <= n; l++)
			for(int m = 0; m <= n; m++)
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
				if(graphCopy[start][i] != -1) {
					dfs(i,graphCopy,visited);
				}
			}
		}
	}
}	
