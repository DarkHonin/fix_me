package fix_me;

import java.util.HashMap;
import java.util.Random;

import message.FixMessage;
import message.IDOption;
import message.Option.eOption;
import message.TypeOption;
import message.TypeOption.MessageType;

public class Market extends FixWorker{

	HashMap<String, Integer> InstrumentTable = new HashMap<String, Integer>();

	public Market() {
		super("Market", 5001);
		Random ran = new Random(InstrumentTable.hashCode());
		for(int x = 0; x <= 10; x++){
			String hexName = Integer.toHexString(x);
			InstrumentTable.put(hexName, ran.nextInt(20));
		}
	}

	@Override
	String acceptBuy(FixMessage ms) {
		// TODO buy
		return null;
	}

	@Override
	String acceptSell(FixMessage ms) {
		// TODO Sell
		return null;
	}

	void sell(String name, int n){
		FixMessage msg = new FixMessage();
		((IDOption) msg.getOption(eOption.ID)).setID(getID());
		((TypeOption) msg.getOption(eOption.Type)).setType(MessageType.Sell);
		getWorker().send(msg.toString());
	}

}