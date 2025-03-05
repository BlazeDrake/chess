package dataaccess;

import dataaccess.interfaces.UserDAO;
import network.datamodels.UserData;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
