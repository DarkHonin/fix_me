package fix_me;

import message.FixMessage;

public class Broker extends FixWorker {

	public Broker() {
		super("Broker", 5000);
	}

	@Override
	String acceptBuy(FixMessage ms) {
		return null;
	}

	@Override
	String acceptSell(FixMessage ms) {
		return null;
	}



}