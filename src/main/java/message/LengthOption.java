package message;

public class LengthOption extends Option{

	int messageLength = -1;

	LengthOption() {
		super(eOption.Length);
	}

	LengthOption(String value) {
		super(eOption.Length);
		decode(value);
	}

	private String messageString(){
		return message().toString(eOption.Length, eOption.Checksum);

	}

	@Override
	String encode() {

		return Integer.toString(messageLength);
	}

	@Override
	void decode(String e) {
		messageLength = Integer.parseInt(e);
	}

	@Override
	boolean validate() {
		if(messageLength == -1){
			messageLength = messageString().length();
			return true;
		}
		return messageString().length() == messageLength;
	}


}