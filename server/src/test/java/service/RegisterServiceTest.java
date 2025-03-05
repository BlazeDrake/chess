package service;

import dataaccess.DataAccessException;
import dataaccess.SQLUserDAO;
import dataaccess.TakenException;
import network.datamodels.UserData;
import network.requests.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {
    RegisterService service;

    @BeforeEach
    void setUp() {
        service = new RegisterService();
    }

    @Test
    public void testAddUser() {
        var usersToAdd = new ArrayList<UserData>(List.of(
                new UserData("username", "password", "email@test,com"),
                new UserData("next", "cool", "email@test,com"),
                new UserData("final", "password", "evil@nightblood,com")
        ));
        try {
            for (var user : usersToAdd) {
                service.register(new RegisterRequest(user));

            }
            var testDao = new SQLUserDAO();
            //Check to ensure lists line up
            for (var user : usersToAdd) {
                assertNotNull(testDao.getUser(user.username()));
            }
        } catch (DataAccessException e) {
            fail("Unexpect ed error: " + e.getMessage());
        }
    }

    @Test
    public void testTakenUsername() {

        var user1 = new UserData("username", "password", "email@test,com");
        var user2 = new UserData("username", "cool", "email@test,com");
        try {
            service.register(new RegisterRequest(user1));
        } catch (DataAccessException e) {
            fail("Unexpected error: " + e.getMessage());
        }

        assertThrows(TakenException.class, () -> service.register(new RegisterRequest(user2)));
    }
}