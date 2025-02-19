package dataaccess.localImplementation;
import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import network.dataModels.AuthData;

import java.util.TreeMap;

public class MemoryAuthDAO  implements AuthDAO{

    MockDatabase db;

    public MemoryAuthDAO(MockDatabase db){
        this.db=db;
    }
    @Override
    public void createAuth(AuthData data) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(AuthData data) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException{
        if(db==null){
            throw new DataAccessException("Database can't be accessed");
        }
        db.setAuthTokens(new TreeMap<>());
    }
}
