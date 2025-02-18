package Handler;

import Service.ListGamesService;
import dataaccess.localImplementation.MockDatabase;

public class ListGamesHandler {
    MockDatabase db;
    ListGamesService service;
    public ListGamesHandler(MockDatabase db){
        this.db=db;
        this.service=new ListGamesService(db);
    }
}
