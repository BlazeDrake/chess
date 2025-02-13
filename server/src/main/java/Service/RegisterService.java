package Service;

import dataaccess.localImplementation.MockDatabase;

public class RegisterService {
    MockDatabase db;
    public RegisterService(MockDatabase db){
        this.db=db;
    }
}
