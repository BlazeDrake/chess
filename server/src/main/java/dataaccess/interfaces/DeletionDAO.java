package dataaccess.interfaces;

import dataaccess.DataAccessException;

public interface DeletionDAO{
    void clear() throws DataAccessException;
}
