 package com.fdmgroup.mockitoshaven.game.items;

public enum EquipmentSlot {
	HELM("Helmet"), CHESTPLATE("Chest Plate"), PLATELEGS("Plate Legs"), BOOTS("Boots"), GLOVES ("Gloves"), NECKLACE("Necklace"), RING ("Ring"), //Equipable Armor
	ONEHANDEDWEAPON("One Handed Weapon"), TWOHANDEDWEAPON("Two Handed Weapon"), LEFT_HAND("Left hand"), RIGHT_HAND("Right hand");					//Equipable Weapons
	
	   private String slotName;
	   
	   private EquipmentSlot(String name) {
	     slotName = name;
	   }
	   
	   public String getFullName() {
	     return slotName;
	   }
}