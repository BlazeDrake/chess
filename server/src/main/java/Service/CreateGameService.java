package Service;

import dataaccess.localImplementation.MockDatabase;

public class CreateGameService {
    MockDatabase db;
    public CreateGameService(MockDatabase db){
        this.db=db;
    }
}
