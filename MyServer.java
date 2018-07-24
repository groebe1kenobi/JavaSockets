import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MyServer {
	
	private static final int SEARCHES = 5;
    private static Node[] nodes;
	private static Searcher searcher = new SearcherImpl();

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		new MyServer().runServer();
	}

	public void runServer() throws IOException, ClassNotFoundException {
		Map<Node, Map<Node, Integer>> toReturn = null;
		try {
		ServerSocket serverSocket = new ServerSocket(8000);
		System.out.println("Server up and ready to go...");
		// listen for socket
		Socket socket = serverSocket.accept();
		ObjectInputStream objInputStream = new ObjectInputStream(socket.getInputStream());
		ObjectOutputStream objOutputStream = new ObjectOutputStream(socket.getOutputStream());
		toReturn = (Map<Node, Map<Node, Integer>>)objInputStream.readObject();
		 
		objOutputStream.writeObject(toReturn);
		objOutputStream.close();
		objInputStream.close();
		socket.close();
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	public static Map<Node, Map<Node, Integer>> searchBenchmark(int howMany, Node[] nodes) {
		Map<Node, Map<Node, Integer>> outterMap = new HashMap<Node, Map<Node, Integer>>();
		Map<Node, Integer> innerMap = new HashMap<Node, Integer>();
		// Display measurement header.
		System.out.printf("%7s %8s %13s %13s\n", "Attempt", "Distance", "Time", "TTime");
		for (int i = 0; i < howMany; i++) {
			// Select two random nodes.
			// final int idxFrom = random.nextInt(nodes.length);
			// final int idxTo = random.nextInt(nodes.length);
			final int idxFrom = i;
			for (int j = 0; j < howMany; j++) {
				// Calculate distance, measure operation time
				final int idxTo = j;
				final long startTimeNs = System.nanoTime();
				final int distance = searcher.getDistance(nodes[idxFrom], nodes[idxTo]);
				final long durationNs = System.nanoTime() - startTimeNs;

				// Calculate transitive distance, measure operation time
				final long startTimeTransitiveNs = System.nanoTime();
				final int transitiveDistance = searcher.getTransitiveDistance(4, nodes[idxFrom], nodes[idxTo]);
				final long transitiveDurationNs = System.nanoTime() - startTimeTransitiveNs;
				
				

				if (distance != transitiveDistance) {
					System.out.printf("Standard and transitive algorithms inconsistent (%d != %d)\n", distance,
							transitiveDistance);
				} else {
					// Print the measurement result.
					System.out.printf("%7d %8d %13d %13d\n", i, distance, durationNs / 1000,
							transitiveDurationNs / 1000);
					innerMap.put(nodes[idxTo], distance);
				}
			}
			outterMap.put(nodes[idxFrom], innerMap);
		}
		//System.out.println(Arrays.asList(outterMap));
		return outterMap;
	}

}
