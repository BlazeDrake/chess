package Handler;

import dataaccess.localImplementation.MockDatabase;

public class LoginHandler {
    MockDatabase db;
    public LoginHandler(MockDatabase db){
        this.db=db;
    }
}
