package sim;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;

public class NetWorker extends SimWorker {

	public static interface NetAcceptor{
		public void acceptMessage(NetWorker instance, String message);
		public NetWorker getWorker();
	};

	private SocketChannel channel = null;
	private ByteBuffer	buffer = null;
	private NetAcceptor acceptor;

	public NetWorker(SocketChannel chanel) {
		super(Executors.newSingleThreadExecutor(), "Net");
		buffer = ByteBuffer.allocate(255);
		channel = chanel;
	}

	@Override
	protected boolean work() {
		try {
			if(!channel.finishConnect())
				return true;


			// Listen for messages
			if (( channel.read(buffer)) != 0) {
				if(acceptor != null){
					String str = new String(buffer.array()).trim();
					System.out.printf("%s : %s : %s\n", acceptor, "Recieved Message ", str);
					acceptor.acceptMessage(this, str);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void send(String str){
		ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
		try {
			System.out.printf("%s : %s : %s\n", this, "Attempting to send message", str);
			channel.write(buffer);
		} catch (IOException e) {
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
			e.printStackTrace();
		}

	}

	public SocketChannel getChannel(){
		return channel;
	}
	public String toString() {
		return super.toString()+ "[" + acceptor + "]";
	}
}