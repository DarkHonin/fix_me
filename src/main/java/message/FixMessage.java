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

public abstract class FixMessage {
	// TODO Fix message <-> String <-> NetWorker
	static MessageDigest md = null;




	private class Opt{
		Option O;
		String value;
		Opt(Option o, String value){
			O = o;
			this.value = value;
		}

		public String toString(){
			return String.format("%d=%s;", O.ordinal(), value);
		}
	};

	public static enum FixType {
		Handshake, Announce, Buy, Sell
	};

	HashMap<Option, Opt> messageTable;

	FixMessage(int ID, FixType type) {
		messageTable = new HashMap<Option, Opt>();
		if (md == null)
			try {
				md = MessageDigest.getInstance("md5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		setProperty("ID", Integer.toString(ID));
		setProperty("Type", type.name());
	}

	FixMessage(String message) throws ParseException {
		messageTable = new HashMap<Option, Opt>();
		if (md == null)
			try {
				md = MessageDigest.getInstance("md5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		String[] parts = message.split(";");
		for(String rawProp : parts){
			String[] pair = rawProp.split("=");
			int optID = Integer.valueOf(pair[0]);
			setProperty(Option.values()[optID], pair[1]);
		}
		if(!checkCheckSum())
			throw new ParseException("Checksums do not match", 0);
	}

	public void setProperty(Option i, String value){

		messageTable.put(i, new Opt(i, value));
	}

	public void setProperty(String name, String value) {
		Option i = Option.valueOf(name);
		setProperty(i, value);
	}

	private String genChecksum(String checksumData) {
		String ret = "";
		byte[] rawDigest = md.digest(checksumData.getBytes());
		for (byte e : rawDigest)
			ret += String.format("%X", e);
		return ret;
	}

	private boolean checkCheckSum(){
		List<Option> opts = Arrays.asList(Option.values());
		String raw = "";
		for(Option o : opts){
			if(o == Option.Checksum)
				continue;
			if(messageTable.containsKey(o))
				raw += messageTable.containsKey(o);
		}
		String sum = genChecksum(raw);
		return sum.equals(messageTable.get(Option.Checksum).value);

	}

	abstract Option[] getOptions();

	public void print(){
		for(Option o : Option.values()){
			if(messageTable.containsKey(o))
				System.out.printf("\t%s : %s\n", o.name(), messageTable.get(o).value);
		}
	}

	public String toString(){
		String ret = "";
		int len = 0;
		for(Option o : Option.values()){
			if(o == Option.Length)
				messageTable.put(Option.Length, new Opt(Option.Length, Integer.toString(len)));
			if(o == Option.Checksum){
				messageTable.put(Option.Checksum, new Opt(Option.Checksum, genChecksum(ret)));
			}

			if(messageTable.containsKey(o)){
				Opt t = messageTable.get(o);
				ret += t.toString();
				len = ret.length();
			}
		}
		return ret;
	}
}