package com.fdmgroup.mockitoshaven.game.items;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fdmgroup.mockitoshaven.game.dungeon.Coordinate;
import com.fdmgroup.mockitoshaven.game.dungeon.Locatable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Item implements Locatable {
	@Id
	private String identifier;
	private String displayName;
	@Embedded
	private Coordinate coordinate;
	private String imageName;
	@ElementCollection(targetClass=ItemClassification.class, fetch=FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	
	private List<ItemClassification> classification = new ArrayList<>();
	
	
	
	public List<ItemClassification> getClassification() {
		return classification;
	}

	public void setClassification(List<ItemClassification> classification) {
		this.classification = classification;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	private int amount = 1;
	
	
	
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Item() {
		
	}
	
	public String getIdentifier() {
		return this.identifier;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}

	@Override
	public Coordinate getCoordinate() {
		return coordinate;
	}

	@Override
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
		
	}

	@Override
	public String toString() {
		return "Item [identifier=" + identifier + ", displayName=" + displayName + ", coordinate=" + coordinate
				+ ", imageName=" + imageName + ", classification=" + classification + ", amount=" + amount + "]";
	}

	public Item copy() {
		Item item = new Item();
		item.amount=amount;
		item.classification=classification;
		item.coordinate=coordinate;
		item.displayName=displayName;
		item.imageName=imageName;
		item.identifier=identifier;
		return item;
	}
	
	
	
	
}
