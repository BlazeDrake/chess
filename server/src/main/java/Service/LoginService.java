package Service;

import dataaccess.localImplementation.MockDatabase;

public class LoginService {
    MockDatabase db;
    public LoginService(MockDatabase db){
        this.db=db;
    }
}
