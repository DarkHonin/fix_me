package fix_me;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;

import sim.NetWorker;
import sim.SimWorker;
import sim.NetWorker.NetAcceptor;

public class Market extends SimWorker implements NetAcceptor {

	private NetWorker worker;
	private SocketChannel channel;

	public Market() {
		super(Executors.newSingleThreadExecutor(), "Market");
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
				channel.connect(new InetSocketAddress("localhost", 5001));
				channel.finishConnect();
				System.out.println(this + " : Connection complete");
				return true;
			} catch (IOException e) {
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
		worker.stop();

	}

	@Override
	public void acceptMessage(NetWorker instance, String message) {
		System.out.println(this + " : Message : " + message);
		// TODO Parse and do logic...
	}

}