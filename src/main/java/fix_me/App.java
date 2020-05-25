package fix_me;

public class App {
    public static void main(String[] args) {
        Router router = new Router();
        router.open();

        Market m = new Market();
        Broker b = new Broker();
        m.start();
        b.start();


    }
}
