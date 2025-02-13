package network.requests;

public record CreateGameRequest(String authToken, String gameName) {
}
