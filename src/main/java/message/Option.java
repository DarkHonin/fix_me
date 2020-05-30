package message;

public abstract class Option {

	public static enum eOption {
		ID, Type, Length, Instrument, Quantity, Market, Price, Checksum,
	};

	eOption option;
	FixMessage message;

	Option(eOption e, FixMessage message){}

	public abstract String encode();

	public String toString(){
		return String.format("%d=%s", option.ordinal());
	}
}