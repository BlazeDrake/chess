package dataaccess.interfaces;

import dataaccess.DataAccessException;
import network.dataModels.UserData;

public interface UserDAO {
    int authorizeUser(String username, String password) throws DataAccessException;
    UserData getUser(String username);
    void createUser(UserData data)  throws DataAccessException;
}
