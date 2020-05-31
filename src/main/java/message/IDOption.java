package message;

public class IDOption extends Option{

	int ID = -1;

	IDOption() {
		super(Option.eOption.ID);
	}

	IDOption(String id) {
		super(Option.eOption.ID);
		System.out.println("ID DECODE!!!");
		decode(id);
		System.out.println("ID SHOULD BE: " + id);
	}

	@Override
	public String encode() {
		return Integer.toString(ID);
	}

	@Override
	public void decode(String e) {
		ID = Integer.parseInt(e);
	}

	@Override
	boolean validate() {
		if(ID == -1){
			System.err.printf("ID option may not be less than 0 : %d\n", ID);
			return false;

		}
		return true;
	}

	public void setID(int id){
		ID = id;
	}

	public int getID(){
		return ID;
	}


}