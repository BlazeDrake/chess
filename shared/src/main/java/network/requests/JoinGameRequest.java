package network.requests;

public record JoinGameRequest(String authToken, String playerColor, int gameID) {
}
