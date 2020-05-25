package sim;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;


public class NetCatchWorker extends SimWorker {

	private ServerSocketChannel server;

	public static interface NetCatchAcceptor{
		public void acceptSocket(NetCatchWorker instance, SocketChannel ch);
	};

	NetCatchAcceptor acceptor = null;

	public NetCatchWorker(int port) {
		super(Executors.newSingleThreadExecutor(), "NetCatch:"+Integer.toString(port));
		try {
			server = ServerSocketChannel.open();
			server.configureBlocking(false);
			server.bind(new InetSocketAddress("localhost", port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setAcceptor(NetCatchAcceptor e){
		acceptor = e;
	}

	@Override
	protected boolean work() {
		SocketChannel ch = null;
		try {
			// System.out.println(this + " : Awaiting connect");
			if ((ch = server.accept()) != null) {
				System.out.println(this + " : Connected to : " + ch );
				if(acceptor != null) acceptor.acceptSocket(this, ch);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	protected void shutdown() {
		if(server.isOpen())
			try {
				server.close();
			} catch (IOException e) {

				e.printStackTrace();
			}

	}

}