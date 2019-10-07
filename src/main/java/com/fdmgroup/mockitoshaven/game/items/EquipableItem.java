package com.fdmgroup.mockitoshaven.game.items;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

import com.fdmgroup.mockitoshaven.game.Stats;

@Entity
public class EquipableItem extends Item {
	@Enumerated(EnumType.STRING)
	private EquipmentSlot slot;
	@OneToOne(cascade =CascadeType.ALL)
	private Stats stats;
	


	public EquipableItem() {
		super();
	}

	@Override
	public String toString() {
		return super.toString()+"||||EquipableItem [slot=" + slot + ", stats=" + stats + "]";
	}

	public EquipableItem(Stats stats) {
		this.stats = stats;
	}

	public EquipmentSlot getSlot() {
		return slot;
	}

	public void setSlot(EquipmentSlot slot) {
		this.slot = slot;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}
	
	@Override
	public Item copy() {
		EquipableItem item = new EquipableItem();
		item.setAmount(getAmount());
		item.setClassification(getClassification());
		item.setCoordinate(getCoordinate());
		item.setDisplayName(getDisplayName());
		item.setImageName(getImageName());
		item.setIdentifier(getIdentifier());
		item.setSlot(getSlot());
		item.setStats(Stats.copyStats(getStats()));
		return item;
	}
	
	
}
