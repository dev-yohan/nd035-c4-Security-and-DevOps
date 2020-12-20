package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
        Item item = new Item();
        item.setId(1L);
        item.setName("Udacity gift card");
        item.setPrice(BigDecimal.valueOf(150.50));
        item.setDescription("Giftcard to take a nanodegree");
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findByName("Udacity gift card")).thenReturn(Collections.singletonList(item));
    }

    @Test
    public void findAllItemsHappyPath() {
        ResponseEntity<List<Item>> res = itemController.getItems();
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        List<Item> items = res.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void getItemByIdHappyPath() {
        ResponseEntity<Item> res = itemController.getItemById(1L);
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        Item item = res.getBody();
        assertNotNull(item);
    }

    @Test
    public void getItemByIdNotFound() {
        ResponseEntity<Item> res = itemController.getItemById(2L);
        assertNotNull(res);
        assertEquals(404, res.getStatusCodeValue());
    }

    @Test
    public void getItemsByNameHappyPath() {
        ResponseEntity<List<Item>> res = itemController.getItemsByName("Udacity gift card");
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        List<Item> items = res.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void getItemsByNameNotFound() {
        ResponseEntity<List<Item>> res = itemController.getItemsByName("Coursera gift card");
        assertNotNull(res);
        assertEquals(404, res.getStatusCodeValue());
    }
}
