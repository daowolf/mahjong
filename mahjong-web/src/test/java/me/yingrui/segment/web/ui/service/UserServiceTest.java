package me.yingrui.segment.web.ui.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import me.yingrui.segment.web.Application;
import me.yingrui.segment.web.UsingFixtures;
import me.yingrui.segment.web.ui.model.User;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
public class UserServiceTest extends UsingFixtures {

    @Autowired
    private UserService userService;

    @Test
    public void should_add_user_in_database() {
        String firstName = uniq("Yingrui");
        String lastName = uniq("Feng");
        String email = uniq("a@b.com");

        User user = addUser(firstName, lastName, email);

        assert(user.getId() > 0);
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
    }

    @Test
    public void should_remove_user_by_id_from_database() {
        String firstName = uniq("Yingrui");
        String lastName = uniq("Feng");
        String email = uniq("a@b.com");

        User user = addUser(firstName, lastName, email);

        userService.removeUser(user.getId());
        User userInDatabase = userService.getUserById(user.getId());
        assertNull(userInDatabase);
    }

    @Test
    public void should_list_all_users() {
        List<User> list = userService.listUser();

        String user1FirstName = uniq("Yingrui");
        String user1LastName = uniq("Feng");
        String user1Email = uniq("a@b.com");
        String user2FirstName = uniq("Yingrui");
        String user2LastName = uniq("Feng");
        String user2Email = uniq("a@b.com");

        User user1 = addUser(user1FirstName, user1LastName, user1Email);
        User user2 = addUser(user2FirstName, user2LastName, user2Email);

        List<User> users = userService.listUser();
        int userCount = 0;
        for (User u : users) {
            if (equals(u, user1)) {
                userCount++;
            }
            if (equals(u, user2)) {
                userCount++;
            }
        }
        assertEquals(2, userCount);
    }

    @Test
    public void should_be_unique_user() {
        List<User> list = userService.listUser();

        String user1FirstName = uniq("Yingrui");
        String user1LastName = uniq("Feng");
        String email = uniq("a@b.com");
        String user2FirstName = uniq("Yingrui");
        String user2LastName = uniq("Feng");

        addUser(user1FirstName, user1LastName, email);
        try {
            addUser(user2FirstName, user2LastName, email);
            fail();
        } catch (DataIntegrityViolationException exception) {
        }
    }

    @Test
    public void should_get_user_by_email_from_database() {
        String firstName = uniq("Yingrui");
        String lastName = uniq("Feng");
        String email = uniq("a@b.com");

        User user = addUser(firstName, lastName, email);
        User userInDatabase = userService.getUserByEmail(user.getEmail());
        assertEquals(firstName, userInDatabase.getFirstName());
    }

    private boolean equals(User user, User other) {
        return other.getFirstName().equals(user.getFirstName()) && other.getLastName().equals(user.getLastName());
    }

    private User addUser(String firstName, String lastName, String email) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        userService.addUser(user);
        return user;
    }
}
