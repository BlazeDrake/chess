package Service;

import dataaccess.DataAccessException;
import dataaccess.interfaces.DeletionDAO;
import dataaccess.localImplementation.MemoryDeletionDAO;
import dataaccess.localImplementation.MockDatabase;

public class ClearService {
    MockDatabase db;
    public DeletionDAO deletionDAO;
    public ClearService(MockDatabase db){
        this.db=db;
        deletionDAO = new MemoryDeletionDAO(db);
    }

    public void clear() throws DataAccessException {
        deletionDAO.clear();
    }
}
