package fix_me;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sim.NetWorker;
import sim.SimWorker;
import sim.NetWorker.NetAcceptor;

public class Broker extends SimWorker implements NetAcceptor {

	private NetWorker worker;
	private SocketChannel channel;

	public Broker() {
		super(Executors.newSingleThreadExecutor(), "Broker");
		try {
			channel = SocketChannel.open();
			channel.configureBlocking(false);
			worker = new NetWorker(channel);
			worker.setAcceptor(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected boolean work() {
		if(!channel.isConnected()){
			try {
				System.out.println(this + " : Attempting connect");
				channel.connect(new InetSocketAddress("localhost", 5000));
				channel.finishConnect();
				System.out.println(this + " : Connection complete");
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if(!worker.isRunning()){
			worker.start();
		}
		// Do super secret things here
		return true;
	}

	@Override
	protected void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void acceptMessage(NetWorker instance, String message) {
		System.out.println(this + " : Message : " + message);

	}

}