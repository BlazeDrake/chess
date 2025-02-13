package Handler;

import dataaccess.localImplementation.MockDatabase;

public class LogoutHandler {
    MockDatabase db;
    public LogoutHandler(MockDatabase db){
        this.db=db;
    }
}
