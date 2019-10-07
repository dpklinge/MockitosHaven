package com.fdmgroup.mockitoshaven.game.character;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fdmgroup.mockitoshaven.game.Game;
import com.fdmgroup.mockitoshaven.game.Stats;
import com.fdmgroup.mockitoshaven.game.dungeon.Coordinate;
import com.fdmgroup.mockitoshaven.game.dungeon.Locatable;
import com.fdmgroup.mockitoshaven.game.items.Consumable;
import com.fdmgroup.mockitoshaven.game.items.EquipableItem;
import com.fdmgroup.mockitoshaven.game.items.EquipmentSlot;
import com.fdmgroup.mockitoshaven.game.items.Inventory;
import com.fdmgroup.mockitoshaven.game.items.Item;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class GameCharacter implements Locatable {
	protected static int idCount = 0;
	@Id
	private String identifier;
	private String displayName;
	@Transient
	private Boolean readyToAttack = true;
	@Embedded
	private Coordinate coordinate;
	private String imageName;
	@OneToOne(cascade = CascadeType.ALL)
	private Stats stats = new Stats();
	@OneToOne(cascade = CascadeType.ALL)
	private Inventory inventory = new Inventory();
	@MapKeyEnumerated(EnumType.STRING)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Map<EquipmentSlot, EquipableItem> equipment = new HashMap<>();
	@Transient
	private Logger logger = LogManager.getLogger();

	@Transient
	public double getActionDelay() {
		int speed = stats.getSpeed();
		return 1 / Math.sqrt(speed);
	}

	public GameCharacter() {
		identifier = "" + idCount;
		idCount++;
	}

	public GameCharacter(Stats stats, Inventory inventory, Map<EquipmentSlot, EquipableItem> equipment) {
		this.stats = stats;
		this.inventory = inventory;
		this.equipment = equipment;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Boolean isReadyToAttack() {
		return readyToAttack;
	}

	public void setReadyToAttack(Boolean readyToAttack) {
		this.readyToAttack = readyToAttack;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public Map<EquipmentSlot, EquipableItem> getEquipment() {
		return equipment;
	}

	public void setEquipment(Map<EquipmentSlot, EquipableItem> equipment) {
		this.equipment = equipment;
	}

	public boolean useItem(String identifier, Game game) {

		logger.info("Attempting to user item: " + identifier);
		if (inventory.containsItem(identifier)) {
			logger.info("Item found");
			Item item = inventory.getItemWithoutRemove(identifier);
			
			if (item instanceof EquipableItem) {
				logger.info("Equipable");
				return equipItem((EquipableItem) item);
			} else if (item instanceof Consumable) {
				logger.info("Consumable");
				consumeItem((Consumable) item);
				return true;
			}
			logger.info("Item neither Equipable nor consumable");
		}
		return false;
	}

	private boolean equipItem(EquipableItem item) {
		if (equipment.containsValue(item)) {
			logger.trace("Item already equipped, aborting");
			return false;
		}
		logger.trace("Weapon is equippable");
		EquipableItem equipItem = (EquipableItem) item;

		if (equipItem.getSlot() == EquipmentSlot.ONEHANDEDWEAPON) {
			unequipItem(EquipmentSlot.TWOHANDEDWEAPON);
			if (!equipment.containsKey(EquipmentSlot.LEFT_HAND)) {
				logger.trace("Equipping item to empty left hand");
				equipment.put(EquipmentSlot.LEFT_HAND, equipItem);
				stats.addStats(equipItem.getStats());
			} else if (!equipment.containsKey(EquipmentSlot.RIGHT_HAND)) {
				logger.trace("Equipping item to empty right hand");
				equipment.put(EquipmentSlot.RIGHT_HAND, equipItem);
				stats.addStats(equipItem.getStats());
			} else {
				logger.trace("Replacing left hand item");
				unequipItem(EquipmentSlot.LEFT_HAND);
				equipment.put(EquipmentSlot.LEFT_HAND, equipItem);
				stats.addStats(equipItem.getStats());
			}
		} else {
			if (equipItem.getSlot() == EquipmentSlot.TWOHANDEDWEAPON) {
				unequipItem(EquipmentSlot.LEFT_HAND);
				unequipItem(EquipmentSlot.RIGHT_HAND);
			}
			unequipItem(equipItem.getSlot());
			equipment.put(equipItem.getSlot(), equipItem);
			stats.addStats(equipItem.getStats());
		}
		return true;

	}

	public boolean unequipItem(EquipmentSlot slot) {
		logger.trace("Attempting to unequip slot: " + slot);
		if (equipment.containsKey(slot)) {
			EquipableItem item = equipment.remove(slot);
			logger.trace("Removed " + item + " from slot " + slot);
			EquipableItem equipItem = (EquipableItem) item;
			stats.addStats(Stats.negateStats(equipItem.getStats()));
			return true;
		}
		return false;
	}

	public void consumeItem(Consumable item) {
		logger.info("Consuming item");
		stats.addStats(item.getStats());
		Timer timer = new Timer();
		if (item.getDuration() < 10000) {
			TimerTask wearOff = new TimerTask() {
				@Override
				public void run() {
					stats.addStats(Stats.negateStats(item.getStats()));
					timer.cancel();
					this.cancel();
				}
			};
			timer.schedule(wearOff, item.getDuration());
		}
		inventory.removeItem(item, 1);
	}

	@Override
	public Coordinate getCoordinate() {
		return coordinate;
	}

	public synchronized void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public boolean addItem(Item item) {
		return inventory.addItem(item);
	}

	@Override
	public String toString() {
		return "Character [identifier=" + identifier + ", displayName=" + displayName + ", readyToAttack="
				+ readyToAttack + ", coordinate=" + coordinate + ", imageName=" + imageName + ", stats=" + stats
				+ ", inventory=" + inventory + ", equipment=" + equipment + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GameCharacter))
			return false;
		GameCharacter other = (GameCharacter) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}

	public void addKillExperience(GameCharacter targetChar) {
		if (targetChar.getStats().getExperienceValue() + stats.getExperience() < stats.getExperienceToNextLevel()) {
			stats.setExperience(stats.getExperience() + targetChar.getStats().getExperienceValue());
		} else {
			stats.setExperience(Math.abs(stats.getExperienceToNextLevel() - stats.getExperience()
					- targetChar.getStats().getExperienceValue()));
			levelUp();
			while (stats.getExperience() >= stats.getExperienceToNextLevel()) {
				stats.setExperience(stats.getExperience() - stats.getExperienceToNextLevel());
				levelUp();
			}
		}

	}

	protected void levelUp() {
		logger.trace("Generic character levelup");
		stats.setLevel(stats.getLevel() + 1);
		stats.addStats(new Stats(5, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 50));

	}

	public void startAttackCooldown() {
		readyToAttack = false;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				readyToAttack = true;
				timer.cancel();
				this.cancel();
			}
		}, (int) (getActionDelay() * 1000));

	}

	public boolean unequipItem(String identifier) {
		EquipmentSlot unequipTarget = null;
		for (Entry<EquipmentSlot, EquipableItem> entry : equipment.entrySet()) {
			if (entry.getValue().getIdentifier().equals(identifier)) {
				unequipTarget = entry.getKey();
				break;
			}
		}
		return unequipItem(unequipTarget);

	}

}
