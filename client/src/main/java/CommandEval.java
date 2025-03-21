import chess.ChessGame;
import network.datamodels.UserData;
import network.requests.CreateGameRequest;
import network.requests.ListGamesRequest;
import server.ResponseException;
import server.ServerFacade;
import ui.EscapeSequences;

import java.util.ArrayList;
import java.util.Scanner;

public class CommandEval {
    private static final String loggedOutString = "LoggedOut";
    private ServerFacade facade;
    private boolean loggedIn;
    private String username;
    private String authToken;
    private ArrayList<Integer> gameIDList;

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
        String[] args = input.split(" ");
        return switch (args[0]) {
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
            case "register" -> {
                checkCount(args.length, 4);
                facade.register(new UserData(args[1], args[2], args[3]));
                yield "User " + args[1] + " successfully registered";
            }
            case "login" -> {
                checkCount(args.length, 3);
                var result = facade.login(new UserData(args[1], args[2], null));
                loggedIn = true;
                username = result.username();
                authToken = result.authToken();
                yield "Logged in successfully. Welcome " + result.username();
            }

            case "quit" -> "Quitting client. Goodbye";
            default ->
                    throw new ResponseException(400, "Error: Unknown Command " + input + ". Use help to see a list of commands");
        };
    }

    private String loggedInCommand(String input) throws ResponseException {
        String[] args = input.split(" ");
        return switch (args[0]) {
            case "help" -> "Logged in commands: \n" +
                    commandInfo("help",
                            "Displays this dialog",
                            "help") +
                    commandInfo("list",
                            "Lists info on all current games",
                            "list") +
                    commandInfo("create",
                            "Create a new game",
                            "create <gameName>") +
                    commandInfo("join",
                            "Join an existing game as a player",
                            "join <gameID> <team (WHITE | BLACK)>") +
                    commandInfo("observe",
                            "Observe an existing game",
                            "observe ,gameID>") +
                    commandInfo("logout",
                            "Log out.",
                            "logout");
            case "list" -> {
                var games = facade.listGames(new ListGamesRequest(authToken));
                gameIDList = new ArrayList<>();
                StringBuilder builder = new StringBuilder("Current Games:");
                int i = 0;
                for (var game : games.games()) {
                    i++;
                    builder.append("\n");
                    builder.append(EscapeSequences.SET_TEXT_BOLD_AND_BLUE);
                    builder.append(i);
                    builder.append(" - ");
                    builder.append(game.gameName());
                    builder.append(": ");
                    builder.append(EscapeSequences.RESET_TEXT_BOLD_FAINT);
                    gameIDList.add(game.gameID());

                    var whiteUser = game.whiteUsername();
                    if (whiteUser == null) {
                        builder.append("\n  White is unclaimed");
                    }
                    else {
                        builder.append("\n  White Player: ");
                        builder.append(whiteUser);
                    }
                    var blackUser = game.blackUsername();
                    if (blackUser == null) {
                        builder.append("\n  Black is unclaimed");
                    }
                    else {
                        builder.append("\n  Black Player: ");
                        builder.append(blackUser);
                    }
                }
                yield builder.toString();
            }
            case "create" -> {
                checkCount(args.length, 2);
                facade.createGame(new CreateGameRequest(authToken, args[1]));
                StringBuilder builder = new StringBuilder("Created Game:");
                builder.append("\n    Game name: ");
                builder.append(args[1]);
                yield builder.toString();
            }
            case "join" -> {
                yield "fixme";
            }
            case "observe" -> {
                yield "fixme";
            }
            case "logout" -> {
                facade.logout(authToken);
                username = loggedOutString;
                authToken = "";
                loggedIn = false;
                yield "Logged out successfully. Goodbye";
            }
            case "quit" -> throw new ResponseException(400, "Error: Must log out before quitting");
            default ->
                    throw new ResponseException(400, "Error: Unknown Command " + input + ". Use help to see a list of commands");
        };
    }

    private static void checkCount(int length, int expected) throws ResponseException {
        if (length != expected) {
            throw new ResponseException(400, "Error: Incorrect number of arguments; expected " + (expected - 1));
        }
    }

    private String commandInfo(String name, String info, String format) {
        return EscapeSequences.SET_TEXT_BOLD_AND_BLUE + name + "\n" +
                "   " + EscapeSequences.SET_TEXT_NORMAL_AND_WHITE + info + "\n" +
                "   format: " + EscapeSequences.SET_TEXT_COLOR_GREEN + format + "\n";
    }

    private void drawBoard(ChessGame game, ChessGame.TeamColor playerColor) {
        var board = game.getBoard();

    }

    private void handleError(ResponseException ex) {
        String msg = switch (ex.StatusCode()) {
            case 400 -> ex.getMessage();
            case 401 -> "Error: unauthorized";
            case 403 -> "Error: already taken";
            default -> "Internal error";
        };
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + msg);
    }
}
