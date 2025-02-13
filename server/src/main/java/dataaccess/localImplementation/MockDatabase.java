package dataaccess.localImplementation;

import network.dataModels.AuthData;
import network.dataModels.GameData;
import network.dataModels.UserData;

import java.util.TreeMap;

/**
 * A class for mocking a database, storing the data in memory. FOr use with local implementations of the DAO interface
 */
public class MockDatabase {
    //Key is uuid to AuthData
    TreeMap<String, AuthData> authTokens;
    //Key is game id
    TreeMap<Integer, GameData> games;

    //Stores users by username
    TreeMap<String, UserData> users;

    public MockDatabase(){
        reset();
    }

    public TreeMap<String, AuthData> getAuthTokens() {
        return new TreeMap<>(authTokens);
    }

    public TreeMap<Integer, GameData> getGames(){
        return new TreeMap<>(games);
    }
    public TreeMap<String, UserData> getUsers() {
        return new TreeMap<>(users);
    }

    public void setAuthTokens(TreeMap<String, AuthData> authTokens) {
        this.authTokens = authTokens;
    }

    public void setGames(TreeMap<Integer, GameData> games) {
        this.games = games;
    }

    public void setUsers(TreeMap<String, UserData> users) {
        this.users = users;
    }

    public void reset(){
        authTokens=new TreeMap<>();
        games = new TreeMap<>();
        users = new TreeMap<>();
    }
}
