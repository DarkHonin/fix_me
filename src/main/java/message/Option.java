package message;

public abstract class Option {

	public static enum eOption {
		ID, Type, Length, Instrument, Quantity, Market, Price, Checksum,
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



	public static Option[] parseOptions(String s){
		String[] parts = s.split(";");
		Option[] opts = new Option[eOption.values().length];
		for(String p:parts){
			String[] pieces = p.split("=");
			eOption opt = eOption.values()[Integer.parseInt(pieces[0])];	// Validate option ID
			switch(opt){
				case ID:
					opts[opt.ordinal()] = new IDOption();
					break;
				case Length:
					opts[opt.ordinal()] = new LengthOption();
					break;
				case Type:
					opts[opt.ordinal()] = new TypeOption();
					break;
				case Checksum:
					opts[opt.ordinal()] = new ChecksumOption();
					break;
				case Instrument:
					opts[opt.ordinal()] = new IDOption();
					break;
				case Market:
					opts[opt.ordinal()] = new IDOption();
					break;
				case Price:
					break;
				case Quantity:
					break;
				default:
					return null;
			}

			opts[opt.ordinal()].fromString(pieces[1]);
		}
		return opts;
	}
}