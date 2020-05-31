package message.Messages;

import message.FixMessage;
import message.Option;
import message.TypeOption;
import message.Option.eOption;
import message.TypeOption.MessageType;

public class HandshakeMessage extends FixMessage {

	public HandshakeMessage() {
		super(TypeOption.MessageType.Handshake);
	}

	public HandshakeMessage(Option[] options) {
		super(TypeOption.MessageType.Handshake, options);
		System.out.println("Handshake message generated from options : ID : " + getOption(eOption.ID));
	}

	@Override
	public eOption[] requiredOptions() {
		return new eOption[0];
	}

}