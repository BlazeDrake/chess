package dataaccess.localImplementation;

import dataaccess.DataAccessException;
import dataaccess.interfaces.DeletionDAO;

public class MemoryDeletionDAO implements DeletionDAO {

    MockDatabase db;
    public MemoryDeletionDAO(MockDatabase db){
        this.db=db;
    }
    @Override
    public void clear() throws DataAccessException {
        if(db==null){
            throw new DataAccessException("Database can't be accessed");
        }
        else{
            db.reset();
        }
    }
}
