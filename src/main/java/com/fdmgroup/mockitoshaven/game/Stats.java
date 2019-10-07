package com.fdmgroup.mockitoshaven.game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Stats {
	@Id
	private String identifier;
	private int health;
	private int maxHealth;
	private int stamina;
	private int mana;
	private int magicalPower;
	private int physicalPower;
	private int strength;
	private int observation;
	private int luck;
	private int intelligence;
	private int dexterity;
	private int speed;
	@Column(name="character_level")
	private int level;
	private int experience;
	private int experienceToNextLevel;
	private int experienceValue;
	private int experienceIncrement=10;
	public Stats() {
	}


	
	public Stats(int health, int stamina, int mana, int magicalPower, int physicalPower, int strength, int observation,
			int luck, int intelligence, int dexterity, int speed, int level, int experience, int experienceToNextLevel,
			int experienceValue) {
		super();
		this.health = health;
		this.stamina = stamina;
		this.mana = mana;
		this.magicalPower = magicalPower;
		this.physicalPower = physicalPower;
		this.strength = strength;
		this.observation = observation;
		this.luck = luck;
		this.intelligence = intelligence;
		this.dexterity = dexterity;
		this.speed = speed;
		this.level = level;
		this.experience = experience;
		this.experienceToNextLevel = experienceToNextLevel;
		this.experienceValue = experienceValue;
	}



	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		experienceToNextLevel+=level*experienceIncrement;
		experienceValue+=(level*experienceIncrement/2);
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getExperienceToNextLevel() {
		return experienceToNextLevel;
	}

	public void setExperienceToNextLevel(int experienceToNextLevel) {
		this.experienceToNextLevel = experienceToNextLevel;
	}

	public int getExperienceValue() {
		return experienceValue;
	}

	public void setExperienceValue(int experienceValue) {
		this.experienceValue = experienceValue;
	}

	public int getHealth() {
		return health;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String id) {
		this.identifier = id;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void setHealth(int health) {
		if (health < 0) {
			this.health = 0;
		} else {
			this.health = health;
		}
	}
	

	public int getMaxHealth() {
		return maxHealth;
	}



	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}



	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		if (mana < 0) {
			this.mana = 0;
		} else {
			this.mana = mana;
		}
	}

	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		if (stamina < 0) {
			this.stamina = 0;
		} else {
			this.stamina = stamina;
		}
	}

	public int getMagicalPower() {
		return magicalPower;
	}

	public void setMagicalPower(int magicalPower) {
		if (magicalPower < 0) {
			this.magicalPower = 0;
		} else {
			this.magicalPower = magicalPower;
		}
	}

	public int getPhysicalPower() {
		return physicalPower;
	}

	public void setPhysicalPower(int physicalPower) {
		if (physicalPower < 0) {
			this.physicalPower = 0;
		} else {
			this.physicalPower = physicalPower;
		}
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		if (strength < 0) {
			this.strength = 0;
		} else {
			this.strength = strength;
		}
	}

	public int getObservation() {
		return observation;
	}

	public void setObservation(int observation) {
		if (observation < 0) {
			this.observation = 0;
		} else {
			this.observation = observation;
		}
	}

	public int getLuck() {
		return luck;
	}

	public void setLuck(int luck) {
		if (luck < 0) {
			this.luck = 0;
		} else {
			this.luck = luck;
		}
	}

	public int getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(int intelligence) {
		if (intelligence < 0) {
			this.intelligence = 0;
		} else {
			this.intelligence = intelligence;
		}
	}

	public int getDexterity() {
		return dexterity;
	}

	public void setDexterity(int dexterity) {
		if (dexterity < 0) {
			this.dexterity = 0;
		} else {
			this.dexterity = dexterity;
		}
	}

	public void addStats(Stats stats) {
		
		
		this.strength += stats.getStrength();
		this.observation += stats.getObservation();
		this.luck += stats.getLuck();
		this.intelligence += stats.getIntelligence();
		this.dexterity += stats.getDexterity();
		this.speed += stats.getSpeed();
		this.level += stats.getLevel();
		this.experience += stats.getExperience();
		this.experienceToNextLevel += stats.getExperienceToNextLevel();
		this.experienceValue+= stats.getExperienceValue();
		this.maxHealth=stats.getMaxHealth();
		setMagicalPower(magicalPower + stats.getMagicalPower());
		setPhysicalPower(physicalPower + stats.getPhysicalPower());
		setHealth(health + stats.getHealth());
		setStamina(stamina + stats.getStamina());
		setMana(mana + stats.getMana());
		
			
	}

	public static Stats negateStats(Stats oldStat) {
		Stats negatedStats = new Stats();
		negatedStats.health = -oldStat.health;
		negatedStats.maxHealth = -oldStat.maxHealth;
		negatedStats.stamina = -oldStat.stamina;
		negatedStats.mana = -oldStat.mana;
		negatedStats.magicalPower = -oldStat.magicalPower;
		negatedStats.physicalPower = -oldStat.physicalPower;
		negatedStats.strength = -oldStat.strength;
		negatedStats.observation = -oldStat.observation;
		negatedStats.luck = -oldStat.luck;
		negatedStats.intelligence = -oldStat.intelligence;
		negatedStats.dexterity = -oldStat.dexterity;
		negatedStats.speed = -oldStat.speed;
		negatedStats.experience = -oldStat.experience;
		negatedStats.experienceToNextLevel = -oldStat.experienceToNextLevel;
		negatedStats.experienceValue = -oldStat.experienceValue;

		return negatedStats;
	}

	public static Stats copyStats(Stats oldStat) {
		Stats copiedStats = new Stats();
		copiedStats.health = oldStat.health;
		copiedStats.maxHealth = -oldStat.maxHealth;
		copiedStats.stamina = oldStat.stamina;
		copiedStats.mana = oldStat.mana;
		copiedStats.magicalPower = oldStat.magicalPower;
		copiedStats.physicalPower = oldStat.physicalPower;
		copiedStats.strength = oldStat.strength;
		copiedStats.observation = oldStat.observation;
		copiedStats.luck = oldStat.luck;
		copiedStats.intelligence = oldStat.intelligence;
		copiedStats.dexterity = oldStat.dexterity;
		copiedStats.speed = oldStat.speed;
		copiedStats.experience = oldStat.experience;
		copiedStats.experienceToNextLevel = oldStat.experienceToNextLevel;
		copiedStats.experienceValue = oldStat.experienceValue;

		return copiedStats;
	}

	@Override
	public String toString() {
		return "Stats [id=" + identifier + ", health=" + health + ", stamina=" + stamina + ", mana=" + mana + ", magicalPower="
				+ magicalPower + ", physicalPower=" + physicalPower + ", strength=" + strength + ", observation="
				+ observation + ", luck=" + luck + ", intelligence=" + intelligence + ", dexterity=" + dexterity
				+ ", speed=" + speed + "]";
	}


}
