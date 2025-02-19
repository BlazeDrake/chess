package Service;

import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import dataaccess.localImplementation.MemoryAuthDAO;
import dataaccess.localImplementation.MemoryGameDAO;
import dataaccess.localImplementation.MemoryUserDAO;
import dataaccess.localImplementation.MockDatabase;

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
