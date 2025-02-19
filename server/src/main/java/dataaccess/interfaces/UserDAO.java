package dataaccess.interfaces;

import dataaccess.DataAccessException;
import network.dataModels.UserData;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;
    void createUser(UserData data)  throws DataAccessException;
    void clear() throws DataAccessException;
}
