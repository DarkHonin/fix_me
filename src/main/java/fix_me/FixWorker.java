package fix_me;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;

import message.FixMessage;
import message.IDOption;
import message.Option.eOption;
import message.TypeOption;
import sim.NetWorker;
import sim.NetWorker.NetAcceptor;
import sim.SimWorker;

public abstract class FixWorker extends SimWorker implements NetAcceptor{

	private int ID = -1, port;
	private NetWorker worker;
	private SocketChannel channel;

	public FixWorker(String name, int port) {
		super(Executors.newSingleThreadExecutor(), name);
		this.port = port;
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
				channel.connect(new InetSocketAddress("localhost", port));
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

	public int getID(){
		return ID;
	}

	@Override
	public NetWorker getWorker() {
		return worker;
	}

	@Override
	public void acceptMessage(NetWorker instance, String message) {
		System.out.println(this + " : Confirm message recieved : " + message);
		FixMessage fm = new FixMessage(message);
		fm.validate();
		TypeOption type = fm.getOption(eOption.Type);
		String resp = null;
		switch(type.getType()){
			case Handshake:
				resp = acceptHandshake(fm);
				System.out.println("Handshake recieved: ID : " + Integer.toString(ID));
				break;

			case Buy:
				resp = acceptBuy(fm);
				break;
			case Sell:
				resp = acceptSell(fm);
				break;
			default:
				break;
		};
		if(resp != null)
			instance.send(resp);
		// tp.getType()
		// TODO Parse and do logic...
	}

	String acceptHandshake(FixMessage ms){
		IDOption id = ms.getOption(eOption.ID);
		ID = id.getID();
		return null;
	}
	abstract String acceptBuy(FixMessage ms);
	abstract String acceptSell(FixMessage ms);

	@Override
	public String toString(){
		return super.toString() + "[" + ID + "]";
	}


}