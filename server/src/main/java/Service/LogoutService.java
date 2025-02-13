package Service;

import dataaccess.localImplementation.MockDatabase;

public class LogoutService {
    MockDatabase db;
    public LogoutService(MockDatabase db){
        this.db=db;
    }
}
