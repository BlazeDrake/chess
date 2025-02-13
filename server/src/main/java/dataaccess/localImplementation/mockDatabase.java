package dataaccess.localImplementation;

import dataaccess.interfaces.*;
import network.dataModels.AuthData;
import network.dataModels.GameData;
import network.dataModels.UserData;

import java.util.Dictionary;

/**
 * A class for mocking a database, storing the data in memory. FOr use with local implementations of the DAO interface
 */
public class mockDatabase {
    //Key is uuid to AuthData
    Dictionary<String, AuthData> authTokens;
    //Key is game id
    Dictionary<Integer, GameData> games;
    //Stores users by username
    Dictionary<String, UserData> users;
}
