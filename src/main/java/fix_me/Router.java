package fix_me;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import sim.NetCatchWorker;
import sim.NetCatchWorker.NetCatchAcceptor;
import sim.NetWorker;
import sim.NetWorker.NetAcceptor;
import java.util.Random;

import message.IDOption;
import message.Messages.HandshakeMessage;
import message.Option.eOption;

public class Router implements NetCatchAcceptor, NetAcceptor {
	NetCatchWorker brokerWorker, marketWorker;
	private Random e;
	HashMap<Integer, NetWorker> Brokers, Markets;

	public Router() {
		e = new Random(this.hashCode());
		brokerWorker = new NetCatchWorker(5000);
		marketWorker = new NetCatchWorker(5001);
		brokerWorker.setAcceptor(this);
		marketWorker.setAcceptor(this);

		Brokers = new HashMap<Integer, NetWorker>();
		Markets = new HashMap<Integer, NetWorker>();
	}

	public void open() {
		brokerWorker.start();
		marketWorker.start();
	}

	public void close() {
		brokerWorker.stop();
		marketWorker.stop();
	}

	@Override
	public void acceptSocket(NetCatchWorker instance, SocketChannel ch) {
		try {
			ch.finishConnect();
			int id = genID();
			NetWorker c = new NetWorker(ch);
			c.start();
			c.setAcceptor(this);
			HandshakeMessage handshake = new HandshakeMessage();
			IDOption o = handshake.getOption(eOption.ID);
			o.setID(id);
			handshake.validate();
			c.send(handshake.toString());
			if (instance == brokerWorker) {
				Brokers.put(id, c);
			}
			if (instance == marketWorker) {
				Markets.put(id, c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void acceptMessage(NetWorker instance, String message) {
		System.out.println("Router recieved: " + message);
		System.out.println("\t From: " + instance);
		// TODO Parse and do logic...
	}

	int genID(){

		int ret = 0;
		for(int i = 0; i < 6; i++){
			ret += e.nextInt(9) * Math.pow(10, i);
		}
		return ret;
	}

	@Override
	public NetWorker getWorker() {
		return Brokers.get(0);
	}

	public NetWorker getMarketWorker(int id){
		return Markets.get(id);
	}
	public NetWorker getBrokerWorker(int id){
		return Brokers.get(id);
	}
}