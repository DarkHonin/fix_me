package sim;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;

public class NetWorker extends SimWorker {

	public static interface NetAcceptor{
		public void acceptMessage(NetWorker instance, String message);
	};

	private SocketChannel channel = null;
	private ByteBuffer	buffer = null;
	private NetAcceptor acceptor;

	public NetWorker(SocketChannel chanel) {
		super(Executors.newSingleThreadExecutor(), "Net");
		buffer = ByteBuffer.allocate(48);
		channel = chanel;
	}

	@Override
	protected boolean work() {
		try {
			if(!channel.finishConnect())
				return true;


			// Listen for messages
			int readLen = 0;
			if ((readLen = channel.read(buffer)) != 0) {
				if(acceptor != null)
					acceptor.acceptMessage(this, new String(buffer.array()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public void send(String str){
		ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
		try {
			channel.write(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setAcceptor(NetAcceptor e){
		acceptor = e;
	}

	@Override
	protected void shutdown() {
		try {
			channel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}