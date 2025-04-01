package ui;

import websocket.messages.ServerMessage;

public class Printer {


    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void notify(ServerMessage notification) {
        //FIXME: Make it not interfere with username (perhaps by adding a newline, then reprinting?)
        System.out.println("\n" + EscapeSequences.SET_TEXT_COLOR_GREEN + notification.getMessage());
        printName();
    }

    public void printWelcome() {
        System.out.println("Welcome to Chess client!");
    }

    public void printError(String err) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + err);
    }

    public void printResponse(String response) {
        System.out.println(EscapeSequences.SET_TEXT_BOLD_AND_BLUE + response);
    }

    public void printName() {
        System.out.print(EscapeSequences.SET_TEXT_NORMAL_AND_WHITE + EscapeSequences.SET_TEXT_BOLD +
                "[" + username + "]: ");
    }
}