package message;

import java.util.Arrays;
import java.util.List;

public class TypeOption extends Option{

	public static enum MessageType{
		Handshake,
		Buy,
		Sell
	};

	MessageType type = null;

	TypeOption() {
		super(eOption.Type);
	}
	TypeOption(String value) {
		super(eOption.Type);
		type = MessageType.valueOf(value);
	}

	TypeOption(MessageType t) {
		super(eOption.Type);
		type = t;
	}

	@Override
	String encode() {
		return type.name();
	}

	@Override
	void decode(String e) {
		type = MessageType.valueOf(e);
	}

	public void setType(MessageType t){
		type = t;
	}

	public MessageType getType(){
		return type;
	}

	@Override
	boolean validate() {

		if(type == null){
			System.err.println("Message type can not be null");
			return false;
		}

		List<eOption> opts = Arrays.asList(new eOption[]{eOption.ID, eOption.Type, eOption.Length, eOption.Checksum});

		for(eOption e: opts)
			if(!message().hasOption(e)){
				System.err.printf("Message type '%s' requires the following option: %s\n", type.name(), e.name());
				return false;
			}
		return true;
	}

	public static MessageType extractType(Option[] options){
		Option type =  (options[eOption.Type.ordinal()]);
		TypeOption tp = (TypeOption) type;
		return tp.getType();
	}

}