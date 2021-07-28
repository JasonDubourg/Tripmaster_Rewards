package com.tripmaster.rewards;

import com.tripmaster.rewards.repository.UserDatabase;
import com.tripmaster.rewards.user.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserDatabaseTest {

    @Test
    public void setAndInitializeTheInternalUsers() {
        UserDatabase userDatabase = new UserDatabase();
        UserDatabase.setInternalUserNumber(3);
        userDatabase.initializeUserDatabase();
        List<User> allUsers = userDatabase.getAllUsers();
        assertTrue(allUsers.size() > 0);
    }

    @Test
    public void addUser() {
        UserDatabase userDatabase = new UserDatabase();
        UserDatabase.setInternalUserNumber(3);
        userDatabase.initializeUserDatabase();
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");
        userDatabase.addUser(user2);
        User retrievedUser = userDatabase.getUser(user2.getUserName());
        assertEquals(user2, retrievedUser);
    }

    @Test
    public void getAllUsers() {
        UserDatabase userDatabase = new UserDatabase();
        UserDatabase.setInternalUserNumber(3);
        userDatabase.initializeUserDatabase();
        List<User> allUsers = userDatabase.getAllUsers();
        assertTrue(allUsers.size() > 2);
    }


}
