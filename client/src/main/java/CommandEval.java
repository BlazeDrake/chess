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
            System.out.print(EscapeSequences.SET_TEXT_NORMAL_AND_WHITE + EscapeSequences.SET_TEXT_BOLD +
                    "[" + username + "]: ");
            input = scanner.nextLine();
            try {
                if (loggedIn) {
                    System.out.println(EscapeSequences.SET_TEXT_BOLD_AND_BLUE + loggedInCommand(input));
                }
                else {
                    System.out.println(EscapeSequences.SET_TEXT_BOLD_AND_BLUE + loggedOutCommand(input));
                }
            } catch (ResponseException ex) {
                handleError(ex);
            }
        }
        while (loggedIn || !"quit".equals(input));
    }

    private String loggedOutCommand(String input) throws ResponseException {
        return switch (input) {
            case "help" -> "Logged out commands: \n" +
                    commandInfo("help",
                            "Displays this dialog",
                            "help") +
                    commandInfo("login",
                            "Logs the user in. Will provide access to the List, Create, Join, and Observe commands."
                                    + "    Use help after logging in for more info.",
                            "login <username> <password>") +
                    commandInfo("register",
                            "Registers a new user.",
                            "register <username> <password> <email>") +
                    commandInfo("quit",
                            "Exits the client",
                            "quit");
            case "quit" -> "Quitting client. Goodbye";
            default -> throw new ResponseException(400, "Unknown Command " + input);
        };
    }

    private String loggedInCommand(String input) throws ResponseException {
        return switch (input) {
            case "help" -> EscapeSequences.SET_TEXT_BOLD_AND_BLUE +
                    "Logged in commands: \n";
            case "quit" -> throw new ResponseException(400, "Must log out before quitting");
            default -> throw new ResponseException(400, "Unknown Command " + input);
        };
    }

    private String commandInfo(String name, String info, String format) {
        return EscapeSequences.SET_TEXT_BOLD_AND_BLUE + name + "\n" +
                "   " + EscapeSequences.SET_TEXT_NORMAL_AND_WHITE + info + "\n" +
                "   format: " + EscapeSequences.SET_TEXT_COLOR_GREEN + format + "\n";
    }

    private void handleError(ResponseException ex) {
        String msg = switch (ex.StatusCode()) {
            case 400 -> "Error: unknown command. Type help to see a list of commands";
            case 401 -> "Error: unauthorized";
            case 403 -> "Error: already taken";
            default -> "Internal error";
        };
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + msg);
    }
}
