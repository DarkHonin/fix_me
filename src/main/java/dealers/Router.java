package dealers;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Router implements Runnable{

	private Map<String, SocketChannel> Brokers, Markets;

	private ServerSocketChannel server;

	private ExecutorService service;

	public Router() {
		Brokers = new HashMap<String, SocketChannel>();
		Markets = new HashMap<String, SocketChannel>();
		service = Executors.newSingleThreadExecutor();	// Max 10 threads concurrent
	}

	public void open(){
		try {
			server = ServerSocketChannel.open();
			server.configureBlocking(false);
			server.bind(new InetSocketAddress("localhost", 5000));
			service.execute(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close(){
		try {
			server.close();
			service.shutdownNow();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			// server.bind(new InetSocketAddress("localhost", 5001));
			System.out.println("Router is listening");
			while(server.isOpen()){
				SocketChannel channel;
				if((channel = server.accept()) != null){	// Caught connection
					System.out.println("Remote connected to router : " + channel.getRemoteAddress().toString());

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}