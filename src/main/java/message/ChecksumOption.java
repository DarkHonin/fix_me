package message;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ChecksumOption extends Option {

	MessageDigest md;
	byte [] checksum;

	ChecksumOption() {
		super(eOption.Checksum);
		try {
			md = MessageDigest.getInstance("md5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	ChecksumOption(String value){
		super(eOption.Checksum);
		try {
			md = MessageDigest.getInstance("md5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		System.out.println("Checksum decoding: "+value);
		decode(value);
		System.out.println("Checksum decoded: "+ hexString(checksum));
	}

	String hexString(byte[] bytes){
		String ret = "";
		for(byte b : bytes) ret += String.format("%X", b);
		if(ret.length() % 2 != 0)
			ret = "0" + ret;
		return ret;
	}

	byte[] fromHex(String s){
		byte[]	checksum = new byte[s.length() / 2];
		for(int x = 0; x < checksum.length; x++){
			String pack = s.substring(x * 2, (x * 2) + 2);
			checksum[x] = (byte)Integer.parseInt(pack, 16);
		}
		return checksum;
	}

	public String getSeed(){
		return message().toString(eOption.Checksum);
	}

	@Override
	String encode() {
		return hexString(checksum);
	}

	@Override
	void decode(String e) {
		checksum = fromHex(e);
	}

	@Override
	boolean validate() {
		if(checksum == null){
			checksum = md.digest(getSeed().getBytes());
			return true;
		}
		String actual = hexString(md.digest(getSeed().getBytes()));
		String expected = hexString(checksum);

		return actual.equals(expected);
	}


}