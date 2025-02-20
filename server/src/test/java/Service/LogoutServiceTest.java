package Service;

import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import dataaccess.localImplementation.MockDatabase;
import network.dataModels.AuthData;
import network.requests.LogoutRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class LogoutServiceTest {

    private MockDatabase db;
    private LogoutService service;
    @BeforeEach
    public void setup(){
        db = new MockDatabase();
        service= new LogoutService(db);

        var authTokens=new TreeMap<String, AuthData>();
        authTokens.put("abc123",new AuthData("abc123","syl"));
        authTokens.put("mmmmmm",new AuthData("mmmmmm","pattern"));
        db.setAuthTokens(authTokens);


    }
    @Test
    public void logoutValid() throws DataAccessException {

        service.logout(new LogoutRequest("abc123"));
        assertEquals(1,db.getAuthTokens().size());
        service.logout(new LogoutRequest("mmmmmm"));
        assertEquals(0,db.getAuthTokens().size());
    }

    @Test
    public void logoutInvalid() {
        assertThrows(UnauthorizedException.class, ()->service.logout(new LogoutRequest("airsick")));
    }
}