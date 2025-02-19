package Service;

import dataaccess.localImplementation.MockDatabase;
import network.requests.LoginRequest;
import network.results.LoginResult;

public class LoginService {
    MockDatabase db;
    public LoginService(MockDatabase db){
        this.db=db;
    }
    public LoginResult login(LoginRequest request){
        return null;
    }
}
