package dataaccess.localImplementation;

import dataaccess.DataAccessException;
import dataaccess.interfaces.UserDAO;
import network.dataModels.UserData;

public class MemoryUserDAO implements UserDAO {

    MockDatabase db;

    public MemoryUserDAO(MockDatabase db){
        this.db=db;
    }

    @Override
    public int authorizeUser(String username, String password) throws DataAccessException {
        return 0;
    }

    @Override
    public UserData getUser(String username) {
        return db.getUsers().get(username);
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {

    }
}
