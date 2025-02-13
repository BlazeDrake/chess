package Service;

import dataaccess.localImplementation.MockDatabase;

public class ListGamesService {
    MockDatabase db;
    public ListGamesService(MockDatabase db){
        this.db=db;
    }
}
