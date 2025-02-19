package dataaccess.localImplementation;

import dataaccess.DataAccessException;
import dataaccess.interfaces.UserDAO;
import network.dataModels.UserData;

import java.util.TreeMap;

public class MemoryUserDAO implements UserDAO {

    MockDatabase db;

    public MemoryUserDAO(MockDatabase db){
        this.db=db;
    }


    @Override
    public UserData getUser(String username) {
        return db.getUsers().get(username);
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {

    }

    public void clear() throws DataAccessException{

        if(db==null){
            throw new DataAccessException("Database can't be accessed");
        }
        db.setUsers(new TreeMap<>());
    }
}
