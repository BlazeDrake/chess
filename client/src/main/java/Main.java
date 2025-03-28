import facade.ServerFacade;
import facade.websocket.NotificationHandler;

public class Main {
    public static void main(String[] args) {
        var facade = new ServerFacade("http://localhost:8080");
        var eval = new CommandEval(facade, new NotificationHandler());
        eval.run();
    }
}