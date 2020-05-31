package fix_me;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.ParseException;

import message.FixMessage;
import message.Option.eOption;
import message.TypeOption.MessageType;

public class App {
    public static void main(String[] args) {
        // PrintStream ps;
        // try {
        //     ps = new PrintStream("Log.txt");
        //     System.setOut(ps);
        // } catch (FileNotFoundException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        Router r = new Router();
        Market m = new Market();


        r.open();
        m.start();

    }
}
