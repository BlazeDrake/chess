package dataaccess.interfaces;

import dataaccess.DataAccessException;

public interface DeletionDAO{
    //throws if data can't be accessed
    void clear() throws DataAccessException;
}
