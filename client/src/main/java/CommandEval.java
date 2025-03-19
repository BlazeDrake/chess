import server.ResponseException;
import server.ServerFacade;
import ui.EscapeSequences;

import java.util.Scanner;

public class CommandEval {
    private static final String loggedOutString = "LoggedOut";
    private ServerFacade facade;
    private boolean loggedIn;
    private String username;

    public CommandEval(ServerFacade facade) {
        this.facade = facade;
    }

    public void run() {
        var scanner = new Scanner(System.in);
        String input;
        loggedIn = false;
        username = loggedOutString;
        System.out.println("Welcome to Chess client!");
        do {
            System.out.print(EscapeSequences.SET_TEXT_BOLD + "[" + username + "]: " +
                    EscapeSequences.SET_TEXT_BLINKING + EscapeSequences.RESET_TEXT_BOLD_FAINT);
            input = scanner.nextLine();
            try {
                if (loggedIn) {
                    System.out.println(loggedInCommand(input));
                }
                else {
                    System.out.println(loggedOutCommand(input));
                }
            } catch (ResponseException ex) {
                handleError(ex);
            }
        }
        while (loggedIn || !"quit".equals(input));
    }

    private String loggedInCommand(String input) throws ResponseException {
        return switch (input) {
            case "help" -> """
                    Logged in commands:
                    """;
            default -> throw new ResponseException(400, "Unknown Command " + input);
        };
    }

    private String loggedOutCommand(String input) throws ResponseException {
        return switch (input) {
            case "help" -> """
                    Logged out commands:
                    """;
            default -> throw new ResponseException(400, "Unknown Command " + input);
        };
    }

    private void handleError(ResponseException ex) {
        String msg = switch (ex.StatusCode()) {
            case 400 -> "Error: unknown command. Type help to see a list of commands";
            case 401 -> "Error: unauthorized";
            case 403 -> "Error: already taken";
            default -> "Internal error";
        };
        System.out.println(msg);
    }
}
