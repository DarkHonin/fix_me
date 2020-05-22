package dealers;

import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class Router {

	class Acceptor implements Runnable {

		private ServerSocketChannel server;

		private int port;
		Router router;

		public Acceptor(Router e, int port) {
			this.port = port;
			router = e;
		}

		@Override
		public void run() {
			try {
				server = ServerSocketChannel.open();
				server.bind(new InetSocketAddress("localhost", port));
				server.configureBlocking(false);
				while (server.isOpen()) {
					SocketChannel chanel;
					if ((chanel = server.accept()) != null) {
						while(!chanel.finishConnect());
						router.accept(chanel, port);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private static Map<Integer, SocketChannel> Brokers = new HashMap<Integer, SocketChannel>(),
	Markets = new HashMap<Integer, SocketChannel>();


	private ExecutorService service;

	Acceptor	brokerAcceptor, marketAcceptor;

	public Router() {
		brokerAcceptor = new Acceptor(this, 5000);
		marketAcceptor = new Acceptor(this, 5001);
		service = Executors.newFixedThreadPool(2);	// Max 10 threads concurrent
	}

	protected void accept(SocketChannel channel, int port){
		int id = genID();
		System.out.println(Integer.toString(port) + " : Connection made: " + Integer.toString(id));

		sendMessage(channel, "ID=" + Integer.toString(id));
		if(port == 5000){	// Connected to broker
			Brokers.put(id, channel);
		}if(port == 5001){
			Markets.put(id, channel);
		}
	}

	protected void sendMessage(SocketChannel ch, String message){
		System.out.println("Sending message: " + message);
		try {
			ByteBuffer buff = ByteBuffer.wrap(message.getBytes());
			while(buff.hasRemaining())
				ch.write(buff);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void open(){
		service.submit(brokerAcceptor);
		service.submit(marketAcceptor);
	}

	public void close(){
		try {
			brokerAcceptor.server.close();
			marketAcceptor.server.close();
			service.shutdownNow();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Random rgen = new Random();

	private int genID(){
		int ret = 0;
		for(int x = 0; x < 6;x++)
			ret += rgen.nextInt(9) * (Math.pow(10, x));
		return ret;
	}

}