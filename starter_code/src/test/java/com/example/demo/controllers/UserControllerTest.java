package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);

        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);
        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.findByUsername("another")).thenReturn(null);
    }

    @Test
    public void createUserHappyPath() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("test");
        req.setPassword("testPassword");
        req.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(req);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void createUserPasswordMissmatch() {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("test");
        req.setPassword("1234");
        req.setConfirmPassword("12345");
        final ResponseEntity<User> response = userController.createUser(req);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void createUserInvalidPassword() {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("test");
        req.setPassword("123456");
        req.setConfirmPassword("123456");
        final ResponseEntity<User> response = userController.createUser(req);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findUserByUsernameHappyPath() {
        final ResponseEntity<User> res = userController.findByUserName("test");
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        User foundUser = res.getBody();
        assertNotNull(foundUser);
        assertEquals("test", foundUser.getUsername());
    }

    @Test
    public void findByUsernameNotFound() {
        final ResponseEntity<User> res = userController.findByUserName("joe.doe");
        assertNotNull(res);
        assertEquals(404, res.getStatusCodeValue());
    }

    @Test
    public void findByUserIdHappyPath() {
        final ResponseEntity<User> res = userController.findById(0L);
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        User foundUser = res.getBody();
        assertNotNull(foundUser);
        assertEquals(0L, foundUser.getId());
    }

    @Test
    public void findByUserIdNotFound() {
        final ResponseEntity<User> res = userController.findById(1L);
        assertNotNull(res);
        assertEquals(404, res.getStatusCodeValue());
    }
}
