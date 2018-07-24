
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class ClientServer {
	
	private static final int GRAPH_NODES = 1000;
	private static final int GRAPH_EDGES = 2000;
	private static final int SEARCHES = 5;

	private static Random random = new Random();
	private static Searcher searcher = new SearcherImpl();
	private static Node[] nodes;
	
	
	public static void createNodes(int howMany) {
		nodes = new Node[howMany];

		for (int i = 0; i < howMany; i++) {
			nodes[i] = new NodeImpl();
		}
	}
	
	public void connectAllNodes() {
		for (int idxFrom = 0; idxFrom < nodes.length; idxFrom++) {
			for (int idxTo = idxFrom + 1; idxTo < nodes.length; idxTo++) {
				nodes[idxFrom].addNeighbor(nodes[idxTo]);
				nodes[idxTo].addNeighbor(nodes[idxFrom]);
			}
		}
	}
	
	public static void connectSomeNodes(int howMany) {
		for (int i = 0; i < howMany; i++) {
			final int idxFrom = random.nextInt(nodes.length);
			final int idxTo = random.nextInt(nodes.length);

			nodes[idxFrom].addNeighbor(nodes[idxTo]);
		}
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		try {
			createNodes(GRAPH_NODES);
			connectSomeNodes(GRAPH_EDGES);
			
			Socket socket = new Socket("localhost", 8000);
			
			Map<Node, Map<Node, Integer>> mapFromServer = new HashMap<Node, Map<Node, Integer>>();
			mapFromServer = MyServer.searchBenchmark(SEARCHES, nodes);
			
			ObjectOutputStream objOutputStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream objInputStream = new ObjectInputStream(socket.getInputStream());
			objOutputStream.writeObject(mapFromServer);
			mapFromServer = (Map<Node, Map<Node, Integer>>)objInputStream.readObject();
			objOutputStream.close();
			objInputStream.close();
			socket.close();
		
		
		
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
