package service;

import data_access.DataAccessException;
import data_access.interfaces.AuthDAO;
import data_access.interfaces.GameDAO;
import data_access.interfaces.UserDAO;
import data_access.local_implementation.MemoryAuthDAO;
import data_access.local_implementation.MemoryGameDAO;
import data_access.local_implementation.MemoryUserDAO;
import data_access.local_implementation.MockDatabase;

public class ClearService {
    MockDatabase db;
    private AuthDAO authDao;
    private GameDAO gameDAO;
    private UserDAO userDAO;
    public ClearService(MockDatabase db){
        this.db=db;
        authDao = new MemoryAuthDAO(db);
        gameDAO = new MemoryGameDAO(db);
        userDAO = new MemoryUserDAO(db);
    }

    public void clear() throws DataAccessException {
        authDao.clear();
        gameDAO.clear();
        userDAO.clear();
    }
}
