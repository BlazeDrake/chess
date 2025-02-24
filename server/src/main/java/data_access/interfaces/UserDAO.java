package data_access.interfaces;

import data_access.DataAccessException;
import network.data_models.UserData;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;
    void createUser(UserData data)  throws DataAccessException;
    void clear() throws DataAccessException;
}
