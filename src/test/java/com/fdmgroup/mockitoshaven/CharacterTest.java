//package com.fdmgroup.mockitoshaven;
//
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.Test;
//
//import com.fdmgroup.mockitoshaven.game.Stats;
//import com.fdmgroup.mockitoshaven.game.character.GameCharacter;
//import com.fdmgroup.mockitoshaven.game.items.EquipableItem;
//import com.fdmgroup.mockitoshaven.game.items.EquipmentSlot;
//import com.fdmgroup.mockitoshaven.game.items.Inventory;
//import com.fdmgroup.mockitoshaven.game.items.Item;
//
//public class CharacterTest {
//	
//	//test_classname_methodname_WhatIsTesting_methodreturn
//
//	@Test
//	public void test_Character_isCharacterSlotEmpty_ChecksIfCharacterMapIsEmpty_ReturnsTrueorFalse() {
//		Stats stats = mock(Stats.class);
//		Inventory inventory = mock(Inventory.class);
//		
//		GameCharacter character = new GameCharacter(stats, inventory, new HashMap<EquipmentSlot, EquipableItem>());
//		
//		assertTrue(character.isEquipmentSlotEmpty(EquipmentSlot.BOOTS));
//	}
//	
//	@Test
//	public void test_Character_isCharacterSlotEmpty_ChecksCharacterMapForSlots_ReturnsTrueorFalse() {
//		Stats stats = mock(Stats.class);
//		Inventory inventory = mock(Inventory.class);
//		Map<EquipmentSlot, Item> equipment = new HashMap<EquipmentSlot, Item>();
//		
//		GameCharacter character = new GameCharacter(stats, inventory, equipment);
//		equipment = character.getEquipment();
//		
//		equipment.put(EquipmentSlot.BOOTS, new Item());
//		
//		assertTrue(!character.isEquipmentSlotEmpty(EquipmentSlot.BOOTS));
//	}
//	
//	@Test
//	public void test_Character_equipItem_ChecksIfEquipmentWasAdded_ReturnsNothing() {
//		String identifier = "_0001_Iron_Dagger";
//		
//		Stats stats = mock(Stats.class);
//		Inventory inventory = mock(Inventory.class);
//		EquipableItem item = mock(EquipableItem.class);
//		Map<EquipmentSlot, Item> equipment = new HashMap<EquipmentSlot, Item>();
//		GameCharacter character = new GameCharacter(stats, inventory, equipment);
//		
//		when(item.getSlot()).thenReturn(EquipmentSlot.CHESTPLATE);
//		when(inventory.getItemWithoutRemove(identifier)).thenReturn(item);
//		when(inventory.containsItem(identifier)).thenReturn(true);
//		
//		character.equipItem(identifier);
//		
//		assertTrue(! character.isEquipmentSlotEmpty(EquipmentSlot.CHESTPLATE));
//	}
//	
//	@Test
//	public void test_Character_equipItem_ChecksIfEquipmentCanBeRemoved_ReturnsNothing() {
//		String identifier = "_0001_Iron_Dagger";
//		
//		Stats stats = mock(Stats.class);
//		Inventory inventory = mock(Inventory.class);
//		EquipableItem item = mock(EquipableItem.class);
//		Map<EquipmentSlot, Item> equipment = new HashMap<EquipmentSlot, Item>();
//		GameCharacter character = new GameCharacter(stats, inventory, equipment);
//		
//		when(item.getSlot()).thenReturn(EquipmentSlot.CHESTPLATE);
//		when(inventory.getItemWithoutRemove(identifier)).thenReturn(item);
//		when(inventory.containsItem(identifier)).thenReturn(true);
//		when(item.getStats()).thenReturn(new Stats());
//		
//		character.equipItem(identifier);
//		
//		assertTrue(! character.isEquipmentSlotEmpty(EquipmentSlot.CHESTPLATE));
//		
//		character.unequipItem(EquipmentSlot.CHESTPLATE);
//		
//		assertTrue(character.isEquipmentSlotEmpty(EquipmentSlot.CHESTPLATE));
//	}
//	
//	@Test
//	public void test_Character_consumeItem_ChecksIfItemCanBeConsumed_ReturnsNothing() {
//		String identifier = "_0002_Green_Potion";
//		
//		Stats stats = mock(Stats.class);
//		Inventory inventory = mock(Inventory.class);
//		EquipableItem item = mock(EquipableItem.class);
//		Map<EquipmentSlot, Item> equipment = new HashMap<EquipmentSlot, Item>();
//		GameCharacter character = new GameCharacter(stats, inventory, equipment);
//		
//		 character.consumeItem(identifier);
//		 
//		 
//	}
//	
//}
