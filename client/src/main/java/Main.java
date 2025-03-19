import chess.*;
import server.Server;
import server.ServerFacade;

public class Main {
    public static void main(String[] args) {
        var server = new Server();
        server.run(8080);
        var facade = new ServerFacade("http://localhost:8080");
        var eval = new CommandEval(facade);
        eval.run();
        server.stop();
    }
}