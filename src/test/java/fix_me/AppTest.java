package fix_me;

import org.junit.Test;

import message.FixMessage;
import message.Messages.HandshakeMessage;
import message.Option.eOption;
import message.TypeOption.MessageType;
/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void fixTest() {
        Router r = new Router();
        Market m = new Market();

        r.open();
        m.start();

    }



}
