package com.fdmgroup.mockitoshaven.game.logic;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fdmgroup.mockitoshaven.game.Stats;
import com.fdmgroup.mockitoshaven.game.character.GameCharacter;

public class CombatLogic {

	private static Logger logger = LogManager.getLogger();

	public static void performPhysicalAttack(GameCharacter attacker, GameCharacter defender) {
		if(!attacker.isReadyToAttack()) {
			return;
		}
		logger.trace(attacker.getIdentifier()+" attacks "+defender.getIdentifier());
		Stats statsOfAttacker = attacker.getStats();
		Stats statsOfDefender = defender.getStats();
		Random rand = new Random();
		attacker.startAttackCooldown();

		double damage = statsOfAttacker.getPhysicalPower();
		damage += statsOfAttacker.getStrength()/2;
		double damageRange=0.4;
		damage=(damage*(1-(damageRange/2) + rand.nextDouble()*damageRange));

		int healthOfDefender = statsOfDefender.getHealth();

		int randomNum = rand.nextInt(101);
		logger.trace("Random roll:" +randomNum);
		logger.trace("Defender dexterity: "+statsOfDefender.getDexterity());

		if (statsOfDefender.getDexterity() >= randomNum) {
			logger.info(defender.getDisplayName() + " has thwarted " + attacker.getDisplayName()
					+ "'s attempt to body him. SKRRRT");
		} else if (statsOfAttacker.getLuck() >= randomNum) {
			double criticalAttackDecimals = (damage * 1.5);

			int criticalAttack = (int) criticalAttackDecimals;

			logger.info("CRITICAL ATTACK. RKO OUT OF NOWHERE. " + criticalAttack);

			statsOfDefender.setHealth(healthOfDefender - criticalAttack);
		} else {
			logger.info("Devastating Blow! to the head..." + damage);

			statsOfDefender.setHealth(healthOfDefender - (int)damage);
		}

	}

	public static void performMagicalAttack(GameCharacter attacker, GameCharacter defender) {

		Stats statsOfAttacker = attacker.getStats();
		Stats statsOfDefender = defender.getStats();
		Random rand = new Random();

		int attackerMagicalPower = statsOfAttacker.getMagicalPower();

		int healthOfDefender = statsOfDefender.getHealth();

		int randomNum = rand.nextInt(101);

		if (statsOfAttacker.getMana() >= 10) {
			if (statsOfDefender.getDexterity() <= randomNum) {
				logger.info("dodged");
			} else if (statsOfAttacker.getLuck() <= randomNum) {
				double criticalAttackDecimals = (attackerMagicalPower * 1.5);

				int criticalAttack = (int) criticalAttackDecimals;

				logger.info("Critical Attack - magical");

				statsOfDefender.setHealth(healthOfDefender - criticalAttack);
			} else {
				logger.info("Successful Magical Attack");

				statsOfDefender.setHealth(healthOfDefender - attackerMagicalPower);
			}

		}
	}
}
