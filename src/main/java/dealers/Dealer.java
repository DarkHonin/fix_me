package dealers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Dealer implements Runnable {
	private static ArrayList<Dealer>	dealers = new ArrayList<Dealer>();	// Dealers

	private SocketChannel socket = null;
	private ExecutorService service = null;

	private String Host;
	private int Port;
	private int ID;

	public Dealer(String host, int port) {
		super();
		Host = host;
		Port = port;
		service = Executors.newSingleThreadExecutor();
		dealers.add(this);
	}

	private void connect() {
		try {
			socket = SocketChannel.open(new InetSocketAddress(Host, Port));
			System.out.println(toString()+", opening connection: " + socket.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isConnected(){
		if(socket == null) return false;
		return socket.isConnected();
	}

	public boolean isRunning(){
		return service.isShutdown();
	}

	public void open(){
		if(!isConnected())	connect();
		System.out.println("Running "+toString()+" thread");
		service.execute((Runnable) this);
	}

	public void close(){
		System.out.println("Shutting down "+toString()+" : " + socket.toString());
		if(isRunning()) service.shutdownNow();
		System.out.println("\tService offline");
		if(isConnected())
			try {
				socket.close();
				System.out.println("\tSocket offline");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public void run(){

	}

	public static void closeAll(){
		for(Dealer i : dealers){
			i.close();
		}
	}

	@Override
	public String toString() {
		return "Dealer";
	}
}