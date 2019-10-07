package com.fdmgroup.mockitoshaven;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fdmgroup.mockitoshaven.game.Stats;

public class StatsTest {

	@Test
	public void test_attributeFieldsWillNotFallBelowZero() {
		Stats stats = new Stats();
		int impossible = -5;
		
		stats.setHealth(impossible);
		int health = stats.getHealth();
		
		stats.setStamina(impossible);
		int stamina = stats.getHealth();
		
		stats.setMana(impossible);
		int mana = stats.getHealth();
		
		stats.setMagicalPower(impossible);
		int magicalPower = stats.getHealth();
		
		stats.setPhysicalPower(impossible);
		int physicalPower = stats.getHealth();
		
		stats.setStrength(impossible);
		int strength = stats.getHealth();
		
		stats.setObservation(impossible);
		int observation = stats.getHealth();
		
		stats.setLuck(impossible);
		int luck = stats.getHealth();
		
		stats.setIntelligence(impossible);
		int intelligence = stats.getHealth();
		
		stats.setDexterity(impossible);
		int dexterity = stats.getDexterity();
		
		assertEquals(0, health);
		assertEquals(0, stamina);
		assertEquals(0, mana);
		assertEquals(0, magicalPower);
		assertEquals(0, physicalPower);
		assertEquals(0, strength);
		assertEquals(0, observation);
		assertEquals(0, luck);
		assertEquals(0, intelligence);
		assertEquals(0, dexterity);
	}
	

}
