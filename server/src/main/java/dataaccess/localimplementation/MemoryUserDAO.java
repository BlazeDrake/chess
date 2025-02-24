package dataaccess.localimplementation;

import dataaccess.DataAccessException;
import dataaccess.interfaces.UserDAO;
import network.data_models.UserData;

import java.util.TreeMap;

public class MemoryUserDAO implements UserDAO {

    MockDatabase db;

    public MemoryUserDAO(MockDatabase db){
        this.db=db;
    }


    @Override
    public UserData getUser(String username) throws DataAccessException {
        if(db==null){
            throw new DataAccessException("Error: Database can't be accessed");
        }
        return db.getUsers().get(username);
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {
        if(db==null){
            throw new DataAccessException("Error: Database can't be accessed");
        }
        var curUsers=db.getUsers();
        curUsers.put(data.username(),data);
        db.setUsers(curUsers);
    }

    public void clear() throws DataAccessException{

        if(db==null){
            throw new DataAccessException("Error: Database can't be accessed");
        }
        db.setUsers(new TreeMap<>());
    }
}
