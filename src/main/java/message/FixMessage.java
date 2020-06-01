package message;

import java.util.Arrays;
import java.util.List;

import message.Option.eOption;

public class FixMessage {
	// TODO Fix message <-> String <-> NetWorker

	Option[] options = new Option[eOption.values().length];

	public FixMessage(){
		setOption(new IDOption());
		setOption(new TypeOption());
		setOption(new LengthOption());
		setOption(new ChecksumOption());
	}

	public FixMessage(String s){
		setOption(new IDOption());
		setOption(new TypeOption());
		setOption(new LengthOption());
		setOption(new ChecksumOption());
		fromString(s);
	}

	void setOption(Option o){
		options[o.option.ordinal()] = o;
		o.setMessage(this);
	}


	public <T extends Option> T getOption(eOption type){
		Object t = options[type.ordinal()];
		if(t instanceof Option)
			return (T) t;
		else
			return null;
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
		if(t.ordinal() >= options.length) return false;
		return options[t.ordinal()] != null;
	}
}