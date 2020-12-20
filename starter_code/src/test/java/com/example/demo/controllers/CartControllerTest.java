package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        User user = new User();
        Cart cart = new Cart();
        user.setId(0L);
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("Spring course");
        item.setPrice(BigDecimal.valueOf(43.5));
        item.setDescription("using spring with kotlin");
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
    }

    @Test
    public void addToCartHappyPath() {
        ModifyCartRequest req = new ModifyCartRequest();
        req.setItemId(1L);
        req.setQuantity(1);
        req.setUsername("test");

        ResponseEntity<Cart> res = cartController.addTocart(req);
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        Cart cart = res.getBody();
        assertNotNull(cart);
        assertEquals(BigDecimal.valueOf(43.5), cart.getTotal());
    }

    @Test
    public void addToCartInvalidUser() {
        ModifyCartRequest req = new ModifyCartRequest();
        req.setItemId(1L);
        req.setQuantity(1);
        req.setUsername("joe.doe");

        ResponseEntity<Cart> res = cartController.addTocart(req);
        assertNotNull(res);
        assertEquals(404, res.getStatusCodeValue());
    }

    @Test
    public void removeFromCartHappyPath() {
        ModifyCartRequest req = new ModifyCartRequest();
        req.setItemId(1L);
        req.setQuantity(2);
        req.setUsername("test");
        ResponseEntity<Cart> res = cartController.addTocart(req);
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());

        req = new ModifyCartRequest();
        req.setItemId(1L);
        req.setQuantity(1);
        req.setUsername("test");
        res = cartController.removeFromcart(req);
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        Cart cart = res.getBody();
        assertNotNull(cart);
        assertEquals(BigDecimal.valueOf(43.5), cart.getTotal());
    }

    @Test
    public void removeFromCartInvalidUser() {
        ModifyCartRequest req = new ModifyCartRequest();
        req.setItemId(1L);
        req.setQuantity(1);
        req.setUsername("joe.doe");
        ResponseEntity<Cart> res = cartController.removeFromcart(req);
        assertNotNull(res);
        assertEquals(404, res.getStatusCodeValue());
    }

    @Test
    public void  removeFromCartInvalidItem() {
        ModifyCartRequest req = new ModifyCartRequest();
        req.setItemId(2L);
        req.setQuantity(1);
        req.setUsername("test");
        ResponseEntity<Cart> res = cartController.removeFromcart(req);
        assertNotNull(res);
        assertEquals(404, res.getStatusCodeValue());
    }
}
