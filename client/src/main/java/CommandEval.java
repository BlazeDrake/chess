import chess.*;
import network.datamodels.UserData;
import network.requests.CreateGameRequest;
import network.requests.JoinGameRequest;
import network.requests.ListGamesRequest;
import server.ResponseException;
import server.ServerFacade;
import ui.EscapeSequences;

import java.util.ArrayList;
import java.util.Scanner;

public class CommandEval {
    private static final String LOGGED_OUT_STRING = "LoggedOut";
    private static final String WHITE_COLS = EscapeSequences.ROW_COL_FORMAT + "    a   b   c  d   e  f   g  h     " + EscapeSequences.RESET_BG_COLOR;
    private static final String BLACK_COLS = EscapeSequences.ROW_COL_FORMAT + "    h   g   f  e   d  c   b  a     " + EscapeSequences.RESET_BG_COLOR;
    private ServerFacade facade;
    private boolean loggedIn;
    private String username;
    private String authToken;
    private ArrayList<Integer> gameIDList;

    public CommandEval(ServerFacade facade) {
        this.facade = facade;
        gameIDList = new ArrayList<>();
    }

    public void run() {
        var scanner = new Scanner(System.in);
        String input;
        loggedIn = false;
        username = LOGGED_OUT_STRING;
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
                checkCount(args.length, 3);
                var colorStr = args[2].toUpperCase();
                ChessGame.TeamColor color;

                int id = getGameId(args[1]);

                if (colorStr.equals("WHITE")) {
                    color = ChessGame.TeamColor.WHITE;
                }
                else if (colorStr.equals("BLACK")) {
                    color = ChessGame.TeamColor.BLACK;
                }
                else {
                    throw new ResponseException(400, "Invalid team color. Must be WHITE or BLACK");
                }
                facade.joinGame(new JoinGameRequest(authToken, colorStr, id));
                yield drawBoard(new ChessGame(), color);
            }
            case "observe" -> {
                checkCount(args.length, 2);

                int id = getGameId(args[1]);
                yield drawBoard(new ChessGame(), ChessGame.TeamColor.WHITE);
            }
            case "logout" -> {
                facade.logout(authToken);
                username = LOGGED_OUT_STRING;
                authToken = "";
                loggedIn = false;
                yield "Logged out successfully. Goodbye";
            }
            case "quit" -> throw new ResponseException(400, "Error: Must log out before quitting");
            default ->
                    throw new ResponseException(400, "Error: Unknown Command " + input + ". Use help to see a list of commands");
        };
    }

    private int getGameId(String input) throws ResponseException {
        try {
            int inputId = Integer.parseInt(input) - 1;
            if (inputId < 0 || inputId >= gameIDList.size()) {
                throw new ResponseException(400, "Unknown game ID. Use list to see all games with their ID");
            }
            return gameIDList.get(inputId);
        } catch (NumberFormatException e) {
            throw new ResponseException(400, "Invalid game ID: Must be a number");
        }
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

    private String drawBoard(ChessGame game, ChessGame.TeamColor playerColor) {
        var board = game.getBoard();
        StringBuilder builder = new StringBuilder();
        //Print column letters
        if (playerColor == ChessGame.TeamColor.BLACK) {
            //black perspective
            builder.append(BLACK_COLS);
            builder.append("\n");
            for (int i = 1; i <= 8; i++) {
                printRow(board, i, builder, -1, 9);
            }
            builder.append(BLACK_COLS);

        }
        else {
            //white perspective
            builder.append(WHITE_COLS);
            builder.append("\n");
            for (int i = 8; i >= 1; i--) {
                printRow(board, i, builder, 1, 0);
            }
            builder.append(WHITE_COLS);
        }

        return builder.toString();
    }

    private void printRow(ChessBoard board, int i, StringBuilder builder, int jMult, int jOffset) {
        builder.append(EscapeSequences.ROW_COL_FORMAT);
        builder.append(" ");
        builder.append(i);
        builder.append(" ");
        for (int j = 1; j <= 8; j++) {
            builder.append((i + j) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_BROWN : EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            var piece = board.getPiece(new ChessPosition(i, jOffset + (jMult * j)));
            builder.append(printPiece(piece));
        }
        builder.append(EscapeSequences.ROW_COL_FORMAT);
        builder.append(" ");
        builder.append(i);
        builder.append(" ");
        builder.append(EscapeSequences.RESET_BG_COLOR);
        builder.append("\n");
    }

    private String printPiece(ChessPiece piece) {

        if (piece == null) {
            return EscapeSequences.EMPTY;
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return EscapeSequences.SET_TEXT_COLOR_WHITE + switch (piece.getPieceType()) {
                case KING -> EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.WHITE_QUEEN;
                case BISHOP -> EscapeSequences.WHITE_BISHOP;
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case ROOK -> EscapeSequences.WHITE_ROOK;
                case PAWN -> EscapeSequences.WHITE_PAWN;
            };
        }
        else {
            return EscapeSequences.SET_TEXT_COLOR_BLACK + switch (piece.getPieceType()) {
                case KING -> EscapeSequences.BLACK_KING;
                case QUEEN -> EscapeSequences.BLACK_QUEEN;
                case BISHOP -> EscapeSequences.BLACK_BISHOP;
                case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                case ROOK -> EscapeSequences.BLACK_ROOK;
                case PAWN -> EscapeSequences.BLACK_PAWN;
            };
        }
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
