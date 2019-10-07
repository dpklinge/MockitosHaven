package com.fdmgroup.mockitoshaven;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.fdmgroup.mockitoshaven.game.items.Inventory;
import com.fdmgroup.mockitoshaven.game.items.Item;

public class InventoryTest {

	@Test
	public void test_Inventory_CanAddItemToInventory() {
		int size = 5;
		Inventory inventory = new Inventory(size);
		Item item = mock(Item.class);
		when(item.getIdentifier()).thenReturn("sword");
		
		inventory.addItem(item);
		
		assertTrue(inventory.getItems().size() == 1);
	}
	
	@Test
	public void test_Inventory_CanAddMultipleItemsToInventory() {
		int size = 5;
		Inventory inventory = new Inventory(size);
		Item item = mock(Item.class);
		Item item2 = mock(Item.class);
		when(item.getIdentifier()).thenReturn("sword");
		when(item2.getIdentifier()).thenReturn("bow");
		
		inventory.addItem(item);
		inventory.addItem(item);
		
		assertTrue(inventory.getItems().size() == 2);
	}
	
	@Test
	public void test_Inventory_CanAddMultipleSameItemsToInventory() {
		int size = 5;
		Inventory inventory = new Inventory(size);
		Item item = new Item();
		Item item2 = new Item();
		String identifier = "sword";
		item.setIdentifier(identifier);
		item2.setIdentifier(identifier);
		
		inventory.addItem(item);
		inventory.addItem(item2);
		
		int expectedAmount=2;
		
		assertSame(expectedAmount, inventory.getItems().get(identifier));
	}
	
	@Test
	public void test_Inventory_addItem_CannotAddMoreItemsThanSizeOfInventory() {
		int iinventorySize = 5;
		Inventory inventory = new Inventory(iinventorySize);
		Item item = new Item();
		Item item2 = new Item();
		Item item3 = new Item();
		Item item4 = new Item();
		Item item5 = new Item();
		Item item6 = new Item();

		item.setIdentifier("Sword");
		item2.setIdentifier("Bow");
		item3.setIdentifier("Axe");
		item4.setIdentifier("Potion");
		item5.setIdentifier("Helmet");
		item6.setIdentifier("Ring");
		
		inventory.addItem(item);
		inventory.addItem(item2);
		inventory.addItem(item3);
		inventory.addItem(item4);
		inventory.addItem(item5);
		inventory.addItem(item6);
		
		int expectedAmount=5;
		
		assertSame(expectedAmount, inventory.getItems().size());
	}
	
	@Test
	public void test_Inventory_removeItem_successfullyRemovesItem() {
		int iinventorySize = 5;
		Inventory inventory = new Inventory(iinventorySize);
		Item item = new Item();
		Item item2 = new Item();
		Item item3 = new Item();
		Item item4 = new Item();
		Item item5 = new Item();

		item.setIdentifier("Sword");
		item2.setIdentifier("Bow");
		item3.setIdentifier("Axe");
		item4.setIdentifier("Potion");
		item5.setIdentifier("Helmet");
		
		inventory.addItem(item);
		inventory.addItem(item2);
		inventory.addItem(item3);
		inventory.addItem(item4);
		inventory.addItem(item5);
		
		inventory.removeItem(item, 1);
		
		int expectedAmount=1;
				
		assertSame(expectedAmount, inventory.getRemainingSize());
	}
	
	@Test
	public void test_Inventory_removeItem_successfullyRemovesItemOfSameID() {
		int iinventorySize = 5;
		Inventory inventory = new Inventory(iinventorySize);
		Item item = new Item();
		Item item2 = new Item();
		Item item3 = new Item();
		Item item4 = new Item();
		Item item5 = new Item();
		String identifier = "Sword";

		item.setIdentifier(identifier);
		item2.setIdentifier(identifier);
		item3.setIdentifier(identifier);
		item4.setIdentifier(identifier);
		item5.setIdentifier(identifier);
		
		inventory.addItem(item);
		inventory.addItem(item2);
		inventory.addItem(item3);
		inventory.addItem(item4);
		inventory.addItem(item5);
		
		System.out.println(inventory.getRemainingSize());
		inventory.removeItem(item, 1);
		System.out.println(inventory.getRemainingSize());
		
		int expectedAmount=1;
				
		assertSame(expectedAmount, inventory.getRemainingSize());
	}

}
