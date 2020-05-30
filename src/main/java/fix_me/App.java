package fix_me;

import java.text.ParseException;

import fix_me.FixMessage.FixType;

public class App {
    public static void main(String[] args) {
        try {
            FixMessage f = new FixMessage("0=12;1=Buy;2=11;7=BF9B716B5F5BE0957555E8437F9E8B0;") {
                @Override
                Option[] getOptions() {
                    return new Option[] { Option.Instrument, Option.Market, Option.Price };
                }
            };
            f.print();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Router router = new Router();
        // router.open();

        // Market m = new Market();
        // Broker b = new Broker();
        // m.start();
        // b.start();


    }
}
