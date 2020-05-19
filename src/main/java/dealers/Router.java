package dealers;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Router {

	private Map<String, IDealer> Brokers, Markets;
	private ServerSocketChannel server;

	public Router() {
		Brokers = new HashMap<String, IDealer>();
		Markets = new HashMap<String, IDealer>();
	}

	public void open() {
		try {
			server = ServerSocketChannel.open();
			server.configureBlocking(false);
			server.bind(new InetSocketAddress("localhost", 5000));
			server.bind(new InetSocketAddress("localhost", 5001));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}