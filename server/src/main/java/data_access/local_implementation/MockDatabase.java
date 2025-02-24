package data_access.local_implementation;

import network.data_models.AuthData;
import network.data_models.GameData;
import network.data_models.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * A class for mocking a database, storing the data in memory. FOr use with local implementations of the DAO interface
 */
public class MockDatabase {
    //Key is uuid to AuthData
    private TreeMap<String, AuthData> authTokens;
    //Key is game id
    private ArrayList<GameData> games;

    //Stores users by username
    private TreeMap<String, UserData> users;

    public MockDatabase(){
        reset();
    }

    public TreeMap<String, AuthData> getAuthTokens() {
        return new TreeMap<>(authTokens);
    }

    public ArrayList<GameData> getGames(){
        return new ArrayList<> (List.copyOf(games));
    }
    public TreeMap<String, UserData> getUsers() {
        return new TreeMap<>(users);
    }

    public void setAuthTokens(TreeMap<String, AuthData> authTokens) {
        this.authTokens = authTokens;
    }

    public void setGames(ArrayList<GameData> games) {
        this.games = games;
    }

    public void setUsers(TreeMap<String, UserData> users) {
        this.users = users;
    }

    public void reset(){
        authTokens=new TreeMap<>();
        games = new ArrayList<>();
        users = new TreeMap<>();
    }
}
