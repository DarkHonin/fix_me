package fix_me;

import java.io.IOException;

import dealers.Dealer;
import dealers.Router;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        Router r = new Router();
        Dealer d = new Dealer("localhost", 5000);
        Dealer q = new Dealer("localhost", 5001);
        Dealer f = new Dealer("localhost", 5000);
        r.open();
        d.open();
        q.open();
        f.open();
        System.out.println("Press any key to close");
        try {
            while(System.in.read() < 1);
            System.out.println("Closing ...");
            r.close();
            Dealer.closeAll();
            System.exit(0);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
