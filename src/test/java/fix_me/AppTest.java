package fix_me;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import org.junit.Test;

import fix_me.Router;
import sim.NetWorker;
import sim.NetCatchWorker;
import sim.NetCatchWorker.NetCatchAcceptor;
import sim.NetWorker.NetAcceptor;

/**
 * Unit test for simple App.
 */
public class AppTest {

    static class NetAcceptorMonitor implements NetAcceptor {

        NetAcceptor acceptor;
        AppTest tester;
        String label;

        NetAcceptorMonitor(String label, NetAcceptor ar, AppTest tester) {
            acceptor = ar;
            this.label = label;
            this.tester = tester;
        }

        @Override
        public void acceptMessage(NetWorker instance, String message) {
            System.out.printf("%s : Message recieved : %s\n", label, message);
            acceptor.acceptMessage(instance, message);
            instance.send("Rep:::" + message);
        }

        @Override
        public NetWorker getWorker() {
            return null;
        }
    }

    /**
     * Rigorous Test :-)
     */

    Router router;
    Market market;
    Broker broker;

    boolean marketConnected, Idrecieved, IdRespond, IdRespondRecieved;

    HashMap<NetAcceptorMonitor, NetWorker> workers;

    NetAcceptorMonitor mon, mon2;

    // @Test
    public void routerMessageRecieved() {
        marketConnected = Idrecieved = IdRespond = IdRespondRecieved = false;

        router = new Router();
        market = new Market();
        broker = new Broker();
        market.getWorker().setAcceptor(mon = new NetAcceptorMonitor("MarketMon", market, this));
        broker.getWorker().setAcceptor(mon2 = new NetAcceptorMonitor("BrokerMon", market, this));
        router.open();
        market.start();
        broker.start();

    }

    public void messageRecieved(){

    }


}
