package message;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import message.Option.eOption;
import message.TypeOption.MessageType;

public abstract class FixMessage {
	// TODO Fix message <-> String <-> NetWorker

	Option[] options;

	public FixMessage(MessageType t, Option ...opts){
		this.options = new Option[Option.eOption.values().length];



		if(opts != null && opts.length > 0)
			for(Option e: options)
				if(e != null)
					setOption(e);

		if(!hasOption(eOption.ID)) this.setOption(new IDOption());
		if(!hasOption(eOption.Type)) this.setOption(new TypeOption(t));
		if(!hasOption(eOption.Length)) this.setOption(new LengthOption());
		if(!hasOption(eOption.Checksum)) this.setOption(new ChecksumOption());
	}

	public void setOption(Option e){
		System.out.println("Message option: " + e.option);
		this.options[e.option.ordinal()] = e;
		e.setMessage(this);
	}


	public <T extends Option> T getOption(eOption type){
		Option t = options[type.ordinal()];
		return (T) t;
	}


	public void fromString(String s){
		String[] parts = s.split(";");
		for(String p:parts){
			String[] pieces = p.split("=");
			eOption opt = eOption.values()[Integer.parseInt(pieces[0])];	// Validate option ID
			options[opt.ordinal()].fromString(pieces[1]);
		}
	}


	public String toString(Option.eOption ...exclude){
		String ret = "";
		List<Option.eOption> opts = Arrays.asList(exclude);
		for(Option o: options)
			if(o != null && !opts.contains(o.option))
				ret += String.format("%s;", o);
		return ret;
	}

	public String toString(){
		String ret = "";
		for(Option o: options)
			if(o != null)
				ret += String.format("%s;", o);
		return ret;
	}

	public boolean validate(){

		for(Option o: options)
			if(o != null){
				if(!o.validate()){
					System.out.printf("Option failed: %s\n", o);
					return false;
				}
			}
		return true;
	}

	public boolean hasOption(eOption t){
		return options[t.ordinal()] != null;
	}

	public abstract eOption[] requiredOptions();

}