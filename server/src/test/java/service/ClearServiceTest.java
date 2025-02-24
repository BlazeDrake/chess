package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.localimplementation.MockDatabase;
import network.data_models.AuthData;
import network.data_models.GameData;
import network.data_models.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

class ClearServiceTest {

    MockDatabase db;
    ClearService clearService;

    @BeforeEach
    void setup(){
        db=new MockDatabase();
        clearService = new ClearService(db);
    }
    @Test
    void clearValidEmpty() {
        runAssertions();
    }

    private void runAssertions() {
        try{
            clearService.clear();
            Assertions.assertEquals(0,db.getAuthTokens().size());
            Assertions.assertEquals(0,db.getGames().size());
            Assertions.assertEquals(0,db.getUsers().size());
        } catch (DataAccessException e) {
            Assertions.fail("Exception thrown with clearing");
        }
    }

    @Test
    void clearValidNonempty() {
        var auth = new TreeMap<String, AuthData>();
        auth.put("eeeeeee",new AuthData("eeeeeee","testUser"));
        db.setAuthTokens(auth);

        var games = new ArrayList<>(List.of(
             new GameData(5,"","","coolGame", new ChessGame())
        ));
        db.setGames(games);

        var users=new TreeMap<String, UserData>();
        users.put("user",new UserData("user","e","hi@email.com"));
        db.setUsers(users);

        runAssertions();
    }
    @Test
    void clearInvalid() {
        clearService = new ClearService(null);
        Assertions.assertThrows(DataAccessException.class,()->clearService.clear());
    }
}