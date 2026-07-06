/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author admin
 */
public class CartTest {
    
    public CartTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getProductID method, of class Cart.
     */
    @Test
    public void testGetProductID() {
        Cart instance = new Cart();
        assertEquals(0, instance.getProductID());
    }

    /**
     * Test of setProductID method, of class Cart.
     */
    @Test
    public void testSetProductID() {
        Cart instance = new Cart();
        instance.setProductID(12);
        assertEquals(12, instance.getProductID());
    }

    /**
     * Test of getProductName method, of class Cart.
     */
    @Test
    public void testGetProductName() {
        Cart instance = new Cart();
        assertNull(instance.getProductName());
    }

    /**
     * Test of setProductName method, of class Cart.
     */
    @Test
    public void testSetProductName() {
        Cart instance = new Cart();
        instance.setProductName("Laptop");
        assertEquals("Laptop", instance.getProductName());
    }

    /**
     * Test of getPrice method, of class Cart.
     */
    @Test
    public void testGetPrice() {
        Cart instance = new Cart();
        assertEquals(0.0, instance.getPrice(), 0.0);
    }

    /**
     * Test of setPrice method, of class Cart.
     */
    @Test
    public void testSetPrice() {
        Cart instance = new Cart();
        instance.setPrice(19.99);
        assertEquals(19.99, instance.getPrice(), 0.0);
    }

    /**
     * Test of getQuantity method, of class Cart.
     */
    @Test
    public void testGetQuantity() {
        Cart instance = new Cart();
        assertEquals(0, instance.getQuantity());
    }

    /**
     * Test of setQuantity method, of class Cart.
     */
    @Test
    public void testSetQuantity() {
        Cart instance = new Cart();
        instance.setQuantity(3);
        assertEquals(3, instance.getQuantity());
    }

    /**
     * Test of getAvailableStock method, of class Cart.
     */
    @Test
    public void testGetAvailableStock() {
        Cart instance = new Cart();
        assertEquals(0, instance.getAvailableStock());
    }

    /**
     * Test of setAvailableStock method, of class Cart.
     */
    @Test
    public void testSetAvailableStock() {
        Cart instance = new Cart();
        instance.setAvailableStock(25);
        assertEquals(25, instance.getAvailableStock());
    }
    
}
