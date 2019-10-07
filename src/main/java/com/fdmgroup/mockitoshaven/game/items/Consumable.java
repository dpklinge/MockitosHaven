package com.fdmgroup.mockitoshaven.game.items;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.fdmgroup.mockitoshaven.game.Stats;

@Entity
public class Consumable extends Item {
	private int duration;
	@OneToOne
	private Stats stats;
	
	public Consumable(Stats stats, int duration) {
		this.stats = stats;
		this.duration = duration;
	}
	
	
	
	public Consumable() {
	}



	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public Stats getStats() {
		return stats;
	}
	
	public void setStats(Stats stats) {
		this.stats = stats;
	}



	@Override
	public String toString() {
		return super.toString()+"Consumable [duration=" + duration + ", stats=" + stats + "]";
	}
	
	@Override
	public Item copy() {
		Consumable item = new Consumable();
		item.setAmount(getAmount());
		item.setClassification(getClassification());
		item.setCoordinate(getCoordinate());
		item.setDisplayName(getDisplayName());
		item.setImageName(getImageName());
		item.setIdentifier(getIdentifier());
		item.setDuration(getDuration());
		item.setStats(Stats.copyStats(getStats()));
		return item;
	}
	
	
	
}