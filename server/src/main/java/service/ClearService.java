package service;

import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import dataaccess.localimplementation.MemoryAuthDAO;
import dataaccess.localimplementation.MemoryGameDAO;
import dataaccess.localimplementation.MemoryUserDAO;


public class ClearService {
    private AuthDAO authDao;
    private GameDAO gameDAO;
    private UserDAO userDAO;

    public ClearService() {
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
