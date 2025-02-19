package Service;

import dataaccess.UnauthorizedException;
import dataaccess.localImplementation.MockDatabase;
import network.dataModels.UserData;
import network.requests.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {

    MockDatabase db;
    LoginService service;

    @BeforeEach
    void setup(){
        db=new MockDatabase();
        service=new LoginService(db);

        var userData=new TreeMap<String, UserData>();
        userData.put("nightblood",new UserData("nightblood","evil","ex@roshar.com"));
    }
    @Test
    void loginValid() {
        var request=new LoginRequest("nightblood","evil");
        var result=service.login(request);
        var authTokens=db.getAuthTokens();
        assertNotNull(result);
        assertNotNull(authTokens.get(result.authToken()));
    }
    @Test
    void loginInvalid_badInfo() {
        var request=new LoginRequest("nightblood","pancakes");
        assertThrows(UnauthorizedException.class,()->service.login(request));
    }
    @Test
    void loginInvalid_nonexistent() {
        var request=new LoginRequest("pattern","lies");
        assertThrows(UnauthorizedException.class,()->service.login(request));
    }
}