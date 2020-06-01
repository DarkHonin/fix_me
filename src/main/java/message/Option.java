package message;

public abstract class Option {

	public static enum eOption {
		ID, Type, Length, Instrument, Quantity, Market, Price, Status, Checksum,
	};

	eOption option;
	private FixMessage message;

	Option(eOption e){
		option = e;
	}

	public void setMessage(FixMessage message){
		this.message = message;
	}

	/*
		encoding will fetch its respective value from the message object
		and convert it into a string format
	*/
	abstract String encode();

	/*
		Decode will convert a string representation of its data into a
		usable object
	*/
	abstract void decode(String e);

	abstract boolean validate();

	FixMessage message(){
		return message;
	}

	public void fromString(String e){
		decode(e);
	}

	public String toString(){
		return String.format("%d=%s", option.ordinal(), encode());
	}
}