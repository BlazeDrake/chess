package Service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import dataaccess.localImplementation.MockDatabase;
import network.dataModels.AuthData;
import network.dataModels.GameData;
import network.requests.ListGamesRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import org.junit.jupiter.api.Assertions;

class ListGamesServiceTest {

    MockDatabase db;
    ListGamesService service;
    ArrayList<GameData> gamesList;
    AuthData auth;

    @BeforeEach
    void setup(){
        db = new MockDatabase();
        service = new ListGamesService(db);

        gamesList=new ArrayList<>(List.of(
                new GameData(0,"a","b","air",new ChessGame()),
                new GameData(1,"c","d","sick",new ChessGame()),
                new GameData(2,"e","f","low",new ChessGame())
        ));
        db.setGames(gamesList);

        auth = new AuthData("abc123","nightblood");
        var authTokens=new TreeMap<String,AuthData>();
        authTokens.put(auth.authToken(),auth);
        db.setAuthTokens(authTokens);
    }
    @Test
    void listGames_valid() throws DataAccessException {
        var result = service.listGames(new ListGamesRequest(auth.authToken()));
        Assertions.assertEquals((Collection<GameData>)gamesList,result.games());
    }

    @Test
    void listGames_invalid(){
        Assertions.assertThrows(UnauthorizedException.class,()->service.listGames(new ListGamesRequest("Odium")));
    }
}