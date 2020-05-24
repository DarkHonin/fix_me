package dealers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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
			socket.configureBlocking(false);
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
		service.execute(this);
	}

	public void close(){
		if(isRunning()) service.shutdownNow();
		if(isConnected())
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Closed "+toString());
	}

	void messageRecieved(String e){
		System.out.println(toString() + " :Message recieved: " + e);
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

	@Override
	public void run(){
		String message = "";
		while(isConnected()){
			try {
				ByteBuffer buff = ByteBuffer.allocate(255);	// Buffer alloc
				while(socket.read(buff) > 0){
					message += new String(buff.array());
					buff.clear();
				}
				if(message != ""){
					messageRecieved(message);
					message = "";
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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