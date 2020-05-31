package fix_me;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;

import message.IDOption;
import message.Option;
import message.Option.eOption;
import message.TypeOption.MessageType;
import message.TypeOption;
import message.Messages.HandshakeMessage;
import sim.NetWorker;
import sim.SimWorker;
import sim.NetWorker.NetAcceptor;

public class Market extends SimWorker implements NetAcceptor {

	private NetWorker worker;
	private SocketChannel channel;
	int ID;

	public Market() {
		super(Executors.newSingleThreadExecutor(), "Market");
		try {
			channel = SocketChannel.open();
			channel.configureBlocking(false);

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
				worker = new NetWorker(channel);
				worker.setAcceptor(this);
				worker.start();
				System.out.println(this + " : Connection complete");
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
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
		System.out.println(this + " : Confirm message recieved : " + message);
		Option[] options = Option.parseOptions(message);
		MessageType mt = TypeOption.extractType(options);

		switch(mt){
			case Handshake:
				System.out.println("Message type: " + mt.name());
				HandshakeMessage MSG = new HandshakeMessage(options);
				if(!MSG.validate())
				System.err.printf("%s : Failed to validate message", this);
				else
				System.out.println("Message validation OK");
				System.out.println(MSG);
				IDOption opt = MSG.getOption(eOption.ID);
				ID = opt.getID();
				System.out.printf("%s : ID assigned : %d\n", this, ID);
				instance.send(MSG.toString());
				break;
		}

		// tp.getType()
		// TODO Parse and do logic...
	}

	@Override
	public NetWorker getWorker() {
		return worker;
	}

}