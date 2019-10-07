//package com.fdmgroup.mockitoshaven;
//
//import org.junit.Test;
//
//import com.fdmgroup.mockitoshaven.dataaccess.MockitosHavenDao;
//import com.fdmgroup.mockitoshaven.dataaccess.MockitosHavenJpaDao;
//import com.fdmgroup.mockitoshaven.game.Stats;
//import com.fdmgroup.mockitoshaven.game.character.NonPlayerCharacter;
//import com.fdmgroup.mockitoshaven.game.character.NpcCategory;
//import com.fdmgroup.mockitoshaven.game.character.NpcType;
//import com.fdmgroup.mockitoshaven.game.dungeon.Coordinate;
//import com.fdmgroup.mockitoshaven.game.items.EquipableItem;
//import com.fdmgroup.mockitoshaven.game.items.EquipmentSlot;
//import com.fdmgroup.mockitoshaven.game.items.Inventory;
//
//public class DaoTest {
//	
//	
//	@Test
//	public void test_ifDaoCanWriteNPC() {
//		MockitosHavenDao dao = new MockitosHavenJpaDao();
//		NonPlayerCharacter character = new NonPlayerCharacter();
//		character.setCoordinate(new Coordinate(5, 6, "0"));
//		character.setImageName("UnitTest.png");
//		character.setDisplayName("Unit Test");
//		character.setIdentifier("UnitTest");
//		character.setInventory(new Inventory(20));
//		character.setType(new NpcType(NpcCategory.UNIT_TEST));
//		EquipableItem item = new EquipableItem(new Stats());
//		item.setSlot(EquipmentSlot.NECKLACE);
//		item.setIdentifier("Necklace");
//		character.addItem(item);
//		character.equipItem("Necklace");
//		
//		dao.writeObject(character);
//		System.out.println("===========================");
//		System.out.println("===========================");
//		System.out.println("===========================");
//		System.out.println(dao.findAllFieldValuesByObject(NonPlayerCharacter.class, "type"));
//	}
//	
//
//	
//	
//
//}
