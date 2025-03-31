import chess.*;
import facade.websocket.NotificationHandler;
import facade.websocket.WebSocketFacade;
import network.datamodels.GameData;
import network.datamodels.UserData;
import network.requests.CreateGameRequest;
import network.requests.JoinGameRequest;
import network.requests.ListGamesRequest;
import network.ResponseException;
import facade.ServerFacade;
import ui.EscapeSequences;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class CommandEval {
    private static final String LOGGED_OUT_STRING = "LoggedOut";
    private static final String WHITE_COLS = EscapeSequences.ROW_COL_FORMAT + "    a   b   c  d   e  f   g  h     " + EscapeSequences.RESET_BG_COLOR;
    private static final String BLACK_COLS = EscapeSequences.ROW_COL_FORMAT + "    h   g   f  e   d  c   b  a     " + EscapeSequences.RESET_BG_COLOR;
    private ServerFacade facade;
    private State curState;
    private String username;
    private String authToken;
    private ChessGame.TeamColor curColor;
    private ChessGame curGame;
    private int curId;
    private ArrayList<Integer> gameIDList;

    private NotificationHandler notificationHandler;

    private WebSocketFacade ws;

    public CommandEval(ServerFacade facade, NotificationHandler notificationHandler) {
        this.facade = facade;
        this.notificationHandler = notificationHandler;
        gameIDList = new ArrayList<>();
    }

    public void run() {
        var scanner = new Scanner(System.in);
        String input;
        curState = State.LoggedOut;
        username = LOGGED_OUT_STRING;
        System.out.println("Welcome to Chess client!");
        do {
            System.out.print(EscapeSequences.SET_TEXT_NORMAL_AND_WHITE + EscapeSequences.SET_TEXT_BOLD +
                    "[" + username + "]: ");
            input = scanner.nextLine();
            try {
                String command = EscapeSequences.SET_TEXT_BOLD_AND_BLUE;

                command += switch (curState) {
                    case LoggedOut:
                        yield loggedOutCommand(input);
                    case LoggedIn:
                        yield loggedInCommand(input);
                    case Gameplay:
                        yield gameplayCommand(input);
                };

                System.out.println(command);
            } catch (ResponseException ex) {
                handleError(ex);
            }
        }
        while (curState != State.LoggedOut || !"quit".equals(input));
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
                curState = State.LoggedIn;
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
                    gameIDList.add(game.gameID());
                    displayGame(game, builder, i);
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

                ws = new WebSocketFacade(facade.getUrl(), notificationHandler);

                curId = getGameId(args[1]);

                if (colorStr.equals("WHITE")) {
                    curColor = ChessGame.TeamColor.WHITE;
                }
                else if (colorStr.equals("BLACK")) {
                    curColor = ChessGame.TeamColor.BLACK;
                }
                else {
                    throw new ResponseException(400, "Invalid team color. Must be WHITE or BLACK");
                }
                facade.joinGame(new JoinGameRequest(authToken, colorStr, curId));
                ws.joinGame(authToken, curId);
                curState = State.Gameplay;
                updateBoard();
                yield "now playing as " + colorStr + " in game " + curId + "\n" + drawBoard(curGame, curColor, null);
            }
            case "observe" -> {
                checkCount(args.length, 2);

                curId = getGameId(args[1]);


                curColor = ChessGame.TeamColor.WHITE;
                curState = State.Gameplay;
                updateBoard();
                yield "Now observing game " + curId + drawBoard(curGame, curColor, null);
            }
            case "logout" -> {
                facade.logout(authToken);
                username = LOGGED_OUT_STRING;
                authToken = "";
                curState = State.LoggedOut;
                yield "Logged out successfully. Goodbye";
            }
            case "quit" -> throw new ResponseException(400, "Error: Must log out before quitting");
            default ->
                    throw new ResponseException(400, "Error: Unknown Command " + input + ". Use help to see a list of commands");
        };
    }

    private String gameplayCommand(String input) throws ResponseException {
        String[] args = input.split(" ");
        return switch (args[0]) {
            case "help" -> "Gameplay commands: \n" +
                    commandInfo("help",
                            "Displays this dialog",
                            "help") +
                    commandInfo("redraw",
                            "Redraws the chess board",
                            "redraw") +
                    commandInfo("leave",
                            "Leaves the current game that's being played or observed",
                            "leave") +
                    commandInfo("move",
                            "Moves one of your pieces if it is your turn. Promotion is only used if the piece is a pawn and moves to the end of the board.",
                            "move <start column> <start row> <end column> <end row> [promotion: KNIGHT|BISHOP|ROOK|QUEEN]") +
                    commandInfo("resign",
                            "Forfeits the current game. Does not exit it.",
                            "resign") +
                    commandInfo("highlight",
                            "Highlights all legal moves for one of your pieces",
                            "highlight <column> <row>");
            case "redraw" -> {
                updateBoard();
                yield drawBoard(curGame, curColor, null);
            }
            case "leave" -> {
                String colorStr = curColor.name();
                //Websocket stuff, update database from websocket
                curState = State.LoggedIn;
                yield "Successfully left game";
            }
            case "move" -> {
                //FIXME: Make it so it doesn't work for observers with websocket!
                ChessPiece.PieceType promotion = null;
                checkCount(args.length, 5, 6);
                if (curColor != curGame.getTeamTurn()) {
                    throw new ResponseException(400, "Error: It is not your turn");
                }
                if (args.length == 6) {
                    var pieceStr = args[5].toLowerCase(Locale.ROOT);
                    promotion = switch (pieceStr) {
                        case "knight" -> ChessPiece.PieceType.KNIGHT;
                        case "rook" -> ChessPiece.PieceType.ROOK;
                        case "bishop" -> ChessPiece.PieceType.BISHOP;
                        case "queen" -> ChessPiece.PieceType.QUEEN;
                        default ->
                                throw new ResponseException(400, "Error: Invalid promotion piece type; must be knight, rook, bishop, or queen");
                    };
                }
                var startPos = new ChessPosition(Integer.parseInt(args[2]), colToInt(args[1]));
                var endPos = new ChessPosition(Integer.parseInt(args[4]), colToInt(args[3]));
                try {
                    curGame.makeMove(new ChessMove(startPos, endPos, null));
                } catch (InvalidMoveException ex) {
                    throw new ResponseException(400, ex.getMessage());
                }
                yield drawBoard(curGame, curColor, null);
            }
            case "resign" -> "IMPLEMENT ME";
            case "highlight" -> {
                checkCount(args.length, 3);
                boolean[][] highlightedPos = new boolean[8][8];
                var startPos = new ChessPosition(Integer.parseInt(args[2]), colToInt(args[1]));
                var validMoves = curGame.validMoves(startPos);
                if (validMoves != null) {
                    for (var move : validMoves) {
                        var pos = move.getEndPosition();
                        highlightedPos[pos.getRow() - 1][pos.getColumn() - 1] = true;
                    }
                }

                yield drawBoard(curGame, curColor, highlightedPos);
            }
            case "logout" -> throw new ResponseException(400, "Error: Must leave game out before logging out");
            case "quit" -> throw new ResponseException(400, "Error: Must log out before quitting");
            default ->
                    throw new ResponseException(400, "Error: Unknown Command " + input + ". Use help to see a list of commands");
        };
    }

    private static void displayGame(GameData game, StringBuilder builder, int i) {
        builder.append("\n");
        builder.append(EscapeSequences.SET_TEXT_BOLD_AND_BLUE);
        builder.append(i);
        builder.append(" - ");
        builder.append(game.gameName());
        builder.append(": ");
        builder.append(EscapeSequences.RESET_TEXT_BOLD_FAINT);

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

    private static void checkCount(int length, int first, int second) throws ResponseException {
        if (length != first && length != second) {
            throw new ResponseException(400, "Error: Incorrect number of arguments; expected " + (first - 1) + (" or " + (second - 1)));
        }
    }

    private String commandInfo(String name, String info, String format) {
        return EscapeSequences.SET_TEXT_BOLD_AND_BLUE + name + "\n" +
                "   " + EscapeSequences.SET_TEXT_NORMAL_AND_WHITE + info + "\n" +
                "   format: " + EscapeSequences.SET_TEXT_COLOR_GREEN + format + "\n";
    }

    private int colToInt(String col) throws ResponseException {
        return switch (col.toLowerCase(Locale.ROOT)) {
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default -> throw new ResponseException(400, "Error: Invalid column. Must be from a to h");
        };
    }

    private void updateBoard() {
        //update curGame;
        curGame = new ChessGame();
    }

    private String drawBoard(ChessGame game, ChessGame.TeamColor playerColor, boolean[][] highlightedPos) {
        var board = game.getBoard();
        StringBuilder builder = new StringBuilder();
        boolean highlight = highlightedPos != null;
        //Print column letters
        if (playerColor == ChessGame.TeamColor.BLACK) {
            //black perspective
            builder.append(BLACK_COLS);
            builder.append("\n");
            for (int i = 1; i <= 8; i++) {
                printRow(board, i, builder, -1, 9, highlight ? highlightedPos[i - 1] : null);
            }
            builder.append(BLACK_COLS);

        }
        else {
            //white perspective
            builder.append(WHITE_COLS);
            builder.append("\n");
            for (int i = 8; i >= 1; i--) {
                printRow(board, i, builder, 1, 0, highlight ? highlightedPos[i - 1] : null);
            }
            builder.append(WHITE_COLS);
        }

        return builder.toString();
    }

    private void printRow(ChessBoard board, int row, StringBuilder builder, int jMult, int jOffset, boolean[] highlighted) {
        builder.append(EscapeSequences.ROW_COL_FORMAT);
        builder.append(" ");
        builder.append(row);
        builder.append(" ");
        builder.append(EscapeSequences.RESET_TEXT_BOLD_FAINT);
        for (int j = 1; j <= 8; j++) {
            boolean isDarkSquare = (row + j) % 2 == 0;
            int col = jOffset + (jMult * j);
            if (highlighted != null && highlighted[col - 1]) {
                //green is highlighted
                builder.append(isDarkSquare ? EscapeSequences.SET_BG_COLOR_DARK_GREEN : EscapeSequences.SET_BG_COLOR_LIGHTER_GREEN);
            }
            else {
                //normal color
                builder.append(isDarkSquare ? EscapeSequences.SET_BG_COLOR_BROWN : EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            }
            var piece = board.getPiece(new ChessPosition(row, col));
            builder.append(printPiece(piece));
        }
        builder.append(EscapeSequences.ROW_COL_FORMAT);
        builder.append(" ");
        builder.append(row);
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
        String msg = switch (ex.statusCode()) {
            case 400 -> ex.getMessage();
            case 401 -> "Error: unauthorized";
            case 403 -> "Error: already taken";
            default -> "Internal error";
        };
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + msg);
    }


    private enum State {
        LoggedOut,
        LoggedIn,
        Gameplay
    }
}
